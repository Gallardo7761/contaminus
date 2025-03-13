const timestampToTime = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString();
}

const timestampToDate = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleDateString();
}

const formatTime = (date) => {
    if (date.length === 8) {
        return date.slice(0,5);
    } else {
        return `0${date.slice(0,3)}`;
    }
}

export { timestampToTime, timestampToDate, formatTime };