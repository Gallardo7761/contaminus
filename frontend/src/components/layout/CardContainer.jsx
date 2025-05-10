import Card from "./Card.jsx";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

const CardContainer = ({ links, cards, className, text }) => {
    return (
        <div className={`row justify-content-center g-0 ${className}`}>
            {cards.map((card, index) => (
                links ? (
                    <Link to={card.to} key={index} style={{ textDecoration: 'none' }}>
                        <Card title={card.title} status={card.status} styleMode={card.styleMode} className={card.className} titleIcon={card.titleIcon} style={card.style}>
                            {text
                                ? <p className="card-text text-center">{card.content}</p>
                                : <div className="my-2">{card.content}</div>
                            }
                        </Card>
                    </Link>
                ) : (
                    <Card key={index} title={card.title} status={card.status} styleMode={card.styleMode} className={card.className} titleIcon={card.titleIcon} style={card.style}>
                        {text
                            ? <p className="card-text text-center">{card.content}</p>
                            : <div className="my-2">{card.content}</div>
                        }
                    </Card>
                )
            ))}
        </div>
    );
};

CardContainer.propTypes = {
    links: Boolean,
    text: Boolean,
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
