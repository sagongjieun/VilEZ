import axios from "axios";
// import { postRefreshToken } from "./user";

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

// http 요청 전 headers에 토큰 추가
authJsonAxios.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem("accessToken");
    console.log("accessToken > ", accessToken);

    if (accessToken) {
      // config.defaults.headers.common["Authorization"] = `${accessToken}`;
      config.headers.common["access-token"] = `${accessToken}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// http 응답에서 토큰이 만료된 경우 처리할 로직
authJsonAxios.interceptors.response.use(
  (response) => response, // 여기서는 자동으로 원래 요청 응답을 받는다?
  (error) => {
    console.log("error > ", error);
    console.log("accesstoken 만료된 경우");
    const originalRequest = error.config;
    console.log("originalRequest > ", originalRequest);
    console.log("originalRequest._retry > ", originalRequest._retry);

    // error.response.status 가 정확히 맞는지 확인하기
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      postRefreshToken().then((res) => {
        console.log("갱신 후 interceptor에서 받은 res > ", res);

        // 갱신된 accessToken을 받으면
        if (res) {
          console.log("갱신된 accessToken을 정상적으로 받으면 : ", res);
          localStorage.setItem("accessToken", res);
          originalRequest.headers.common["Authorization"] = res; // 다시 원래 수행하려고 했던 요청 수행
        } else {
          alert("로그인이 만료되었습니다. 다시 로그인 해주세요!");
          // window.location.href = "/login";
        }

        return authJsonAxios(originalRequest);
      });
    }

    return Promise.reject(error);
  }
  // function (response) {
  //   console.log("interceptor response enter !!");
  //   console.log("response : ", response);

  //   if (!response) {
  //     // 액세스토큰이 조작된경우
  //     console.log("액세스 토큰이 조작된 경우 : ", response);
  //   }

  //   return response;
  // },
  // function (error) {
  //   console.log("accesstoken : ", localStorage.getItem("accessToken"));
  //   console.log("interceptor error enter !!");
  //   console.log("error : ", error);

  //   const originalRequest = error.config;
  //   console.log("원래의 요청 : ", originalRequest);

  //   // 액세스 토큰 만료된 경우
  //   if (error.response.status === 401) {
  //     let newAccessToken = "";

  //     postRefreshToken().then((res) => {
  //       if (res) {
  //         newAccessToken = res;
  //       }
  //     });
  //     console.log("newAccessToken : ", newAccessToken);

  //     if (newAccessToken) {
  //       localStorage.setItem("accessToken", newAccessToken);

  //       authJsonAxios.defaults.headers.common["access_token"] = `${newAccessToken}`;

  //       // 원래 요청 진행
  //       return authJsonAxios(originalRequest);
  //     }
  //   }
  // }
);
