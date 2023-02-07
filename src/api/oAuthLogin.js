import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

async function requestKakaoLogin(code) {
  try {
    const { data } = await jsonAxios.get(`/oauth2/code/kakao?code=${code}`);
    if (data.flag === "login_success") {
      // localStorage.setItem("accessToken", data.data[0].accessToken);
      // localStorage.setItem("refreshToken", data.data[0].refreshToken);
      // localStorage.setItem("id", data.data[0].id);
      return data.data;
    } else {
      alert("이메일 혹은 비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
      console.log(data);
    }
  } catch (error) {
    console.log(error);
  }
}

async function requestNaverLogin(code) {
  try {
    console.log(code);
    const { data } = await jsonAxios.get(`/oauth2/code/kakao?code=${code}`);
    return data;
  } catch (error) {
    console.log(error);
  }
}

export { requestKakaoLogin, requestNaverLogin };
