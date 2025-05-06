import "@/css/SideMenu.css";
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faHome } from '@fortawesome/free-solid-svg-icons';

import { DataProvider } from '@/context/DataContext';
import { useDataContext } from "@/hooks/useDataContext";

import { useConfig } from '@/hooks/useConfig.js';
import { useTheme } from "@/hooks/useTheme";

import Card from './Card';

const SideMenu = ({ isOpen, onClose }) => {
    const { config, configLoading, configError } = useConfig();

    if (configLoading) return <p>Cargando configuración...</p>;
    if (configError) return <p>Error al cargar configuración: {configError}</p>;
    if (!config) return <p>Configuración no disponible.</p>;

    const BASE = config.appConfig.endpoints.DATA_URL;
    const ENDPOINT = config.appConfig.endpoints.GET_DEVICES;

    const reqConfig = {
        baseUrl: `${BASE}${ENDPOINT}`,
        params: {}
    }

    return (
        <DataProvider config={reqConfig}>
            <SideMenuContent isOpen={isOpen} onClose={onClose} />
        </DataProvider>
    );
};

const SideMenuContent = ({ isOpen, onClose }) => {
    const { data, dataLoading, dataError } = useDataContext();
    const { theme } = useTheme();

    if (dataLoading) return <p>Cargando datos...</p>;
    if (dataError) return <p>Error al cargar datos: {dataError}</p>;
    if (!data) return <p>Datos no disponibles.</p>;

    return (
        <div className={`side-menu ${isOpen ? 'open' : ''} ${theme}`}>
            <button className="home-btn" onClick={() => window.location.href = '/'}>
                <FontAwesomeIcon icon={faHome} />
            </button>
            <button className="close-btn" onClick={onClose}>
                <FontAwesomeIcon icon={faTimes} />
            </button>
            <hr className="separation w-100"></hr>
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