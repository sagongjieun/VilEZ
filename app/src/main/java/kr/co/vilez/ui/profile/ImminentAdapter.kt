package kr.co.vilez.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.data.dto.BoardData
import kr.co.vilez.databinding.BoardListItemBinding
import kr.co.vilez.databinding.HomeBoardListItemBinding
import kr.co.vilez.databinding.ImminentListItemBinding
import kr.co.vilez.ui.ask.AskDetailActivity
import kr.co.vilez.ui.chat.ChatRoomActivity
import kr.co.vilez.ui.share.ShareDetailActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE
import retrofit2.awaitResponse

class ImminentAdapter(val list: MutableList<BoardData>):
RecyclerView.Adapter<ImminentAdapter.ShareHolder>(){

    var pos = -1
    private lateinit var itemClickListener: OnItemClickListener
    interface OnItemClickListener { // 클릭 이벤트 리스너 인터페이스
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(OnItemClickListener: OnItemClickListener) {
        this.itemClickListener = OnItemClickListener
    }

    inner class ShareHolder(var binding: ImminentListItemBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bindingInfo(item: BoardData) {
            binding.boardData = item
            binding.root.setOnClickListener {
                // TODO : 클릭시 채팅창으로 가게 해야함
                val intent = Intent(binding.root.context, ChatRoomActivity::class.java)
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.retrofitChatService.isExistChatroom(item.boardId, item.type, ApplicationClass.prefs.getId()).awaitResponse().body()
                    if(result?.flag == "success") {
                        intent.putExtra("roomId", result.data[0].id)
                        if(result.data[0].type == BOARD_TYPE_SHARE) {
                            // 공유 글이면 내가 대여
                            intent.putExtra("otherUserId", result.data[0].notShareUserId)
                            val writer = ApplicationClass.retrofitUserService.getUserDetail(result.data[0].notShareUserId).awaitResponse().body()
                            if(writer?.flag == "success") {
                                intent.putExtra("nickName", writer.data[0].nickName) // 글 작성자 닉네임
                                intent.putExtra("profile", writer.data[0].profile_img) // 글 작성자 프로필 이미지
                                binding.root.context.startActivity(intent)
                            } else {
                                Toast.makeText(binding.root.context, "이 약속의 채팅 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val writer = ApplicationClass.retrofitUserService.getUserDetail(result.data[0].shareUserId).awaitResponse().body()
                            // 대여 글이면 내가 공유
                            intent.putExtra("otherUserId", result.data[0].shareUserId)
                            if(writer?.flag == "success") {
                                intent.putExtra("nickName", writer.data[0].nickName) // 글 작성자 닉네임
                                intent.putExtra("profile", writer.data[0].profile_img) // 글 작성자 프로필 이미지
                                binding.root.context.startActivity(intent)
                            } else {
                                Toast.makeText(binding.root.context, "이 약속의 채팅 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    } else {
                        Toast.makeText(binding.root.context, "이 약속의 채팅 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareHolder {
        // 아이템 하나를 바인딩해서 바로 xml을 찾아갈 수 있도록 함
        val binding = ImminentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShareHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareHolder, position: Int) {
        holder.bindingInfo(list[position]) // 데이터 하나씩 넣기
    }

    override fun getItemCount(): Int = list.size

}