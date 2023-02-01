package kr.co.vilez.ui.share.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.vilez.R


data class Category(val name: String, val img: Int)
private const val TAG = "빌리지_GridRecyclerViewAdapter"
class GridRecyclerViewAdapter(private val context: Context,
                              private val dataList: MutableList<Category>) :
    RecyclerView.Adapter<GridRecyclerViewAdapter.CategoryViewHolder>() {
    // 외부에서 OnItemClickListener를 공급 받기 위한 작업
    interface OnItemClickListener {
        fun onClick(view: View, position: Int)
    }

    lateinit var onItemClickListener : OnItemClickListener

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.apply {
            bindInfo(dataList[position])
            itemView.setOnClickListener {
                onItemClickListener.onClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class CategoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindInfo(category: Category) {
            view.findViewById<TextView>(R.id.tv_category_title).text = category.name
            Glide.with(view.context).load(category.img).into(view.findViewById(R.id.iv_category))

            //로딩시 로딩이미지 보이기
                Glide.with(view.context).load(category.img).apply(
                    RequestOptions().placeholder(R.drawable.loading_animation)).into(view.findViewById(R.id.iv_category))
        }
    }
}