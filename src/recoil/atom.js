import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();
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
  effects_UNSTABLE: [persistAtom],
});

const enterChatRoomState = atom({
  key: "enterChatRoomState",
  default: 0,
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

const checkUserLeaveState = atom({
  key: "checkUserLeaveState",
  default: false,
});

const goCntZero = atom({
  key: "goCntZero",
  default: 0,
});

const mainSearchTextState = atom({
  key: "mainSearchTextState",
  default: "",
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
  checkUserLeaveState,
  goCntZero,
  mainSearchTextState,
};
