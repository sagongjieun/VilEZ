package kr.co.vilez.ui.ask

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.data.dto.AskData

import kr.co.vilez.databinding.AskBoardListItemBinding

class AskListAdapter(val list: MutableList<AskData>):
RecyclerView.Adapter<AskListAdapter.AskHolder>(){

    var pos = -1
    private lateinit var itemClickListener: OnItemClickListener
    interface OnItemClickListener { // 클릭 이벤트 리스너 인터페이스
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(OnItemClickListener: OnItemClickListener) {
        this.itemClickListener = OnItemClickListener
    }

    inner class AskHolder(var binding: AskBoardListItemBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bindingInfo(item: AskData) {
            binding.askData = item
            binding.root.setOnClickListener {
                //itemClickListener.onClick(it, layoutPosition)
                Log.d("빌리지_요청_AskListAdapter", "bindingInfo: item 클릭!! :${item.toString()}")
                val intent = Intent(binding.root.context, AskDetailActivity::class.java)
                intent.putExtra("boardId", item.boardId)
                intent.putExtra("userId", item.userId)

                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AskHolder {
        // 아이템 하나를 바인딩해서 바로 xml을 찾아갈 수 있도록 함
        val binding = AskBoardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AskHolder(binding)
    }

    override fun onBindViewHolder(holder: AskHolder, position: Int) {
        holder.bindingInfo(list[position]) // 데이터 하나씩 넣기
    }

    override fun getItemCount(): Int = list.size

}