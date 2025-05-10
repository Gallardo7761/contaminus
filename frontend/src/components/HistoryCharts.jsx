import { Line } from "react-chartjs-2";
import { Chart as ChartJS, LineElement, PointElement, LinearScale, CategoryScale, Filler } from "chart.js";
import CardContainer from "./layout/CardContainer";
import PropTypes from "prop-types";

import { useTheme } from "@/hooks/useTheme";
import { DataProvider } from "@/context/DataContext.jsx";
import { useDataContext } from "@/hooks/useDataContext";
import { useConfig } from "@/hooks/useConfig";

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Filler);

const HistoryCharts = ({ groupId, deviceId }) => {
  const { config, configLoading, configError } = useConfig();

  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  const BASE = config.appConfig.endpoints.LOGIC_URL;
  const ENDPOINT = config.appConfig.endpoints.GET_DEVICE_HISTORY;
  const endp = ENDPOINT
    .replace(':groupId', groupId)
    .replace(':deviceId', deviceId); // si tu endpoint lo necesita

  const reqConfig = {
    baseUrl: `${BASE}${endp}`,
    params: {}
  };

  return (
    <DataProvider config={reqConfig}>
      <HistoryChartsContent />
    </DataProvider>
  );
};

const HistoryChartsContent = () => {
  const { config } = useConfig();
  const { data, loading, error } = useDataContext();
  const { theme } = useTheme();

  const optionsDark = config?.appConfig?.historyChartConfig?.chartOptionsDark ?? {};
  const optionsLight = config?.appConfig?.historyChartConfig?.chartOptionsLight ?? {};
  const options = theme === "dark" ? optionsDark : optionsLight;

  if (loading) return <p>Cargando datos...</p>;
  if (error) return <p>Datos no disponibles.</p>;

  const grouped = {
    temperature: [],
    humidity: [],
    pressure: [],
    carbonMonoxide: []
  };

  data?.forEach(sensor => {
    if (sensor.value != null && grouped[sensor.valueType]) {
      grouped[sensor.valueType].push({
        timestamp: new Date(sensor.timestamp),
        value: sensor.value
      });
    }
  });

  const sortAndExtract = (entries) => {
    const sorted = entries.sort((a, b) => a.timestamp - b.timestamp);
    const labels = sorted.map(e =>
      new Date(e.timestamp * 1000).toLocaleTimeString('es-ES', {
        timeZone: 'Europe/Madrid',
        hour: '2-digit',
        minute: '2-digit'
      })
    );
    const values = sorted.map(e => e.value);
    return { labels, values };
  };

  const temp = sortAndExtract(grouped.temperature);
  const hum = sortAndExtract(grouped.humidity);
  const press = sortAndExtract(grouped.pressure);
  const co = sortAndExtract(grouped.carbonMonoxide);

  const timeLabels = temp.labels.length ? temp.labels : hum.labels.length ? hum.labels : co.labels.length ? co.labels : ["Sin datos"];

  const historyData = [
    { title: "🌡️ Temperatura", data: temp.values, borderColor: "#00FF85", backgroundColor: "rgba(0, 255, 133, 0.2)" },
    { title: "💦 Humedad", data: hum.values, borderColor: "#00D4FF", backgroundColor: "rgba(0, 212, 255, 0.2)" },
    { title: "⏲ Presión", data: press.values, borderColor: "#B12424", backgroundColor: "rgba(255, 0, 0, 0.2)" },
    { title: "☁️ Contaminación", data: co.values, borderColor: "#FFA500", backgroundColor: "rgba(255, 165, 0, 0.2)" }
  ];

  return (
    <CardContainer
      cards={historyData.map(({ title, data, borderColor, backgroundColor }) => ({
        title,
        content: (
          <Line style= {{ minHeight: "250px" }}
            data={{
              labels: timeLabels,
              datasets: [{ data, borderColor, backgroundColor, fill: true, tension: 0.4 }]
            }}
            options={options}
          />
        ),
        styleMode: "override",
        className: "col-lg-6 col-xxs-12 d-flex flex-column align-items-center p-3 card-container",
        style: { minHeight: "250px" }
      }))}
      className=""
    />
  );
};

HistoryCharts.propTypes = {
  groupId: PropTypes.string.isRequired,
  deviceId: PropTypes.string.isRequired
};

HistoryChartsContent.propTypes = {
  options: PropTypes.object,
  timeLabels: PropTypes.array,
  data: PropTypes.array
};

export default HistoryCharts;