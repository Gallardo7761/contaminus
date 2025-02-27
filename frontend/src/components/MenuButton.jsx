import "../css/MenuButton.css";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from '@fortawesome/free-solid-svg-icons';
import PropTypes from "prop-types";

/** ⚠️ EN PRUEBAS ⚠️
 * MenuButton.jsx
 * 
 * Este archivo define el componente MenuButton, que muestra un botón de menú con un icono de barras.
 * 
 * Importaciones:
 * - "../css/MenuButton.css": Archivo CSS que contiene los estilos para el botón de menú.
 * - FontAwesomeIcon, faBars: Componentes e iconos de FontAwesome para mostrar el icono de barras.
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * 
 * Funcionalidad:
 * - MenuButton: Componente que renderiza un botón con un icono de barras.
 *   - Utiliza la propiedad `onClick` para manejar el evento de clic del botón.
 * 
 * PropTypes:
 * - MenuButton espera una propiedad `onClick` que es una función requerida.
 *  ⚠️ EN PRUEBAS ⚠️ **/

export default function MenuButton({ onClick }) {
    return (
        <button className="menuBtn" onClick={onClick}>
            <FontAwesomeIcon icon={faBars} />
        </button>
    );
}

MenuButton.propTypes = {
    onClick: PropTypes.func.isRequired,
};