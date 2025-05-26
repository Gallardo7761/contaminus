import '@/css/App.css'
import 'leaflet/dist/leaflet.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import Dashboard from '@/pages/Dashboard.jsx'
import Groups from '@/pages/Groups.jsx'
import Header from '@/components/layout/Header.jsx'
import GroupView from '@/pages/GroupView.jsx'

import { Routes, Route } from 'react-router-dom'
import ContentWrapper from '@/components/layout/ContentWrapper'
import Docs from '@/pages/Docs'
import FloatingMenu from '@/components/layout/FloatingMenu'

const App = () => {

  return (
    <>
      <FloatingMenu />
      <Header subtitle='Midiendo la calidad del aire y las calles en Sevilla 🌿🚛' />
      <ContentWrapper>
        <Routes>
          <Route path="/" element={<Groups />} />
          <Route path="/groups/:groupId" element={<GroupView />} />
          <Route path="/groups/:groupId/devices/:deviceId" element={<Dashboard />} />
          <Route path="/docs" element={<Docs url={"/apidoc.json"} />} />
        </Routes>
      </ContentWrapper>
    </>
  );
}

export default App;
