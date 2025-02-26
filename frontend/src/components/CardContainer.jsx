import Card from "./Card.jsx";
import PropTypes from "prop-types";

/**
 * CardContainer.jsx
 * 
 * Este archivo define el componente CardContainer, que actúa como contenedor para múltiples componentes Card.
 * 
 * Importaciones:
 * - Card: Componente que representa una tarjeta individual.
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * 
 * Funcionalidad:
 * - CardContainer: Componente que renderiza un contenedor (`div`) con una fila de tarjetas (`Card`).
 *   - Utiliza `props.cards` para mapear y renderizar cada tarjeta con su contenido.
 * 
 * PropTypes:
 * - CardContainer espera una propiedad `cards` que es un array de objetos con las propiedades `title`, `content` y `status`.
 * - CardContainer espera una propiedad `className` que es un string opcional.
 * 
 */

const CardContainer = ({ cards, className }) => {
    return (
        <div className={`row justify-content-center g-0 ${className}`}>
            {cards.map((card, index) => (
                <Card key={index} title={card.title} status={card.status} styleMode={card.styleMode} className={card.className}>
                    <p className="card-text text-center">{card.content}</p>
                </Card>
            ))}
        </div>
    );
};

CardContainer.propTypes = {
    cards: PropTypes.arrayOf(
        PropTypes.shape({
            title: PropTypes.string.isRequired,
            content: PropTypes.string.isRequired,
            status: PropTypes.string.isRequired,
        })
    ).isRequired,
    className: PropTypes.string,
};

export default CardContainer;
