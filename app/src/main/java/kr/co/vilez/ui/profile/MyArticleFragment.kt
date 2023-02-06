package kr.co.vilez.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.dto.AskData
import kr.co.vilez.data.dto.ShareData
import kr.co.vilez.databinding.FragmentSharedListBinding
import kr.co.vilez.ui.ask.AskListAdapter
import kr.co.vilez.ui.share.ShareListAdapter
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse

private const val TAG = "빌리지_프로필_MyArticleFragment"
class MyArticleFragment : Fragment() {

    private lateinit var binding: FragmentSharedListBinding
    private lateinit var activity: ProfileMenuActivity

    private val MY_SHARE_ARTICLE = 0
    private val MY_ASK_ARTICLE = 1

    private lateinit var shareAdapter:ShareListAdapter
    private lateinit var shareList:ArrayList<ShareData>
    private lateinit var askAdapter:AskListAdapter
    private lateinit var askList:ArrayList<AskData>
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity = context as ProfileMenuActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shared_list, container, false)
        binding.fragment = this
        initToolBar()
        initTabLayout()
        initShareData() // default : 나의 공유글
        return binding.root
    }

    private fun initTabLayout() {
        binding.tabLayoutMyArticle.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "initSearchBtn: 선택된 탭: ${binding.tabLayoutMyArticle.selectedTabPosition}")
                when(tab!!.position) {
                    MY_SHARE_ARTICLE -> {
                        Log.d(TAG, "onTabSelected: 나의 공유글 클릭")
                        initShareData()
                    }
                    MY_ASK_ARTICLE -> {
                        Log.d(TAG, "onTabSelected: 나의 요청글 클릭")
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
    
    private fun initAskData() {
        index = 0
        askList = arrayListOf()
        askAdapter = AskListAdapter(askList)
        binding.rvArticleList.apply {
            adapter = askAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitAskService.getMyArticle(ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "onViewCreated: 공유 검색 데이터 불러오는중 result : $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 개수 : ${result.data.size}")

                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                }
                for (data in result.data) {
                    val askData = AskData(
                        data.id,
                        if (data.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.list[0].path,
                        data.title,
                        data.date,
                        "",
                        data.startDay + " ~ " + data.endDay,
                        data.state,
                        data.userId
                    )
                    askList.add(askData)
                }
            }
            askAdapter.notifyItemInserted(index - 1)
        }
    }
    private fun initShareData() {
        index = 0
        shareList = arrayListOf()
        
        shareAdapter = ShareListAdapter(shareList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvArticleList.apply {
            adapter = shareAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitShareService.getMyArticle(ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "onViewCreated: 공유 검색 데이터 불러오는중 result : $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 개수 : ${result.data[0].size}")

                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                } 
                for (data in result.data[0]) {
                    val shareData = ShareData(
                        data.id,
                        if (data.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.list[0].path,
                        data.title,
                        data.date,
                        "",
                        data.startDay + " ~ " + data.endDay,
                        data.bookmarkCnt.toString(),
                        data.state,
                        data.userId
                    )
                    shareList.add(shareData)
                }
            }
            shareAdapter.notifyItemInserted(index - 1)
        }
    }    
    

    private fun initToolBar() {
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "나의 작성글"
    }

    fun onBackPressed(view: View) {
        activity.finish()
    }




}