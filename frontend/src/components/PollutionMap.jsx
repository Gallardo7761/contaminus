import { MapContainer, TileLayer, Circle, Popup } from 'react-leaflet';
import PropTypes from 'prop-types';

import { useConfig } from '@/hooks/useConfig.js';

import { DataProvider } from '@/context/DataContext.jsx';
import { useDataContext } from '@/hooks/useDataContext';

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

const PollutionMap = ({ groupId, deviceId }) => {
    const { config, configLoading, configError } = useConfig();

    if (configLoading) return <p>Cargando configuración...</p>;
    if (configError) return <p>Error al cargar configuración: {configError}</p>;
    if (!config) return <p>Configuración no disponible.</p>;

    const BASE = config.appConfig.endpoints.LOGIC_URL;
    const ENDPOINT = config.appConfig.endpoints.GET_DEVICE_POLLUTION_MAP;
    const endp = ENDPOINT
        .replace(':groupId', groupId)
        .replace(':deviceId', deviceId);

    const reqConfig = {
        baseUrl: `${BASE}${endp}`,
        params: {}
    };

    return (
        <DataProvider config={reqConfig}>
            <PollutionMapContent />
        </DataProvider>
    );
};


const PollutionMapContent = () => {
  const { config, configLoading, configError } = useConfig();
  const { data, dataLoading, dataError } = useDataContext();

  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  if (dataLoading) return <p>Cargando datos...</p>;
  if (dataError) return <p>Error al cargar datos: {configError}</p>;
  if (!data) return <p>Datos no disponibles.</p>;

  const SEVILLA = config?.userConfig.city;

  const pollutionData = data.map((measure) => ({
    lat: measure.lat,
    lng: measure.lon,
    level: measure.carbonMonoxide
  }));

  return (
    <div className='p-3'>
      <MapContainer center={SEVILLA} zoom={13} scrollWheelZoom={false} style={mapStyles}>
        <TileLayer
          attribution='&copy; Contribuidores de <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <PollutionCircles data={pollutionData} />
      </MapContainer>
    </div>
  );
}

const mapStyles = {
  height: '500px',
  width: '100%',
  borderRadius: '20px'
};

PollutionMap.propTypes = {
  groupId: PropTypes.number.isRequired,
  deviceId: PropTypes.number.isRequired
};

export default PollutionMap;