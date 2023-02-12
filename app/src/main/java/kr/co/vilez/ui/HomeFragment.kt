package kr.co.vilez.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import kr.co.vilez.R
import kr.co.vilez.data.dto.AskData
import kr.co.vilez.data.dto.ShareDto
import kr.co.vilez.databinding.FragmentHomeBinding
import kr.co.vilez.ui.ask.AskListAdapter
import kr.co.vilez.ui.dialog.MyAlertDialog
import kr.co.vilez.ui.search.SearchActivity
import kr.co.vilez.ui.share.*
import kr.co.vilez.ui.search.category.MenuCategoryActivity
import kr.co.vilez.ui.share.write.ShareWriteActivity
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_ASK
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE

private const val TAG = "빌리지_HomeFragment"

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity

    // 공유글
    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareItems: ArrayList<ShareDto>
    private var shareIndex = 0

    
    private lateinit var askAdapter: AskListAdapter
    private var askItems = ArrayList<AskData>()
    private var index = 0

    private val TAB_SHARE = 0 // 공유
    private val TAB_ASK = 1  // 요청글

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.activity = mainActivity
        binding.fragment = this

        if (ApplicationClass.prefs.getLng() != "0.0" && ApplicationClass.prefs.getLat() != "0.0") {
            binding.userLocationValid = true
            initTabLayout()
            initShareData() // default : 공유글 띄우기
        } else {
            binding.userLocationValid = false
        }

        initToolBar()

        binding.swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Main) {
                    when(binding.homeTabLayout.selectedTabPosition) {
                        TAB_ASK -> {
                            initAskData()
                            if(askItems.size > 0) askAdapter.notifyDataSetChanged()
                        }
                        TAB_SHARE -> {
                            initShareData()
                            if(shareItems.size > 0) shareAdapter.notifyDataSetChanged()
                        }
                    }
                    delay(600)
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        return binding.root
    }


    override fun onResume() {
        reloadData()
        super.onResume()
    }

    private fun reloadData() {
        Log.d(TAG, "reloadData: @@@@@@@@@@@@@@@@@@@@@@@@@@@@RELOAD")
        // 게시글 추가, 수정, 삭제 후 다시 돌아왔을때 데이터 재로딩
        // 원래있던거 다 날리기

        /*when(binding.homeTabLayout.selectedTabPosition) {
            TAB_ASK -> {
                initAskData()
            }
            TAB_SHARE -> {
                initShareData()
            }
        }*/
    }

    var isOpen = false
    fun toggleFloatingBtn(view: View) {
        if(isOpen) { // 원래대로 돌아감
            ObjectAnimator.ofFloat(binding.btnWriteAsk, "translationY", 0f).apply {
                duration = 400
                start()
            }
            ObjectAnimator.ofFloat(binding.btnWriteShare, "translationY", 0f).apply {
                duration = 400
                start()
            }
            /*ObjectAnimator.ofFloat(binding.floatingActionButton, View.ROTATION, 45f,0f).apply {
                duration = 300
                start()
            }*/
        } else { //펼쳐짐
            ObjectAnimator.ofFloat(binding.btnWriteAsk, "translationY", -130f).apply {
                duration = 400
                start()
            }
            ObjectAnimator.ofFloat(binding.btnWriteShare, "translationY", -260f).apply {
                duration = 400
                start()
            }
            /*ObjectAnimator.ofFloat(binding.floatingActionButton, View.ROTATION, 0f,45f).apply {
                duration = 300
                start()
            }*/
        }
        isOpen = !isOpen

    }

    private fun initTabLayout() {
        binding.homeTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position) {
                    TAB_SHARE-> { // 공유 탭
                        initShareData()
                    }
                    TAB_ASK -> { // 요청 탭
                        initAskData()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_menu_menu -> {
                val intent = Intent(mainActivity, MenuCategoryActivity::class.java)
                intent.putExtra("type", BOARD_TYPE_SHARE)
                startActivity(intent)
            }
            R.id.option_menu_search -> {
                val intent = Intent(mainActivity, SearchActivity::class.java)
                intent.putExtra("type", BOARD_TYPE_SHARE)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initToolBar() {
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
    }


    private fun initShareData() {
        // 데이터 가져오기
        shareItems = arrayListOf()

        // 어댑터 생성
        shareAdapter = ShareListAdapter(shareItems)

        // 리사이클러뷰에 어댑터 등록
        binding.rvShareList.apply {
            adapter = shareAdapter
            layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        }

        shareAdapter.notifyDataSetChanged()

        var num = 0;
        var max = 10;
        val lat = ApplicationClass.prefs.getLat();
        val lng = ApplicationClass.prefs.getLng();
        Log.d(TAG, "$lat $lng")
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.hShareApi.boardList(
                    num++,
                    0,
                    max,
                    ApplicationClass.prefs.getId()
                ).awaitResponse().body();
            Log.d(TAG, "onViewCreated: 데이터 불러오는중 result : $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvNoArticleMsg.visibility = View.VISIBLE
                }
                for (data in result.data) {
                    var shareData = ShareDto(
                            data.shareListDto.id,
                            if(data.shareListDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.shareListDto.list[0].path,
                            data.shareListDto.title,
                            data.shareListDto.date,
                            "구미",
                            data.shareListDto.startDay + " ~ "
                                    + data.shareListDto.endDay,
                            Integer.toString(data.listCnt),
                            data.shareListDto.state,
                            data.shareListDto.userId
                        );
                    shareItems.add(shareData)
                }
            }
            shareAdapter.notifyItemInserted(shareIndex - 1)
        }

        binding.rvShareList.setOnScrollChangeListener { v, scollX, scrollY,
                                                        oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result =
                        ApplicationClass.hShareApi.boardList(
                            num++,
                            0,
                            max,
                            ApplicationClass.prefs.getId()
                        ).awaitResponse()
                            .body();
                    Log.d(TAG, "initView: ${result?.data}")
                    if (result?.data?.size != 0) {
                        if (result?.flag == "success") {
                            for (data in result.data) {
                                var shareData = ShareDto(
                                    data.shareListDto.id,
                                    if(data.shareListDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.shareListDto.list[0].path,
                                    data.shareListDto.title,
                                    data.shareListDto.date,
                                    "구미",
                                    data.shareListDto.startDay + " ~ "
                                            + data.shareListDto.endDay,
                                    Integer.toString(data.listCnt),
                                    data.shareListDto.state,
                                    data.shareListDto.userId
                                );
                                shareItems.add(shareData)
                            }
                        }
                        shareAdapter.notifyItemInserted(shareIndex - 1)
                    }
                }
            }
        }
    }

    private fun initAskData() {
        askItems = arrayListOf()

        askAdapter = AskListAdapter(askItems)

        binding.rvShareList.apply {
            adapter = askAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        askAdapter.notifyDataSetChanged()
        
        var num = 0
        var max = 10
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hAskApi.boardList(num++, 0, max, ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "initData: result: $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initList: result: $result")
                if(result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvNoArticleMsg.visibility = View.VISIBLE
                }
                for (data in result.data[0]) {
                    val askData = AskData(
                        data.askDto.id,
                        if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
                        data.askDto.title,
                        data.askDto.date,
                        "", // 이거 안쓰기로함
                        data.askDto.startDay+" ~ "+data.askDto.endDay,
                        data.askDto.state,
                        data.askDto.userId
                    )
                    Log.d(TAG, "추가?: $askData")
                    askItems.add(askData)
                }
            } else {
                Log.d(TAG, "initData: 실패!!")
            }
            Log.d(TAG, "추가완료: askItems: $askItems")
            askAdapter.notifyItemInserted(index - 1)
        }

        binding.rvShareList.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.hAskApi.boardList(num++, 0, max, ApplicationClass.prefs.getId()).awaitResponse().body()
                    Log.d(TAG, "initData: result: $result")
                    if (result?.flag == "success") {
                        Log.d(TAG, "initList: result: $result")
                        if(result.data.isEmpty()) {
                            Log.d(TAG, "onViewCreated: 데이터 0개")
                            binding.tvNoArticleMsg.visibility = View.VISIBLE
                        }
                        for (data in result.data[0]) {
                            val askData = AskData(
                                data.askDto.id,
                                if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
                                data.askDto.title,
                                data.askDto.date,
                                "", // 이거 안쓰기로함
                                data.askDto.startDay+" ~ "+data.askDto.endDay,
                                data.askDto.state,
                                data.askDto.userId
                            )
                            Log.d(TAG, "추가?: $askData")
                            askItems.add(askData)
                        }
                    } else {
                        Log.d(TAG, "initData: 실패!!")
                    }
                }
            }
        }
    }

    /**
     * @return 포인트 충분하면 true, 부족시 토스트 띄우고 false 리턴
     */
    fun checkMyPoint(): Boolean{
        if(ApplicationClass.prefs.getPoint() < 30) { // 대여할 때 무조건 30 포인트 삭감
            val dialog = MyAlertDialog(mainActivity, "포인트가 부족해서 요청글 작성이 불가합니다." +
                    "\n다른 주민들에게 공유하기를 통해 포인트를 모아보세요!")
            dialog.show(mainActivity.supportFragmentManager, "ShortPoint")
            return false
        }
        return true
    }

    fun moveToShareWriteActivity(view: View) { // 공유하기 글쓰기 버튼 클릭
        val intent = Intent(mainActivity, ShareWriteActivity::class.java)
        intent.putExtra("type", BOARD_TYPE_SHARE)
        mainActivity.startActivity(intent)
    }

    fun moveToAskWriteActivity(view: View) { // 요청글쓰기 버튼 클릭
        // 포인트 확인 후 적으면 막기
        if(!checkMyPoint()) return // 포인트 부족하면 글쓰기 화면으로 이동 불가
        val intent = Intent(mainActivity, ShareWriteActivity::class.java)
        intent.putExtra("type", BOARD_TYPE_ASK)
        mainActivity.startActivity(intent)
    }

}