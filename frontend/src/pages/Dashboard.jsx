import PollutionMap from '@/components/PollutionMap.jsx'
import HistoryCharts from '@/components/HistoryCharts.jsx'
import SummaryCards from '@/components/SummaryCards.jsx'

import { useParams } from 'react-router-dom';

const Dashboard = () => {
    const { groupId, deviceId } = useParams();

    return (
        <main className='container justify-content-center gap-3 d-flex flex-column'>
            <SummaryCards groupId={groupId} deviceId={deviceId} />
            <PollutionMap groupId={groupId} deviceId={deviceId} />
            <HistoryCharts groupId={groupId} deviceId={deviceId} />
        </main>
    );
};

export default Dashboard;
