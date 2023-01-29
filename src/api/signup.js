import { jsonInstance } from "./instance";

const jsonAxios = jsonInstance();

// POST

async function confirmEmail(email) {
  try {
    console.log(email);
    const { data } = await jsonAxios.post("/emailConfirm/", { email: email });
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

async function postUserInformation(userInformation) {
  try {
    console.log(userInformation);
    const { data } = await jsonAxios.post("/users/join", userInformation);
    if (data.flag === "success") {
      alert("회원가입이 완료되었습니다. 로그인을 진행해주세요.");
      return data.data;
    } else if (data.flag === "fail") {
      alert("회원가입이 정상적으로 완료되지 않았습니다. 다시 진행해주세요.");
      return "";
    }
  } catch (error) {
    console.log(error);
  }
}

async function checkNickName(nickName) {
  try {
    console.log(nickName);
    const { data } = await jsonAxios.get(`/users/check?nickname=${nickName}`);
    if (data.flag === "success") {
      return { text: `${nickName}은(는) 사용 가능한 닉네임입니다.`, isNickNameAvailable: true };
    } else if (data.flag === "fail") {
      return {
        text: `${nickName}은(는) 사용중인 닉네임입니다. 다른 닉네임을 입력해 주세요.`,
        isNickNameAvailable: false,
      };
    }
  } catch (error) {
    console.log(error);
  }
}

// GET

export { confirmEmail, checkNickName, postUserInformation };
