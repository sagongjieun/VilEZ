package kr.co.vilez.util

import androidx.appcompat.app.AppCompatActivity
import kr.co.vilez.ui.dialog.AlertDialog
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Common {

    companion object {

        val DEFAULT_PROFILE_IMG = "https://kr.object.ncloudstorage.com/vilez/basicProfile.png"

        fun showAlertDialog(context: AppCompatActivity, title:String, tag:String) {
            val dialog = AlertDialog(context, title)
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.show(context.supportFragmentManager, tag)
        }



        fun verifyPassword(password: String): Boolean {
            val regexPassword = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$"); // 비밀번호 정규식 : 8~16 (영어+숫자)
            return regexPassword.matches(password)
        }

        fun verifyEmail(email: String) : Boolean { // 이메일 정규식
            val regexEmail = """^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})${'$'}""".toRegex()
            return regexEmail.matches(email)
        }

        fun verifyNickname(nickname: String?): Boolean {
            val trimmedNickname = nickname?.trim().toString()
            val exp = Regex("^[가-힣ㄱ-ㅎa-zA-Z0-9]{2,6}\$")
            return !trimmedNickname.isNullOrEmpty() && exp.matches(trimmedNickname)
        }

        fun makeRandomPassword(id: String) :String {
            return "${getHash(id)}"
        }

        // SHA-256 해시함수
        fun getHash(str: String): String {
            var digest: String = ""
            digest = try {

                //암호화
                val sh = MessageDigest.getInstance("SHA-256") // SHA-256 해시함수를 사용
                sh.update(str.toByteArray()) // str의 문자열을 해싱하여 sh에 저장
                val byteData = sh.digest() // sh 객체의 다이제스트를 얻는다.


                //얻은 결과를 hex string으로 변환
                val hexChars = "0123456789abcdef"
                val hex = CharArray(byteData.size * 2)
                for (i in byteData.indices) {
                    val v = byteData[i].toInt() and 0xff
                    hex[i * 2] = hexChars[v shr 4]
                    hex[i * 2 + 1] = hexChars[v and 0xf]
                }

                String(hex) //최종 결과를 String 으로 변환

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                "" //오류 뜰경우 stirng은 blank값임
            }
            return digest
        }


        fun elapsedTime(date:String):String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val nowDate: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
            val formatted = nowDate.format(formatter)

            val endDate = dateFormat.parse(formatted.toString()).time
            val startDate = dateFormat.parse(date).time

            val resultTime = (endDate - startDate) / 1000

            val arr = arrayOf(60*60*24*365, 60*60*24*30, 60*60*24, 60*60, 60)
            val date = arrayOf("년", "개월", "일", "시간", "분")
            for(i in 0..4){
                val time = (resultTime / arr[i]).toInt()

                if(time > 0){
                    return Integer.toString(time)+date[i]+"전"
                }
            }
            return "방금 전"
        }


    }



}