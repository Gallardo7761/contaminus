import PropTypes from 'prop-types';

const ContentWrapper = ({ children, className }) => {
    return (
        <div className={`container-xl ${className}`}>
            {children}
        </div>
    );
}

ContentWrapper.propTypes = {
    children: PropTypes.node.isRequired,
    className: PropTypes.string,
}

export default ContentWrapper;