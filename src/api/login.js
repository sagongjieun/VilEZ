import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// POST

async function requestLogin(email, password) {
  try {
    const { data } = await jsonAxios.post(`/users/login`, { email, password });
    if (data.flag === "success") {
      localStorage.setItem("accessToken", data.data[0].accessToken);
      localStorage.setItem("refreshToken", data.data[0].refreshToken);
      localStorage.setItem("id", data.data[0].id);
      return data.data;
    } else alert("이메일 혹은 비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
  } catch (error) {
    console.log(error);
  }
}

async function refreshAccessToken() {
  try {
    const refresh_token = localStorage.getItem("refreshToken");
    const { data } = await jsonAxios.post(`/users/refresh`, { refresh_token });
    if (data.flag === "success") {
      localStorage.setItem("accessToken", data.data[0].accessToken);
      return data.data;
    } else alert("잘못된 접근입니다. 로그인을 다시 진행해주세요.");
  } catch (error) {
    console.log(error);
  }
}

export { requestLogin, refreshAccessToken };
