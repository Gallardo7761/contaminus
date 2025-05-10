import PropTypes from "prop-types";
import { useState, useEffect, useRef } from "react";
import "@/css/Card.css";
import { useTheme } from "@/hooks/useTheme";

const Card = ({ title, status, children, styleMode, className, titleIcon, style }) => {
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
            <div className={`card p-3 w-100 ${theme}`} style={styleMode === "override" ? style : {}}>
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
    style: PropTypes.object,
};

Card.defaultProps = {
    styleMode: "",
};

export default Card;
