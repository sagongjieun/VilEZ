package kr.co.vilez.data.model

data class KakaoMap(
    val roomId: Int,
    val toUserId: Int,
    val lat: Double,
    val lng: Double,
    val zoomLevel: Int,
    val isMarker: Boolean
) {


}