package kr.co.vilez.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import kr.co.vilez.data.model.User
import kr.co.vilez.data.model.UserDetail

private const val TAG = "빌리지_SharedPreferencesUtil"
class SharedPreferencesUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("userInfo", Application.MODE_PRIVATE)

    fun setFCMToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getFCMToken():String {
        return prefs.getString("token", "none")!!
    }

    fun getUserAccessToken(): String {
        return prefs.getString("accessToken", "")!!
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
            state = prefs.getInt("state", 0),
            areaLat = prefs.getString("areaLat","0.0")!!,
            areaLng = prefs.getString("areaLng","0.0")!!
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
            putString("areaLat", user.areaLat)
            putString("areaLng",user.areaLng)
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

    fun setOAuth(oauth: String) {
        prefs.edit().putString("oauth", oauth).apply()
    }
    fun getOAuth():String {
        return prefs.getString("oauth", "")!!
    }

    fun setLat(lat: String) {
        prefs.edit().putString("areaLat",lat).apply()
    }
    fun setLng(lng: String) {
        prefs.edit().putString("areaLng",lng).apply()
    }
    fun getLat():String {
        return prefs.getString("areaLat","0.0")!!
    }
    fun getLng():String {
        return prefs.getString("areaLng","0.0")!!
    }
    fun setUserDetail(detail: UserDetail) {
        prefs.edit {
            putString("profileImg", detail.profile_img)
            putString("nickName", detail.nickName)
            putInt("point", detail.point)
            putInt("manner", detail.manner)
            putString("area", detail.area)
            putString("areaLat",detail.areaLat)
            putString("areaLng",detail.areaLng)
        }
    }

    fun getUserDetail(): UserDetail {
        return UserDetail(
            prefs.getString("area", "")!!,
            prefs.getInt("manner", 0),
            prefs.getString("nickName", "")!!,
            prefs.getInt("point", 0),
            prefs.getString("profileImg", Common.DEFAULT_PROFILE_IMG)!!,
            prefs.getString("areaLat","0.0")!!,
            prefs.getString("areaLng","0.0")!!
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
        Log.d(TAG, "setAutoLogin: true")
        prefs.edit {
            putString("email", user.email)
            putString("password", user.password)
            putBoolean("autoLogin", true)
            putString("oauth", user.oauth)
        }
    }

    fun getAutoLogin() :User {
        return User(
            email = prefs.getString("email", "")!!,
            password = prefs.getString("password", "")!!,
            oauth = prefs.getString("oauth", "")!!,
        )
    }

    fun removeAll() {
        prefs.edit {
            remove("email")
            remove("password")
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
            remove("areaLat")
            remove("areaLng")
        }
    }
}