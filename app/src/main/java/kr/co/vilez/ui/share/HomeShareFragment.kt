package kr.co.vilez.ui.share

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
import kr.co.vilez.data.dto.ShareData
import kr.co.vilez.databinding.FragmentHomeShareBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.search.SearchActivity
import kr.co.vilez.ui.share.*
import kr.co.vilez.ui.search.category.MenuCategoryActivity
import kr.co.vilez.ui.share.write.ShareWriteActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common.Companion.elapsedTime
import retrofit2.awaitResponse
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.BOARD_TYPE_SHARE

private const val TAG = "빌리지_HomeFragment"

class HomeShareFragment : Fragment() {
    private lateinit var binding: FragmentHomeShareBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareDatas: ArrayList<ShareData>
    private var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_share, container, false)
        binding.activity = mainActivity
        binding.fragment = this

        if (ApplicationClass.prefs.getLng() != "0.0" && ApplicationClass.prefs.getLat() != "0.0") {
            binding.userLocationValid = true
            initData()
        } else {
            binding.userLocationValid = false
        }

        initToolBar()

        return binding.root
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
                Log.d(TAG, "onContextItemSelected: ${item.title} clicked")
                Toast.makeText(mainActivity, "${item.title} 클릭", Toast.LENGTH_SHORT).show()
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


    private fun initData() {
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
        binding.rvShareList.apply {
            adapter = shareAdapter
            layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        }

        var num = 0;
        var max = 10;
        val lat = ApplicationClass.prefs.getLat();
        val lng = ApplicationClass.prefs.getLng();
        Log.d(TAG, "$lat $lng")
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitShareService.boardList(
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
                    var shareData = ShareData(
                            data.shareListDto.id,
                            if(data.shareListDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.shareListDto.list[0].path,
                            data.shareListDto.title,
                            elapsedTime(data.shareListDto.date),
                            "구미",
                            data.shareListDto.startDay + "~"
                                    + data.shareListDto.endDay,
                            Integer.toString(data.listCnt),
                            data.shareListDto.state,
                            data.shareListDto.userId
                        );
                    shareDatas.add(shareData)
                }
            }
            shareAdapter.notifyItemInserted(index - 1)
        }

        binding.rvShareList.setOnScrollChangeListener { v, scollX, scrollY,
                                                        oldScrollX, oldScrollY ->
            if (!v.canScrollVertically(1)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result =
                        ApplicationClass.retrofitShareService.boardList(
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
                                var shareData = ShareData(
                                    data.shareListDto.id,
                                    if(data.shareListDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.shareListDto.list[0].path,
                                    data.shareListDto.title,
                                    elapsedTime(data.shareListDto.date),
                                    "구미",
                                    data.shareListDto.startDay + "~"
                                            + data.shareListDto.endDay,
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


    fun moveToShareWriteActivity(view: View) {
        val intent = Intent(mainActivity, ShareWriteActivity::class.java)
        intent.putExtra("type", BOARD_TYPE_SHARE)
        mainActivity.startActivity(intent)
    }

}