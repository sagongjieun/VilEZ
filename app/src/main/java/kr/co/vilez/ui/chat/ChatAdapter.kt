package kr.co.vilez.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.R

class ChatAdapter(val itemList: ArrayList<ChatlistData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left_item, parent, false)

        if(viewType == 1){
            return LeftViewHolder(view);
        }
        view = LayoutInflater.from(parent.context).inflate(R.layout.chat_right_item, parent, false)

        return RightViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is LeftViewHolder) {
            holder.content.text = itemList[position].content
        } else if(holder is RightViewHolder){
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

    }
    inner class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.textv_msg)

    }

}