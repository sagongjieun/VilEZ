package kr.co.vilez.data.model

data class ShareDto(
    val area: String,
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
    val path: Any,
    val startDay: String,
    val state: Int,
    val title: String,
    val userId: Int,
    val address: String,
)