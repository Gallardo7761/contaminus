import PropTypes from 'prop-types';

import { useConfig } from '@/hooks/useConfig.js';

import { DataProvider } from '@/context/DataContext.jsx';
import { useDataContext } from '@/hooks/useDataContext';

import L from "leaflet";
import "leaflet.heat";

import { useEffect } from 'react';

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

  useEffect(() => {
    if (!data || !config) return;

    const isToday = (timestamp) => {
      const today = new Date();
      const date = new Date(timestamp * 1000); // si viene en segundos

      return (
        today.getFullYear() === date.getFullYear() &&
        today.getMonth() === date.getMonth() &&
        today.getDate() === date.getDate()
      );
    };


    const mapContainer = document.getElementById("map");
    if (!mapContainer) return;

    const SEVILLA = config.userConfig.city;

    const map = L.map(mapContainer).setView(SEVILLA, 12);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    const points = data
      .filter(p => isToday(p.timestamp))
      .map(p => [p.lat, p.lon, p.carbonMonoxide]);

    L.heatLayer(points, { radius: 25 }).addTo(map);

    return () => {
      map.remove();
    };
  }, [data, config]);

  if (configLoading) return <p>Cargando configuración...</p>;
  if (configError) return <p>Error al cargar configuración: {configError}</p>;
  if (!config) return <p>Configuración no disponible.</p>;

  if (dataLoading) return <p>Cargando datos...</p>;
  if (dataError) return <p>Error al cargar datos: {configError}</p>;
  if (!data) return <p>Datos no disponibles.</p>;

  return (
    <div id="map" className='rounded-4' style={{ height: "60vh" }}></div>
  );
}

PollutionMap.propTypes = {
  groupId: PropTypes.number.isRequired,
  deviceId: PropTypes.number.isRequired
};

export default PollutionMap;