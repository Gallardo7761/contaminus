import { createContext, useContext, useState, useEffect } from "react";
import PropTypes from "prop-types";

/**
 * ConfigContext.jsx
 * 
 * Este archivo define el contexto de configuración para la aplicación, permitiendo cargar y manejar la configuración desde un archivo externo.
 * 
 * Importaciones:
 * - createContext, useContext, useState, useEffect: Funciones de React para crear y utilizar contextos, manejar estados y efectos secundarios.
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * 
 * Funcionalidad:
 * - ConfigContext: Contexto que almacena la configuración cargada, el estado de carga y cualquier error ocurrido durante la carga de la configuración.
 * - ConfigProvider: Proveedor de contexto que maneja la carga de la configuración y proporciona el estado de la configuración a los componentes hijos.
 *   - Utiliza `fetch` para cargar la configuración desde un archivo JSON.
 *   - Maneja el estado de carga y errores durante la carga de la configuración.
 * - useConfig: Hook personalizado para acceder al contexto de configuración.
 * 
 * PropTypes:
 * - ConfigProvider espera un único hijo (`children`) que es requerido y debe ser un nodo de React.
 * 
 */

const ConfigContext = createContext();

export const ConfigProvider = ({ children }) => {
  const [config, setConfig] = useState(null);
  const [configLoading, setLoading] = useState(true);
  const [configError, setError] = useState(null);

  useEffect(() => {
    const fetchConfig = async () => {
      try {
        const response = await fetch("/config/settings.json");
        if (!response.ok) throw new Error("Error al cargar settings.json");
        const json = await response.json();
        setConfig(json);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchConfig();
  }, []);

  return (
    <ConfigContext.Provider value={{ config, configLoading, configError }}>
      {children}
    </ConfigContext.Provider>
  );
};

ConfigProvider.propTypes = {
  children: PropTypes.node.isRequired,
};

export const useConfig = () => useContext(ConfigContext);
