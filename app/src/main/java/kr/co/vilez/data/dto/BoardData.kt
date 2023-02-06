package kr.co.vilez.data.dto

class BoardData (
    var boardId : Int,
    var representImage: String,
    var title: String,
    var date: String,
    var hopePeriod:String,
    var bookmark:String,
    var userId: Int,
    val type:Int, // 요청글 BOARD_TYPE_ASK(1), 공유글 BOARD_TYPE_SHARE(2)
)