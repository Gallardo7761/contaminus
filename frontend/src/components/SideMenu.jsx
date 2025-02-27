import "../css/SideMenu.css";
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

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
    return (
        <div className={`side-menu ${isOpen ? 'open' : ''}`}>
            <button className="close-btn" onClick={onClose}>
                <FontAwesomeIcon icon={faTimes} />
            </button>
            <ul>
                <li><a href="#inicio">Inicio</a></li>
                <li><a href="#mapa">Mapa</a></li>
                <li><a href="#historico">Histórico</a></li>
            </ul>
        </div>
    );
};

SideMenu.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired
}

export default SideMenu;