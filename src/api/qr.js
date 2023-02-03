import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// GET

async function getQrCode(userId) {
  try {
    const { data } = await jsonAxios.post(`/qrcodes?userId=${userId}`);
    if (data.flag === "success") return data.data;
    else console.log(data.flag);
  } catch (error) {
    console.log(error);
  }
}

async function deleteQrCode(imgUrl) {
  try {
    const { data } = await jsonAxios.delete(`/qrcodes?imgUrl=${imgUrl}`);
    console.log(data.flag);
  } catch (error) {
    console.log(error);
  }
}

export { getQrCode, deleteQrCode };
