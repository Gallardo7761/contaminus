import PropTypes from 'prop-types';

/**
 * Dashboard.jsx
 * 
 * Este archivo define el componente Dashboard, que actúa como contenedor para los componentes principales de la página.
 * 
 * Importaciones:
 * - PropTypes: Librería para la validación de tipos de propiedades en componentes de React.
 * 
 * Funcionalidad:
 * - Dashboard: Componente que renderiza un contenedor principal (`main`) con los componentes hijos pasados como `props.children`.
 * 
 * PropTypes:
 * - Dashboard espera una propiedad `children` que es un nodo de React.
 * 
 */

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