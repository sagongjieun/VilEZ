import { authJsonAxios, authFormDataAxios, defaultAxios } from "./instance";

// GET

async function getUserDetail(userId) {
  try {
    const { data } = await authJsonAxios.get(`/users/detail/${userId}`);

    if (data.flag === "success") return data.data[0];
    else console.log("일치하는 유저 정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getCheckNickName(nickName) {
  try {
    const { data } = await authJsonAxios.get(`/users/check?nickname=${nickName}`);

    if (data.flag === "success") {
      return { text: `"${nickName}"은(는) 사용 가능한 닉네임입니다.`, isNickNameAvailable: true };
    } else if (data.flag === "fail") {
      return {
        text: `"${nickName}"은(는) 사용중인 닉네임입니다. 다른 닉네임을 입력해 주세요.`,
        isNickNameAvailable: false,
      };
    }
  } catch (error) {
    console.log(error);
  }
}

// POST

async function postMannerPoint(body) {
  try {
    const { data } = await authJsonAxios.post(`/users/manner`, body);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

async function postLogin(email, password) {
  try {
    const { data } = await defaultAxios.post(`/users/login`, { email, password });

    if (data.flag === "success") {
      if (!data.data) {
        alert("이메일 혹은 비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
      } else {
        console.log(data);
        const res = data.data[0];

        localStorage.setItem("accessToken", res.accessToken);
        localStorage.setItem("refreshToken", res.refreshToken);
        localStorage.setItem("id", res.id);
        localStorage.setItem("nickName", res.nickName);
        localStorage.setItem("profileImg", res.profileImg);

        return data.data;
      }
    } else {
      alert("이메일 혹은 비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
      return false;
    }
  } catch (error) {
    console.log(error);
  }
}

async function postLogout(userInfo) {
  try {
    const { data } = await defaultAxios.post("users/logout", userInfo);

    if (data.flag === "success") return true;
    else console.log("로그아웃이 정상적으로 완료되지 않았습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function postRefreshToken() {
  try {
    // 리프레쉬 토큰을 이용해 액세스 토큰을 갱신
    const refreshToken = localStorage.getItem("refreshToken");
    const headers = {
      "Content-Type": "application/json;charset=utf-8",
      "refresh-token": refreshToken,
    };

    const { data } = await defaultAxios.post(
      `/users/refresh`,
      {},
      {
        headers: headers,
      }
    );

    if (data.flag === "success") {
      return data.data[0];
    } else if (data.flag === "fail") {
      console.log(data);
    } else {
      return false;
    }
  } catch (error) {
    console.log(error);
  }
}

async function postUserInformation(userInformation) {
  try {
    const { data } = await defaultAxios.post("/users/join", userInformation);

    if (data.flag === "success") {
      alert("회원가입이 완료되었습니다. 로그인을 진행해주세요.");
      return data.data;
    } else if (data.flag === "fail") {
      alert("회원가입이 정상적으로 완료되지 않았습니다. 다시 진행해주세요.");
      return "";
    }
  } catch (error) {
    console.log(error);
  }
}

// PUT

async function putUserPasswordByEmail(email, password) {
  try {
    const { data } = await authJsonAxios.put(`/users/modify/password?email=${email}&password=${password}`);

    if (data.flag === "success") alert("비밀번호가 성공적으로 변경되었습니다. 로그인을 진행해주세요.");
    else alert("비밀번호가 변경에 실패했습니다. 다시 시도해주세요.");
  } catch (error) {
    console.log(error);
  }
}

async function putUserPasswordNickName(userId, nickName, password) {
  try {
    console.log(userId, nickName, password, "여기요");
    const { data } = await authJsonAxios.put("/users/modify", { id: userId, nickName: nickName, password: password });
    if (data.flag === "success") {
      return data;
    } else alert("프로필 변경에 실패했습니다. 다시 시도해주세요.");
  } catch (error) {
    console.log(error);
  }
}

async function putUserProfileImage(formData) {
  try {
    const { data } = await authFormDataAxios.put("/users/profile", formData);

    if (data.flag === "success") return data.data;
    else alert("프로필 변경에 실패했습니다. 다시 시도해주세요.");
  } catch (error) {
    console.log(error);
  }
}

export {
  getUserDetail,
  postMannerPoint,
  putUserPasswordByEmail,
  putUserPasswordNickName,
  putUserProfileImage,
  postLogin,
  postLogout,
  postRefreshToken,
  postUserInformation,
  getCheckNickName,
};
