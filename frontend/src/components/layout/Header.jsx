import '@/css/Header.css';
import { useTheme } from "@/hooks/useTheme";
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';

const Header = ({ subtitle }) => {
    const { theme } = useTheme();

    return (
        <header className={`row justify-content-center text-center mb-4 ${theme}`}>
            <div className='col-xl-4 col-lg-6 col-8'>
                <Link to="/" className="text-decoration-none">
                    <img src={`/images/logo-${theme}.png`} className='img-fluid' />
                </Link>
            </div>
            <p className='col-12 text-center my-3'>{subtitle}</p>
            {/*<nav className='d-flex justify-content-center gap-4 my-3'>
                <Link to="/" className="nav-link">Inicio</Link>
                <Link to="/groups" className="nav-link">Grupos</Link>
            </nav> */}
        </header>
    );
}

Header.propTypes = {
    subtitle: PropTypes.string,
};

export default Header;