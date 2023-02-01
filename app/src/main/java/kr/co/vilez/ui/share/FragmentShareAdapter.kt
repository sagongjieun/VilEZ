package kr.co.vilez.ui.share

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.databinding.HomeBoardListItemBinding
import kr.co.vilez.ui.MainActivity

class FragmentShareAdapter(val list:MutableList<ShareData>):
RecyclerView.Adapter<FragmentShareAdapter.ShareHolder>(){

    var pos = -1
    private lateinit var itemClickListener: OnItemClickListener
    interface OnItemClickListener { // 클릭 이벤트 리스너 인터페이스
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(OnItemClickListener: OnItemClickListener) {
        this.itemClickListener = OnItemClickListener
    }

    inner class ShareHolder(var binding:HomeBoardListItemBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bindingInfo(item: ShareData) {
            binding.shareData = item
            binding.root.setOnClickListener {
                //itemClickListener.onClick(it, layoutPosition)
                val intent = Intent(binding.root.context, ShareDetailActivity::class.java)
                intent.putExtra("boardId", item.board_id)

                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareHolder {
        // 아이템 하나를 바인딩해서 바로 xml을 찾아갈 수 있도록 함
        val binding = HomeBoardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShareHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareHolder, position: Int) {
        holder.bindingInfo(list[position]) // 데이터 하나씩 넣기
    }

    override fun getItemCount(): Int = list.size

}