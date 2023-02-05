package kr.co.vilez.data.model

data class RESTAskDto(
    val address: String,
    val area: Any,
    val category: String,
    val content: Any,
    val date: String,
    val endDay: String,
    val hopeAreaLat: String,
    val hopeAreaLng: String,
    val id: Int,
    val list: List<ImgPath>,
    val manner: Int,
    val nickName: Any,
    val startDay: String,
    val state: Int,
    val title: String,
    val type: String,
    val userId: Int
)