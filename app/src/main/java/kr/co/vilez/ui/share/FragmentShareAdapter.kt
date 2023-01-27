package kr.co.vilez.ui.share

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.vilez.databinding.RecyclerviewItemBinding
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

    inner class ShareHolder(var binding:RecyclerviewItemBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bindingInfo(item: ShareData) {
            binding.path = item.iv_profile
            binding.root.setOnClickListener {
                //itemClickListener.onClick(it, layoutPosition)
                val intent = Intent(binding.root.context, MainActivity::class.java)
                binding.root.context.startActivity(intent)
            }
//            binding.ivProfile.set(item.iv_profile)
            binding.tvName.text = item.tv_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareHolder {
        // 아이템 하나를 바인딩해서 바로 xml을 찾아갈 수 있도록 함
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShareHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareHolder, position: Int) {
        holder.bindingInfo(list[position]) // 데이터 하나씩 넣기
    }

    override fun getItemCount(): Int = list.size

}