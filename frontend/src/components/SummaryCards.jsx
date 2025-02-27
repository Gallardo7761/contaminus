import PropTypes from 'prop-types';
import CardContainer from './CardContainer';

import { DataProvider } from '../contexts/DataContext';
import { useData } from '../contexts/DataContext';

import { useConfig } from '../contexts/ConfigContext';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCloud, faGauge, faTemperature0, faWater } from '@fortawesome/free-solid-svg-icons';

/**
 * SummaryCards.jsx
 * 
 * Este archivo define el componente SummaryCards, que muestra tarjetas resumen con información relevante obtenida de sensores.
 * 
 * Importaciones:
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * - CardContainer: Componente que actúa como contenedor para las tarjetas.
 * - DataProvider, useData: Funciones del contexto de datos para obtener y manejar datos.
 * - useConfig: Hook personalizado para acceder al contexto de configuración.
 * 
 * Funcionalidad:
 * - SummaryCards: Componente que configura la solicitud de datos y utiliza el DataProvider para obtener datos de sensores.
 *   - Muestra mensajes de carga y error según el estado de la configuración.
 * - SummaryCardsContent: Componente que procesa los datos obtenidos y actualiza el contenido de las tarjetas.
 *   - Utiliza el hook `useData` para acceder a los datos de sensores.
 *   - Actualiza el contenido y estado de las tarjetas según los datos obtenidos.
 * 
 * PropTypes:
 * - SummaryCards espera una propiedad `data` que es un array.
 * 
 */

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
        { id: 1, title: "Temperatura", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faTemperature0} /> },
        { id: 2, title: "Humedad", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faWater} /> },
        { id: 3, title: "Contaminación", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faCloud} /> },
        { id: 4, title: "Presión", content: "N/A", status: "Esperando datos...", titleIcon: <FontAwesomeIcon icon={faGauge} />  }
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