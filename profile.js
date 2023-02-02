import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

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

async function putUserProfile(id, nickName, password) {
  try {
    const { data } = await jsonAxios.post("/users/join", { id, nickName, password });
    if (data.flag === "success") {
      alert("프로필 수정이 완료되었습니다.");
      return data.data;
    } else if (data.flag === "fail") {
      alert("프로필 수정을 다시 진행해주세요.");
      return "";
    }
  } catch (error) {
    console.log(error);
  }
}

export { getUserDetail, getUserAsk, getUserShare, putUserProfile };
