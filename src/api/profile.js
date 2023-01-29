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
    if (data.flag === "success") return data.data;
    else console.log("일치하는 작성글 정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

export { getUserDetail, getUserAsk, getUserShare };
