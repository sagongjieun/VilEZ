import { jsonInstance, formdataInstance } from "./instance";

const jsonAxios = jsonInstance(); //eslint-disable-line no-unused-vars
const formdataAxios = formdataInstance();

// GET

async function getShareArticleByBoardId(boardId) {
  try {
    const { data } = await jsonAxios.get(`/shareboard/detail/${boardId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 게시글이 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getBookmarkStateByUserId(boardId, userId) {
  try {
    const { data } = await jsonAxios.get(`/shareboard/bookmark/${boardId}/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 게시글이나 회원정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

// POST

async function postShareArticle(formData) {
  try {
    const { data } = await formdataAxios.post(`/shareboard`, formData);

    if (data.flag === "success") alert("등록되었습니다 😀");
    else alert("공유 글 등록에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

export { getShareArticleByBoardId, getBookmarkStateByUserId, postShareArticle };
