import { MapContainer, TileLayer, Circle, Popup } from 'react-leaflet';


import { ConfigProvider } from '../contexts/ConfigContext.jsx';
import { useConfig } from '../contexts/ConfigContext.jsx';

import { DataProvider } from '../contexts/DataContext.jsx';
import { useData } from '../contexts/DataContext.jsx';

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

const PollutionMap = () => {
  return (
    <DataProvider apiUrl="https://contaminus.miarma.net/api/v1/sensors">
      <ConfigProvider>
        <PollutionMapContent />
      </ConfigProvider>
    </DataProvider>
  );
};

const PollutionMapContent = () => {
  const { config } = useConfig();
  const SEVILLA = config?.userConfig.city;

  const { data, loading } = useData();

  if (loading) return <p>Cargando datos...</p>;

  const pollutionData = data.map((sensor) => ({
    lat: sensor.lat,
    lng: sensor.lon,
    level: sensor.value
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

export default PollutionMap;