export const DateParser = {
  // ...deja sqlToString como está si la sigues usando...
  timestampToString: (timestamp) => {
    if (!timestamp) return '—';

    // 1) Si viene un número, lo convertimos a ISO-string
    let tsString = timestamp;
    if (typeof timestamp === 'number') {
      tsString = new Date(timestamp).toISOString();
    }

    // 2) Ahora hacemos el split sabiendo que tsString es algo tipo "YYYY-MM-DDTHH:mm:…"
    const [datePart] = tsString.split('T');
    const [year, month, day] = datePart.split('-');
    return `${day}/${month}/${year}`;
  },

  isoToStringWithTime: (isoString) => {
    if (!isoString) return '—';
    const date = new Date(isoString);
    if (isNaN(date)) return '—';
    return new Intl.DateTimeFormat('es-ES', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit', hour12: false,
      timeZone: 'Europe/Madrid'
    }).format(date);
  }
};
