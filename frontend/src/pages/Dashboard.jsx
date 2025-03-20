import PollutionMapByDevice from '../components/PollutionMapByDevice.jsx'
import HistoryCharts from '../components/HistoryCharts.jsx'
import SummaryCards from '../components/SummaryCards.jsx'

import { useParams } from 'react-router-dom';

/**
 * Dashboard.jsx
 * 
 * Este archivo define el componente Dashboard, que es el panel de control de un device.
 * 
 * Importaciones:
 * - PollutionMap: Un componente que muestra un mapa de la contaminación.
 * - HistoryCharts: Un componente que muestra gráficos históricos de la contaminación.
 * - SummaryCards: Un componente que muestra tarjetas resumen con información relevante.
 * 
 * Funcionalidad:
 * - El componente Home utiliza una estructura de JSX para organizar y renderizar los componentes importados.
 * - El componente Dashboard contiene los componentes SummaryCards, PollutionMap y HistoryCharts.
 * 
 */

const Dashboard = () => {
    const { deviceId } = useParams();

    return (
        <main className='container justify-content-center'>
            <SummaryCards deviceId={deviceId} />
            <PollutionMapByDevice deviceId={deviceId}/>
            <HistoryCharts deviceId={deviceId} />
        </main>
    );
}

export default Dashboard;
