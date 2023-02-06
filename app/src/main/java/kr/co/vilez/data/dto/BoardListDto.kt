package kr.co.vilez.data.dto

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor

data class BoardListDto (
    val boardId : Int,
    val representImage: String,
    val title: String,
    val date: String,
    val hopeArea: String,
    val hopePeriod:String,
    val bookmark:String,
    val state: Int,
    val userId: Int,
    ) {
    constructor():this (
        0,"","","","","","0",0,0
    )
}