package kr.co.vilez.util

import kr.co.vilez.data.model.RoomlistData

class DataState {
    companion object {
        var itemList = ArrayList<RoomlistData>()
        var set = HashSet<Int>()
    }
}