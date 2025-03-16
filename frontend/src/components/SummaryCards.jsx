import PropTypes from 'prop-types';
import CardContainer from './CardContainer';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCloud, faClock, faTemperature0, faWater } from '@fortawesome/free-solid-svg-icons';

import { DataProvider } from '../contexts/DataContext';
import { useData } from '../contexts/DataContext';

import { useConfig } from '../contexts/ConfigContext';
import { timestampToTime, formatTime } from '../util/date.js';

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

const SummaryCards = ({ deviceId }) => {
    const { config, configLoading, configError } = useConfig();

    if (configLoading) return <p>Cargando configuración...</p>;
    if (configError) return <p>Error al cargar configuración: {configError}</p>;
    if (!config) return <p>Configuración no disponible.</p>;

    const BASE = config.appConfig.endpoints.LOGIC_URL;
    const ENDPOINT = config.appConfig.endpoints.GET_DEVICE_LATEST_VALUES;
    const endp = ENDPOINT.replace('{0}', deviceId);

    const reqConfig = {
        baseUrl: `${BASE}/${endp}`,
        params: {}
    }

    return (
        <DataProvider config={reqConfig}>
            <SummaryCardsContent deviceId={deviceId} />
        </DataProvider>
    );
}

const SummaryCardsContent = () => {
    const { data, dataLoading, dataError } = useData();

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

        let lastTime = timestampToTime(coData.airValuesTimestamp);
        let lastDate = new Date(coData.airValuesTimestamp);

        CardsData[0].content = tempData.temperature + "°C";
        CardsData[0].status = "Temperatura actual";
        CardsData[1].content = tempData.humidity + "%";
        CardsData[1].status = "Humedad actual";
        CardsData[2].content = coData.carbonMonoxide + " ppm";
        CardsData[2].status = "Nivel de CO actual";
        CardsData[3].content = formatTime(lastTime);
        CardsData[3].status = "Día " + lastDate.toLocaleDateString();

    }

    return (
        <CardContainer cards={CardsData} />
    );
}

SummaryCards.propTypes = {
    deviceId: PropTypes.number.isRequired
};

export default SummaryCards;