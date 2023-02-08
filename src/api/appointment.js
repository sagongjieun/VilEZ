import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

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

async function getShareState(roomId) {
  try {
    const { data } = await jsonAxios.get(`/returns/state?roomId=${roomId}`);

    if (data.flag === "success") return data.data[0];
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function getShareReturnState(roomId) {
  try {
    const { data } = await jsonAxios.get(`/returns?roomId=${roomId}`);

    if (data.flag === "success") return data.data[0].state;
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function getShareListByUserId(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/give/${userId}`);

    if (data.flag === "success") return data.data[0].state;
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function getNotShareListByUserId(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/${userId}`);

    if (data.flag === "success") return data.data[0].state;
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function getCheckShareCancelRequest(roomId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/request/cancel/${roomId}`);

    if (data.flag === "success") return data.data[0];
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function getAppointmentDate(boardId, notShareUserId, shareUserId, type) {
  try {
    const { data } = await jsonAxios.get(
      `/appointments/date?boardId=${boardId}&notShareUserId=${notShareUserId}&shareUserId=${shareUserId}&type=${type}`
    );

    if (data.flag === "success") return data.data[0];
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function getAppointmentsWithinAWeek(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/date/${userId}`);
    if (data.flag === "success") return data.data[0];
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}

async function getMyShareAppointments(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/give/${userId}`);

    if (data.flag === "success") return data.data[0];
    else console.log(data);
  } catch (error) {
    console.log(error);
  }
}
async function getMyRentAppointments(userId) {
  try {
    const { data } = await jsonAxios.get(`/appointments/my/${userId}`);

    if (data.flag === "success") return data.data[0];
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
    else return false;
  } catch (error) {
    console.log(error);
  }
}

async function postShareReturnState(roomId) {
  try {
    const { data } = await jsonAxios.post(`/returns`, { roomId });

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

async function postShareCancelRequest(body) {
  try {
    const { data } = await jsonAxios.post(`/appointments/request/cancel`, body);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

// PUT

async function putShareDate(body) {
  try {
    const { data } = await jsonAxios.put(`/appointments/set/period`, body);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

// DELETE

async function deleteAppointment(reason, roomId) {
  try {
    const { data } = await jsonAxios.delete(`/appointments?reason=${reason}&roomId=${roomId}`);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

export {
  getAppointmentsByUserId,
  getPointListByUserId,
  getShareDate,
  getShareState,
  getShareReturnState,
  getShareListByUserId,
  getNotShareListByUserId,
  getCheckShareCancelRequest,
  getAppointmentDate,
  getAppointmentsWithinAWeek,
  getMyShareAppointments,
  getMyRentAppointments,
  postShareDate,
  postShareReturnState,
  postShareCancelRequest,
  putShareDate,
  deleteAppointment,
};
