package kr.co.vilez.data.model

data class KakaoMap(
    val roomId: String,
    val type: Int,
    val lat: Double,
    val lng: Double,
    val zoomLevel: Int,
    val isMarker: Boolean,
    val marker: Boolean
) {

    constructor():this(
        "",0,0.0,0.0,0,false,false
    )

}