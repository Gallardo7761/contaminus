import { faSpinner } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const LoadingIcon = () => {
    return (
        <FontAwesomeIcon icon={faSpinner} className='fa-spin fa-lg' />
    );
}

export default LoadingIcon;