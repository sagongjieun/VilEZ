import { defaultAxios } from "./instance";

async function requestKakaoLogin(code) {
  try {
    const { data } = await defaultAxios.get(`/oauth2/code/kakao?code=${code}`);

    if (data.flag === "oauth_join_success & login_success" || data.flag === "login_success") {
      return data.data;
    } else {
      alert("이메일 혹은 비밀번호가 일치하지 않네요. 다시 확인해주시겠어요?");
    }
  } catch (error) {
    console.log(error);
    alert("꺼져");
  }
}

async function requestNaverLogin(code) {
  try {
    const { data } = await defaultAxios.get(`/oauth2/code/naver?code=${code}`);

    if (data.flag === "oauth_join_success & login_success" || data.flag === "login_success") {
      return data.data;
    } else {
      alert("이메일 혹은 비밀번호가 일치하지 않네요. 다시 확인해주시겠어요?");
    }
  } catch (error) {
    console.log(error);
    alert("꺼져");
  }
}

export { requestKakaoLogin, requestNaverLogin };
