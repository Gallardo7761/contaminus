import { useConfig } from '../contexts/ConfigContext.jsx';
import { DataProvider, useData } from '../contexts/DataContext.jsx';
import Map from './Map.jsx';
/**
 * PollutionMapAll.jsx
 * 
 * Muestra el mapa de contaminación para todos los dispositivos.
 */
const PollutionMapAll = () => {
  const { config, configLoading, configError } = useConfig();

  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  const BASE = config.appConfig.endpoints.LOGIC_URL;
  const ENDPOINT = config.appConfig.endpoints.GET_ALL_POLLUTION_MAP;

  const reqConfig = {
    baseUrl: `${BASE}/${ENDPOINT}`,
    params: {}
  };

  return (
    <DataProvider config={reqConfig}>
      <PollutionMapAllContent />
    </DataProvider>
  );
};

const PollutionMapAllContent = () => {
  const { config, configLoading, configError } = useConfig();
  const { data, dataLoading, dataError } = useData();

  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  if (dataLoading) return <p>Cargando datos...</p>;
  if (dataError) return <p>Error al cargar datos: {configError}</p>;
  if (!data) return <p>Datos no disponibles.</p>;

  const pollutionData = data.map((measure) => ({
    lat: measure.lat,
    lng: measure.lon,
    level: measure.carbonMonoxide
  }));

  return <Map data={pollutionData} />;
};

export default PollutionMapAll;
