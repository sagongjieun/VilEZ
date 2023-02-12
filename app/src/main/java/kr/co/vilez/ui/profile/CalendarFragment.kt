package kr.co.vilez.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.data.dto.CalendarDto
import kr.co.vilez.databinding.FragmentCalendarBinding
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common.Companion.APPOINTMENT_STATE_SHARE
import retrofit2.awaitResponse
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "빌리지_캘린더_CalendarFragment"
class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var profileMenuActivity: ProfileMenuActivity


    private lateinit var calendarList: ArrayList<CalendarDto>
    lateinit var calendarAdapter:CalendarAdapter
    private lateinit var itemList:ArrayList<CalendarDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        profileMenuActivity = context as ProfileMenuActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, kr.co.vilez.R.layout.fragment_calendar, container, false)
        binding.fragment = this
        initToolBar()
        initData()
        return binding.root
    }

    private fun initData() {
        itemList = arrayListOf()
        calendarList = arrayListOf()
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hAppointmentApi.getMyCalendar(ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "initData: 개수: ${result?.data?.get(0)?.size} 데이터 result: $result")
            if(result?.flag == "success") {
                Log.d(TAG, "initData: success! 개수 : ${result.data[0].size}")
                for(element in result.data[0]) {
                    //Log.d(TAG, "initData: 데이터: $element")

                    var st = StringTokenizer(element.appointmentStart, "-")
                    val syear = st.nextToken().toInt()
                    val smonth = st.nextToken().toInt()-1
                    val sday = st.nextToken().toInt()
                    st = StringTokenizer(element.appointmentEnd, "-")
                    val eyear = st.nextToken().toInt()
                    val emonth = st.nextToken().toInt()-1
                    val eday = st.nextToken().toInt()
                    val start = Calendar.getInstance().also{it.set(syear, smonth, sday) }
                    val end = Calendar.getInstance().also{it.set(eyear, emonth, eday)}

                    val calendar = CalendarDto(
                        element.title,
                        element.appointmentStart,
                        element.appointmentEnd,
                        element.state,
                        element.type,
                        startCalendar = start,
                        endCalendar = end,
                    )
                    itemList.add(calendar)
                }
                initCalendar()
            } else {
                Log.d(TAG, "initData: 불러오기 실패")
            }
        }
    }

    private fun initCalendar() {
        binding.calendarView.setDate(Date(System.currentTimeMillis())) // 디폴트 : 오늘

        val events: MutableList<EventDay> = ArrayList()
        for(element in itemList) {
            // state 1 : 내가 공유자 (공유 -> 초록색)
            // state 0 : 내가 피공유자 (대여 -> 파란색)
            if(element.state == APPOINTMENT_STATE_SHARE) {
                events.add(EventDay(element.startCalendar,kr.co.vilez.R.drawable.circle_green))
                events[0].calendar
                events.add(EventDay(element.endCalendar,kr.co.vilez.R.drawable.circle_green))
            } else {
                events.add(EventDay(element.startCalendar,kr.co.vilez.R.drawable.circle_blue))
                events.add(EventDay(element.endCalendar,kr.co.vilez.R.drawable.circle_blue))
            }
            // 이 날짜에 클릭 이벤트 주기
        }
        binding.calendarView.setEvents(events)



        binding.calendarView.setOnDayClickListener(object:OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {

                val selectedYear = eventDay.calendar.get(1)
                val selectedMonth = eventDay.calendar.get(2)+1
                val selectedDay = eventDay.calendar.get(5)
                Log.d(TAG, "onDayClick: ${eventDay.calendar.get(1)}, ${eventDay.calendar.get(2)+1} , ${eventDay.calendar.get(5)}")

//                getCalendarDtoByDate(calendar = eventDay.calendar)
                calendarList = arrayListOf()
                calendarAdapter = CalendarAdapter(calendarList)
                calendarAdapter.notifyDataSetChanged()
                binding.rvCalendar.apply {
                    adapter = calendarAdapter
                    layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    //            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                }
                for(element in itemList) {
                    if(eventDay.calendar == element.startCalendar) {
                        calendarList.add(element)
                        if(element.state == APPOINTMENT_STATE_SHARE)  {
                           // Toast.makeText(activity, "${element.title} 공유 시작날짜", Toast.LENGTH_SHORT).show()
                        }
                        else  {
                            //Toast.makeText(activity, "${element.title} 대여 시작날짜", Toast.LENGTH_SHORT).show()
                        }
                    } else if (eventDay.calendar == element.endCalendar) {
                        calendarList.add(element)
                        if(element.state == APPOINTMENT_STATE_SHARE) {
                            //Toast.makeText(activity, "${element.title} 공유 종료날짜", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            //Toast.makeText(activity, "${element.title} 대여 종료날짜", Toast.LENGTH_SHORT).show()
                        }
                    }
                    calendarAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun initToolBar() {
        profileMenuActivity.setSupportActionBar(binding.toolbar)
        profileMenuActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
    }

    fun onBackPressed(view: View) {
        profileMenuActivity.finish()
    }

}