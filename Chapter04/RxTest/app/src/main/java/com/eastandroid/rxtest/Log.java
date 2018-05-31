package com.eastandroid.rxtest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author r
 */
public class Log {

    public static class 에러아님_Exception extends Throwable {
        private static final long serialVersionUID = -8900034648685639609L;
    }

    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int DEBUG = android.util.Log.DEBUG;
    public static final int INFO = android.util.Log.INFO;
    public static final int WARN = android.util.Log.WARN;
    public static final int ERROR = android.util.Log.ERROR;
    public static final int ASSERT = android.util.Log.ASSERT;

    public static boolean LOG = false;

    public static enum eMODE {
        ECLIPSE, STUDIO
    }

    public static eMODE MODE = eMODE.STUDIO;
    public static boolean FLOG = false;
    public static final String LOG_ROOTPATH = "_flog";

    private static final String PREFIX = "``";
    private static final String PREFIX_MULTILINE = PREFIX + "▼";
    private static final String LF = "\n";
    private static final int MAX_LOG_LINE_BYTE_SIZE = 3600;
    public static String LOG_CLASS = "^android\\.log\\..+";
    private static final String ANDROID_CLASS = "^android\\.app\\..+|^android\\.os\\..+|^com\\.android\\..+|^java\\..+|^android\\.view\\.BWebView\\$BWebViewClient";

    public static int p(int priority, Object... args) {
        if (!LOG)
            return -1;

        final StackTraceElement info = getStack();
        final String tag = getTag(info);
        final String locator = getLocator(info);
        final String msg = _MESSAGE(args);
        return println(priority, tag, locator, msg);
    }

    public static int ps(int priority, StackTraceElement info, Object... args) {
        if (!LOG)
            return -1;
        final String tag = getTag(info);
        final String locator = getLocator(info);
        final String msg = _MESSAGE(args);
        return println(priority, tag, locator, msg);
    }

    public static int println(int priority, String tag, String locator, String msg) {
        if (!LOG)
            return -1;

        final ArrayList<String> sa = new ArrayList<String>(100);
        final StringTokenizer st = new StringTokenizer(msg, LF, false);
        while (st.hasMoreTokens()) {
            final byte[] byte_text = st.nextToken().getBytes();
            int offset = 0;
            final int N = byte_text.length;
            while (offset < N) {
                int count = safeCut(byte_text, offset, MAX_LOG_LINE_BYTE_SIZE);
                sa.add(PREFIX + new String(byte_text, offset, count));
                offset += count;
            }
        }

        if (MODE == eMODE.ECLIPSE) {
            int N = sa.size();
            if (N <= 0)
                return android.util.Log.println(priority, tag, PREFIX + locator);

            if (N == 1)
                return android.util.Log.println(priority, tag, sa.get(0) + locator);

            int sum = android.util.Log.println(priority, tag, PREFIX_MULTILINE + locator);
            for (String s : sa)
                sum += android.util.Log.println(priority, ":", s);

            return sum;
        } else {
            StringBuilder sb = new StringBuilder(".........................................................................................");

            sb.replace(0, tag.length(), tag);
            sb.replace(sb.length() - locator.length(), sb.length(), locator);
            String adj_tag = sb.toString();

            int N = sa.size();
            if (N <= 0)
                return android.util.Log.println(priority, adj_tag, PREFIX);

            if (N == 1)
                return android.util.Log.println(priority, adj_tag, sa.get(0));

            int sum = android.util.Log.println(priority, adj_tag, PREFIX_MULTILINE);
            for (String s : sa)
                sum += android.util.Log.println(priority, adj_tag, s);

            return sum;
        }
    }

    private static String getLocator(StackTraceElement info) {
        if (info == null)
            return "";

        if (MODE == eMODE.ECLIPSE)
            return String.format(Locale.getDefault(), ":at (%s:%d)", info.getFileName(), info.getLineNumber());//eclipse
        else
            return String.format(Locale.getDefault(), "(%s:%d)", info.getFileName(), info.getLineNumber());//android studio
    }

    private static String getTag(StackTraceElement info) {
        if (info == null)
            return "";
        String tag = info.getMethodName();
        try {
            final String name = info.getClassName();
            tag = name.substring(name.lastIndexOf('.') + 1) + "." + info.getMethodName();
        } catch (Exception e) {
        }
        return tag.replaceAll("\\$", "_");
    }

    private static StackTraceElement getStack() {
        final StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
        int i = 0;

//		String methodName = null;
        int N = stackTraceElements.length;
        StackTraceElement info = stackTraceElements[i];
        for (; i < N; i++) {
            info = stackTraceElements[i];
            final String className = info.getClassName();
//            final String fileName = info.getFileName();
//            final int lineNumber = info.getLineNumber();
//            final String methodName = info.getMethodName();
//            android.util.Log.d("DEBUG", className + "," + fileName + "," + methodName + "," + lineNumber);
            if (className.matches(LOG_CLASS))
                continue;
            break;
        }

        for (; i < N; i++) {
            info = stackTraceElements[i];
            final String className = info.getClassName();
//            final String fileName = info.getFileName();
//            final int lineNumber = info.getLineNumber();
//            final String methodName = info.getMethodName();
//            android.util.Log.d("DEBUG", className + "," + fileName + "," + methodName + "," + lineNumber);
            if (className.matches(ANDROID_CLASS))
                continue;
            break;
        }

        for (; i >= 0; i--) {
            info = stackTraceElements[i];
//			final String className = info.getClassName();
//			final String fileName = info.getFileName();
            final int lineNumber = info.getLineNumber();
//			final String methodName = info.getMethodName();
//			android.util.Log.e("DEBUG", className + "," + fileName + "," + methodName + "," + lineNumber);
            if (lineNumber < 0)
                continue;
            break;
        }

        return info;
    }

