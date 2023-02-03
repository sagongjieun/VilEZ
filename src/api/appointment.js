import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();
// const formdataAxios = formdataInstance();

// GET

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

async function getShareDate(boardId, notShareUserId, shareUserId, type) {
  try {
    const { data } = await jsonAxios.get(
      `/appointments/set/check?boardId=${boardId}&notShareUserId=${notShareUserId}&shareUserId=${shareUserId}&type=${type}`
    );

    if (data.flag === "success") return data.data;
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

// POST

async function postShareDate(body) {
  try {
    const { data } = await jsonAxios.post(`/appointments/set/period`, body);

    if (data.flag === "success") return true;
    else return true;
  } catch (error) {
    console.log(error);
  }
}

// DELETE

async function deleteShareDate(body) {
  try {
    const { data } = await jsonAxios.delete(`/appointments/set/period`, body);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

export { getAppointmentsByUserId, getPointListByUserId, getShareDate, postShareDate, deleteShareDate };
