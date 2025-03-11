const timestampToTime = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString();
}

const timestampToDate = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleDateString();
}

export { timestampToTime, timestampToDate };