    private static StackTraceElement getStack(String methodNameKey) {
        final StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
        StackTraceElement info = stackTraceElements[0];
        int N = stackTraceElements.length;
        int s = 0;
        for (; s < N; s++) {
            info = stackTraceElements[s];
            final String className = info.getClassName();
//            final String fileName = info.getFileName();
//            final int lineNumber = info.getLineNumber();
//            final String methodName = info.getMethodName();
//            android.util.Log.d("DEBUG", className + "," + fileName + "," + methodName + "," + lineNumber);
            if (className.matches(LOG_CLASS)) {
//                android.util.Log.i("pass", className + "," + methodName + "," + fileName + " " + lineNumber);
                continue;
            }
//            android.util.Log.e("stop", className + "," + methodName + "," + fileName + " " + lineNumber);
            break;
        }

        int e = N - 1;
        for (; e >= s; e--) {
            info = stackTraceElements[e];
            final String methodName = info.getMethodName();
            final String className = info.getClassName();
//            final String fileName = info.getFileName();
//            final int lineNumber = info.getLineNumber();
//            android.util.Log.d("DEBUG", className + "," + methodName + "," + fileName + " " + lineNumber);
            if (methodNameKey.equals(methodName) && !className.matches(ANDROID_CLASS)) {
//                android.util.Log.e("stop", className + "," + methodName + "," + fileName + " " + lineNumber);
                break;
            }
//            android.util.Log.i("pass", className + "," + methodName + "," + fileName + " " + lineNumber);
        }
        return info;
    }

    private static StackTraceElement getStackC(String methodNameKey) {
        final StackTraceElement[] stackTraceElements = new Exception().getStackTrace();

        StackTraceElement last_info = stackTraceElements[0];
        int N = stackTraceElements.length;
        int s = 0;
        for (; s < N; s++) {
            final StackTraceElement info = stackTraceElements[s];
            last_info = info;
            final String className = info.getClassName();
//            final String fileName = info.getFileName();
//            final int lineNumber = info.getLineNumber();
//            final String methodName = info.getMethodName();
//            android.util.Log.d("DEBUG", className + "," + fileName + "," + methodName + "," + lineNumber);
            if (className.matches(LOG_CLASS))
                continue;
            break;
        }

        int e = N - 1;

        for (; e >= s; e--) {
            final StackTraceElement info = stackTraceElements[e];
            final String methodName = info.getMethodName();
//            final String className = info.getClassName();
//            final String fileName = info.getFileName();
//            final int lineNumber = info.getLineNumber();
//            android.util.Log.w("DEBUG", className + "," + methodName + "," + fileName + " " + lineNumber);
            if (methodNameKey.equals(methodName))
                break;
            last_info = info;
        }
        return last_info;
    }
//    private static StackTraceElement getStackC2(String methodNameKey) {
//        final StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
//        int N = stackTraceElements.length;
//
//        StackTraceElement info = stackTraceElements[0];
//        int i = 0;
//        for (; i < N; i++) {
//            info = stackTraceElements[i];
//            final String methodName = info.getMethodName();
//            final String className = info.getClassName();
//            final String fileName = info.getFileName();
//            final int lineNumber = info.getLineNumber();
//            android.util.Log.w("DEBUG", className + "," + methodName + "," + fileName + " " + lineNumber);
//            if (methodNameKey.equals(methodName)) {
//                android.util.Log.i("break", className + "," + methodName + "," + fileName + " " + lineNumber);
//                break;
//            }
//        }
//
//        for (; i < N; i++) {
//            info = stackTraceElements[i];
//            final String methodName = info.getMethodName();
//            final String fileName = info.getFileName();
//            final String className = info.getClassName();
//            final int lineNumber = info.getLineNumber();
//            final boolean isNativeMethod = info.isNativeMethod();
//            android.util.Log.d("DEBUG", className + "," + methodName + "," + isNativeMethod + "," + fileName + " " + lineNumber);
//            if (methodName.contains("access$")) {
//                android.util.Log.i("pass", className + "," + methodName + "," + fileName + " " + lineNumber);
//                continue;
//            }
//
//            if (fileName == null || fileName.length() <= 0) {
//                android.util.Log.i("pass", className + "," + methodName + "," + fileName + " " + lineNumber);
//                continue;
//            }
//
//            if (lineNumber <= 0) {
//                android.util.Log.i("pass", className + "," + methodName + "," + fileName + " " + lineNumber);
//                continue;
//            }
//
//            if (className.contains("android.") && methodNameKey.equals(methodName)) {
//                android.util.Log.i("pass", className + "," + methodName + "," + fileName + " " + lineNumber);
//                continue;
//            }
//
//            android.util.Log.w("break", className + "," + methodName + "," + fileName + " " + lineNumber);
//            break;
//        }
//
////        for (; i < N; i++) {
////            StackTraceElement rr = stackTraceElements[i];
////            final String methodName = rr.getMethodName();
////            final String className = rr.getClassName();
////            final String fileName = rr.getFileName();
////            final int lineNumber = rr.getLineNumber();
////            android.util.Log.w("LOG", className + "," + methodName + "," + fileName + " " + lineNumber);
////        }
//
//        return info;
//    }

    private static int safeCut(byte[] byte_text, int byte_start_index, int byte_length) {
        int text_length = byte_text.length;
        if (text_length <= byte_start_index)
            throw new ArrayIndexOutOfBoundsException("!!text_length <= start_byte_index");

        if (byte_length <= 0)
            throw new UnsupportedOperationException("!!must length > 0 ");

        if ((byte_text[byte_start_index] & (byte) 0xc0) == (byte) 0x80)
            throw new UnsupportedOperationException("!!start_byte_index must splited index");

        int po = byte_start_index + byte_length;
        if (text_length <= po)
            return text_length - byte_start_index;

        for (; byte_start_index <= po; po--) {
            if ((byte_text[po] & (byte) 0xc0) != (byte) 0x80)
                break;
        }

        if (po <= byte_start_index)
            throw new UnsupportedOperationException("!!byte_length too small");

        return po - byte_start_index;
    }

