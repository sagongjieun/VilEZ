package kr.co.vilez.ui.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.R


class ChatAdapter(val itemList: ArrayList<ChatlistData>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatlist_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        holder.nick_name.text = itemList[position].nickName
        holder.area.text = itemList[position].area
        holder.content.text = itemList[position].content
    }

    override fun getItemCount(): Int {
        println(itemList.count())
        return itemList.count()
    }


    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nick_name = itemView.findViewById<TextView>(R.id.chat_nick_name)
        val area = itemView.findViewById<TextView>(R.id.chat_area)
        val content = itemView.findViewById<TextView>(R.id.chat_content)
    }


}