package kr.co.vilez.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kr.co.vilez.data.model.User
import kr.co.vilez.data.model.UserDetail

class SharedPreferencesUtil(context: Context) {
    private var userEmail: String? = null

    private val prefs: SharedPreferences = context.getSharedPreferences("userInfo", Application.MODE_PRIVATE)

    fun getUserAccessToken(): String {
        return prefs.getString("accessToken", "")!!
    }

    fun setUserAccessToken(token: String) {
        prefs.edit().putString("accessToken", token).apply()
    }

    fun updateUserAccessToken(): Boolean {
        // TODO: refreshToken을 보내서 자동갱신 하기

        return true
    }

    fun getRefreshToken(): String {
        return prefs.getString("refreshToken", "")!!
    }

    fun getUser():User {
        return User (
            accessToken = prefs.getString("accessToken", "")!!,
            area = prefs.getString("area", "")!!,
            date = prefs.getString("date", "")!!,
            email = prefs.getString("email", "")!!,
            id = prefs.getInt("id", -1),
            manner = prefs.getInt("manner", 0),
            nickName = prefs.getString("nickName", "")!!,
            oauth = prefs.getString("oauth", "")!!,
            password = prefs.getString("password", "")!!,
            point = prefs.getInt("point", 0),
            profileImg = prefs.getString("profileImg", Common.DEFAULT_PROFILE_IMG)!!,
            refreshToken = prefs.getString("refreshToken", "")!!,
            state = prefs.getInt("state", 0)
        )
    }

    fun setUser(user:User) {
        // 이메일 아이디 빼고 다 넣기
        prefs.edit{
            putInt("id", user.id)
            putString("oauth", user.oauth)
            putString("date", user.date)
            putString("nickName", user.nickName)
            putString("accessToken", user.accessToken)
            putString("refreshToken", user.refreshToken)
            putString("profileImg", user.profileImg)
            putInt("point", user.point)
            putInt("manner", user.manner)
            putString("area", user.area)
            putInt("state", user.state)
        }
    }

    fun getId():Int {
        return prefs.getInt("id", -1)
    }

    fun getEmail(): String {
        return prefs.getString("email", "")!!
    }

    fun setProfileImg(img: String) {
        prefs.edit().putString("profileImg", img).apply()
    }
    fun setPoint(point:Int) {
        prefs.edit().putInt("point", point).apply()
    }
    fun setManner(manner:Int) {
        prefs.edit().putInt("manner", manner).apply()
    }
    fun setArea(area:String) {
        prefs.edit().putString("area", area).apply()
    }
    fun setNickName(name:String) {
        prefs.edit().putString("nickName", name).apply()
    }

    fun getNickName():String {
        return prefs.getString("nickName", "")!!
    }

    fun setUserDetail(detail: UserDetail) {
        prefs.edit {
            putString("profileImg", detail.profile_img)
            putString("nickName", detail.nickName)
            putInt("point", detail.point)
            putInt("manner", detail.manner)
            putString("area", detail.area)
        }
    }

    fun getUserDetail(): UserDetail {
        return UserDetail(
            prefs.getString("area", "")!!,
            prefs.getInt("manner", 0),
            prefs.getString("nickName", "")!!,
            prefs.getInt("point", 0),
            prefs.getString("profileImg", Common.DEFAULT_PROFILE_IMG)!!
        )
    }

    fun removeAutoLogin() {
        prefs.edit {
            remove("autoLogin")
            remove("email")
            remove("password")
        }
    }

    fun isAutoLogin():Boolean {
        return prefs.getBoolean("autoLogin", false)
    }

    fun setAutoLogin(user:User) {
        prefs.edit {
            putString("email", user.email)
            putString("password", user.password)
            putBoolean("autoLogin", true)
        }
    }

    fun getAutoLogin() :User {
        return User(
            email = prefs.getString("email", "test@naver.com")!!,
            password = prefs.getString("passwqord", "12345")!!
        )
    }

    fun removeAll() {
        prefs.edit {
            remove("email")
            remove("passwrod")
            remove("autoLogin")
            remove("accessToken")
            remove("area")
            remove("date")
            remove("id")
            remove("manner")
            remove("nickName")
            remove("oauth")
            remove("password")
            remove("point")
            remove("profileImg")
            remove("refreshToken")
            remove("state")
        }
    }
}