import PropTypes from 'prop-types';
import CardContainer from './layout/CardContainer';

import { DataProvider } from '@/context/DataContext';
import { useDataContext } from '@/hooks/useDataContext';

import { useConfig } from '@/hooks/useConfig.js';

const SummaryCards = ({ groupId, deviceId }) => {
    const { config, configLoading, configError } = useConfig();

    if (configLoading) return <p>Cargando configuración...</p>;
    if (configError) return <p>Error al cargar configuración: {configError}</p>;
    if (!config) return <p>Configuración no disponible.</p>;

    const BASE = config.appConfig.endpoints.LOGIC_URL;
    const ENDPOINT = config.appConfig.endpoints.GET_DEVICE_LATEST_VALUES;
    const endp = ENDPOINT
        .replace(':groupId', groupId)
        .replace(':deviceId', deviceId);

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
        {
            id: 1,
            title: "Temperatura",
            content: "N/A",
            status: "Esperando datos...",
            titleIcon: '🌡 ',
            className: "col-12 col-md-6 col-lg-3",
            link: false,
            text: true
        },

        {
            id: 2,
            title: "Humedad",
            content: "N/A",
            status: "Esperando datos...",
            titleIcon: '💦 ',
            className: "col-12 col-md-6 col-lg-3",
            link: false,
            text: true
        },

        {
            id: 3,
            title: "Presión",
            content: "N/A",
            status: "Esperando datos...",
            titleIcon: '⏲ ',
            className: "col-12 col-md-6 col-lg-3",
            link: false,
            text: true
        },

        {
            id: 4,
            title: "Nivel de CO",
            content: "N/A",
            status: "Esperando datos...",
            titleIcon: '☁ ',
            className: "col-12 col-md-6 col-lg-3",
            link: false,
            text: true
        }
    ];


    if (data) {
        let coData = data[2];
        let tempData = data[1];

        CardsData[0].content = tempData.temperature + "°C";
        CardsData[0].status = "Temperatura actual";
        CardsData[1].content = tempData.humidity + "%";
        CardsData[1].status = "Humedad actual";
        CardsData[3].content = coData.carbonMonoxide + " ppm";
        CardsData[3].status = "Nivel de CO actual";
        CardsData[2].content = tempData.pressure + " hPa";
        CardsData[2].status = "Presión actual";

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