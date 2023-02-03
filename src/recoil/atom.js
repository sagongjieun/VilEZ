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

src/recoil/atom.js
const locationState = atom({
  key: "locationState",
  default: { areaLat: 0, areaLng: 0 },
});

export { loginUserState, shareDateState, shareDataState, modalOpenState, locationState };

const enterChatRoomState = atom({
  key: "enterChatRoomState",
  default: null,
});

export { loginUserState, shareDataState, modalOpenState, enterChatRoomState };

