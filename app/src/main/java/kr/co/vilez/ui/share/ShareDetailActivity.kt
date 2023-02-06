package kr.co.vilez.ui.share

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kr.co.vilez.R
import kr.co.vilez.data.model.Bookmark
import kr.co.vilez.data.model.Chatroom
import kr.co.vilez.data.model.RoomlistData
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.ActivityShareDetailBinding
import kr.co.vilez.ui.IntroActivity
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.board.BoardImagePagerAdapter
import kr.co.vilez.ui.board.BoardMapFragment
import kr.co.vilez.ui.chat.ChatRoomActivity
import kr.co.vilez.ui.dialog.AlertDialogInterface
import kr.co.vilez.ui.dialog.AlertDialogWithCallback
import kr.co.vilez.ui.dialog.ConfirmDialog
import kr.co.vilez.ui.dialog.ConfirmDialogInterface
import kr.co.vilez.ui.share.write.ShareWriteActivity
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE
import kr.co.vilez.util.DataState
import me.relex.circleindicator.CircleIndicator3
import retrofit2.awaitResponse


private const val TAG = "빌리지_ShareDetailActivity"
class ShareDetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityShareDetailBinding
    private lateinit var mPager: ViewPager2
    private var pagerAdapter: FragmentStateAdapter? = null
    private var mIndicator: CircleIndicator3? = null
    private var boardId:Int? = 0
    private var userId:Int? = 0
    private var otherUserId:Int? = 0

    private lateinit var mapLat:String
    private lateinit var mapLng:String

    private lateinit var mContext:FragmentActivity

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_detail)
        binding.activity = this
        boardId = intent.getIntExtra("boardId", 0)
        userId = intent.getIntExtra("userId", 0)
        Log.d(TAG, "onCreate: boardId: $boardId $userId")
        setContentView(binding.root)
        mContext = this@ShareDetailActivity
        initData()
        initToolBar()
        if(userId == ApplicationClass.prefs.getId()) {
            binding.btnChat.visibility = View.INVISIBLE
        }
    }

    private fun initMap() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.share_detail_map, BoardMapFragment.newInstance(mapLat.toDouble(), mapLng.toDouble()))
            .commit()
    }
        //boardId
    fun clickBookmark(view: View) {
        if(binding.bookmark!!) {
            // 관심목록에서 삭제
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitShareService.deleteBookmark(boardId!!, ApplicationClass.prefs.getId()).awaitResponse().body()
                if(result?.flag == "success") {
                    binding.bookmark = false
                    binding.article!!.bookmarkCnt--
                    binding.tvBookmark.text = binding.article!!.bookmarkCnt.toString()
                } else {
                    Snackbar.make(view, "관심목록 삭제를 실패했습니다.", Snackbar.LENGTH_SHORT).show();
                }
            }
        } else {
            // 관심목록에 추가
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitShareService.addBookmark(Bookmark(boardId!!, ApplicationClass.prefs.getId())).awaitResponse().body()
                if(result?.flag == "success") {
                    binding.bookmark = true
                    binding.article!!.bookmarkCnt++
                    binding.tvBookmark.text = binding.article!!.bookmarkCnt.toString()
                    Snackbar.make(
                        view,
                        "나의 관심글에 추가됐습니다.",
                        Snackbar.LENGTH_SHORT
                    ) // 스낵바 Action 설정("표시할 텍스트", onClick)
                        .setAction("관심목록 보기") { view -> // 스낵바의 OK 클릭시 실행할 작업
                            val intent = Intent(this@ShareDetailActivity, ProfileMenuActivity::class.java)
                            intent.putExtra("fragment", "나의 관심글")
                            startActivity(intent)
                        }.show()
                } else {
                    Snackbar.make(view, "나의 관심글 추가를 실패했습니다.", Snackbar.LENGTH_SHORT).show();
                }
            }

        }
    }

    fun onChatBtnClick(view: View) {
        if(binding.article!!.state == 0) { // 공유 가능
            Snackbar.make(view, "공유가능한 물건 채팅 시작하기", Snackbar.LENGTH_SHORT).show();
        } else { // 공유중 => 예약
            Snackbar.make(view, "공유중인 물건 예약 시작하기", Snackbar.LENGTH_SHORT).show();
        }
        CoroutineScope(Dispatchers.Main).launch {
            // 먼저 채팅방이 존재하는지 확인하기
            val isExist = ApplicationClass.retrofitChatService.isExistChatroom(boardId!!,
                Common.BOARD_TYPE_SHARE, ApplicationClass.prefs.getId()).awaitResponse().body()
            if(isExist?.flag == "success" ) { // 이미 채팅방이 존재함
                Log.d(TAG, "onChatBtnClick: 채팅방 이미 존재")
                Toast.makeText(this@ShareDetailActivity, "이미 채팅중인 게시글이어서\n기존 채팅방으로 이동합니다.", Toast.LENGTH_SHORT).show()
                val fragment = supportFragmentManager.findFragmentById(R.id.share_detail_map)
                if(fragment != null)
                    supportFragmentManager.beginTransaction().remove(fragment!!).commitNow()
                //todo 확인할 필요있음!!!!!! 서버도 봅시다
                val intent = Intent(this@ShareDetailActivity, ChatRoomActivity::class.java)
                Log.d(TAG, "otherUserId $userId")
                intent.putExtra("roomId", isExist.data[0].id)
                intent.putExtra("otherUserId", userId!!)
                intent.putExtra("nickName", binding.writer!!.nickName)
                intent.putExtra("profile", binding.writer!!.profile_img)
                intent.putExtra("type", Common.BOARD_TYPE_SHARE)
                DataState.set.add(isExist.data[0].id)
                startActivity(intent)

            } else if (isExist?.flag == "fail") { // 채팅방 없음 => 새로 만들기
                Log.d(TAG, "onChatBtnClick: 새로운 채팅방 만들기")
                val chatRoom = Chatroom(boardId!!, 0,  ApplicationClass.prefs.getId(), userId!!,
                    Common.BOARD_TYPE_SHARE
                )
                val result = ApplicationClass.retrofitChatService.createChatroom(chatRoom).awaitResponse().body()
                if(result?.flag == "success") {

                    val fragment = supportFragmentManager.findFragmentById(R.id.share_detail_map)
                    if(fragment != null)
                        supportFragmentManager.beginTransaction().remove(fragment!!).commitNow()

                    val intent = Intent(this@ShareDetailActivity, ChatRoomActivity::class.java)
                    intent.putExtra("roomId", result.data[0].id)
                    intent.putExtra("otherUserId", userId!!)
                    intent.putExtra("nickName", binding.writer!!.nickName)
                    intent.putExtra("profile", binding.writer!!.profile_img)
                    intent.putExtra("type", Common.BOARD_TYPE_SHARE)
                    DataState.set.add(result.data[0].id)

                    DataState.itemList.add(
                        0, RoomlistData(
                            result.data[0].id,
                            binding.writer!!.nickName,
                            "대화가 시작 되었습니다.",
                            "",
                             userId!!,
                            1,
                            binding.writer!!.profile_img
                        )
                    )
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this@ShareDetailActivity, "채팅방 생성을 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initToolBar() {
        this.setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "공유 게시글 상세보기"
    }

    fun onBackPressed(view: View) {
        finish()
    }

    private fun initData(){
        var count = 0
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitShareService.getBoardDetail(boardId!!).awaitResponse().body()

            // 북마크 정보 가져오기
            Log.d(TAG, "initData 북마크: boardId: $boardId, userId: ${ApplicationClass.prefs.getId()}")
            val bookmarkResult = ApplicationClass.retrofitShareService.getShareBookmark(boardId!!, ApplicationClass.prefs.getId()).awaitResponse().body()
            if(bookmarkResult?.flag == "success") {
                binding.bookmark = (bookmarkResult.data as List<Bookmark>)[0] != null
            } else {
                binding.bookmark = false
            }
            Log.d(TAG, "init: $result")
            if (result?.flag =="success") {
                /*
      *****************************************************************************************************
      **************************************** 모바일 캐러셀(carousel) **************************************
      ****************************************      pageNavigator    **************************************
      *****************************************************************************************************
       */
                Log.d(TAG, "init: @@@공유 디테일 ${result.data[0]}")
                binding.article = result.data[0]

                // 희망 공유 장소를 위한 맵 초기화
                mapLat = result.data[0].hopeAreaLat
                mapLng = result.data[0].hopeAreaLng
                Log.d(TAG, "initData: lat: $mapLat, lng: $mapLng")
                initMap()


                // 해당 글을 작성한 작성자 데이터 가져오기
                val userResult = ApplicationClass.retrofitUserService.getUserDetail(result.data[0].userId).awaitResponse().body()
                Log.d(TAG, "initData: @@@@@@@@공유글 작성자: ${result.data[0].userId}, ${result.data[0]}")
                otherUserId = result.data[0].userId
                if(userResult?.flag == "success") {
                    binding.writer = userResult.data[0]
                } else {
                    return@launch
                }

                Log.d(TAG, "init: success됨, result:$result")
                count = result!!.data[0].list.size
                val myData = result.data[0].list
                if(count == 0) {
                   Log.d(TAG, "init: 사진 없는 게시글임... 사진 안보잉게 하기")
                    binding.llPhoto.visibility = View.GONE

                } else {
                    binding.llPhoto.visibility = View.VISIBLE

                    mPager = binding.viewpager
                    //Adapter
                    pagerAdapter = BoardImagePagerAdapter(mContext, count, myData)
                    Log.d(TAG, "init: pagerAdapter: ${pagerAdapter}")
                    mPager.adapter = pagerAdapter

                    mIndicator = binding.indicator
                    mIndicator!!.setViewPager(mPager)
                    mIndicator!!.createIndicators(count, 0);

                    mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    mPager!!.setCurrentItem(1000); //시작 지점
                    mPager!!.setOffscreenPageLimit(5); //최대 이미지 수

                    mPager!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int
                        ) {
                            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                            if (positionOffsetPixels == 0) {
                                mPager!!.currentItem = position
                            }
                        }

                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            mIndicator!!.animatePageSelected(position % count)
                        }
                    })

                    println("*******************************************************")
                    println("*******************              **********************")
                    println("${result?.data}")
                    println("*******************              **********************")
                    println("*******************************************************")
                }
            } else {
                Log.d(TAG, "init: fail, result:$result")
                Log.d(TAG, "init: 사진 없는 게시글,,,? 가리기")
                binding.llPhoto.visibility = View.GONE
                Toast.makeText(this@ShareDetailActivity, "불러올 수 없는 게시글입니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        isReady = true // 데이터 모두 준비된경우
    } // end of initData()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 게시글 작성자가 나인지 확인한다
        // 나인경우 수정하기, 삭제하기 버튼이 보여야 하고 채팅하기 버튼 비활성화
        if(ApplicationClass.prefs.getId() != otherUserId) { // 내가 작성자가 아니면 수정/삭제 메뉴 없애기
            Log.d(TAG, "checkWriter: 내가 작성자 아님/")
            return super.onCreateOptionsMenu(menu)
        } else {
            Log.d(TAG, "checkWriter: 내가작성자")
            menuInflater.inflate(R.menu.board_top_app_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.board_edit -> { // 게시글 수정하기 버튼 클릭
                val intent = Intent(this@ShareDetailActivity, ShareWriteActivity::class.java)
                intent.putExtra("type", BOARD_TYPE_SHARE)
                intent.putExtra("boardId", boardId)
                startActivity(intent)
                finish()
            }
            R.id.board_remove -> { // 게시글 삭제
                val dialog = ConfirmDialog(object : ConfirmDialogInterface {
                    override fun onYesButtonClick(id: String) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result = ApplicationClass.retrofitShareService.deleteShareBoard(boardId!!).awaitResponse().body()
                            if(result?.flag == "success") { // 삭제 성공
                                // TODO : 채팅 목록 삭제시키기 (or 채팅종료)
                                // TODO : 채팅 목록 삭제시키기 (or 채팅종료)
                                Toast.makeText(this@ShareDetailActivity, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ShareDetailActivity, MainActivity::class.java)
                                intent.putExtra("target", "홈")
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@ShareDetailActivity, "게시글 삭제를 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }, "정말로 삭제하시겠습니까?\n진행중인 채팅 목록도 종료됩니다.", "")
                dialog.isCancelable = true // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.show(supportFragmentManager, "DeleteShareBoard")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch() {
            while(!isReady) {
                delay(500)
            }
            Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@onStart: isReady = true")
            binding.contentContainer.visibility = View.VISIBLE
        }
    }
}