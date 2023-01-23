import { jsonInstance, formdataInstance } from "./instance";

const jsonAxios = jsonInstance();
const formdataAxios = formdataInstance(); //eslint-disable-line no-unused-vars

// GET

async function getUserDetail(userId) {
  try {
    const { data } = await jsonAxios.get(`/users/detail/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 게시글이 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

export { getUserDetail };
