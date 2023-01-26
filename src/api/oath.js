import { jsonInstance } from "./instance";

//POST
const jsonAxios = jsonInstance(); //eslint-disable-line no-unused-vars

async function postCanvas(information) {
  try {
    const { data } = await jsonAxios.post("/signs", information);
    if (data.flag === "success") {
      alert("서명이 완료되었어요");
      return data.data;
    }
  } catch (error) {
    console.log(error);
  }
}
export { postCanvas };
