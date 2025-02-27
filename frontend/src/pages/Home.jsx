import Dashboard from '../components/Dashboard.jsx'
import PollutionMap from '../components/PollutionMap.jsx'
import HistoryCharts from '../components/HistoryCharts.jsx'
import SummaryCards from '../components/SummaryCards.jsx'

/**
 * Home.jsx
 * 
 * Este archivo define el componente Home, que es una página principal de la aplicación.
 * 
 * Importaciones:
 * - Dashboard: Un componente que actúa como contenedor para los componentes principales de la página.
 * - PollutionMap: Un componente que muestra un mapa de la contaminación.
 * - HistoryCharts: Un componente que muestra gráficos históricos de la contaminación.
 * - SummaryCards: Un componente que muestra tarjetas resumen con información relevante.
 * 
 * Funcionalidad:
 * - El componente Home utiliza una estructura de JSX para organizar y renderizar los componentes importados.
 * - El componente Dashboard contiene los componentes SummaryCards, PollutionMap y HistoryCharts.
 * 
 */

const Home = () => {
    return (
        <>
            <Dashboard>
                <SummaryCards />
                <PollutionMap />
                <HistoryCharts />
            </Dashboard>
        </>
    )
}

export default Home;
