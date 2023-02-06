package kr.co.vilez.data.ask

import kr.co.vilez.data.model.ImgPath

data class MyAskArticle(
    val address: String,
    val area: Any,
    val category: String,
    val content: String,
    val date: String,
    val endDay: String,
    val hopeAreaLat: String,
    val hopeAreaLng: String,
    val id: Int,
    val list: List<ImgPath>,
    val manner: Int,
    val nickName: String,
    val startDay: String,
    val state: Int,
    val title: String,
    val type: Any,
    val userId: Int
)