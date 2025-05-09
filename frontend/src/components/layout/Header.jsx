import PropTypes from 'prop-types';
import '@/css/Header.css';
import { useTheme } from "@/hooks/useTheme";

const Header = ({ subtitle }) => {
    const { theme } = useTheme();

    return (
        <header className={`justify-content-center text-center mb-4 ${theme}`}>
            <img src='/images/logo.png' width={500} />
            <p className='subtitle'>{subtitle}</p>
        </header>
    );
}

Header.propTypes = {
    subtitle: PropTypes.string
}

export default Header;