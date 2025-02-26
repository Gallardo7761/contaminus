import PropTypes from 'prop-types';

const Dashboard = (props) => {
    return (
        <main className='container justify-content-center'> 
            {props.children}
        </main>
    );
}

Dashboard.propTypes = {
    children: PropTypes.node
}

export default Dashboard;