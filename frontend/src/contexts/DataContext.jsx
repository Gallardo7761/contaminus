import { createContext, useContext, useState, useEffect } from "react";
import PropTypes from "prop-types";

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