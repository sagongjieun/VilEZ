package kr.co.vilez.service

import kr.co.vilez.data.ask.MyAskArticle
import kr.co.vilez.data.dto.WriteBoard
import kr.co.vilez.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

data class RESTMyAskArticleResult(
    val `data`: List<MyAskArticle>,
    val flag: String
)
data class RESTAskResult(
    val `data`: List<List<AskData>>,
    val flag: String
)
data class RESTAskDetailResult(
    val `data`: List<AskBoardDetail>,
    val flag: String
)
interface AskApi {
    // 내가 쓴 글 목록을 불러온다.
    @GET("/vilez/askboard/my/{userId}")
    fun getMyArticle(@Path("userId")id:Int):Call<RESTMyAskArticleResult>

    // 모든 요청 글 목록을 불러온다. (area는 쓰지 않는다)
    @GET("/vilez/askboard")
    fun boardList(
        @Query(value = "cnt") cnt: Int,
        @Query(value = "low") low: Int,
        @Query(value = "high") high: Int,
        @Query(value = "userId") id: Int
    ): Call<RESTAskResult>

    // 검색 키워드를 포함하는 제목, 본문을 가진 게시글 리스트를 불러온다.
    @GET("/vilez/askboard")
    fun boardSearchList(
        @Query(value = "cnt") cnt: Int,
        @Query(value = "low") low: Int,
        @Query(value = "high") high: Int,
        @Query(value = "userId") id: Int,
        @Query(value = "word") word: String ?= null,
        @Query(value ="category")category:String? = null
    ): Call<RESTAskResult>

    // 카테고리에 해당하는 게시글 리스트를 불러온다.
    @GET("/vilez/askboard")
    fun boardCategoryList(
        @Query(value = "cnt") cnt: Int,
        @Query(value = "low") low: Int,
        @Query(value = "high") high: Int,
        @Query(value = "userId") id: Int,
        @Query(value ="category")category:String
    ): Call<RESTAskResult>

    // 해당하는 요청글의 상세 정보를 불러온다.
    @GET("/vilez/askboard/detail/{boardId}")
    fun getBoardDetail(@Path("boardId") boardId: Int): Call<RESTAskDetailResult>

    // 새로운 요청 게시글을 추가한다.
    // 이미지 리스트는 멀티파트 리스트로 param에, 나머지 정보는 json으로 멀티파트에 넣어서 보낸다.
    @Multipart
    @POST("/vilez/askboard")
    fun postBoard(
        @Part("board") board: WriteBoard,
        @Part image: List<MultipartBody.Part>?
    ): Call<RESTAskDetailResult>

    // 요청 게시글을 삭제한다. 성공시 flag에 success
    @DELETE("/vilez/askboard/{boardId}")
    fun deleteBoard(@Path("boardId") boardId: Int): Call<RESTResult>

    // 요청 게시글을 수정한다.
    // 파라미터는 게시글 추가와 같다.
    @Multipart
    @PUT("/vilez/askboard")
    fun putBoard(
        @Part("board") board: WriteBoard,
        @Part image: List<MultipartBody.Part>?
    ): Call<RESTAskDetailResult>

}