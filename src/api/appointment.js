import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();
// const formdataAxios = formdataInstance();

async function getAppointmentsByUserId(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/appointlist/${userId}`);
    if (data.flag === "success") return data.data;
    else console.log("일치하는 약속이 없습니다.");
  } catch (error) {
    console.log(error);
  }
}

async function getPointListByUserId(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/point?userId=${userId}`);
    if (data.flag === "success") return data.data;
    else console.log("포인트 내역이 존재하지 않습니다.");
  } catch (error) {
    console.log(error);
  }
}

export { getAppointmentsByUserId, getPointListByUserId };
