package kr.co.vilez.ui.profile

import android.annotation.SuppressLint
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
import kr.co.vilez.data.model.PointData
import kr.co.vilez.databinding.FragmentPointBinding
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse

private const val TAG = "빌리지_PointFragment"
class PointFragment : Fragment() {
    private lateinit var binding: FragmentPointBinding
    private lateinit var activity: ProfileMenuActivity

    private lateinit var pointAdapter: PointAdapter
    private lateinit var pointList: ArrayList<PointData>


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

        initView()
        initData()

        binding.pointSum = ApplicationClass.prefs.getPoint() // 현재 있는 포인트

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hAppointmentApi.getPointList(ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "initData: 포인트 리스트 개수 : ${result?.data?.get(0)?.size}")
            if(result?.flag=="success") {
                for( i in 0 until result.data[0].size) {
                    val data = result.data[0][i]
                    if (data.pointVO.point > 0) { // 적립한 포인트 총합
                        pointPlusSum += data.pointVO.point
                    } else { // 사용한 포인트 총합
                        pointMinusSum += -data.pointVO.point
                    }
                    Log.d(TAG, "initData: 포인트 :  $data")
                    pointList.add(data)
                }
                pointAdapter.notifyDataSetChanged()
                binding.savePoint = pointPlusSum // 적립 포인트
                binding.usedPoint = pointMinusSum // 차감 포인트
                binding.progressBar.progress = ((pointPlusSum.toDouble()/(pointPlusSum.toDouble()+pointMinusSum.toDouble())).toDouble()*100.0).toInt()
            } else {
                Log.d(TAG, "initData: 포인트 불러오기 실패")
            }
        }
    }

    fun initView() {
        // 어댑터 생성
        pointAdapter = PointAdapter(pointList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvPointRecord.apply {
            adapter = pointAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false) // 최신순으로
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