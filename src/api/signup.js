import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// POST

async function confirmEmail(value) {
  try {
    console.log(value);
    const { data } = await jsonAxios.post("/emailConfirm/", value);
    if (data.flag === "success") {
      alert("이메일 전송에 성공했습니다. 메일함에서 인증 번호를 확인해주세요.");
      return data.data;
    } else if (data.flag === "duplicated") {
      alert("이미 계정이 존재하는 이메일입니다. 다른 이메일을 입력해주세요.");
    } else {
      alert("이메일 전송에 실패했습니다. 이메일 주소를 다시 확인해주세요.");
    }
  } catch (error) {
    console.log(error);
  }
}

// GET

export { confirmEmail };
