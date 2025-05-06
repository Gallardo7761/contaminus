import axios from "axios";

const createAxiosInstance = (baseURL, token) => {
  const instance = axios.create({
    baseURL,
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });

  return instance;
};

export default createAxiosInstance;
