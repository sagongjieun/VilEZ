package kr.co.vilez.ui.profile

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
import kr.co.vilez.data.dto.ShareDto
import kr.co.vilez.databinding.FragmentInterestBinding
import kr.co.vilez.ui.share.ShareListAdapter
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse


private const val TAG = "프로필_빌리지_InterestFragment"
class InterestFragment : Fragment() {

    private lateinit var binding: FragmentInterestBinding
    private lateinit var activity: ProfileMenuActivity

    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareList:ArrayList<ShareDto>
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = context as ProfileMenuActivity
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_interest, container, false)
        binding.fragment = this
        initToolBar()
        initData()
        return binding.root
    }

    private fun initData() {
        shareList = arrayListOf()

        shareAdapter = ShareListAdapter(shareList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvBookmarkList.apply {
            adapter = shareAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitShareService.getBookmark(ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "onViewCreated: 공유 검색 데이터 불러오는중 result : $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 개수 : ${result.data.size}")

                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                }
                for (data in result.data) {
                    val shareData = ShareDto(
                        data.shareListDto.id,
                        if (data.shareListDto.list.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.shareListDto.list[0].path,
                        data.shareListDto.title,
                        data.shareListDto.date,
                        "",
                        data.shareListDto.startDay + " ~ " + data.shareListDto.endDay,
                        data.listCnt.toString(),
                        data.shareListDto.state,
                        data.shareListDto.userId
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
    }

    fun onBackPressed(view: View) {
        activity.finish()
    }
}