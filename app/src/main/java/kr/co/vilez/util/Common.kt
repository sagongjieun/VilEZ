package kr.co.vilez.util

class Common {

    companion object {
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
    }

}