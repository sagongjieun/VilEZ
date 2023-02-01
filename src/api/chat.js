import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// GET

async function getLatestMapLocation(chatRoomId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/map/${chatRoomId}`);

    if (data.flag === "success") return data.data;
    else console.log("이전 위치 내역이 없습니다. 😅");
  } catch (error) {
    console.log(error);
  }
}

async function getChatHistory(chatRoomId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/room/enter/${chatRoomId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 채팅 내역이 없습니다. 😅");
  } catch (error) {
    console.log(error);
  }
}

async function getAppointmentsByBoardId(boardId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/${boardId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 게시글이 없습니다. 😅");
  } catch (error) {
    console.log(error);
  }
}

async function getMyAppointments(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 회원이 없습니다. 😅");
  } catch (error) {
    console.log(error);
  }
}

async function getLatestChattingListByUserId(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/room/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 회원이 없습니다. 😅");
  } catch (error) {
    console.log(error);
  }
}

async function getBoardIdByRoomId(roomId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/room/board/${roomId}`);

    if (data.flag === "success") return data.data;
    else console.log("일치하는 채팅방이 없습니다. 😅");
  } catch (error) {
    console.log(error);
  }
}

async function getCheckMyRoom(boardId, type, userId) {
  try {
    const { data } = await jsonAxios.get(
      `/appointments/board/checkroom?boardId=${boardId}&type=${type}&userId=${userId}`
    );

    // 채팅방 이미 존재
    if (data.flag === "success") return data.data;
    // 채팅방 미존재
    else return false;
  } catch (error) {
    console.log(error);
  }
}

async function getMyAppointmentList(userId) {
  try {
    const { data } = await jsonAxios.post(`/appointments/my/appointlist/${userId}`);

    if (data.flag === "success") return data.data;
    else console.log("내역이 없습니다. 😥");
  } catch (error) {
    console.log(error);
  }
}

// POST

async function postChatRoom(body) {
  try {
    const { data } = await jsonAxios.post(`/appointments/room`, body);

    if (data.flag === "success") return data.data;
    else alert("채팅에 연결하지 못했습니다 😅");
  } catch (error) {
    console.log(error);
  }
}

async function postAppointment(body) {
  try {
    const { data } = await jsonAxios.post(`/appointments`, body);

    if (data.flag === "success") return true;
    else alert("약속 정보 저장에 실패하였습니다. 😅");
  } catch (error) {
    console.log(error);
  }
}

export {
  getLatestMapLocation,
  getChatHistory,
  getAppointmentsByBoardId,
  getMyAppointments,
  getLatestChattingListByUserId,
  getBoardIdByRoomId,
  getCheckMyRoom,
  getMyAppointmentList,
  postChatRoom,
  postAppointment,
};
