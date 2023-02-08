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
import kr.co.vilez.data.dto.AskData
import kr.co.vilez.databinding.FragmentCategorySearchBinding
import kr.co.vilez.data.dto.ShareDto
import kr.co.vilez.ui.ask.AskListAdapter
import kr.co.vilez.ui.share.ShareListAdapter
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE
import retrofit2.awaitResponse

private const val ARG_PARAM1 = "category"
private const val ARG_TYPE = "type"

private const val TAG = "빌리지_CategorySearchFragment"
class CategorySearchFragment : Fragment() {
    private var category: String? = null
    private var type:Int = 0
    private lateinit var binding: FragmentCategorySearchBinding
    private lateinit var activity: MenuCategoryActivity

    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareDatas: ArrayList<ShareDto>
    private lateinit var askAdapter: AskListAdapter
    private lateinit var askDatas: ArrayList<AskData>
    
    private var index = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_PARAM1)
            type = it.getInt(ARG_TYPE)
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
        if(type == BOARD_TYPE_SHARE) { 
            initFilterCheckBox()
            initShareData() // default : 카테고리에 해당하는 게시글 모두 출력 (공유중 + 공유가능 모두)
        } else { // 요청글은 공유가능글만 보기 필터링 없음
            binding.checkboxShare.visibility = View.GONE
            initAskData()
        }

        return binding.root
    }
    private fun initFilterCheckBox() {
        binding.checkboxShare.visibility = View.VISIBLE
        binding.checkboxShare.isChecked = false // 디폴트 : 모두 보기
        binding.checkboxShare.setOnCheckedChangeListener { button, b ->
            if(b) { // 체크된 경우 => 공유가능만 보기
                initShareData(true)
            } else { // 체크 해제된 경우 => 전체보기
                initShareData()
            }
        }
    }
    
    private fun initAskData() {
        // 데이터 가져오기
        askDatas = arrayListOf()

        askAdapter = AskListAdapter(askDatas)
        askAdapter.setItemClickListener(object: AskListAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "onClick: ${askDatas[position]}클릭")
            }

        })
        binding.rvShareSearch.apply {
            adapter = askAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        var num = 0
        var max = 10
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitAskService.boardCategoryList(num++, 0, max, ApplicationClass.prefs.getId(), category!!).awaitResponse().body()
            Log.d(TAG, "initData: result: $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initList: result: $result")
                if (result.data[0].isEmpty()) {
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
                        data.askDto.startDay+"~"+data.askDto.endDay,
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
                    val result = ApplicationClass.retrofitAskService.boardCategoryList(num++, 0, max, ApplicationClass.prefs.getId(), category!!).awaitResponse().body()
                    Log.d(TAG, "initData: result: $result")
                    if (result?.flag == "success") {
                        Log.d(TAG, "initList: result: $result")
                        for (data in result.data[0]) {
                            val askData = AskData(
                                data.askDto.id,
                                if (data.askDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.askDto.list[0].path,
                                data.askDto.title,
                                data.askDto.date,
                                "", // 이거 안쓰기로함
                                data.askDto.startDay+"~"+data.askDto.endDay,
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

    /**
     * @param type : 글 종류 (SHARE, ASK)
     * @param filtered : 공유 가능만 볼건지 (SHARE)만 해당
     */
    private fun initShareData(filtered:Boolean = false) {
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
                    var shareData: ShareDto
                    if(filtered && data.shareListDto.state == 1) continue // 공유가능만 보기인데 공유중인 글은 skip
                    if (data.shareListDto.list.isEmpty() ) { // 이미지 없는거 테스트용
                        shareData = ShareDto(
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
                        shareData = ShareDto(
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
                                var shareDto: ShareDto
                                if (data.shareListDto.list.size == 0) {
                                    shareDto = ShareDto(
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
                                    shareDto = ShareDto(
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
                                shareDatas.add(shareDto)
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
        fun newInstance(type: Int, category: String) =
            CategorySearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, category)
                    putInt(ARG_TYPE, type)
                }
            }
    }
}