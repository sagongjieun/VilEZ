package kr.co.vilez.util

import kr.co.vilez.data.dto.WriteBoard
import kr.co.vilez.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


data class RESTShareBoardDetail(
    val `data`: List<ShareBoardDetail>,
    val flag: String
)

interface RetrofitShareService {

    // 모든 공유 게시글을 가져온다. 특정 동네에 대한것을 가져오는 기능은 현재 없다.
    @GET("/vilez/shareboard")
    fun boardList(@Query(value = "cnt") cnt:Int, @Query(value = "low") low:Int,@Query(value = "high") high:Int, @Query(value =  "userId") id:Int): Call<RestShare>

    // 해당 게시글의 상세 정보를 가져온다.
    @GET("/vilez/shareboard/detail/{boardId}")
    fun getBoardDetail(@Path("boardId") boardId:Int):Call<RESTShareBoardDetail>

    // 유저가 해당 게시글에 북마크를 달았는지 확인한다
    @GET("/vilez/shareboard/bookmark/{boardId}/{userId}")
    fun getShareBookmark(@Path("boardId")boardId: Int, @Path("userId")id:Int): Call <RESTBookmarkResult>

    // 해당 게시글의 북마크를 삭제한다.
    @DELETE("/vilez/shareboard/bookmark/{boardId}/{userId}")
    fun deleteBookmark(@Path("boardId")boardId: Int, @Path("userId")id:Int): Call <RESTResult>

    // 해당 게시글에 북마크를 추가한다.
    @POST("/vilez/shareboard/bookmark")
    fun addBookmark(@Body bookmark : Bookmark): Call <RESTResult>

    // 새로운 공유 게시글을 추가한다.
    // 이미지 리스트는 멀티파트 리스트로 param에, 나머지 정보는 json으로 멀티파트에 넣어서 보낸다.
    @Multipart
    @POST("/vilez/shareboard")
    fun postShareBoard(@Part("board") board: WriteBoard, @Part image: List<MultipartBody.Part>?) : Call<RESTShareBoardDetail>

    // 공유 게시글을 삭제한다. 성공시 flag에 success
    @DELETE("/vilez/shareboard/{boardId}")
    fun deleteShareBoard(@Path("boardId")boardId: Int) : Call<RESTResult>

    // 공유 게시글을 수정한다.
    // 파라미터는 게시글 추가와 같다.
    @Multipart
    @PUT("/vilez/shareboard")
    fun putShareBoard(@Part("board") board: WriteBoard, @Part image: List<MultipartBody.Part>?) : Call<RESTShareBoardDetail>

}