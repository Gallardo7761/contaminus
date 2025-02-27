import PropTypes from "prop-types";
import { useState, useEffect, useRef } from "react";
import "../css/Card.css";
import { useTheme } from "../contexts/ThemeContext";

/**
 * Card.jsx
 * 
 * Este archivo define el componente Card, que representa una tarjeta individual con un título, estado y contenido.
 * 
 * Importaciones:
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * - useState, useEffect, useRef: Hooks de React para manejar estados, efectos secundarios y referencias.
 * - "../css/Card.css": Archivo CSS que contiene los estilos para las tarjetas.
 * - useTheme: Hook personalizado para acceder al contexto del tema.
 * 
 * Funcionalidad:
 * - Card: Componente que renderiza una tarjeta con un título, estado y contenido.
 *   - Utiliza el hook `useTheme` para aplicar la clase correspondiente al tema actual.
 *   - Ajusta el título de la tarjeta según el tamaño de la tarjeta.
 * 
 * PropTypes:
 * - Card espera una propiedad `title` que es un string requerido.
 * - Card espera una propiedad `status` que es un string requerido.
 * - Card espera una propiedad `children` que es un nodo de React requerido.
 * - Card espera una propiedad `styleMode` que es opcional y puede ser "override" o una cadena vacía.
 * - Card espera una propiedad `className` que es un string opcional.
 * 
 */

const Card = ({ title, status, children, styleMode, className, titleIcon }) => {
    const cardRef = useRef(null);
    const [shortTitle, setShortTitle] = useState(title);
    const { theme } = useTheme();

    useEffect(() => {
        const checkSize = () => {
            if (cardRef.current) {
                const width = cardRef.current.offsetWidth;
                if (width < 300 && title.length > 15) {
                    setShortTitle(title.slice(0, 10) + ".");
                } else {
                    setShortTitle(title);
                }
            }
        };

        checkSize();
        window.addEventListener("resize", checkSize);
        return () => window.removeEventListener("resize", checkSize);
    }, [title]);

    return (
        <div
            ref={cardRef}
            className={styleMode === "override" ? `${className}` : 
            `col-xl-3 col-sm-6 d-flex flex-column align-items-center p-3 card-container ${className}`}
        >
            <div className={`card p-3 w-100 ${theme}`}>
                <h3 className="text-center">
                    {titleIcon}
                    {shortTitle}
                </h3>
                <div className="card-content">{children}</div>
                {status ? <span className="status text-center mt-2">{status}</span> : null}
            </div>
        </div>
    );
};

Card.propTypes = {
    title: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
    children: PropTypes.node.isRequired,
    styleMode: PropTypes.oneOf(["override", ""]), 
    className: PropTypes.string, 
    titleIcon: PropTypes.node,
};

Card.defaultProps = {
    styleMode: "",
};

export default Card;
