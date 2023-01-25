import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// POST

async function requestLogin(email, password) {
  try {
    const { data } = await jsonAxios.post(`/users/login`, { email, password });

    if (data.flag === "success") return data.data;
    else console.log("이메일 혹은 비밀번호를 확인해주세요.");
  } catch (error) {
    console.log(error);
  }

  //   return await axios
  //     .post(`${BASE_URL}/login/`, {
  //       email: email,
  //       password: pw,
  //     })
  //     .then((response) => {
  //       axios.defaults.headers.common["Authorization"] = `Bearer ${response.data.access_token}`;
  //       return response.data;
  //     })
  //     .catch((error) => {
  //       console.log(error.response.data);
  //       return "이메일 혹은 비밀번호를 확인하세요.";
  //     });
}

// async function requestAccessToken(refresh_token) {
//   return await axios
//     .post(`${BASE_URL}/token/refresh/`, {
//       refresh: refresh_token,
//     })
//     .then((response) => {
//       return response.data.access;
//     })
//     .catch((error) => {
//       console.log(error.response.data);
//     });
// }

// async function checkAccessToken(refresh_token) {
//   if (axios.defaults.headers.common["Authorization"] === undefined) {
//     return await requestAccessToken(refresh_token).then((response) => {
//       return response;
//     });
//   } else {
//     return axios.defaults.headers.common["Authorization"].split(" ")[1];
//     // Bearer 떼고 반환
//   }
// }

export { requestLogin };
