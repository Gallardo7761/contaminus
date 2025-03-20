import { MapContainer, TileLayer, Circle, Popup } from 'react-leaflet';
import PropTypes from 'prop-types';

const Map = ({ data, city }) => {
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

  return (
    <div className='p-3'>
      <MapContainer center={city} zoom={13} scrollWheelZoom={false} style={mapStyles}>
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
Map.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      lat: PropTypes.number.isRequired,
      lng: PropTypes.number.isRequired,
      level: PropTypes.number.isRequired,
    })
  ).isRequired,
  city: PropTypes.arrayOf(PropTypes.number).isRequired,
};

export default Map;