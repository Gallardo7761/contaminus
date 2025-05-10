import CardContainer from "@/components/layout/CardContainer";
import LoadingIcon from "@/components/LoadingIcon";

import { useParams } from "react-router-dom";
import { useConfig } from "@/hooks/useConfig";
import { useDataContext } from "@/hooks/useDataContext";
import { useEffect, useState } from "react";

import { DataProvider } from "@/context/DataContext";

import { MapContainer, TileLayer, Marker } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Icono de marcador por defecto (porque Leaflet no lo carga bien en algunos setups)
const markerIcon = new L.Icon({
    iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
});

const MiniMap = ({ lat, lon }) => (
    <MapContainer
        center={[lat, lon]}
        zoom={15}
        scrollWheelZoom={false}
        dragging={false}
        doubleClickZoom={false}
        zoomControl={false}
        style={{ height: '150px', width: '100%', borderRadius: '10px' }}
    >
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        <Marker position={[lat, lon]} icon={markerIcon} />
    </MapContainer>
);

const GroupView = () => {
    const { groupId } = useParams();
    const { config, configLoading } = useConfig();

    if (configLoading || !config) return <p className="text-center my-5"><LoadingIcon /></p>;

    const replacedEndpoint = config.appConfig.endpoints.GET_GROUP_DEVICES.replace(':groupId', groupId);
    const reqConfig = {
        baseUrl: `${config.appConfig.endpoints.DATA_URL}${replacedEndpoint}`,
        latestValuesUrl: `${config.appConfig.endpoints.LOGIC_URL}${config.appConfig.endpoints.GET_DEVICE_LATEST_VALUES}`,
    };

    return (
        <DataProvider config={reqConfig}>
            <GroupViewContent />
        </DataProvider>
    );
}

const GroupViewContent = () => {
    const { data, dataLoading, dataError, getData } = useDataContext();
    const { groupId } = useParams();
    const [latestData, setLatestData] = useState({});

    const { config } = useConfig(); // lo pillamos por si acaso
    const latestValuesUrl = config.appConfig.endpoints.LOGIC_URL + config.appConfig.endpoints.GET_DEVICE_LATEST_VALUES;

    useEffect(() => {
        if (!data || data.length === 0) return;

        const fetchLatestData = async () => {
            const results = {};

            await Promise.all(data.map(async device => {
                const endpoint = latestValuesUrl
                    .replace(':groupId', groupId)
                    .replace(':deviceId', device.deviceId);

                try {
                    const res = await getData(endpoint);
                    results[device.deviceId] = res;
                } catch (err) {
                    console.error(`Error al obtener latest values de ${device.deviceId}:`, err);
                }
            }));

            setLatestData(results);
        };

        fetchLatestData();
    }, [data, groupId]);

    if (dataLoading) return <p className="text-center my-5"><LoadingIcon /></p>;
    if (dataError) return <p className="text-center my-5">Error al cargar datos: {dataError}</p>;

    return (
        <CardContainer
            links
            cards={data.map(device => {
                const latest = latestData[device.deviceId];
                const gpsSensor = latest?.data[0];
                const mapPreview = gpsSensor?.lat && gpsSensor?.lon
                    ? <MiniMap lat={gpsSensor.lat} lon={gpsSensor.lon} />
                    : "Sin posición";

                return {
                    title: device.deviceName,
                    status: `ID: ${device.deviceId}`,
                    content: mapPreview,
                    to: `/groups/${groupId}/devices/${device.deviceId}`,
                    styleMode: "override",
                    className: "col-12 col-md-6 col-lg-4"
                };
            })}

        />
    );
};

export default GroupView;