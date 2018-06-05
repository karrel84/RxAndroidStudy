package karrel.com.switchmapsample.etc;


import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.BiFunction;

/**
 * Created by Rell on 2018. 6. 4..
 */
public class test {
    private void test() {
        Context c = null;
        String url = null;
        Observable.zip((ObservableSource<? extends Context>) c, null, new BiFunction<Context, String, String>() {
            @Override
            public String apply(Context o, String o2) throws Exception {
                return "";
            }
        });
    }
}
