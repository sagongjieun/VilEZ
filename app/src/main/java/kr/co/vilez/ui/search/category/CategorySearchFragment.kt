package kr.co.vilez.ui.search.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentCategorySearchBinding
import kr.co.vilez.data.dto.ShareData
import kr.co.vilez.ui.share.ShareListAdapter
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse

private const val ARG_PARAM1 = "category"

private const val TAG = "빌리지_CategorySearchFragment"
class CategorySearchFragment : Fragment() {
    private var category: String? = null
    private lateinit var binding: FragmentCategorySearchBinding
    private lateinit var activity: MenuCategoryActivity

    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareDatas: ArrayList<ShareData>
    private var index = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_PARAM1)
        }
        activity = context as MenuCategoryActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_search, container, false)
        binding.fragment = this

        binding.userLocationValid = ApplicationClass.prefs.getLng() != "0.0" && ApplicationClass.prefs.getLat() != "0.0"

        initToolBar()
        initFilterCheckBox()
        initData() // default : 카테고리에 해당하는 게시글 모두 출력 (공유중 + 공유가능 모두)

        return binding.root
    }
    private fun initFilterCheckBox() {
        binding.checkboxShare.isChecked = false // 디폴트 : 모두 보기
        binding.checkboxShare.setOnCheckedChangeListener { button, b ->
            if(b) { // 체크된 경우 => 공유가능만 보기
                initData(true)
            } else { // 체크 해제된 경우 => 전체보기
                initData()
            }
        }
    }
    
    private fun initData(filtered:Boolean = false) {
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
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        var num = 0;
        var max = 10;
        val lat = ApplicationClass.prefs.getLat();
        val lng = ApplicationClass.prefs.getLng();
        Log.d(TAG, "$lat $lng")
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitShareService.boardCategoryList(
                    num++,
                    0,
                    max,
                    ApplicationClass.prefs.getId(),
                    category!!
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
                        ApplicationClass.retrofitShareService.boardCategoryList(
                            num++,
                            0,
                            max,
                            ApplicationClass.prefs.getId(),
                            category!!
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
    
    

    private fun initToolBar() {
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = category
    }

    fun onBackPressed(view: View) {
        activity.supportFragmentManager.popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance(category: String) =
            CategorySearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, category)
                }
            }
    }
}