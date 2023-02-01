
package kr.co.vilez.ui.share.write

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.request.RequestOptions
import kr.co.vilez.R
import kr.co.vilez.databinding.WriteBoardImageItemBinding


private const val TAG = "빌리지_BoardWriteImageAdapter"
class BoardWriteImageAdapter(private val context: Context, private val dataList: MutableList<Uri> ) : RecyclerView.Adapter<BoardWriteImageAdapter.ImageHolder>() {

    lateinit var deleteClickListener: OnItemClickListener
    interface OnItemClickListener { // 클릭 이벤트 리스너 인터페이스
        fun onClick(view: View, position: Int)
    }

    // 개별 데이터(ImageHolder)를 item_horizontal_recyclerview.xml과 연결하는 holder 구성
    inner class ImageHolder(var binding: WriteBoardImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bindInfo(data: Uri, pos:Int) {
            if(pos == 0) {
                binding.tvFirst.visibility = View.VISIBLE
            } else {
                binding.tvFirst.visibility = View.GONE
            }
            binding.btnDelete.setOnClickListener { // 삭제 버튼 클릭
                dataList.removeAt(pos)
                this@BoardWriteImageAdapter.notifyDataSetChanged() // 전체 변경 해줘야 0번 삭제시 대표이미지 글씨 생김
                deleteClickListener.onClick(it, pos)
            }

            //로딩시 로딩이미지 보이기
            Glide.with(context).load(data).apply(
                RequestOptions().placeholder(R.drawable.loading_animation)).into(binding.ivPhoto)
        }
    }

    // 아이템과 아이템 레이아웃을 바인딩 하는 UserInfoHolder 생성 및 반환
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ImageHolder {
        // 아이템 하나를 바인딩해서 바로 xml을 찾아갈 수 있도록 함
        val binding = WriteBoardImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardWriteImageAdapter.ImageHolder, position: Int ) {
        holder.apply {
            bindInfo(dataList[position], position)
            /*itemView.setOnClickListener {
                Toast.makeText(context, "클릭! 위치 $position", Toast.LENGTH_SHORT).show()
            }
            itemView.setOnLongClickListener {
                Toast.makeText(context, "롱~~ 클릭! 위치 $position", Toast.LENGTH_SHORT).show()
                false
            }*/
        }
    }

    override fun getItemCount(): Int = dataList.size


}


