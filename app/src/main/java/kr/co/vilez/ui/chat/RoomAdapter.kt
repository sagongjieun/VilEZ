package kr.co.vilez.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.R


class RoomAdapter(val itemList: ArrayList<RoomlistData>) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.roomlist_item, parent, false)
        return RoomViewHolder(view)
    }


    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {

        holder.nick_name.text = itemList[position].nickName
        holder.area.text = itemList[position].area
        holder.content.text = itemList[position].content

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nick_name = itemView.findViewById<TextView>(R.id.chat_nick_name)
        val area = itemView.findViewById<TextView>(R.id.chat_area)
        val content = itemView.findViewById<TextView>(R.id.chat_content)

    }


}