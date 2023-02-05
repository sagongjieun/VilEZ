package kr.co.vilez.data.dto

class AskData (
    var boardId : Int,
    var representImage: String,
    var title: String,
    var date: String,
    var hopeArea: String,
    var hopePeriod:String,
    var state: Int,
    var userId: Int,
) {
    override fun toString(): String {
        return "title: $title, userId:$userId, boardId: $boardId"
    }
}