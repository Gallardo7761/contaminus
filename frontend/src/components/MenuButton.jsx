import "@/css/MenuButton.css";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from '@fortawesome/free-solid-svg-icons';
import PropTypes from "prop-types";

const MenuButton = ({ onClick }) => {
    return (
        <button className="menuBtn" onClick={onClick}>
            <FontAwesomeIcon icon={faBars} />
        </button>
    );
}

MenuButton.propTypes = {
    onClick: PropTypes.func.isRequired,
};

export default MenuButton;