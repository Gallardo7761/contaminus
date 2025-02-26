import Card from "./Card.jsx";
import PropTypes from "prop-types";

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
