import Card from "./Card.jsx";
import PropTypes from "prop-types";

const CardContainer = ({ cards, className }) => {
    return (
        <div className={`row justify-content-center g-3 ${className}`}>
            {cards.map((card, index) => (
                <div key={index} className={card.className ?? "col-12 col-md-6 col-lg-3"}>
                    <Card {...card}>
                        {card.content}
                    </Card>
                </div>
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
            className: PropTypes.string,
            styleMode: PropTypes.string,
            style: PropTypes.object,
            titleIcon: PropTypes.node,
            link: PropTypes.bool,
            to: PropTypes.string,
            text: PropTypes.bool,
        })
    ).isRequired,
    className: PropTypes.string,
};

export default CardContainer;
