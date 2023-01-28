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

export { getLatestMapLocation, getChatHistory, postChatRoom };
