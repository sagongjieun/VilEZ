package kr.co.vilez.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.dto.PointDto
import kr.co.vilez.databinding.FragmentPointBinding
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse

private const val TAG = "빌리지_PointFragment"
class PointFragment : Fragment() {
    private lateinit var binding: FragmentPointBinding
    private lateinit var activity: ProfileMenuActivity

    private lateinit var pointAdapter: PointAdapter
    private lateinit var pointList: ArrayList<PointDto>


    private var pointPlusSum = 0
    private var pointMinusSum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = context as ProfileMenuActivity
        setHasOptionsMenu(true)

        pointList = arrayListOf()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_point, container, false)

        binding.fragment = this

        initToolBar()
        initData()
        initView()

        binding.pointSum = ApplicationClass.prefs.getPoint() // 현재 있는 포인트
        binding.savePoint = pointPlusSum // 적립 포인트
        binding.usedPoint = pointMinusSum // 차감 포인트
        pointMinusSum = 120
        Log.d(TAG, "onCreateView: 현재 포인트: ${ApplicationClass.prefs.getPoint()}")
        Log.d(TAG, "onCreateView: minussum:$pointMinusSum")
        val tmp = pointMinusSum.toDouble() / ApplicationClass.prefs.getPoint().toDouble()
        Log.d(TAG, "onCreateView: tmp: ${tmp}")
        val progress = 100 - (tmp*100)
        binding.progressBar.progress = progress.toInt()
        Log.d(TAG, "onCreateView: ${progress}")

        return binding.root
    }

    fun initData() {
        // test용 데이터
       /* pointList.add(PointDto(7, 6, 4, "안녕하세요",
            "2023-02-05", 1, true))
        pointList.add(PointDto(7, 6, 4, "안녕하세요2",
            "2023-02-05", 1, false))*/
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitAppointmentService.getPointList(ApplicationClass.prefs.getId()).awaitResponse().body()
            if(result?.flag=="success") {
                val data = result.data[0]
                // point 차감액, 적립액 구하기
                /*for(i in 0 until pointList.size) {
                    if (pointList[i].isIncrease) pointPlusSum+=10
                    else pointMinusSum += 10
                }*/
            } else {

            }
        }



    }

    fun initView() {
        // 어댑터 생성
        pointAdapter = PointAdapter(pointList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvPointRecord.apply {
            adapter = pointAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }
    }

    private fun initToolBar() {
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "포인트 내역"
    }

    fun onBackPressed(view: View) {
        activity.finish()
    }

}