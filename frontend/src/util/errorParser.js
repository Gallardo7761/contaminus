export const errorParser = (err) => {
  const message = err.response?.data?.message;
  try {
    const parsed = JSON.parse(message);
    return Object.values(parsed)[0];
  // eslint-disable-next-line no-unused-vars
  } catch (e) {
    return message || err.message || "Unknown error";
  }
};
