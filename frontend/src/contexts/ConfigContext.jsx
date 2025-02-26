import { createContext, useContext, useState, useEffect } from "react";
import PropTypes from "prop-types";

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
