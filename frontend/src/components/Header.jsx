import PropTypes from 'prop-types';
import '../css/Header.css';
import { useTheme } from "../contexts/ThemeContext";

/**
 * Header.jsx
 * 
 * Este archivo define el componente Header, que muestra el encabezado de la página con un título y un subtítulo.
 * 
 * Importaciones:
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * - "../css/Header.css": Archivo CSS que contiene los estilos para el encabezado.
 * - useTheme: Hook personalizado para acceder al contexto del tema.
 * 
 * Funcionalidad:
 * - Header: Componente que renderiza un encabezado con un título y un subtítulo.
 *   - Utiliza el hook `useTheme` para aplicar la clase correspondiente al tema actual.
 * 
 * PropTypes:
 * - Header espera una propiedad `title` que es un string requerido.
 * - Header espera una propiedad `subtitle` que es un string opcional.
 * 
 */

const Header = (props) => {
    const { theme } = useTheme();

    return (
        <header className={`justify-content-center text-center mb-4 ${theme}`}>
            <h1>{props.title}</h1>
            <p className='subtitle'>{props.subtitle}</p>
        </header>
    );
}

Header.propTypes = {
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string
}

export default Header;