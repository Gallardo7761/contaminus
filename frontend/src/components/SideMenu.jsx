import "../css/SideMenu.css";
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

import { DataProvider } from '../contexts/DataContext';
import { useData } from '../contexts/DataContext';

import { useConfig } from '../contexts/ConfigContext';
import { useTheme } from "../contexts/ThemeContext";

import Card from './Card';

/** ⚠️ EN PRUEBAS ⚠️
 * SideMenu.jsx
 * 
 * Este archivo define el componente SideMenu, que muestra un menú lateral con enlaces de navegación.
 * 
 * Importaciones:
 * - "../css/SideMenu.css": Archivo CSS que contiene los estilos para el menú lateral.
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * - FontAwesomeIcon, faTimes: Componentes e iconos de FontAwesome para mostrar el icono de cerrar.
 * 
 * Funcionalidad:
 * - SideMenu: Componente que renderiza un menú lateral con enlaces de navegación.
 *   - Utiliza la propiedad `isOpen` para determinar si el menú debe estar visible.
 *   - Utiliza la propiedad `onClose` para manejar el evento de cierre del menú.
 * 
 * PropTypes:
 * - SideMenu espera una propiedad `isOpen` que es un booleano requerido.
 * - SideMenu espera una propiedad `onClose` que es una función requerida.
 *  ⚠️ EN PRUEBAS ⚠️ **/

const SideMenu = ({ isOpen, onClose }) => {
    const { config, configLoading, configError } = useConfig();

    if (configLoading) return <p>Cargando configuración...</p>;
    if (configError) return <p>Error al cargar configuración: {configError}</p>;
    if (!config) return <p>Configuración no disponible.</p>;

    const BASE = config.appConfig.endpoints.DATA_URL;
    const ENDPOINT = config.appConfig.endpoints.GET_DEVICES;

    const reqConfig = {
        baseUrl: `${BASE}/${ENDPOINT}`,
        params: {}
    }

    return (
        <DataProvider config={reqConfig}>
            <SideMenuContent isOpen={isOpen} onClose={onClose} />
        </DataProvider>
    );
};

const SideMenuContent = ({ isOpen, onClose }) => {
    const { data, dataLoading, dataError } = useData();
    const { theme } = useTheme();

    if (dataLoading) return <p>Cargando datos...</p>;
    if (dataError) return <p>Error al cargar datos: {dataError}</p>;
    if (!data) return <p>Datos no disponibles.</p>;

    return (
        <div className={`side-menu ${isOpen ? 'open' : ''} ${theme}`}>
            <button className="close-btn" onClick={onClose}>
                <FontAwesomeIcon icon={faTimes} />
            </button>
            <div className="d-flex flex-column gap-3 mt-5">
                {data.map(device => {
                    return (
                        <a href={`/dashboard/${device.deviceId}`} key={device.deviceId} style={{ textDecoration: 'none' }}>
                            <Card
                                title={device.deviceName}
                                status={`ID: ${device.deviceId}`}
                                styleMode={"override"}
                                className={"col-12"}
                            >
                                {[]}
                            </Card>
                        </a>
                    );
                })}
            </div>
        </div>
    );
};

SideMenu.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired
}

SideMenuContent.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired
}

export default SideMenu;