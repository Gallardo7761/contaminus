import { MapContainer, TileLayer, Circle, Popup } from 'react-leaflet';
import PropTypes from 'prop-types';

import { useConfig } from '../contexts/ConfigContext.jsx';
import { DataProvider } from '../contexts/DataContext.jsx';
import { useData } from '../contexts/DataContext.jsx';

/**
 * PollutionCircles.jsx
 * Componente que renderiza los círculos de contaminación sobre el mapa.
 */
const PollutionCircles = ({ data }) => {
  return data.map(({ lat, lng, level }, index) => {
    const baseColor = level < 20 ? '#00FF85' : level < 60 ? '#FFA500' : '#FF0000';
    const steps = 4;
    const maxRadius = 400;
    const stepSize = maxRadius / steps;

    return (
      <div key={index}>
        {[...Array(steps)].map((_, i) => {
          const radius = stepSize * (i + 1);
          const opacity = 0.6 * ((i + 1) / steps);
          return (
            <Circle
              key={`${index}-${i}`}
              center={[lat, lng]}
              pathOptions={{ color: baseColor, fillColor: baseColor, fillOpacity: opacity, weight: 1 }}
              radius={radius}
            />
          );
        })}
        <Circle
          center={[lat, lng]}
          pathOptions={{ color: baseColor, fillColor: baseColor, fillOpacity: 0.8, weight: 2 }}
          radius={50}
        >
          <Popup>Contaminación: {level} µg/m³</Popup>
        </Circle>
      </div>
    );
  });
};

/**
 * Map.jsx
 * Componente genérico para mostrar un mapa con datos de contaminación u otros.
 */
const Map = ({ data }) => {
  const { config, configLoading, configError } = useConfig();

  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  const SEVILLA = config?.userConfig.city;

  return (
    <div className='p-3'>
      <MapContainer center={SEVILLA} zoom={13} scrollWheelZoom={false} style={mapStyles}>
        <TileLayer
          attribution='&copy; Contribuidores de <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <PollutionCircles data={data} />
      </MapContainer>
    </div>
  );
};

const mapStyles = {
  height: '500px',
  width: '100%',
  borderRadius: '20px'
};

/**
 * PollutionMapByDevice.jsx
 * Componente que muestra el mapa con los datos de contaminación de un dispositivo específico.
 */
const PollutionMapByDevice = ({ deviceId }) => {
  const { config, configLoading, configError } = useConfig();
  
  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  const BASE = config.appConfig.endpoints.LOGIC_URL;
  const ENDPOINT = config.appConfig.endpoints.GET_DEVICE_POLLUTION_MAP;
  let endp = ENDPOINT.replace('{0}', deviceId);

  const reqConfig = {
    baseUrl: `${BASE}/${endp}`,
    params: {}
  };

  return (
    <DataProvider config={reqConfig}>
      <PollutionMapContent />
    </DataProvider>
  );
};

PollutionMapByDevice.propTypes = {
  deviceId: PropTypes.number.isRequired
};

/**
 * PollutionMapContent.jsx
 * Componente que procesa los datos y pasa la información a Map.
 */
const PollutionMapContent = () => {
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

/**
 * PollutionMapAll.jsx
 * Componente que muestra el mapa con los datos de contaminación de todos los dispositivos.
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

/**
 * PollutionMapAllContent.jsx
 * Componente que procesa los datos para todos los dispositivos y pasa la información a Map.
 */
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

Map.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      lat: PropTypes.number.isRequired,
      lng: PropTypes.number.isRequired,
      level: PropTypes.number.isRequired,
    })
  ).isRequired,
};

export { PollutionMapByDevice, PollutionMapAll };