import { Line } from "react-chartjs-2";
import { Chart as ChartJS, LineElement, PointElement, LinearScale, CategoryScale, Filler } from "chart.js";
import CardContainer from "./CardContainer";
import "../css/HistoryCharts.css";
import PropTypes from "prop-types";

import { useTheme } from "../contexts/ThemeContext.jsx";
import { DataProvider, useData } from "../contexts/DataContext.jsx";
import { useConfig } from "../contexts/ConfigContext.jsx";

/**
 * HistoryCharts.jsx
 * 
 * Este archivo define el componente HistoryCharts, que muestra gráficos históricos de datos obtenidos de sensores.
 * 
 * Importaciones:
 * - Line: Componente de react-chartjs-2 para renderizar gráficos de líneas.
 * - ChartJS, LineElement, PointElement, LinearScale, CategoryScale, Filler: Módulos de chart.js para configurar y registrar los elementos del gráfico.
 * - CardContainer: Componente que actúa como contenedor para las tarjetas.
 * - "../css/HistoryCharts.css": Archivo CSS que contiene los estilos para los gráficos históricos.
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * - useTheme: Hook personalizado para acceder al contexto del tema.
 * - DataProvider, useData: Funciones del contexto de datos para obtener y manejar datos.
 * - useConfig: Hook personalizado para acceder al contexto de configuración.
 * 
 * Funcionalidad:
 * - HistoryCharts: Componente que configura la solicitud de datos y utiliza el DataProvider para obtener datos de sensores.
 *   - Muestra mensajes de carga y error según el estado de la configuración.
 * - HistoryChartsContent: Componente que procesa los datos obtenidos y renderiza los gráficos históricos.
 *   - Utiliza el hook `useData` para acceder a los datos de sensores.
 *   - Renderiza gráficos de líneas con diferentes colores según el tipo de dato (temperatura, humedad, contaminación).
 * 
 * PropTypes:
 * - HistoryChartsContent espera propiedades `options` (objeto), `timeLabels` (array) y `data` (array).
 * 
 */

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Filler);

const HistoryCharts = () => {
  const { config, configLoading, configError } = useConfig();
  
  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  const BASE = config.appConfig.endpoints.baseUrl;
  const ENDPOINT = config.appConfig.endpoints.sensors;

  const reqConfig = {
      baseUrl: `${BASE}/${ENDPOINT}`,
      params: {}
  }

  return (
    <DataProvider config={reqConfig}>
      <HistoryChartsContent />
    </DataProvider>
  );
};

const HistoryChartsContent = () => {
  const { config } = useConfig();
  const { data, loading } = useData();
  const { theme } = useTheme();
  
  const optionsDark = config?.appConfig?.historyChartConfig?.chartOptionsDark ?? {};
  const optionsLight = config?.appConfig?.historyChartConfig?.chartOptionsLight ?? {};
  const options = theme === "dark" ? optionsDark : optionsLight;
  const timeLabels = config?.appConfig?.historyChartConfig?.timeLabels ?? [];

  if (loading) return <p>Cargando datos...</p>;

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

HistoryChartsContent.propTypes = {
  options: PropTypes.object,
  timeLabels: PropTypes.array,
  data: PropTypes.array
};

export default HistoryCharts;