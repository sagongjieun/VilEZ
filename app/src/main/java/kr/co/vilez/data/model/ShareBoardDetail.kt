package kr.co.vilez.data.model

data class ShareBoardDetail(
    val id: Int,
    val userId: Int,
    val category: String,
    val title: String,
    val content: String,
    val date: String,
    val hopeAreaLat: String,
    val hopeAreaLng: String,
    val startDay: String,
    val endDay: String,
    val list: List<ImgPath>,
    var bookmarkCnt: Int,
    val state: Int,
    val address: String,
)