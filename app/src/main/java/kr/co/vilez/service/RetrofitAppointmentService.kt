package kr.co.vilez.service

import kr.co.vilez.data.dto.MyAppointmentData
import kr.co.vilez.data.model.CalendarData
import kr.co.vilez.data.model.ImminentData
import kr.co.vilez.data.model.PointData
import kr.co.vilez.data.dto.MyShare
import kr.co.vilez.data.model.*
import retrofit2.Call
import retrofit2.http.*


/**
 *
 * 나의 프로필의 공유 캘린더
목적 : 내 약속 정보들 띄워줌 (공유 시작, 공유 끝, 대여 시작, 대여 끝)
/vilez​/appointments​/my​/appointlist​/{userId}    (get)
나의 모든 약속 정보를 불러옴 (공유 완료된 것까지 모두 불러옴)

나의 공유박스
1. 나의 공유물품
목적 : 내가 빌려준 / 빌려줄 예정인 물건 구분해서 띄워줌 & 클릭 시 해당 게시글로 연결
​/vilez​/appointments​/my​/{userId}
내가 빌려주는 약속 중에서 완료된 약속을 제외하고 불러옴

2. 나의 대여물품
목적 : 내가 빌린 / 빌릴 예정인 물건 구분해서 띄워줌 & 클릭 시 해당 게시글로 연결
​/vilez​/appointments​/my​/give/{userId}
내가 빌리는 약속 중에서 완료된 약속을 제외하고 불러옴
 */
data class RESTAppointmentResult( // 내가 빌려준 물품 (공유중 + 공유 예정)
    val `data`: List<List<MyShare>>,
    val flag: String
)
data class RESTPointResult(
    val `data`: List<List<PointData>>,
    val flag: String
)
data class RESTImminentResult(
    val `data`: List<List<ImminentData>>,
    val flag: String
)
data class RESTCalendarResult(
    val `data`: List<List<CalendarData>>,
    val flag: String
)
data class RESTMyShareAskResult( // 나의 공유박스 (나의 공유내역, 나의 요청내역)
    val `data`: List<List<MyAppointmentData>>,
    val flag: String
)
interface RetrofitAppointmentService {

    // 공유 예정 시작 기간, 반납 기간이 7일 미만 남은 데이터들을 반환한다.
    @GET("/vilez/appointments/my/date/{userId}")
    fun getMyImminent(@Path("userId")id:Int): Call<RESTImminentResult>

    // [나의 공유박스] 나의 공유물품을 조회한다. (완료된 약속은 제외)
    @GET("/vilez/appointments/my/{userId}")
    fun getMyAppointment(@Path("userId")id:Int): Call<RESTMyShareAskResult>

    // [나의 공유박스] 나의 대여물품을 조회한다.  (완료된 약속은 제외)
    // type = 1 요청게시글, type = 2 공유 게시글
    @GET("/vilez/appointments/my/give/{userId}")
    fun getMyGiveList(@Path("userId")id:Int): Call<RESTMyShareAskResult>


    // 캘린더에 이용할 내 약속 정보 내역
    // 내 약속 정보 (공유 예정) 내역을 조회한다. type = 1 요청, type = 2 공유
    @GET("/vilez/appointments/my/appointlist/{userId}")
    fun getMyCalendar(@Path("userId")id:Int): Call<RESTCalendarResult>




    // 포인트 내역 조회
    @GET("/vilez/appointments/my/point")
    fun getPointList(@Query("userId") int: Int): Call<RESTPointResult>


    // 나의 작성글
    //@GET("/vilez/appointments/my/{userId}")

}

