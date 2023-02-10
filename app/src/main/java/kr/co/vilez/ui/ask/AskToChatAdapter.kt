package kr.co.vilez.ui.ask

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
import kr.co.vilez.databinding.ImminentListItemBinding
import kr.co.vilez.ui.chat.ChatRoomActivity
import kr.co.vilez.ui.share.ShareDetailActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE
import retrofit2.awaitResponse

private const val TAG = "빌리지_AskToChatAdapter"
class AskToChatAdapter(val list: MutableList<BoardData>):
RecyclerView.Adapter<AskToChatAdapter.ShareHolder>(){

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
        /**
         * ask : 내가 빌려준거 => 내가 무조건 공유자(shareUser), 상대방 : notShareUser
         */
        fun bindingInfo(item: BoardData) {
            binding.boardData = item

            binding.root.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(binding.root.context, ChatRoomActivity::class.java)
                    //val result = ApplicationClass.chatApi.isExistChatroom(item.boardId, item.type, ApplicationClass.prefs.getId()).awaitResponse().body()
                        intent.putExtra("roomId", item.roomId)
                        intent.putExtra("otherUserId", item.notShareUserId) // notShareUserId 가 상대
                        val otherUser = ApplicationClass.userApi.getUserDetail(item.notShareUserId).awaitResponse().body()
                        if(otherUser?.flag == "success") {
                            intent.putExtra("nickName", otherUser.data[0].nickName) // 채팅 상대의 닉네임
                            intent.putExtra("profile", otherUser.data[0].profile_img) // 채팅 상대의 프로필 이미지
                            Toast.makeText(binding.root.context, "예약이 진행된 채팅방으로 이동합니다.", Toast.LENGTH_SHORT).show()
                            binding.root.context.startActivity(intent)
                        } else { // 채팅 상대 정보 불러오기 불가
                            // 웹 채팅 버그로 안불러와질 수 있음. 이때는 게시글 상세보기로 이동.
                            val boardDetailIntent = if(item.type == BOARD_TYPE_SHARE) { // 디테일 상세보기로 이동
                                Intent(binding.root.context, ShareDetailActivity::class.java)
                            } else { // 요청글 상세보기로 이동
                                Intent(binding.root.context, AskDetailActivity::class.java)
                            }
                            boardDetailIntent.putExtra("boardId", item.boardId)
                            boardDetailIntent.putExtra("userId", item.boardWriterId)
                            Toast.makeText(binding.root.context, "이 약속의 채팅 정보를 불러올 수 없어서 게시글 상세보기로 이동합니다.", Toast.LENGTH_SHORT).show()
                            binding.root.context.startActivity(boardDetailIntent)
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