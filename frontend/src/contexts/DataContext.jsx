import { createContext, useContext, useState, useEffect } from "react";
import PropTypes from "prop-types";

/**
 * DataContext.jsx
 * 
 * Este archivo define el contexto de datos para la aplicación, permitiendo obtener y manejar datos de una fuente externa.
 * 
 * Importaciones:
 * - createContext, useContext, useState, useEffect: Funciones de React para crear y utilizar contextos, manejar estados y efectos secundarios.
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * 
 * Funcionalidad:
 * - DataContext: Contexto que almacena los datos obtenidos, el estado de carga y cualquier error ocurrido durante la obtención de datos.
 * - DataProvider: Proveedor de contexto que maneja la obtención de datos y proporciona el estado de los datos a los componentes hijos.
 *   - Utiliza `fetch` para obtener datos de una URL construida a partir de la configuración proporcionada.
 *   - Maneja el estado de carga y errores durante la obtención de datos.
 * - useData: Hook personalizado para acceder al contexto de datos.
 * 
 * PropTypes:
 * - DataProvider espera un único hijo (`children`) que es requerido y debe ser un nodo de React.
 * - DataProvider también espera una configuración (`config`) que debe incluir `baseUrl` (string) y opcionalmente `params` (objeto).
 * 
 */

const DataContext = createContext();

export const DataProvider = ({ children, config }) => {
  const [data, setData] = useState(null);
  const [dataLoading, setLoading] = useState(true);
  const [dataError, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const queryParams = new URLSearchParams(config.params).toString();
        const url = `${config.baseUrl}?${queryParams}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error("Error al obtener datos");
        const result = await response.json();
        setData(result);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [config]);

  return (
    <DataContext.Provider value={{ data, dataLoading, dataError }}>
      {children}
    </DataContext.Provider>
  );
};

DataProvider.propTypes = {
  children: PropTypes.node.isRequired,
  config: PropTypes.shape({
    baseUrl: PropTypes.string.isRequired,
    params: PropTypes.object,
  }).isRequired,
};

export const useData = () => useContext(DataContext);