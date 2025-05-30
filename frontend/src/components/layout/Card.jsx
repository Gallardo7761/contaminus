import PropTypes from "prop-types";
import { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import "@/css/Card.css";
import { useTheme } from "@/hooks/useTheme";

const Card = ({
    title,
    status,
    children,
    styleMode,
    className,
    titleIcon,
    style,
    link,
    to,
    text,
    marquee
}) => {
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

    const cardContent = (
        <div
            ref={cardRef}
            className={`card p-3 w-100 h-100 ${theme} ${className ?? ""}`}
            style={styleMode === "override" ? style : {}}
        >
            <h3 className="text-center">
                {titleIcon}
                {shortTitle}
            </h3>


            {marquee && (
                <div className="contenedor-con-efecto card-content rounded-4 h-100 d-flex align-items-center justify-content-center" style={{ backgroundColor: "black"}}>
                    <marquee scrollamount="30">
                        <p className="card-text text-center m-0">{children}</p>
                    </marquee>
                </div>
            )}

            {!marquee && (
                <div className="card-content h-100" >
                    {text ? (
                        <p className="card-text text-center">{children}</p>
                    ) : (
                        <>{children}</>
                    )}
                </div>
            )}

            {status && <span className="status text-center mt-2">{status}</span>}
        </div>
    );

    return link && to
        ? <Link to={to} style={{ textDecoration: "none" }}>{cardContent}</Link>
        : cardContent;
};

Card.propTypes = {
    title: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
    children: PropTypes.node.isRequired,
    styleMode: PropTypes.oneOf(["override", ""]),
    className: PropTypes.string,
    titleIcon: PropTypes.node,
    style: PropTypes.object,
    link: PropTypes.bool,
    to: PropTypes.string,
    text: PropTypes.bool,
    marquee: PropTypes.bool,
};

export default Card;
