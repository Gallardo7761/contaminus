import PropTypes from 'prop-types';

const CustomContainer = ({ children }) => {
    return (
        <main className="px-4 py-5">
            {children}
        </main>
    );
}

CustomContainer.propTypes = {
    children: PropTypes.node.isRequired,
}

export default CustomContainer;