import PropTypes from 'prop-types';
import '../css/Header.css';
import { useTheme } from "../contexts/ThemeContext";


const Header = (props) => {
    const { theme } = useTheme();

    return (
        <header className={`justify-content-center text-center mb-4 ${theme}`}>
            <h1>{props.title}</h1>
            <p className='subtitle'>{props.subtitle}</p>
        </header>
    );
}

Header.propTypes = {
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string
}

export default Header;