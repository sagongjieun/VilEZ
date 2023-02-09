import { authJsonAxios, authFormDataAxios } from "./instance";

// GET

async function getAskArticleList(areaLat, areaLng, category, cnt, high, low, userId, word) {
  try {
    const { data } = await authJsonAxios.get(
      `/askboard?areaLat=${areaLat}&areaLng=${areaLng}&category=${category}&cnt=${cnt}&high=${high}&low=${low}&userId=${userId}&word=${word}`
    );

    if (data.flag === "success") return data.data;
    else console.log("요청 글 목록을 불러오는 데 실패하였습니다. 😥");
  } catch (error) {
    console.log(error);
  }
}

async function getAskArticleDetailByBoardId(boardId) {
  try {
    const { data } = await authJsonAxios.get(`/askboard/detail/${boardId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 게시글이 없습니다. 😥");
  } catch (error) {
    console.log(error);
  }
}

async function getMyAskArticle(userId) {
  try {
    const { data } = await authJsonAxios.get(`/askboard/my/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 회원이 없습니다. 😥");
  } catch (error) {
    console.log(error);
  }
}

async function getUserAsk(userId) {
  try {
    const { data } = await authJsonAxios.get(`/askboard/my/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 작성글 정보가 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

// POST

async function postAskArticle(formData) {
  try {
    const { data } = await authFormDataAxios.post(`/askboard`, formData);

    if (data.flag === "success") return data.data;
    else alert("요청 글 등록에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

// DELETE

async function deleteAskArticleByBoardId(boardId) {
  try {
    const { data } = await authJsonAxios.delete(`/askboard/${boardId}`);

    if (data.flag === "success") alert("요청글 삭제에 성공하였습니다. 😀");
    else alert("요청 글 삭제에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

// PUT

async function putAskArticle(formData) {
  try {
    const { data } = await authFormDataAxios.put(`/askboard`, formData);

    if (data.flag === "success") {
      alert("수정되었습니다 😀");
      return data.data;
    } else alert("요청 글 수정에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

export {
  getAskArticleList,
  getAskArticleDetailByBoardId,
  getMyAskArticle,
  getUserAsk,
  postAskArticle,
  deleteAskArticleByBoardId,
  putAskArticle,
};
