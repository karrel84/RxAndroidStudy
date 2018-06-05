package karrel.com.switchmapsample.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import karrel.com.switchmapsample.R
import kotlinx.android.synthetic.main.item_image.view.*

/**
 * Created by kimmihye on 2018. 4. 15..
 */
class ImageAdapter(private val context: Context) : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    private var items: MutableList<String> = mutableListOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageHolder(parent)

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        items[position].let { url ->
            //            println("url : $url")
            Glide.with(context).load(url).into(holder.itemView.image)
        }
    }

    fun clearImages() {
//        notifyItemRangeRemoved(0, items.size)
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    items.clear()
                    notifyDataSetChanged()
                }
    }

    fun addImageUrl(url: String) {
        items.add(url)
        println("${items.size}. addImageUrl url : $url")
        if (items.size > 0) notifyItemInserted(items.size - 1)
    }

    class ImageHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image, parent, false))

}