    private static void flog(File logfile, StackTraceElement info, String log) {
        if (!FLOG)
            return;

        try {
            File parentfile = logfile.getParentFile();
            if (!parentfile.isDirectory() && !parentfile.exists())
                parentfile.mkdirs();
            if (!logfile.exists())
                logfile.createNewFile();

            BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile, true), "UTF-8"));

            final String tag = String.format("%-80s %-100s ``", _DUMP_milliseconds(), info.toString());
            final String tagspace = String.format("%80s %100s ``", " ", " ");

            final StringTokenizer st = new StringTokenizer(log, LF, false);
            if (st.hasMoreTokens()) {
                final String token = st.nextToken();
                buf.append(tag).append(token).append(LF);
            }

            while (st.hasMoreTokens()) {
                final String token = st.nextToken();
                buf.append(tagspace).append(token).append(LF);
            }
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long last_filter;

    public static int e_filter(long sec, Object... args) {
        if (!LOG)
            return -1;

        if (last_filter < System.currentTimeMillis() - sec)
            return -1;

        last_filter = System.currentTimeMillis();

        return p(android.util.Log.ERROR, args);
    }

    public static int a(Object... args) {
        if (!LOG)
            return -1;
        return p(android.util.Log.ASSERT, args);
    }

    public static int e(Object... args) {
        if (!LOG)
            return -1;
        return p(android.util.Log.ERROR, args);
    }

    public static int w(Object... args) {
        if (!LOG)
            return -1;
        return p(android.util.Log.WARN, args);
    }

    public static int i(Object... args) {
        if (!LOG)
            return -1;
        return p(android.util.Log.INFO, args);
    }

    public static int d(Object... args) {
        if (!LOG)
            return -1;
        return p(android.util.Log.DEBUG, args);
    }

    public static int v(Object... args) {
        if (!LOG)
            return -1;
        return p(android.util.Log.VERBOSE, args);
    }

    public static int json(String json) {
        if (!LOG)
            return -1;
        return e(_DUMP_json(json));
    }

    public static int object(Object o) {
        if (!LOG)
            return -1;
        return e(_DUMP_object(o));
    }

    public static int milliseconds(long milliseconds) {
        if (!LOG)
            return -1;
        return e(_DUMP_milliseconds(milliseconds));
    }

    public static int provider(Context context, Uri uri) {
        if (!LOG)
            return -1;
        if (context == null || uri == null) {
            e("context==null || uri == null");
            return -1;

        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if (c == null)
            return -1;

        int result = e(c);
        c.close();
        return result;
    }

    public static int pn(int priority, int depth, Object... args) {
        if (!LOG)
            return -1;
        final StackTraceElement info = new Exception().getStackTrace()[1 + depth];
        final String tag = getTag(info);
        final String locator = getLocator(info);
        final String msg = _MESSAGE(args);
        return println(priority, tag, locator, msg);
    }

    public static int viewtree(View parent, int... depth) {
        if (!LOG)
            return -1;
        final int d = (depth.length > 0 ? depth[0] : 0);

        if (!(parent instanceof ViewGroup)) {
            return pn(android.util.Log.ERROR, d + 2, _DUMP(parent, 0));
        }

        final ViewGroup vp = (ViewGroup) parent;
        int N = vp.getChildCount();
        int result = 0;
        for (int i = 0; i < N; i++) {
            final View child = vp.getChildAt(i);
            result += pn(android.util.Log.ERROR, d + 2, _DUMP(child, d));

            if (child instanceof ViewGroup)
                result += viewtree(child, d + 1);
        }
        return result;
    }

    public static int clz(Class<?> clz) {
        if (!LOG)
            return -1;
        int retult = e(clz);
        retult += i("getName", clz.getName());
        retult += i("getPackage", clz.getPackage());
        retult += i("getCanonicalName", clz.getCanonicalName());
        retult += i("getDeclaredClasses", Arrays.toString(clz.getDeclaredClasses()));
        retult += i("getClasses", Arrays.toString(clz.getClasses()));
        retult += i("getSigners", Arrays.toString(clz.getSigners()));
        retult += i("getEnumConstants", Arrays.toString(clz.getEnumConstants()));
        retult += i("getTypeParameters", Arrays.toString(clz.getTypeParameters()));
        retult += i("getGenericInterfaces", Arrays.toString(clz.getGenericInterfaces()));
        retult += i("getInterfaces", Arrays.toString(clz.getInterfaces()));
        //@formatter:off
		if (clz.isAnnotation())							retult += i("classinfo", clz.isAnnotation(), "isAnnotation");
		if (clz.isAnonymousClass())						retult += i(clz.isAnonymousClass(), "isAnonymousClass");
		if (clz.isArray())								retult += i(clz.isArray(), "isArray");
		if (clz.isEnum())								retult += i(clz.isEnum(), "isEnum");
		if (clz.isInstance(CharSequence.class))			retult += i(clz.isInstance(CharSequence.class), "isInstance");
		if (clz.isAssignableFrom(CharSequence.class))	retult += i(clz.isAssignableFrom(CharSequence.class), "isAssignableFrom");
		if (clz.isInterface())							retult += i(clz.isInterface(), "isInterface");
		if (clz.isLocalClass())							retult += i(clz.isLocalClass(), "isLocalClass");
		if (clz.isMemberClass())						retult += i(clz.isMemberClass(), "isMemberClass");
		if (clz.isPrimitive())							retult += i(clz.isPrimitive(), "isPrimitive");
		if (clz.isSynthetic())							retult += i(clz.isSynthetic(), "isSynthetic");
		//@formatter:on
        return retult;
    }

    ////////////////////////////////////////////////////////////////////////////
    public static void toast(Context context, Object... args) {
        if (!LOG)
            return;
        if (context == null)
            return;

        e(args);
        Toast.makeText(context, _MESSAGE(args), Toast.LENGTH_SHORT).show();
    }

    ////////////////////////////////////////////////////////////////////////////
    //_DUMP
    ////////////////////////////////////////////////////////////////////////////
    public static String _MESSAGE(Object... args) {
        return _INTERNAL_MESSAGE(args);
    }

    private static String _INTERNAL_MESSAGE(Object[] args) {
        if (args == null)
            return "null[]";

        StringBuilder sb = new StringBuilder();
        for (Object object : args) {
            try {
                //@formatter:off
				if (object == null)                       sb.append("null");
				else if (object instanceof Class)         sb.append(_DUMP((Class<?>) object));
				else if (object instanceof Cursor)        sb.append(_DUMP((Cursor) object));
				else if (object instanceof View)          sb.append(_DUMP((View) object));
				else if (object instanceof Intent)        sb.append(_DUMP((Intent) object));
				else if (object instanceof Bundle)        sb.append(_DUMP((Bundle) object));
				else if (object instanceof ContentValues) sb.append(_DUMP((ContentValues) object));
				else if (object instanceof Throwable)     sb.append(_DUMP((Throwable) object));
				else if (object instanceof Uri)           sb.append(_DUMP((Uri) object));
				else if (object instanceof Method)        sb.append(_DUMP((Method) object));
				else if (object instanceof JSONObject)    sb.append(((JSONObject) object).toString(2));
				else if (object instanceof JSONArray)     sb.append(((JSONArray) object).toString(2));
				else if (object instanceof CharSequence)  sb.append(_DUMP(object.toString()));
				else if (object.getClass().isArray())     sb.append(_DUMP_array(object));
				else                                      sb.append(object.toString());
				
				//@formatter:on
                sb.append(",");
            } catch (Exception e) {
            }
        }
        return sb.toString();
    }

    public static String _DUMP_json(String json) {
        try {
            if (json.length() > 0) {
                if (json.charAt(0) == '{') {
                    return new JSONObject(json).toString(4);
                }
                if (json.charAt(0) == '[') {
                    return new JSONArray(json).toString(4);
                }
            }
        } catch (Exception e) {
        }
        return json;
    }

    public static String _DUMP(String object) {
        StringBuilder sb = new StringBuilder();
        try {
            char s = object.charAt(0);
            char e = object.charAt(object.length() - 1);
            if (s == '[' && e == ']') {
                String ja = new JSONArray(object).toString(2);
                sb.append("\nJA\n");
                sb.append(ja);
            } else if (s == '{' && e == '}') {
                String jo = new JSONObject(object).toString(2);
                sb.append("\nJO\n");
                sb.append(jo);
            } else if (s == '<' && e == '>') {
                String xml = PrettyXml.format(object);
                sb.append("\nXML\n");
                sb.append(xml);
            } else {
                sb.append(object);
            }
        } catch (Exception e) {
            sb.append(object);
        }
        return sb.toString();
    }

    public static String _DUMP(Method method) {
        StringBuilder result = new StringBuilder(Modifier.toString(method.getModifiers()));
        if (result.length() != 0) {
            result.append(' ');
        }
        result.append(method.getReturnType().getSimpleName());
        result.append("                           ");
        result.setLength(20);
        result.append(method.getDeclaringClass().getSimpleName());
        result.append('.');
        result.append(method.getName());
        result.append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            result.append(parameterType.getSimpleName());
            result.append(',');
        }
        if (parameterTypes.length > 0)
            result.setLength(result.length() - 1);
        result.append(")");

        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes.length != 0) {
            result.append(" throws ");
            for (Class<?> exceptionType : exceptionTypes) {
                result.append(exceptionType.getSimpleName());
                result.append(',');
            }
            if (exceptionTypes.length > 0)
                result.setLength(result.length() - 1);
        }
        return result.toString();
    }

    private static String _DUMP(View v, final int depth) {
        final String SP = "                    ";
        StringBuilder out = new StringBuilder(128);
        out.append(SP);

        if (v instanceof WebView)
            out.insert(depth, "W:" + Integer.toHexString(System.identityHashCode(v)) + ":" + ((WebView) v).getTitle());
        else if (v instanceof TextView)
            out.insert(depth, "T:" + Integer.toHexString(System.identityHashCode(v)) + ":" + ((TextView) v).getText());
        else
            out.insert(depth, "N:" + Integer.toHexString(System.identityHashCode(v)) + ":" + v.getClass().getSimpleName());
        out.setLength(SP.length());
        appendViewInfo(out, v);
        return out.toString();
    }

    private static String _DUMP(View v) {
        return _DUMP(v, 0);
    }

    private static void appendViewInfo(StringBuilder out, View v) {
//		out.append('{');
//		out.append(String.format("#%08x", System.identityHashCode(v)));
//		out.append(' ');
//		switch (v.getVisibility()) {
//			case View.VISIBLE :
//				out.append('V');
//				break;
//			case View.INVISIBLE :
//				out.append('I');
//				break;
//			case View.GONE :
//				out.append('G');
//				break;
//			default :
//				out.append('.');
//				break;
//		}
//		out.append(v.isShown() ? 'S' : '.');
//		out.append(' ');
//		out.append(v.isFocusable() ? 'F' : '.');
//		out.append(v.isClickable() ? 'C' : '.');
//		out.append(' ');
//		out.append(v.isFocused() ? 'F' : '.');
//		out.append(v.isPressed() ? 'P' : '.');
//		out.append(v.isSelected() ? 'S' : '.');
//		out.append('}');
        final int id = v.getId();
        if (id != View.NO_ID) {
            // out.append(String.format(" #%08x", id));
            final Resources r = v.getResources();
            if (((id >>> 24) != 0) && r != null) {
                try {
                    String pkgname;
                    switch (id & 0xff000000) {
                        case 0x7f000000:
                            pkgname = "app";
                            break;
                        case 0x01000000:
                            pkgname = "android";
                            break;
                        default:
                            pkgname = r.getResourcePackageName(id);
                            break;
                    }
                    String typename = r.getResourceTypeName(id);
                    String entryname = r.getResourceEntryName(id);
                    out.append(" ");
                    out.append(pkgname);
                    out.append(":");
                    out.append(typename);
                    out.append("/");
                    out.append(entryname);
                } catch (Resources.NotFoundException e) {
                }
            }
        }
    }

    private static String _DUMP(Cursor c) {
        if (c == null)
            return "null_Cursor";

        StringBuilder sb = new StringBuilder();
        int count = c.getCount();
        sb.append("<" + count + ">");

        try {
            String[] columns = c.getColumnNames();
            sb.append(Arrays.toString(columns));
            sb.append("\n");
        } catch (Exception e) {
        }

        int countColumns = c.getColumnCount();
        if (!c.isBeforeFirst()) {
            for (int i = 0; i < countColumns; i++) {
                try {
                    sb.append(c.getString(i) + ",");
                } catch (Exception e) {
                    sb.append("BLOB,");
                }
            }
        } else {
            int org_pos = c.getPosition();
            while (c.moveToNext()) {
                for (int i = 0; i < countColumns; i++) {
                    try {
                        sb.append(c.getString(i) + ",");
                    } catch (Exception e) {
                        sb.append("BLOB,");
                    }
                }
                sb.append("\n");
            }
            c.moveToPosition(org_pos);
        }
        return sb.toString();
    }

    private static String _DUMP(ContentValues values) {
        if (values == null)
            return "null_ContentValues";

        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> etry : values.valueSet()) {
            String key = etry.getKey();
            String value = etry.getValue().toString();
            String type = etry.getValue().getClass().getSimpleName();
            sb.append(key + "," + type + "," + value).append("\n");
        }

        return sb.toString();
    }

    private static String _DUMP(Bundle bundle) {
        if (bundle == null)
            return "null_Bundle";

        StringBuilder sb = new StringBuilder();
        final Set<String> keys = bundle.keySet();

        for (String key : keys) {
            final Object o = bundle.get(key);
            if (o == null) {
                sb.append("Object" + " " + key + ";//" + null);
            } else if (o.getClass().isArray()) {
                sb.append(o.getClass().getSimpleName() + " " + key + ";//" + _DUMP_array(o));
            } else {
                sb.append(o.getClass().getSimpleName() + " " + key + ";//" + o.toString());
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String _DUMP_array(Object o) {

        //@formatter:off
		if (o == null)
			return "null";

		if (!o.getClass().isArray())
			return "";

		Class<?> elemElemClass = o.getClass().getComponentType();
		if (elemElemClass.isPrimitive()) {
			if      (boolean.class.equals(elemElemClass)) return Arrays.toString((boolean[]) o);
			else if (char   .class.equals(elemElemClass)) return Arrays.toString((char   []) o);
			else if (double .class.equals(elemElemClass)) return Arrays.toString((double []) o);
			else if (float  .class.equals(elemElemClass)) return Arrays.toString((float  []) o);
			else if (int    .class.equals(elemElemClass)) return Arrays.toString((int    []) o);
			else if (long   .class.equals(elemElemClass)) return Arrays.toString((long   []) o);
			else if (short  .class.equals(elemElemClass)) return Arrays.toString((short  []) o);
			else if (byte   .class.equals(elemElemClass)) return           _DUMP((byte   []) o);
			else throw new AssertionError();
		} else 
			return Arrays.toString((Object[]) o);
		//@formatter:on

    }

    private static String _DUMP(Class<?> cls) {
        if (cls == null)
            return "null_Class<?>";
        return cls.getSimpleName();
//		return cls.getSimpleName() + ((cls.getSuperclass() != null) ? (">>" + cls.getSuperclass().getSimpleName()) : "");
    }

    private static String _DUMP(Uri uri) {
        if (uri == null)
            return "null_Uri";

//		return uri.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n Uri                       ").append(uri.toString());
        sb.append("\r\n Scheme                    ").append(uri.getScheme() != null ? uri.getScheme().toString() : "null");
        sb.append("\r\n Host                      ").append(uri.getHost() != null ? uri.getHost().toString() : "null");
//        sb.append("\r\n Port                      ").append(uri.getPort());
        sb.append("\r\n Path                      ").append(uri.getPath() != null ? uri.getPath().toString() : "null");
        sb.append("\r\n Query                     ").append(uri.getQuery() != null ? uri.getQuery().toString() : "null");
//        sb.append("\r\n");
        sb.append("\r\n Fragment                  ").append(uri.getFragment() != null ? uri.getFragment().toString() : "null");
//        sb.append("\r\n LastPathSegment           ").append(uri.getLastPathSegment() != null ? uri.getLastPathSegment().toString() : "null");
//        sb.append("\r\n SchemeSpecificPart        ").append(uri.getSchemeSpecificPart() != null ? uri.getSchemeSpecificPart().toString() : "null");
//        sb.append("\r\n UserInfo                  ").append(uri.getUserInfo() != null ? uri.getUserInfo().toString() : "null");
//        sb.append("\r\n PathSegments              ").append(uri.getPathSegments() != null ? uri.getPathSegments().toString() : "null");
//        sb.append("\r\n Authority                 ").append(uri.getAuthority() != null ? uri.getAuthority().toString() : "null");
//        sb.append("\r\n");
//        sb.append("\r\n EncodedAuthority          ").append(uri.getEncodedAuthority() != null ? uri.getEncodedAuthority().toString() : "null");
//        sb.append("\r\n EncodedPath               ").append(uri.getEncodedPath() != null ? uri.getEncodedPath().toString() : "null");
//        sb.append("\r\n EncodedQuery              ").append(uri.getEncodedQuery() != null ? uri.getEncodedQuery().toString() : "null");
//        sb.append("\r\n EncodedFragment           ").append(uri.getEncodedFragment() != null ? uri.getEncodedFragment().toString() : "null");
//        sb.append("\r\n EncodedSchemeSpecificPart ").append(uri.getEncodedSchemeSpecificPart() != null ? uri.getEncodedSchemeSpecificPart().toString() : "null");
//        sb.append("\r\n EncodedUserInfo           ").append(uri.getEncodedUserInfo() != null ? uri.getEncodedUserInfo().toString() : "null");
//        sb.append("\r\n");
        return sb.toString();
    }

    public static String _DUMP(Intent intent) {
        if (intent == null)
            return "null_Intent";
        StringBuilder sb = new StringBuilder();
        //@formatter:off
		sb.append(intent.getAction    () != null ? (sb.length() > 0 ? "\n" : "") + "Action     " + intent.getAction         ().toString() : "");
		sb.append(intent.getData      () != null ? (sb.length() > 0 ? "\n" : "") + "Data       " + intent.getData           ().toString() : "");
		sb.append(intent.getCategories() != null ? (sb.length() > 0 ? "\n" : "") + "Categories " + intent.getCategories     ().toString() : "");
		sb.append(intent.getType      () != null ? (sb.length() > 0 ? "\n" : "") + "Type       " + intent.getType           ().toString() : "");
		sb.append(intent.getScheme    () != null ? (sb.length() > 0 ? "\n" : "") + "Scheme     " + intent.getScheme         ().toString() : "");
		sb.append(intent.getPackage   () != null ? (sb.length() > 0 ? "\n" : "") + "Package    " + intent.getPackage        ().toString() : "");
		sb.append(intent.getComponent () != null ? (sb.length() > 0 ? "\n" : "") + "Component  " + intent.getComponent      ().toString() : "");
		sb.append(intent.getFlags()      != 0x00 ? (sb.length() > 0 ? "\n" : "") + "Flags      " + Integer.toHexString(intent.getFlags()) : "");
		//@formatter:on

        if (intent.getExtras() != null)
            sb.append((sb.length() > 0 ? "\n" : "") + _DUMP(intent.getExtras()));

        return sb.toString();
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private static String _DUMP(byte[] bytearray) {
        if (bytearray == null)
            return "null_bytearray";
        try {
            char[] chars = new char[2 * bytearray.length];
            for (int i = 0; i < bytearray.length; ++i) {
                chars[2 * i] = HEX_CHARS[(bytearray[i] & 0xF0) >>> 4];
                chars[2 * i + 1] = HEX_CHARS[bytearray[i] & 0x0F];
            }
            return new String(chars);
        } catch (Exception e) {
            return "!!byte[]";
        }
    }

    public static String _DUMP_StackTrace(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    public static String _DUMP(Throwable th) {
        String message = "Throwable";
        try {
            Throwable cause = th;
            while (cause != null) {
                message = cause.getClass().getSimpleName() + "," + cause.getMessage();
                cause = cause.getCause();
            }
        } catch (Exception e) {
        }
        return message;
    }

    public static String _DUMP_milliseconds() {
        return _DUMP_milliseconds(System.currentTimeMillis());
    }

    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.getDefault());
    private static final String LOGN_FORMAT = "%" + Long.toString(Long.MAX_VALUE).length() + "d";

    public static String _DUMP_milliseconds(long milliseconds) {
        return String.format("<%s," + LOGN_FORMAT + ">", sf.format(new Date(milliseconds)), SystemClock.elapsedRealtime());
    }

    public static String _DUMP_elapsed(long elapsedRealtime) {
        return _DUMP_milliseconds(System.currentTimeMillis() - (SystemClock.elapsedRealtime() - elapsedRealtime));
    }

    public static String _h2s(byte[] bytes) {
        return _DUMP(bytes);
    }

    public static byte[] _s2h(String bytes_text) {
        byte[] bytes = new byte[bytes_text.length() / 2];
        try {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt(bytes_text.substring(2 * i, 2 * i + 2), 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static String _DUMP_object(Object o) {
        return _DUMP_object("", o, new HashSet<Object>());
    }

    private static String _DUMP_object(String name, Object value, Set<Object> duplication) {
        StringBuilder sb = new StringBuilder();
        try {
            if (value == null) {
                sb.append(name).append("=null\n");
                return sb.toString();
            }

            if (value.getClass().isArray()) {
                sb.append(name).append('<').append(value.getClass().getSimpleName()).append('>').append(" = ");
                //@formatter:off
				Class<?> componentType = value.getClass().getComponentType();
				if      (boolean.class.isAssignableFrom(componentType)) sb.append(Arrays.toString((boolean[]) value));
				else if (byte   .class.isAssignableFrom(componentType)) sb.append(((byte[])value).length < MAX_LOG_LINE_BYTE_SIZE ?  new String((byte[])value) : "["+((byte[])value).length  +"]"       );
				else if (char   .class.isAssignableFrom(componentType)) sb.append(new String((char[])value)         );
				else if (double .class.isAssignableFrom(componentType)) sb.append(Arrays.toString((double []) value));
				else if (float  .class.isAssignableFrom(componentType)) sb.append(Arrays.toString((float  []) value));
				else if (int    .class.isAssignableFrom(componentType)) sb.append(Arrays.toString((int    []) value));
				else if (long   .class.isAssignableFrom(componentType)) sb.append(Arrays.toString((long   []) value));
				else if (short  .class.isAssignableFrom(componentType)) sb.append(Arrays.toString((short  []) value));
				else                                                    sb.append(Arrays.toString((Object []) value));
				//@formatter:on
            } else if (value.getClass().isPrimitive()//
//					|| (value.getClass().getMethod("toString").getDeclaringClass() != Object.class)// toString이 정의된경우만
                    || value.getClass().isEnum()//
                    || value instanceof Rect//
                    || value instanceof RectF//
                    || value instanceof Point//
                    || value instanceof Number//
                    || value instanceof Boolean//
                    || value instanceof CharSequence)//
            {
                sb.append(name).append('<').append(value.getClass().getSimpleName()).append('>').append(" = ");
                sb.append(value.toString());
            } else {
                if (duplication.contains(value)) {
                    sb.append(name).append('<').append(value.getClass().getSimpleName()).append('>').append(" = ");
                    sb.append("[duplication]\n");
                    return sb.toString();
                }
                duplication.add(value);

                if (value instanceof Collection) {
                    sb.append(name).append('<').append(value.getClass().getSimpleName()).append('>').append(" = ").append(":\n");
                    Iterator<?> it = ((Collection<?>) value).iterator();
                    while (it.hasNext())
                        sb.append(_DUMP_object("  " + name + "[item]", it.next(), duplication));
                } else {
                    final Class<?> clz = value.getClass();
                    sb.append(name).append('<').append(value.getClass().getSimpleName()).append('>').append(" = ").append(":\n");
                    for (Field f : clz.getDeclaredFields()) {
                        f.setAccessible(true);
                        sb.append(_DUMP_object("  " + name + "." + f.getName(), f.get(value), duplication));
                    }
                }
            }
            sb.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // Line Logger
    public static class Line1Logger {
        private static StringBuilder LOGGER;

        public static void append(Object... args) {
            if (LOGGER == null || LOGGER.length() > 1024 * 4)
                LOGGER = new StringBuilder(1024);
            LOGGER.append(_MESSAGE(args));
        }

        public static String pop() {
            final String log = LOGGER.toString();
            LOGGER = null;
            return log;
        }
    }

    // String Logger
    public static class LineNLogger {
        private static StringBuilder LOGGER = new StringBuilder(1024);

        public static void insert(Object... args) {
            LOGGER.insert(0, _MESSAGE(args)).append("\n");
            LOGGER.setLength(1024 * 4);
        }

        public static String get() {
            return LOGGER.toString();
        }

        public static void clear() {
            LOGGER.delete(0, LOGGER.length());
        }
    }

    // LIST_LOGGER
    public static class ListLogger {
        private static ArrayList<String> LOGGER = new ArrayList<String>(100);

        public static void insert(Object... args) {
            LOGGER.add(0, _MESSAGE(args));
            while (LOGGER.size() > 1024)
                LOGGER.remove(1024);
        }

        public static ArrayList<String> pop() {
            final ArrayList<String> result = peek();
            clear();
            return result;
        }

        public static ArrayList<String> get() {
            return peek();
        }

        public static ArrayList<String> peek() {
            return LOGGER;
        }

        public static void clear() {
            LOGGER.clear();
        }
    }

    //tic
    private static Map<Object, Long> SEED_S = new HashMap<Object, Long>();

    public static void tic_s() {
        if (!LOG)
            return;
        String seed = new Exception().getStackTrace()[2].getFileName();
        long e = System.currentTimeMillis();
        SEED_S.put(seed, e);
    }

    public static void tic() {
        if (!LOG)
            return;
        String seed = new Exception().getStackTrace()[2].getFileName();
        long e = System.currentTimeMillis();
        long s = (SEED_S.get(seed) == null ? 0L : SEED_S.get(seed));
        e(String.format("%15d%15d%15d", s, e, e - s));
        SEED_S.put(seed, e);
    }

    public static void tic(String... args) {
        if (!LOG)
            return;
        String seed = new Exception().getStackTrace()[2].getFileName();
        long e = System.currentTimeMillis();
        long s = (SEED_S.get(seed) == null ? 0L : SEED_S.get(seed));
        e(String.format("%15d%15d%15d", s, e, e - s), _INTERNAL_MESSAGE(args));
        SEED_S.put(seed, e);
    }

    public static void tic_s(Object seed) {
        if (!LOG)
            return;
        long e = System.currentTimeMillis();
        SEED_S.put(seed, e);
    }

    public static void tic(Object seed, Object... args) {
        if (!LOG)
            return;
        long e = System.currentTimeMillis();
        long s = (SEED_S.get(seed) == null ? 0L : SEED_S.get(seed));
        e(String.format("%15d%15d%15d", s, e, e - s), seed, _INTERNAL_MESSAGE(args));
        SEED_S.put(seed, e);
    }

    //keep loger
    public static Loger _LOGER = new Loger();

    public static Loger _loger() {
        return _LOGER;
    }

    public static class Loger {
        private StringBuilder sb = new StringBuilder();
        private long start;
        private long end;

        public void log(Object... args) {
            if (!LOG)
                return;

            long now = System.currentTimeMillis();

            if (sb == null) {
                sb = new StringBuilder();
                start = now;
                sb.append("start").append(",");
                sb.append(_DUMP_milliseconds(now)).append(",");
                sb.append("\n");
            }

            sb.append(_DUMP_milliseconds(now)).append(",");
            sb.append(System.currentTimeMillis() - end).append(",");
            sb.append(_MESSAGE(args)).append(",");
            sb.append(getLocator(new Exception().getStackTrace()[1])).append("\n");
            end = now;
        }

        public void print() {
            if (!LOG)
                return;

            long now = System.currentTimeMillis();
            sb.append("end").append(",");
            sb.append(_DUMP_milliseconds(now)).append(",");
            sb.append("length : " + (now - start)).append("\n");
            e(sb.toString());
            sb = null;
        }
    }

    //flog
    public static void flog(Context context, Object... args) {
        //		final StackTraceElement info = getStack();
        //		final String msg = _MESSAGE(args);
        flog(context.getPackageName(), args);
    }

    public static void flog(String file_prefix, Object... args) {
        final StackTraceElement info = getStack();
        final String msg = _MESSAGE(args);

        final File dirPath = new File(Environment.getExternalStorageDirectory(), LOG_ROOTPATH);
        final String yyyymmdd = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
        final File logfile = new File(dirPath, file_prefix + "_" + yyyymmdd + ".log");

        flog(logfile, info, msg);
    }

    //xml
    private static class PrettyXml {
        private static XmlFormatter formatter = new XmlFormatter(2, 80);

        public static String format(String s) {
            return formatter.format(new String(s), 0);
        }

        private static class XmlFormatter {
            private int indentNumChars;
            private int lineLength;
            private boolean singleLine;

            public XmlFormatter(int indentNumChars, int lineLength) {
                this.indentNumChars = indentNumChars;
                this.lineLength = lineLength;
            }

            public synchronized String format(String s, int initialIndent) {
                int indent = initialIndent;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    char currentChar = s.charAt(i);
                    if (currentChar == '<') {
                        char nextChar = s.charAt(i + 1);
                        if (nextChar == '/')
                            indent -= indentNumChars;
                        if (!singleLine) // Don't indent before closing element if we're creating opening and closing elements on a single line.
                            sb.append(buildWhitespace(indent));
                        if (nextChar != '?' && nextChar != '!' && nextChar != '/')
                            indent += indentNumChars;
                        singleLine = false; // Reset flag.
                    }
                    sb.append(currentChar);
                    if (currentChar == '>') {
                        if (s.charAt(i - 1) == '/') {
                            indent -= indentNumChars;
                            sb.append("\n");
                        } else {
                            int nextStartElementPos = s.indexOf('<', i);
                            if (nextStartElementPos > i + 1) {
                                String textBetweenElements = s.substring(i + 1, nextStartElementPos);

                                // If the space between elements is solely newlines, let them through to preserve additional newlines in source document.
                                if (textBetweenElements.replaceAll("\n", "").length() == 0) {
                                    sb.append(textBetweenElements + "\n");
                                }
                                // Put tags and text on a single line if the text is short.
                                else if (textBetweenElements.length() <= lineLength * 0.5) {
                                    sb.append(textBetweenElements);
                                    singleLine = true;
                                }
                                // For larger amounts of text, wrap lines to a maximum line length.
                                else {
                                    sb.append("\n" + lineWrap(textBetweenElements, lineLength, indent, null) + "\n");
                                }
                                i = nextStartElementPos - 1;
                            } else {
                                sb.append("\n");
                            }
                        }
                    }
                }
                return sb.toString();
            }
        }

        private static String buildWhitespace(int numChars) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numChars; i++)
                sb.append(" ");
            return sb.toString();
        }

        private static String lineWrap(String s, int lineLength, Integer indent, String linePrefix) {
            if (s == null)
                return null;

            StringBuilder sb = new StringBuilder();
            int lineStartPos = 0;
            int lineEndPos;
            boolean firstLine = true;
            while (lineStartPos < s.length()) {
                if (!firstLine)
                    sb.append("\n");
                else
                    firstLine = false;

                if (lineStartPos + lineLength > s.length())
                    lineEndPos = s.length() - 1;
                else {
                    lineEndPos = lineStartPos + lineLength - 1;
                    while (lineEndPos > lineStartPos && (s.charAt(lineEndPos) != ' ' && s.charAt(lineEndPos) != '\t'))
                        lineEndPos--;
                }
                sb.append(buildWhitespace(indent));
                if (linePrefix != null)
                    sb.append(linePrefix);

                sb.append(s.substring(lineStartPos, lineEndPos + 1));
                lineStartPos = lineEndPos + 1;
            }
            return sb.toString();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //image save
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void compress(String name, byte[] data) {
        try {
            final File d = new File(Environment.getExternalStorageDirectory(), LOG_ROOTPATH);
            boolean b = d.mkdirs();
            final Date now = new Date(System.currentTimeMillis());
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.ENGLISH);
            final File f = new File(d, sdf.format(now) + "_" + name + ".jpg");
            Log.e(f);
            FileOutputStream fos = new FileOutputStream(f);
            BitmapFactory.decodeByteArray(data, 0, data.length).compress(CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compress(String name, Bitmap bmp) {
        try {
            final File d = new File(Environment.getExternalStorageDirectory(), LOG_ROOTPATH);
            boolean b = d.mkdirs();
            final Date now = new Date(System.currentTimeMillis());
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.ENGLISH);
            final File f = new File(d, sdf.format(now) + "_" + name + ".jpg");
            Log.e(f);
            FileOutputStream fos = new FileOutputStream(f);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //life tools
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //상속트리마지막 위치
    public static int po(int priority, String methodNameKey, Object... args) {
        if (!LOG)
            return -1;

        final StackTraceElement info = getStack(methodNameKey);
        final String tag = getTag(info);
        final String locator = getLocator(info);
        final String msg = _MESSAGE(args);
        return println(priority, tag, locator, msg);
    }

    public static void sendBroadcast(Class<?> clz, Intent intent) {
        if (!LOG)
            return;
        try {
            final String target = ((intent.getComponent() != null) ? intent.getComponent().getShortClassName() : intent.toUri(0));
            Log.pc(Log.ERROR, "sendBroadcast", "▶▶", clz, target, intent);
        } catch (Exception e) {

        }
    }
    public static void onResume(Class<?> clz) {
//		Log.po(Log.ERROR, "onResume", clz);
    }
    public static void onPause(Class<?> clz) {
//		Log.po(Log.WARN, "onPause", clz);
    }
    public static void onDetach(Class<?> clz) {
        Log.po(Log.WARN, "onDetach", clz);
    }
    public static void onDestroyView(Class<?> clz) {
        Log.po(Log.WARN, "onDestroyView", clz);
    }
    public static void onCreate(Class<?> clz, Bundle savedInstanceState) {
        Log.po(Log.ERROR, "onCreate", clz);
    }
    public static void onAttach(Class<?> clz, Context context) {
        Log.po(Log.ERROR, "onAttach", clz);
    }
    public static void onCreate(Class<?> clz) {
        Log.po(Log.ERROR, "onCreate", clz);
    }
    public static void onNewIntent(Class<?> clz) {
        Log.po(Log.ERROR, "onNewIntent", clz);
    }
    public static void onDestroy(Class<?> clz) {
        Log.po(Log.WARN, "onDestroy", clz);
    }
    public static void onStart(Class<?> clz) {
        Log.po(Log.ERROR, "onStart", clz);
    }
    public static void onStop(Class<?> clz) {
        Log.po(Log.WARN, "onStop", clz);
    }
    public static void onRestart(Class<?> clz) {
        Log.po(Log.INFO, "onRestart", clz);
    }

    public static ArrayList<Class<?>> CLZS = new ArrayList<Class<?>>();

    public static void startActivity(Class<?> clz, Intent intent) {
        startActivityForResult(clz, intent, -1);
    }

    public static void startActivity(Class<?> clz, Intent intent, @Nullable Bundle options) {
        startActivityForResult(clz, intent, -1, options);
    }

    public static void onActivityCreated(Class<?> clz, Bundle savedInstanceState) {
        Log.po(Log.ERROR, "onActivityCreated", clz);
    }

    public static void onActivityResult(Class<?> clz, int requestCode, int resultCode, Intent data) {
        if (!LOG)
            return;
        int level = resultCode == Activity.RESULT_OK ? Log.INFO : Log.ERROR;
        Log.po(level, "onActivityResult", "◀◀", clz, String.format("requestCode=0x%08x", requestCode)//
                , (resultCode == Activity.RESULT_OK ? "Activity.RESULT_OK" : "") + (resultCode == Activity.RESULT_CANCELED ? "Activity.RESULT_CANCELED" : ""));
        if (data != null && data.getExtras() != null)
            Log.p(level, data.getExtras());
    }

    public static int pc(int priority, String methodNameKey, Object... args) {
        if (!LOG)
            return -1;

        final StackTraceElement info = getStackC(methodNameKey);
        final String tag = getTag(info);
        final String locator = getLocator(info);
        final String msg = _MESSAGE(args);
        return println(priority, tag, locator, msg);
    }

    public static void startActivities(Class<?> clz, Intent[] intents) {
        if (!LOG)
            return;

        for (Intent intent : intents) {
            try {
                final String target = ((intent.getComponent() != null) ? intent.getComponent().getShortClassName() : intent.toUri(0));
                Log.pc(Log.ERROR, "startActivities", "▶▶", clz, target, intent);
//		printStackTrace();
            } catch (Exception e) {
            }
        }
    }

    public static void startActivityForResult(Class<?> clz, Intent intent, int requestCode) {
        if (!LOG)
            return;
        try {
            final String target = ((intent.getComponent() != null) ? intent.getComponent().getShortClassName() : intent.toUri(0));
            Log.pc(Log.ERROR, (requestCode == -1 ? "startActivity" : "startActivityForResult"), "▶▶", clz, target, intent, String.format("0x%08X", requestCode));
//		printStackTrace();
        } catch (Exception e) {
        }
    }

    public static void startActivityForResult(Class<?> clz, Intent intent, int requestCode, Bundle options) {
        if (!LOG)
            return;
        try {
            final String target = ((intent.getComponent() != null) ? intent.getComponent().getShortClassName() : intent.toUri(0));
            Log.pc(Log.ERROR, (requestCode == -1 ? "startActivity" : "startActivityForResult"), "▶▶", clz, target, intent, options, String.format("0x%08X", requestCode));
//		printStackTrace();
        } catch (Exception e) {
        }
    }

    public static void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!LOG)
            return;
        Log.e(parent.getClass(), parent.getItemAtPosition(position), view, position, id);
    }

    public static void measure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!LOG)
            return;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(String.format("0x%08x,0x%08x", widthMode, heightMode));
        Log.d(String.format("%10d,%10d", widthSize, heightSize));
    }

    //tools
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void printStackTrace() {
        if (!LOG)
            return;
        new 에러아님_Exception().printStackTrace();
    }

    public static void printStackTrace(Exception e) {
        if (!LOG)
            return;
        e.printStackTrace();
    }

    private static long LAST_ACTION_MOVE;

    public static void onTouchEvent(MotionEvent event) {
        if (!LOG)
            return;
        try {
            final int action = event.getAction() & MotionEvent.ACTION_MASK;
            if (action == MotionEvent.ACTION_MOVE) {
                long millis = System.currentTimeMillis();
                if (millis - LAST_ACTION_MOVE < 1000)
                    return;
                LAST_ACTION_MOVE = millis;
            }
            Log.e(event);
        } catch (Exception e) {
        }
    }

    public static void showTable(SQLiteDatabase db) {
        final Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
        e(c);
        c.close();
    }

    //	public static void onCreate(Class<? extends android.support.v4.app.Fragment> clz, FragmentActivity activity) {
//		if (!LOG)
//			return;
//		if (CLZS.contains(clz))
//			return;
//		Log.po(Log.ERROR, "onCreate", clz, activity.getClass());
//	}
//	public static void onDestroy(Class<? extends android.support.v4.app.Fragment> clz, FragmentActivity activity) {
//		if (!LOG)
//			return;
//		if (CLZS.contains(clz))
//			return;
//		Log.po(Log.ERROR, "onDestroy", clz, activity.getClass());
//	}
    //호환 팩
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
