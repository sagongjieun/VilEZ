import axios from "axios";
import { postRefreshToken } from "./user";

/**
 * 인증이 필요없는 기본 요청
 * @returns axios instance
 */
function defaultInstance() {
  const instance = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });

  return instance;
}

/**
 * 인증이 필요한 요청 중 json 타입의 요청
 * @returns axios instance
 */
function authJsonInstance() {
  const instance = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });

  return instance;
}

/**
 * 인증이 필요한 요청 중 form-data 타입의 요청
 * @returns axios instance
 */
function authFormDataInstance() {
  const instance = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return instance;
}

export const defaultAxios = defaultInstance();
export const authJsonAxios = authJsonInstance();
export const authFormDataAxios = authFormDataInstance();

/** 인터셉터 처리 */

// 액세스가 만료되면 401 error, 조작되면 false
// 리프레쉬가 올바르게 들어오면 access_token, 만료되면 false, 조작되면 false;

authJsonAxios.interceptors.request.use(function (config) {
  config.headers["access-token"] = localStorage.getItem("accessToken") ? localStorage.getItem("accessToken") : "";

  return config;
});

authJsonAxios.interceptors.response.use(
  function (response) {
    console.log("interceptor response enter !!");
    console.log("response : ", response);

    if (!response) {
      // 액세스토큰이 조작된경우
      console.log("액세스 토큰이 조작된 경우 : ", response);
    }

    return response;
  },
  function (error) {
    console.log("accesstoken : ", localStorage.getItem("accessToken"));
    console.log("interceptor error enter !!");
    console.log("error : ", error);

    const originalRequest = error.config;
    console.log("원래의 요청 : ", originalRequest);

    // 액세스 토큰 만료된 경우
    if (error.response.status === 401) {
      let newAccessToken = "";

      postRefreshToken().then((res) => {
        if (res) {
          newAccessToken = res;
        }
      });
      console.log("newAccessToken : ", newAccessToken);

      if (newAccessToken) {
        localStorage.setItem("accessToken", newAccessToken);

        authJsonAxios.defaults.headers.common["access_token"] = `${newAccessToken}`;

        // 원래 요청 진행
        return authJsonAxios(originalRequest);
      }
    }
  }
);
