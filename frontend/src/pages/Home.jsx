import Header from '../components/Header.jsx'
import Dashboard from '../components/Dashboard.jsx'
import PollutionMap from '../components/PollutionMap.jsx'
import HistoryCharts from '../components/HistoryCharts.jsx'
import ThemeButton from '../components/ThemeButton.jsx'
import SummaryCards from '../components/SummaryCards.jsx'

const Home = () => {
    return (
        <>
            <Header title='Contamin' subtitle='Midiendo la calidad del aire y las calles en Sevilla 🌿🚛' />
            <Dashboard>
                <SummaryCards />
                <PollutionMap />
                {/*  */}
                <HistoryCharts />
            </Dashboard>
            <ThemeButton />
        </>
    )
}

export default Home;
