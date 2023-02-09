package kr.co.vilez.service

import kr.co.vilez.data.dto.WriteBoard
import kr.co.vilez.data.model.*
import kr.co.vilez.data.share.MyShareArticle
import kr.co.vilez.util.RESTBookmarkResult
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


data class RESTShareBoardDetail(
    val `data`: List<ShareBoardDetail>,
    val flag: String
)
data class RESTMyShareArticleResult(
    val `data`: List<List<MyShareArticle>>,
    val flag: String
)
data class RESTShareResult(
    val `data`: List<ShareResult>,
    val flag: String
)
interface ShareApi {

    // 내 북마크 리스트를 불러온다
    @GET("/vilez/shareboard/bookmark/my/{userId}")
    fun getBookmark(@Path("userId")id:Int):Call<RESTShareResult>

    // 내가 작성한 모든 게시글을 가져온다. state 1 : 내가 공유자, state 0 : 내가 피공유자
    @GET("/vilez/shareboard/my/{userId}")
    fun getMyArticle(@Path("userId")id:Int):Call<RESTMyShareArticleResult>


    // 모든 공유 게시글을 가져온다. 유저 위치 기반해서 반경 2.5km 이내의 데이터를 가져온다.
    @GET("/vilez/shareboard")
    fun boardList(
        @Query(value = "cnt") cnt: Int,
        @Query(value = "low") low: Int,
        @Query(value = "high") high: Int,
        @Query(value = "userId") id: Int
    ): Call<RESTShareResult>

    // 검색 키워드를 포함하는 제목, 본문을 가진 게시글 리스트를 불러온다.
    @GET("vilez/shareboard")
    fun boardSearchList(
        @Query(value = "cnt") cnt: Int,
        @Query(value = "low") low: Int,
        @Query(value = "high") high: Int,
        @Query(value = "userId") id: Int,
        @Query(value = "word") word: String?= null,
        @Query(value = "category") category: String?= null
    ): Call<RESTShareResult>

    // 카테고리에 해당하는 게시글 리스트를 불러온다.
    @GET("vilez/shareboard")
    fun boardCategoryList(
        @Query(value = "cnt") cnt: Int,
        @Query(value = "low") low: Int,
        @Query(value = "high") high: Int,
        @Query(value = "userId") id: Int,
        @Query(value = "category") category: String
    ): Call<RESTShareResult>


    // 해당 게시글의 상세 정보를 가져온다.
    @GET("/vilez/shareboard/detail/{boardId}")
    fun getBoardDetail(@Path("boardId") boardId: Int): Call<RESTShareBoardDetail>

    // 유저가 해당 게시글에 북마크를 달았는지 확인한다
    @GET("/vilez/shareboard/bookmark/{boardId}/{userId}")
    fun getShareBookmark(
        @Path("boardId") boardId: Int,
        @Path("userId") id: Int
    ): Call<RESTBookmarkResult>

    // 해당 게시글의 북마크를 삭제한다.
    @DELETE("/vilez/shareboard/bookmark/{boardId}/{userId}")
    fun deleteBookmark(@Path("boardId") boardId: Int, @Path("userId") id: Int): Call<RESTResult>

    // 해당 게시글에 북마크를 추가한다.
    @POST("/vilez/shareboard/bookmark")
    fun addBookmark(@Body bookmark: Bookmark): Call<RESTResult>

    // 새로운 공유 게시글을 추가한다.
    // 이미지 리스트는 멀티파트 리스트로 param에, 나머지 정보는 json으로 멀티파트에 넣어서 보낸다.
    @Multipart
    @POST("/vilez/shareboard")
    fun postShareBoard(
        @Part("board") board: WriteBoard,
        @Part image: List<MultipartBody.Part>?
    ): Call<RESTShareBoardDetail>

    // 공유 게시글을 삭제한다. 성공시 flag에 success
    @DELETE("/vilez/shareboard/{boardId}")
    fun deleteShareBoard(@Path("boardId") boardId: Int): Call<RESTResult>

    // 공유 게시글을 수정한다.
    // 파라미터는 게시글 추가와 같다.
    @Multipart
    @PUT("/vilez/shareboard")
    fun putShareBoard(
        @Part("board") board: WriteBoard,
        @Part image: List<MultipartBody.Part>?
    ): Call<RESTShareBoardDetail>

}