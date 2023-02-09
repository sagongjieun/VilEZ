import { authJsonAxios } from "./instance";

// GET

async function getQrCode(userId) {
  try {
    const { data } = await authJsonAxios.post(`/qrcodes?userId=${userId}`);
    if (data.flag === "success") return data.data;
    else console.log(data.flag);
  } catch (error) {
    console.log(error);
  }
}

async function deleteQrCode(imgUrl) {
  try {
    const { data } = await authJsonAxios.delete(`/qrcodes?imgUrl=${imgUrl}`);
    console.log(data.flag);
  } catch (error) {
    console.log(error);
  }
}

export { getQrCode, deleteQrCode };
