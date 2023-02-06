import { jsonInstance, formdataInstance } from "./instance";

const jsonAxios = jsonInstance();
const formDataAxios = formdataInstance();

// GET

async function getUserDetail(id) {
  try {
    const { data } = await jsonAxios.get(`/users/detail/${id}`);
    if (data.flag === "success") return data.data[0];
    else console.log("일치하는 유저 정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getUserAsk(id) {
  try {
    const { data } = await jsonAxios.get(`/askboard/my/${id}`);
    if (data.flag === "success") return data.data;
    else console.log("일치하는 작성글 정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getUserShare(id) {
  try {
    const { data } = await jsonAxios.get(`/shareboard/my/${id}`);
    if (data.flag === "success") return data.data[0];
    else console.log("일치하는 작성글 정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getUserBookMark(userId) {
  try {
    const { data } = await jsonAxios.get(`/shareboard/bookmark/my/${userId}`);
    if (data.flag === "success") return data.data;
    else console.log("일치하는 북마크 정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

// PUT

async function putUserPasswordByEmail(email, password) {
  try {
    const { data } = await jsonAxios.put(`/users/modify/password?email=${email}&password=${password}`);
    if (data.flag === "success") {
      alert("비밀번호가 성공적으로 변경되었습니다. 로그인을 진행해주세요.");
    } else alert("비밀번호가 변경에 실패했습니다. 다시 시도해주세요.");
  } catch (error) {
    console.log(error);
  }
}

async function putUserPasswordNickName(id, nickName, password) {
  try {
    const { data } = await jsonAxios.put("/users/modify", { id, nickName, password });
    if (data.flag === "success") {
      alert("프로필 정보가 변경되었습니다.");
    } else alert("프로필 변경에 실패했습니다. 다시 시도해주세요.");
  } catch (error) {
    console.log(error);
  }
}

async function putUserProfileImage(formData) {
  try {
    const { data } = await formDataAxios.put("/users/profile", formData);
    console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function putUserPoint(body) {
  try {
    const { data } = await jsonAxios.put(`/users/point`, body);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

export {
  getUserDetail,
  getUserAsk,
  getUserShare,
  getUserBookMark,
  putUserPasswordByEmail,
  putUserPasswordNickName,
  putUserProfileImage,
  putUserPoint,
};
