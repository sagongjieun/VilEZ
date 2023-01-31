package kr.co.vilez.data.model

data class ShareBoardDetail(
    val bookmarkCnt: Int,
    val category: String,
    val content: String,
    val date: String,
    val endDay: String,
    val hopeAreaLat: String,
    val hopeAreaLng: String,
    val id: Int,
    val list: List<ImgPath>,
    val startDay: String,
    val state: Int,
    val title: String,
    val userId: Int,
    val address: String,
)