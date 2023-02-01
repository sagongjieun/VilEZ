package kr.co.vilez.util

import kr.co.vilez.data.model.*
import retrofit2.Call
import retrofit2.http.*


data class RESTShareBoardDetail(
    val `data`: List<ShareBoardDetail>,
    val flag: String
)
interface RetrofitShareService {
    @GET("/vilez/shareboard")
    fun boardList(@Query(value = "cnt") cnt:Int, @Query(value = "high") high:Int): Call<RestShare>

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

}