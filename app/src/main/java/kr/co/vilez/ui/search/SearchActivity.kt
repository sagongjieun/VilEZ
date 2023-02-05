package kr.co.vilez.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityShareSearchBinding
import kr.co.vilez.ui.dialog.AlertDialog
import kr.co.vilez.data.dto.ShareData
import kr.co.vilez.ui.share.ShareListAdapter
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse

private const val TAG = "빌리지_SearchActivity"
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareSearchBinding
    private var keyword = ""

    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareDatas: ArrayList<ShareData>
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_search)
        binding.activity = this

        binding.userLocationValid = ApplicationClass.prefs.getLng() != "0.0" && ApplicationClass.prefs.getLat() != "0.0"

        initFilterCheckBox()
        initSearchBtn()
        initTabLayout() // 탭 아이템 변경 리스너 등록
    }

    private fun initTabLayout() { // 탭 아이템 변경 리스너
        binding.searchTabLayout.addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: 탭 포지션: ${tab!!.position}")
                when (tab!!.position){
                    0 -> search(0, binding.checkboxShare.isChecked) // 공유 검색
                    1 -> search(1) // 요청 검색
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    fun search(flag:Int, filtered:Boolean = false) {
        when(flag) {
            0 -> { // 공유 검색
                initShareData(filtered)
            }
            1 -> { // 요청 검색
                initAskData()
            }
        }
    }

    private fun initFilterCheckBox() {
        binding.checkboxShare.isChecked = false // 디폴트 : 모두 보기
        binding.checkboxShare.setOnCheckedChangeListener { button, b ->
            keyword = binding.etSearch.text.toString()
            if(keyword.isEmpty()) {
                val dialog = AlertDialog(this@SearchActivity, "1글자 이상 입력해주세요.")
                dialog.show(supportFragmentManager, "SearchFailed")
            }
            if(keyword.length > 1) {
                Log.d(TAG, "initSearchBtn: 선택된 탭: ${binding.searchTabLayout.selectedTabPosition}, 엔터 눌러서 검색함 : $keyword ")
                if(b) { // 체크된 경우 => 공유가능만 보기
                        search(binding.searchTabLayout.selectedTabPosition, true)
                } else { // 체크 해제된 경우 => 전체보기
                    search(binding.searchTabLayout.selectedTabPosition)
                }
            }
        }
    }


    private fun initShareData(filtered: Boolean=false) {
        // 데이터 가져오기
        shareDatas = arrayListOf()

        // 어댑터 생성
        shareAdapter = ShareListAdapter(shareDatas)
        shareAdapter.setItemClickListener(object : ShareListAdapter.OnItemClickListener {
            // listview item 클릭시 실행할 메소드
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "onClick: ${shareDatas[position].title} clicked!")
            }
        })

        // 리사이클러뷰에 어댑터 등록
        binding.rvShareSearch.apply {
            adapter = shareAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        var num = 0;
        var max = 10;
        val lat = ApplicationClass.prefs.getLat();
        val lng = ApplicationClass.prefs.getLng();
        Log.d(TAG, "$lat $lng")
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitShareService.boardSearchList(
                    num++,
                    0,
                    max,
                    ApplicationClass.prefs.getId(),
                    keyword!!
                ).awaitResponse().body();
            Log.d(TAG, "onViewCreated: 공유 검색 데이터 불러오는중 result : $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!!")
                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvWarnNoResult.visibility = View.VISIBLE
                } else {
                    binding.tvWarnNoResult.visibility = View.GONE
                }
                for (data in result.data) {
                    var shareData: ShareData
                    if(filtered && data.shareListDto.state == 1) continue // 공유가능만 보기인데 공유중인 글은 skip
                    if (data.shareListDto.list.isEmpty() ) { // 이미지 없는거 테스트용
                        shareData = ShareData(
                            data.shareListDto.id,
                            "https://kr.object.ncloudstorage.com/vilez/basicProfile.png",
                            data.shareListDto.title,
                            Common.elapsedTime(data.shareListDto.date),
                            "구미",
                            data.shareListDto.startDay + "~"
                                    + data.shareListDto.endDay,
                            Integer.toString(data.listCnt),
                            data.shareListDto.state,
                            data.shareListDto.userId
                        );
                    } else {
                        shareData = ShareData(
                            data.shareListDto.id,
                            data.shareListDto.list[0].path,
                            data.shareListDto.title,
                            Common.elapsedTime(data.shareListDto.date),
                            "구미",
                            data.shareListDto.startDay + "~"
                                    + data.shareListDto.endDay,
                            Integer.toString(data.listCnt),
                            data.shareListDto.state,
                            data.shareListDto.userId
                        );
                    }
                    shareDatas.add(shareData)
//                    Log.d(TAG, "initView: ${data.shareListDto.nickName}");
//                    Log.d(TAG, "initView: ${data.shareListDto.list[0].path}");
                }
            }
            shareAdapter.notifyItemInserted(index - 1)
        }

        binding.rvShareSearch.setOnScrollChangeListener { v, scollX, scrollY,
                                                          oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result =
                        ApplicationClass.retrofitShareService.boardSearchList(
                            num++,
                            0,
                            max,
                            ApplicationClass.prefs.getId(),
                            keyword!!
                        ).awaitResponse()
                            .body();
                    Log.d(TAG, "initView: ${result?.data}")
                    if (result?.data?.size != 0) {
                        if (result?.flag == "success") {
                            Log.d(TAG, "initShareData: success!!!!!")
                            for (data in result.data) {
                                if(filtered && data.shareListDto.state == 1) continue // 공유가능만 보기인데 공유중인 글은 skip
                                var shareData: ShareData
                                if (data.shareListDto.list.size == 0) {
                                    shareData = ShareData(
                                        data.shareListDto.id,
                                        "https://kr.object.ncloudstorage.com/vilez/basicProfile.png",
                                        data.shareListDto.title,
                                        Common.elapsedTime(data.shareListDto.date),
                                        "구미",
                                        data.shareListDto.startDay + "~"
                                                + data.shareListDto.endDay,
                                        Integer.toString(data.listCnt),
                                        data.shareListDto.state,
                                        data.shareListDto.userId,
                                    );
                                } else {
                                    shareData = ShareData(
                                        data.shareListDto.id,
                                        data.shareListDto.list[0].path,
                                        data.shareListDto.title,
                                        Common.elapsedTime(data.shareListDto.date),
                                        "구미",
                                        data.shareListDto.startDay + "~"
                                                + data.shareListDto.endDay,
                                        Integer.toString(data.listCnt),
                                        data.shareListDto.state,
                                        data.shareListDto.userId,
                                    );
                                }
                                shareDatas.add(shareData)
                            }
                        }
                        shareAdapter.notifyItemInserted(index - 1)
                    }
                }
            }
        }
    }

    fun initAskData() {

    }
    fun onBackPressed(view: View) {
        finish()
    }

    fun initSearchBtn() {
        binding.etSearch.setOnEditorActionListener { _, actionId, keyEvent ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    keyword = binding.etSearch.text.toString()
                    if(keyword.isEmpty()) {
                        val dialog = AlertDialog(this@SearchActivity, "1글자 이상 입력해주세요.")
                        dialog.show(supportFragmentManager, "SearchFailed")
                    }
                    if(keyword.length > 1) {
                        Log.d(TAG, "initSearchBtn: 선택된 탭: ${binding.searchTabLayout.selectedTabPosition}, 엔터 눌러서 검색함 : $keyword ")
                        search(binding.searchTabLayout.selectedTabPosition, binding.checkboxShare.isChecked)
                    }
                }
            }
            return@setOnEditorActionListener true
        }
    }


    fun moveToUserLocationSetting(view: View) { // 동네설정으로 넘어가는
        val intent = Intent(this@SearchActivity, ProfileMenuActivity::class.java)
        intent.putExtra("fragment", "내 동네 설정")
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }


}