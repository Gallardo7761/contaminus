import Header from '../components/Header.jsx'
import Dashboard from '../components/Dashboard.jsx'
import PollutionMap from '../components/PollutionMap.jsx'
import HistoryCharts from '../components/HistoryCharts.jsx'
import ThemeButton from '../components/ThemeButton.jsx'
import SummaryCards from '../components/SummaryCards.jsx'

/**
 * Home.jsx
 * 
 * Este archivo define el componente Home, que es una página principal de la aplicación.
 * 
 * Importaciones:
 * - Header: Un componente que muestra el encabezado de la página.
 * - Dashboard: Un componente que actúa como contenedor para los componentes principales de la página.
 * - PollutionMap: Un componente que muestra un mapa de la contaminación.
 * - HistoryCharts: Un componente que muestra gráficos históricos de la contaminación.
 * - ThemeButton: Un componente que permite cambiar el tema de la aplicación.
 * - SummaryCards: Un componente que muestra tarjetas resumen con información relevante.
 * 
 * Funcionalidad:
 * - El componente Home utiliza una estructura de JSX para organizar y renderizar los componentes importados.
 * - El componente Header muestra el título y subtítulo de la página.
 * - El componente Dashboard contiene los componentes SummaryCards, PollutionMap y HistoryCharts.
 * - El componente ThemeButton se renderiza al final para permitir el cambio de tema.
 * 
 */

const Home = () => {
    return (
        <>
            <Header title='Contamin' subtitle='Midiendo la calidad del aire y las calles en Sevilla 🌿🚛' />
            <Dashboard>
                <SummaryCards />
                <PollutionMap />
                <HistoryCharts />
            </Dashboard>
            <ThemeButton />
        </>
    )
}

export default Home;
