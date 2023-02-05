package kr.co.vilez.ui.ask

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.data.dto.BoardListDto
import kr.co.vilez.databinding.AskBoardListItemBinding
import kr.co.vilez.ui.share.ShareDetailActivity

//class ViewmodelAskListAdapter(private var items:LiveData<ArrayList<BoardListDto>>):
//RecyclerView.Adapter<ViewmodelAskListAdapter.ShareHolder>(){
//    var rvItems = ArrayList<BoardListDto>()
//    var pos = -1
//    private lateinit var itemClickListener: OnItemClickListener
//    interface OnItemClickListener { // 클릭 이벤트 리스너 인터페이스
//        fun onClick(view: View, position: Int)
//    }
//
//    fun setItemClickListener(OnItemClickListener: OnItemClickListener) {
//        this.itemClickListener = OnItemClickListener
//    }
//
//    fun setData(newRvItems: ArrayList<BoardListDto>) {
//        val diffCallback = DiffCallback(rvItems, newRvItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        rvItems.clear()
//        rvItems.addAll(newRvItems)
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    inner class ShareHolder(var binding: AskBoardListItemBinding):
//    RecyclerView.ViewHolder(binding.root) {
//
//        fun bindingInfo(item: BoardListDto) = with(binding){
//            askData = item
//            root.setOnClickListener {
//                val intent = Intent(root.context, ShareDetailActivity::class.java)
//                intent.putExtra("boardId", item.boardId)
//                intent.putExtra("userId", item.userId)
//                root.context.startActivity(intent)
//            }
//        }
//    }
//
//    inner class DiffCallback(
//        private var oldList: ArrayList<BoardListDto>,
//        private var newList: ArrayList<BoardListDto>
//    ): DiffUtil.Callback() {
//        override fun getOldListSize(): Int = oldList.size
//
//        override fun getNewListSize(): Int = newList.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldList[oldItemPosition] == newList[newItemPosition]
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldList[oldItemPosition].boardId == newList[newItemPosition].boardId
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareHolder {
//        // 아이템 하나를 바인딩해서 바로 xml을 찾아갈 수 있도록 함
//        val binding = AskBoardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//
//        return ShareHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ShareHolder, position: Int) {
//        items.value?.get(position)?.let {
//            holder.bindingInfo(it) // 데이터 하나씩 넣기
//        }
//    }
//
//    override fun getItemCount(): Int = items.value?.size!!

//}