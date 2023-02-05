import { atom } from "recoil";

const loginUserState = atom({
  key: "loginUserState",
  default: {
    id: null,
    nickName: null,
    manner: null,
    point: null,
    profileImg: null,
  },
});

const shareDataState = atom({
  key: "shareDataState",
  default: {
    boardId: null,
    boardType: null,
    appointmentStart: "",
    appointmentEnd: "",
    shareUserId: null,
    notShareUserId: null,
  },
});

const modalOpenState = atom({
  key: "modalOpenState",
  default: false,
});

const locationState = atom({
  key: "locationState",
  default: { areaLat: 0, areaLng: 0 },
});

const enterChatRoomState = atom({
  key: "enterChatRoomState",
  default: null,
});

const checkShareDateState = atom({
  key: "checkShareDateState",
  default: false,
});

const checkAppointmentState = atom({
  key: "checkAppointmentState",
  default: false,
});

const checkShareCancelAskState = atom({
  key: "checkShareCancelAskState",
  default: false,
});

const checkShareCancelState = atom({
  key: "checkShareCancelState",
  default: false,
});

const checkShareReturnState = atom({
  key: "checkShareReturnState",
  default: false,
});

export {
  loginUserState,
  shareDataState,
  modalOpenState,
  locationState,
  enterChatRoomState,
  checkShareDateState,
  checkAppointmentState,
  checkShareCancelAskState,
  checkShareCancelState,
  checkShareReturnState,
};
