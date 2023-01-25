import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

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

export { postChatRoom };
