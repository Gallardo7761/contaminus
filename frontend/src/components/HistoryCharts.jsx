import { Line } from "react-chartjs-2";
import { Chart as ChartJS, LineElement, PointElement, LinearScale, CategoryScale, Filler } from "chart.js";
import CardContainer from "./layout/CardContainer";
import "@/css/HistoryCharts.css";
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
  
  const currentHour = new Date().getHours();
  const timeLabels = [
    `${currentHour - 3}:00`, `${currentHour - 2}:00`, `${currentHour - 1}:00`, `${currentHour}:00`, `${currentHour + 1}:00`, `${currentHour + 2}:00`, `${currentHour + 3}:00`
  ]

  if (loading) return <p>Cargando datos...</p>;
  if (error) return <p>Datos no disponibles.</p>;

  const temperatureData = [];
  const humidityData = [];
  const pollutionLevels = [];

  data?.forEach(sensor => {
    if (sensor.value != null) {
      if (sensor.sensor_type === "MQ-135") {
        pollutionLevels.push(sensor.value);
      } else if (sensor.sensor_type === "DHT-11") {
        temperatureData.push(sensor.value);
        humidityData.push(sensor.value);
      }
    }    
  });

  const historyData = [
    { title: "🌡️ Temperatura", data: temperatureData.length ? temperatureData : [0], borderColor: "#00FF85", backgroundColor: "rgba(0, 255, 133, 0.2)" },
    { title: "💧 Humedad", data: humidityData.length ? humidityData : [0], borderColor: "#00D4FF", backgroundColor: "rgba(0, 212, 255, 0.2)" },
    { title: "☁️ Contaminación", data: pollutionLevels.length ? pollutionLevels : [0], borderColor: "#FFA500", backgroundColor: "rgba(255, 165, 0, 0.2)" }
  ];  

  return (
    <CardContainer
      cards={historyData.map(({ title, data, borderColor, backgroundColor }) => ({
        title,
        content: (
          <Line
            data={{
              labels: timeLabels,
              datasets: [{ data, borderColor, backgroundColor, fill: true, tension: 0.4 }]
            }}
            options={options}
          />
        ),
        styleMode: "override",
        className: "col-lg-4 col-xxs-12 d-flex flex-column align-items-center p-3 card-container"
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