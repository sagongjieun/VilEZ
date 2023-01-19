package kr.co.vilez.data.model

data class User(
    val accessToken: String,
    val area: String,
    val date: String,
    var email: String,
    val id: Int,
    val manner: Int,
    var nickName: String,
    val oauth: String,
    val password: String,
    val point: Int,
    val profileImg: String,
    val refreshToken: String,
    val state: Int
) {

    constructor():this(
        "","","","",0,0,"","","",0,"","",0
    )
    constructor(email: String, password: String) : this(
        "","","",email,0,0,"","",password,0,"","",0)
    constructor(email: String, password: String, nickName: String) : this(
        "","","",email,0,0,nickName,"",password,0,"","",0)

    constructor(email: String) : this(
        "","","",email,0,0,"","","",0,"","",0)
}