import axios from "axios";

function jsonInstance() {
  const instance = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL,
    headers: { "Content-Type": "application/json;charset=utf-8" },
  });

  return instance;
}

function formdataInstance() {
  const instance = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL,
    headers: { "Content-Type": "multipart/form-data" },
  });

  return instance;
}

export { jsonInstance, formdataInstance };
