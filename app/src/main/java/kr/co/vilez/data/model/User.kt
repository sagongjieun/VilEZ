package kr.co.vilez.data.model

data class User(
    val accessToken: String,
    val area: String,
    val date: String,
    val email: String,
    val id: Int,
    var manner: Int,
    var nickName: String,
    var oauth: String,
    val password: String,
    val point: Int,
    val profileImg: String,
    val refreshToken: String,
    val state: Int,
    val areaLat: String,
    val areaLng: String

) {

    constructor(id: Int):this(
        "","","","",id,0,"","","",0,"","",0,"0.0","0.0"
    )
    constructor(id:Int, areaLat: String="0.0", areaLng: String="0.0"):this(
        "", "", "", "", 0, 0, "", "", "", 0, "", "", 0, "", "",
    )
    constructor(id:Int, password:String, nickName: String="", areaLat: String="0.0", areaLng: String="0.0"):this(
        "","","","",id,0,nickName,"",password,0,"","",0,areaLat,areaLng
    )
    constructor(email: String, password:String, nickName: String="", oauth: String=""):this(
        "","","",email,0,0,nickName,oauth,password,0,"","",0,"0.0","0.0"
    )
    constructor(email: String, password: String) : this(
        "","","",email,0,0,"","",password,0,"","",0,"0.0","0.0")
    constructor(email: String, password: String, nickName: String) : this(
        "","","",email,0,0,nickName,"",password,0,"","",0,"0.0","0.0")

    constructor(email: String, password: String, nickName: String, manner: Int, point: Int) : this(
        "","","",email,0,manner,nickName,"",password,point,"","",0,"0.0","0.0")
    constructor(email: String, password: String, nickName: String, manner: Int, point: Int, profileImg: String) : this(
        "","","",email,0,manner,nickName,"",password,point,"","",0,"0.0","0.0")
    constructor(email: String) : this(
        "","","",email,0,0,"","","",0,"","",0,"0.0","0.0")
}