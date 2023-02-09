package kr.co.vilez.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.data.dto.BoardData
import kr.co.vilez.databinding.BoardListItemBinding
import kr.co.vilez.databinding.HomeBoardListItemBinding
import kr.co.vilez.ui.ask.AskDetailActivity
import kr.co.vilez.ui.share.ShareDetailActivity
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE

class BoardListAdapter(val list: MutableList<BoardData>):
RecyclerView.Adapter<BoardListAdapter.ShareHolder>(){

    var pos = -1
    private lateinit var itemClickListener: OnItemClickListener
    interface OnItemClickListener { // 클릭 이벤트 리스너 인터페이스
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(OnItemClickListener: OnItemClickListener) {
        this.itemClickListener = OnItemClickListener
    }

    inner class ShareHolder(var binding: BoardListItemBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bindingInfo(item: BoardData) {
            binding.boardData = item
            binding.root.setOnClickListener { // 채팅방으로 이동하게 하기
                val intent = if(item.type == BOARD_TYPE_SHARE) {
                    Intent(binding.root.context, ShareDetailActivity::class.java)
                } else { // BOARD_TYPE_ASK
                    Intent(binding.root.context, AskDetailActivity::class.java)
                }
                intent.putExtra("boardId", item.boardId)
                intent.putExtra("userId", item.boardWriterId)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareHolder {
        // 아이템 하나를 바인딩해서 바로 xml을 찾아갈 수 있도록 함
        val binding = BoardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShareHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareHolder, position: Int) {
        holder.bindingInfo(list[position]) // 데이터 하나씩 넣기
    }

    override fun getItemCount(): Int = list.size

}