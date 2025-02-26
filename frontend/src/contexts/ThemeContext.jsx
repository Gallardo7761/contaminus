import { createContext, useContext, useEffect, useState } from "react";
import PropTypes from "prop-types";

/**
 * ThemeContext.jsx
 * 
 * Este archivo define el contexto de tema para la aplicación, permitiendo cambiar entre temas claro y oscuro.
 * 
 * Importaciones:
 * - createContext, useContext, useEffect, useState: Funciones de React para crear y utilizar contextos, manejar efectos secundarios y estados.
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * 
 * Funcionalidad:
 * - ThemeContext: Contexto que almacena el tema actual y la función para cambiarlo.
 * - ThemeProvider: Proveedor de contexto que maneja el estado del tema y proporciona la función para alternar entre temas.
 *   - Utiliza `localStorage` para persistir el tema seleccionado.
 *   - Aplica la clase correspondiente al `body` del documento para reflejar el tema actual.
 * - useTheme: Hook personalizado para acceder al contexto del tema.
 * 
 * PropTypes:
 * - ThemeProvider espera un único hijo (`children`) que es requerido y debe ser un nodo de React.
 * 
 */

const ThemeContext = createContext();

export function ThemeProvider({ children }) {
    const [theme, setTheme] = useState(() => {
        return localStorage.getItem("theme") || "light";
    });

    useEffect(() => {
        document.body.classList.remove("light", "dark");
        document.body.classList.add(theme);
        localStorage.setItem("theme", theme);
    }, [theme]);

    const toggleTheme = () => {
        setTheme(prevTheme => (prevTheme === "light" ? "dark" : "light"));
    };

    return (
        <ThemeContext.Provider value={{ theme, toggleTheme }}>
            {children}
        </ThemeContext.Provider>
    );
}

ThemeProvider.propTypes = {
    children: PropTypes.node.isRequired,
};

export function useTheme() {
    return useContext(ThemeContext);
}
