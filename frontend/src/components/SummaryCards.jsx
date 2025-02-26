import PropTypes from 'prop-types';
import CardContainer from './CardContainer';

import { DataProvider } from '../contexts/DataContext';
import { useData } from '../contexts/DataContext';

import { useConfig } from '../contexts/ConfigContext';

const SummaryCards = () => {
    const { config, configLoading, configError } = useConfig();

    if (configLoading) return <p>Cargando configuración...</p>;
    if (configError) return <p>Error al cargar configuración: {configError}</p>;
    if (!config) return <p>Configuración no disponible.</p>;

    const BASE = config.appConfig.endpoints.baseUrl;
    const ENDPOINT = config.appConfig.endpoints.sensors;

    const reqConfig = {
        baseUrl: `${BASE}/${ENDPOINT}`,
        params: {
            _sort: 'timestamp',
            _order: 'desc'
        }
    }

    return (
        <DataProvider config={reqConfig}>
            <SummaryCardsContent />
        </DataProvider>
    );
}

const SummaryCardsContent = () => {
    const { data } = useData();

    const CardsData = [
        { id: 1, title: "🌡️ Temperatura", content: "N/A", status: "Esperando datos..." },
        { id: 2, title: "💧 Humedad", content: "N/A", status: "Esperando datos..." },
        { id: 3, title: "☁️ Contaminación", content: "N/A", status: "Esperando datos..." },
        { id: 4, title: "🛤️ Carretera", content: "N/A", status: "Esperando datos..." }
    ];

    if (data) {
        data.forEach((sensor) => {
            if (sensor.sensor_type === "MQ-135") {
                CardsData[2].content = `${sensor.value} µg/m³`;
                CardsData[2].status = sensor.value > 100 ? "Alta contaminación 😷" : "Aire moderado 🌤️";
            } else if (sensor.sensor_type === "DHT-11") {
                CardsData[1].content = `${sensor.humidity}%`;
                CardsData[1].status = sensor.humidity > 70 ? "Humedad alta 🌧️" : "Nivel normal 🌤️";
                CardsData[0].content = `${sensor.temperature}°C`;
                CardsData[0].status = sensor.temperature > 30 ? "Calor intenso ☀️" : "Clima agradable 🌤️";
            }
        });
    }

    return (
        <CardContainer cards={CardsData} />
    );
}

SummaryCards.propTypes = {
    data: PropTypes.array
};

export default SummaryCards;