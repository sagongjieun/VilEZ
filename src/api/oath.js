import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// GET

async function getOath(roomId) {
  try {
    const { data } = await jsonAxios.get(`/signs/${roomId}`);

    if (data.flag === "success") return data.data[0];
    else return false;
  } catch (error) {
    console.log(error);
  }
}

// POST

async function postOath(body) {
  try {
    const { data } = await jsonAxios.post("/signs", body);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

// DELETE

async function deleteOath(roomId) {
  try {
    const { data } = await jsonAxios.delete(`/signs/${roomId}`);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

export { getOath, postOath, deleteOath };
