import { atom } from "recoil";

const loginUserState = atom({
  key: "loginUserState",
  default: {},
});

export { loginUserState };
