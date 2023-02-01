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

const shareDateState = atom({
  key: "shareDateState",
  default: {
    startDate: null,
    endDate: null,
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

export { loginUserState, shareDateState, shareDataState };
