import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// GET

async function getOath(boardId, notShareUserId, shareUserId) {
  try {
    const { data } = await jsonAxios.get(
      `/signs?boardId=${boardId}&notShareUserId=${notShareUserId}&shareUserId=${shareUserId}`
    );

    if (data.flag === "success") return data.data;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

//POST

async function postCanvas(information) {
  try {
    const { data } = await jsonAxios.post("/signs", information);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

export { getOath, postCanvas };
