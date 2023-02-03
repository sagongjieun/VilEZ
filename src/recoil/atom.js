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
    appointmentStart: null,
    appointmentEnd: null,
    shareUserId: null,
    notShareUserId: null,
  },
});

const modalOpenState = atom({
  key: "modalOpenState",
  default: false,
});

const enterChatRoomState = atom({
  key: "enterChatRoomState",
  default: null,
});

export { loginUserState, shareDataState, modalOpenState, enterChatRoomState };
