import { jsonInstance } from "./instance";

//POST
const jsonAxios = jsonInstance(); //eslint-disable-line no-unused-vars

async function postCanvas(information) {
  try {
    const { data } = await jsonAxios.post("/signs", information);

    if (data.flag === "success") return true;
    else return false;
  } catch (error) {
    console.log(error);
  }
}

export { postCanvas };
