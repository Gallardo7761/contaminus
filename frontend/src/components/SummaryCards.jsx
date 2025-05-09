import PropTypes from 'prop-types';
import CardContainer from './layout/CardContainer';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCloud, faClock, faTemperature0, faWater } from '@fortawesome/free-solid-svg-icons';

import { DataProvider } from '@/context/DataContext';
import { useDataContext } from '@/hooks/useDataContext';

import { useConfig } from '@/hooks/useConfig.js';
import { DateParser } from '@/util/dateParser';

const SummaryCards = ({ groupId, deviceId }) => {
    const { config, configLoading, configError } = useConfig();

    if (configLoading) return <p>Cargando configuración...</p>;
    if (configError) return <p>Error al cargar configuración: {configError}</p>;
    if (!config) return <p>Configuración no disponible.</p>;

    const BASE = config.appConfig.endpoints.LOGIC_URL;
    const ENDPOINT = config.appConfig.endpoints.GET_DEVICE_LATEST_VALUES;
    const endp = ENDPOINT
        .replace(':groupId', groupId)
        .replace(':deviceId', deviceId); // solo si lo necesitas así

    const reqConfig = {
        baseUrl: `${BASE}${endp}`,
        params: {}
    };

    return (
        <DataProvider config={reqConfig}>
            <SummaryCardsContent />
        </DataProvider>
    );
};

const SummaryCardsContent = () => {
    const { data, dataLoading, dataError } = useDataContext();

    if (dataLoading) return <p>Cargando datos...</p>;
    if (dataError) return <p>Error al cargar datos: {dataError}</p>;
    if (!data) return <p>Datos no disponibles.</p>;

    const CardsData = [
        { id: 1, title: "Temperatura", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faTemperature0} /> },
        { id: 2, title: "Humedad", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faWater} /> },
        { id: 3, title: "Nivel de CO", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faCloud} /> },
        { id: 4, title: "Actualizado a las", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faClock} /> }
    ];

    if (data) {
        let coData = data[1];
        let tempData = data[2];

        let lastTime = DateParser.timestampToString(coData.timestamp);
        let lastDate = new Date(coData.timestamp);

        CardsData[0].content = tempData.temperature + "°C";
        CardsData[0].status = "Temperatura actual";
        CardsData[1].content = tempData.humidity + "%";
        CardsData[1].status = "Humedad actual";
        CardsData[2].content = coData.carbonMonoxide + " ppm";
        CardsData[2].status = "Nivel de CO actual";
        CardsData[3].content = lastTime;
        CardsData[3].status = "Día " + lastDate.toLocaleDateString();

    }

    return (
        <CardContainer cards={CardsData} />
    );
}

SummaryCards.propTypes = {
    groupId: PropTypes.string.isRequired,
    deviceId: PropTypes.number.isRequired
};

export default SummaryCards;