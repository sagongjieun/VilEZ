package kr.co.vilez.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.dto.AskData
import kr.co.vilez.databinding.ActivityShareSearchBinding
import kr.co.vilez.ui.dialog.MyAlertDialog
import kr.co.vilez.data.dto.ShareDto
import kr.co.vilez.ui.ask.AskListAdapter
import kr.co.vilez.ui.share.ShareListAdapter
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_ASK
import retrofit2.awaitResponse

private const val TAG = "빌리지_SearchActivity"

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareSearchBinding

    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareDatas: ArrayList<ShareDto>
    private lateinit var askAdapter: AskListAdapter
    private lateinit var askDatas: ArrayList<AskData>
    private var index = 0

    private var type = 0
    private val SEARCH_SHARE = 0
    private val SEARCH_ASK = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_search)
        binding.activity = this

        binding.userLocationValid =
            ApplicationClass.prefs.getLng() != "0.0" && ApplicationClass.prefs.getLat() != "0.0"
        type = intent.getIntExtra("type", 0)
        initFilterCheckBox()
        initSearchBtn()
        initTabLayout() // 탭 아이템 변경 리스너 등록
        initChipGroup() // 칩 선택 리스너 등록
    }


    private fun initSearchBtn() {
        binding.etSearch.setOnEditorActionListener { _, actionId, keyEvent ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    Log.d(TAG, "@@@@@@@@@@initSearchBtn: 검색버튼클릭 입력값: ${binding.etSearch.text.toString()}")
                    Log.d(TAG, "@@@@@@@@@@initSearchBt: 선택된 탭: ${binding.searchTabLayout.selectedTabPosition}")
                    search(binding.searchTabLayout.selectedTabPosition, binding.checkboxShare.isChecked)
                }
            }
            return@setOnEditorActionListener true
        }
    }


    private fun initChipGroup() {

        binding.chipGroupCategory.setOnCheckedChangeListener { group, _ ->
            group.children.toList().filter { (it as Chip).isChecked }.forEach {
                val chip = it as Chip
                if(chip.text.isNotEmpty()) {
                    
                }
                when (binding.searchTabLayout.selectedTabPosition) {
                    SEARCH_SHARE -> {
                        search(
                            SEARCH_SHARE,
                            binding.checkboxShare.isChecked
                        )
                    }
                    SEARCH_ASK -> {
                        search(SEARCH_ASK)
                    }
                }
            }
        }
    }

    private fun initTabLayout() { // 탭 아이템 변경 리스너
        if (type == BOARD_TYPE_ASK) { // 디폴트 : type에서 넘어온거에 따라
            binding.searchTabLayout.selectTab(binding.searchTabLayout.getTabAt(SEARCH_ASK))
        } else {
            binding.searchTabLayout.selectTab(binding.searchTabLayout.getTabAt(SEARCH_SHARE))
        }
        binding.searchTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: 탭 포지션: ${tab!!.position}")
                when (tab!!.position) {
                    SEARCH_SHARE -> { // 공유 검색
                        binding.checkboxShare.visibility = View.VISIBLE
                        search(SEARCH_SHARE, binding.checkboxShare.isChecked)
                    }
                    SEARCH_ASK -> { // 요청 검색
                        binding.checkboxShare.visibility = View.GONE
                        search(SEARCH_ASK)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    fun search(flag: Int, filtered: Boolean = false) {
        val category = binding.chipGroupCategory.children.toList().filter { (it as Chip).isChecked }.joinToString{ (it as Chip).text }
        Log.d(TAG, "initChipGroup: category: $category")
        val keyword = binding.etSearch.text.toString()
        Log.d(TAG, "initSearchBtn: 선택된 탭: ${binding.searchTabLayout.selectedTabPosition}, 엔터 눌러서 검색함 : $keyword ")
        if (keyword.isEmpty()) {
            val dialog = MyAlertDialog(this@SearchActivity, "1글자 이상 입력해주세요.")
            dialog.show(supportFragmentManager, "SearchFailed")
        } else { // 카테고리 검색은 있으니까 무조건 검색어 입력해야함
            when (flag) {
                SEARCH_SHARE -> { // 공유 검색
                    initShareData(filtered, keyword = keyword, category = category)
                }
                SEARCH_ASK -> { // 요청 검색
                    initAskData(keyword = keyword, category = category)
                }
            }
        }
    }

    private fun initFilterCheckBox() {
        binding.checkboxShare.isChecked = false // 디폴트 : 모두 보기
        binding.checkboxShare.setOnCheckedChangeListener { button, b ->
            Log.d(TAG, "initSearchBtn: 선택된 탭: ${binding.searchTabLayout.selectedTabPosition}, 공유가능만 보기 필터링")

            if (b) { // 체크된 경우 => 공유가능만 보기
                search(binding.searchTabLayout.selectedTabPosition, true)
            } else { // 체크 해제된 경우 => 전체보기
                search(flag = binding.searchTabLayout.selectedTabPosition)
            }

        }
    }


    private fun initShareData(filtered: Boolean = false, keyword: String, category: String? = null) {
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
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false) }

        var num = 0;
        var max = 10;
        val lat = ApplicationClass.prefs.getLat();
        val lng = ApplicationClass.prefs.getLng();
        Log.d(TAG, "$lat $lng")
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.shareApi.boardSearchList(
                num++,
                0,
                max,
                ApplicationClass.prefs.getId(),
                keyword,
                category,
            ).awaitResponse().body();
            Log.d(TAG, "onViewCreated: 공유 검색 데이터 불러오는중 result : $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 : ${result.data.size}")
                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvWarnNoResult.visibility = View.VISIBLE
                } else {
                    binding.tvWarnNoResult.visibility = View.GONE
                }
                for (data in result.data) {
                    var shareData = ShareDto(
                        data.shareListDto.id,
                        if (data.shareListDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.shareListDto.list[0].path,
                        data.shareListDto.title,
                        data.shareListDto.date,
                        "구미",
                        data.shareListDto.startDay + " ~ " + data.shareListDto.endDay,
                        Integer.toString(data.listCnt),
                        data.shareListDto.state,
                        data.shareListDto.userId
                    );
                    shareDatas.add(shareData)
                }
            }
            shareAdapter.notifyItemInserted(index - 1)
        }

        binding.rvShareSearch.setOnScrollChangeListener { v, scollX, scrollY, oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.shareApi.boardSearchList(
                        num++,
                        0,
                        max,
                        ApplicationClass.prefs.getId(),
                        keyword,
                        category,
                    ).awaitResponse().body();
                    Log.d(TAG, "initView: ${result?.data}")
                    if (result?.data?.size != 0) {
                        if (result?.flag == "success") {
                            Log.d(TAG, "initShareData: success!!!!! 검색 결과 : ${result.data.size}")
                            for (data in result.data) {
                                var shareData = ShareDto(
                                    data.shareListDto.id,
                                    if (data.shareListDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.shareListDto.list[0].path,
                                    data.shareListDto.title,
                                    data.shareListDto.date,
                                    "구미",
                                    data.shareListDto.startDay + " ~ " + data.shareListDto.endDay,
                                    Integer.toString(data.listCnt),
                                    data.shareListDto.state,
                                    data.shareListDto.userId
                                );
                                shareDatas.add(shareData)
                            }
                        }

                        shareAdapter.notifyItemInserted(index - 1)
                    }
                }
            }
        }
    }

    fun initAskData(keyword: String, category: String? = null) {
        // 데이터 가져오기
        askDatas = arrayListOf()

        askAdapter = AskListAdapter(askDatas)
        askAdapter.setItemClickListener(object : AskListAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "onClick: ${askDatas[position]}클릭")
            }

        })
        binding.rvShareSearch.apply {
            adapter = askAdapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        var num = 0
        var max = 10
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.askApi.boardSearchList(
                num++, 0, max, ApplicationClass.prefs.getId(), keyword,
                category,
            ).awaitResponse().body()
            Log.d(TAG, "initData: result: $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initList: success!!!!!  검색 결과 : ${result.data[0].size}  result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvWarnNoResult.visibility = View.VISIBLE
                } else {
                    binding.tvWarnNoResult.visibility = View.GONE
                }
                for (data in result.data[0]) {
                    val askData = AskData(
                        data.askDto.id,
                        if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
                        data.askDto.title,
                        data.askDto.date,
                        "", // 이거 안쓰기로함
                        data.askDto.startDay + " ~ " + data.askDto.endDay,
                        data.askDto.state,
                        data.askDto.userId
                    )
                    Log.d(TAG, "추가?: $askData")
                    askDatas.add(askData)
                }
            } else {
                Log.d(TAG, "initData: 실패!!")
            }
            Log.d(TAG, "추가완료: askDatas: $askDatas")
            askAdapter.notifyItemInserted(index - 1)
        }

        binding.rvShareSearch.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.askApi.boardSearchList(
                        num++, 0, max, ApplicationClass.prefs.getId(), keyword,
                        category,
                    ).awaitResponse().body()
                    Log.d(TAG, "initData: result: $result")
                    if (result?.flag == "success") {
                        Log.d(TAG, "initList: success!!!!!  검색 결과 : ${result.data[0].size} result: $result")
                        for (data in result.data[0]) {
                            val askData = AskData(
                                data.askDto.id,
                                if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
                                data.askDto.title,
                                data.askDto.date,
                                "", // 이거 안쓰기로함
                                data.askDto.startDay + " ~ " + data.askDto.endDay,
                                data.askDto.state,
                                data.askDto.userId
                            )
                            Log.d(TAG, "추가?: $askData")
                            askDatas.add(askData)
                        }
                    } else {
                        Log.d(TAG, "initData: 실패!!")
                    }
                }
            }
        }
    }

    fun onBackPressed(view: View) {
        finish()
    }


    fun moveToUserLocationSetting(view: View) { // 동네설정으로 넘어가는
        val intent = Intent(this@SearchActivity, ProfileMenuActivity::class.java)
        intent.putExtra("fragment", "내 동네 설정")
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }


}