import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// GET

async function getQrCode(userId) {
  try {
    const { data } = await jsonAxios.post(`/qrcodes`, userId);
    if (data.flag === "success") return data.data;
    else alert("QR CODE 생성에 실패했습니다. 다시 시도해주세요.");
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
