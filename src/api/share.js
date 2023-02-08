import { jsonInstance, formdataInstance } from "./instance";

const jsonAxios = jsonInstance();
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

async function getShareArticleList(areaLat, areaLng, category, cnt, high, low, userId, word) {
  try {
    const { data } = await jsonAxios.get(
      `/shareboard?areaLat=${areaLat}&areaLng=${areaLng}&category=${category}&cnt=${cnt}&high=${high}&low=${low}&userId=${userId}&word=${word}`
    );
    if (data.flag === "success") return data.data;
    else return false;
    // do something
  } catch (error) {
    console.log(error);
  }
}

async function getBookmarkListByBoardId(boardId) {
  try {
    const { data } = await jsonAxios.get(`/shareboard/bookmark/${boardId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 게시글이 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getMyShareArticle(userId) {
  try {
    const { data } = await jsonAxios.get(`/shareboard/my/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 회원이 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getRelatedShareArticle(boardId, category, userId) {
  try {
    const { data } = await jsonAxios.get(`/shareboard/best/${boardId}/${category}/${userId}`);
    console.log(data);

    if (data.flag === "success") return data.data;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

// POST

async function postShareArticle(formData) {
  try {
    const { data } = await formdataAxios.post(`/shareboard`, formData);

    if (data.flag === "success") return data.data;
    else alert("공유 글 등록에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

async function postBookmark(boardId, userId) {
  try {
    const { data } = await jsonAxios.post(`/shareboard/bookmark`, { boardId, userId });

    if (data.flag === "success") alert("이 게시글을 북마크로 등록하였습니다. 😀");
    else alert("북마크 등록에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

// DELETE

async function deleteBookmark(boardId, userId) {
  try {
    const { data } = await jsonAxios.delete(`/shareboard/bookmark/${boardId}/${userId}`);

    if (data.flag === "success") {
      alert("북마크를 취소하였습니다. 😀");
    } else alert("북마크 취소에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

async function deleteShareArticleByBoardId(boardId) {
  try {
    const { data } = await jsonAxios.delete(`/shareboard/${boardId}`);

    if (data.flag === "success") alert("게시글 삭제에 성공하였습니다. 😀");
    else alert("게시글 삭제에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

// PUT

async function putShareArticle(formData) {
  try {
    const { data } = await formdataAxios.put(`/shareboard`, formData);
    // console.log(formData);
    if (data.flag === "success") {
      alert("수정되었습니다 😀");
      return data.data;
    } else alert("공유 글 수정에 실패하였습니다 😥");
  } catch (error) {
    console.log(error);
  }
}

export {
  getShareArticleByBoardId,
  getBookmarkStateByUserId,
  getShareArticleList,
  getBookmarkListByBoardId,
  getMyShareArticle,
  getRelatedShareArticle,
  postShareArticle,
  postBookmark,
  deleteBookmark,
  deleteShareArticleByBoardId,
  putShareArticle,
};
