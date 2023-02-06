package kr.co.vilez.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kr.co.vilez.R

class ChatAdapter(val itemList: ArrayList<ChatlistData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left_item, parent, false)

        if(viewType == 1){
            return LeftViewHolder(view);
        } else if(viewType == 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.chat_center_item, parent, false)
            return CenterViewHolder(view);
        }
        view = LayoutInflater.from(parent.context).inflate(R.layout.chat_right_item, parent, false)

        return RightViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is LeftViewHolder) {
            if(itemList[position].profile != null) {
                Glide.with(holder.itemView)
                    .load(itemList[position].profile)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(holder.profile)
            }
            holder.content.text = itemList[position].content
        } else if(holder is RightViewHolder){
            holder.content.text = itemList[position].content
        } else if(holder is CenterViewHolder){
            holder.content.text = itemList[position].content
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.textv_msg)
        val profile = itemView.findViewById<ImageView>(R.id.left_user)

    }
    inner class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.textv_msg)

    }

    inner class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.textv_msg)

    }
}