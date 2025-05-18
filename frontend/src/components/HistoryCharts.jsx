import { Line } from "react-chartjs-2";
import { Chart as ChartJS, LineElement, PointElement, LinearScale, CategoryScale, Filler } from "chart.js";
import CardContainer from "./layout/CardContainer";
import PropTypes from "prop-types";

import { useTheme } from "@/hooks/useTheme";
import { DataProvider } from "@/context/DataContext.jsx";
import { useDataContext } from "@/hooks/useDataContext";
import { useConfig } from "@/hooks/useConfig";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";

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
    .replace(':deviceId', deviceId);

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

  const threeDaysAgo = Date.now() - (3 * 24 * 60 * 60 * 1000); // hace 3 días en ms
  const isRecent = (timestamp) => (timestamp * 1000) >= threeDaysAgo;

  data?.forEach(sensor => {
    if (
      sensor.value != null &&
      grouped[sensor.valueType] &&
      isRecent(sensor.timestamp)
    ) {
      grouped[sensor.valueType].push({
        timestamp: sensor.timestamp * 1000,
        value: sensor.value
      });
    }
  });

  const sortAndExtract = (entries) => {
    const sorted = entries.sort((a, b) => a.timestamp - b.timestamp);

    const labels = sorted.map(e =>
      new Date(e.timestamp).toLocaleTimeString('es-ES', {
        timeZone: 'UTC',
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
    <>
      <CardContainer
        cards={historyData.map(({ title, data, borderColor, backgroundColor }) => ({
          title,
          content: (
            <Line style={{ minHeight: "250px" }}
              data={{
                labels: timeLabels,
                datasets: [{ data, borderColor, backgroundColor, fill: true, tension: 0.4 }]
              }}
              options={options}
            />
          ),
          styleMode: "override",
          className: "col-lg-6 col-xxs-12 d-flex flex-column align-items-center",
          style: { minHeight: "250px" }
        }))}
      />
      <span className="m-0 p-0 d-flex align-items-center justify-content-center">
        <FontAwesomeIcon icon={faInfoCircle} className="me-2" />
        <p className="m-0 p-0">El historial muestra datos de los últimos 3 días</p>
      </span>
    </>
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