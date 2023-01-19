package kr.co.vilez.data.model

data class User(
    val accessToken: String,
    val area: Any,
    val date: Any,
    val email: String,
    val id: Int,
    val manner: Int,
    val nickName: String,
    val oauth: Any,
    val password: String,
    val point: Int,
    val profileImg: Any,
    val refreshToken: String,
    val state: Int
) {

    constructor():this(
        "","","","",0,0,"","","",0,"","",0
    )
    constructor(email: String, password: String) : this(
        "","","",email,0,0,"","",password,0,"","",0)
}