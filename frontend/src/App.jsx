import '@/css/App.css'
import 'leaflet/dist/leaflet.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import Home from '@/pages/Home.jsx'
import Dashboard from '@/pages/Dashboard.jsx'
import MenuButton from '@/components/layout/MenuButton.jsx'
import SideMenu from '@/components/layout/SideMenu.jsx'
import ThemeButton from '@/components/layout/ThemeButton.jsx'
import Header from '@/components/layout/Header.jsx'
import GroupView from '@/pages/GroupView.jsx'

import { Routes, Route } from 'react-router-dom'
import { useState } from 'react'

const App = () => {
  const [isSideMenuOpen, setIsSideMenuOpen] = useState(false);

  const toggleSideMenu = () => {
    setIsSideMenuOpen(!isSideMenuOpen);
  };

  const closeSideMenu = () => {
    setIsSideMenuOpen(false);
  }

  return (
    <>
      <MenuButton onClick={toggleSideMenu} />
      <SideMenu isOpen={isSideMenuOpen} onClose={toggleSideMenu} />
      <ThemeButton />
      <div className={isSideMenuOpen ? 'blur m-0 p-0' : 'm-0 p-0'} onClick={closeSideMenu}>
        <Header subtitle='Midiendo la calidad del aire y las calles en Sevilla 🌿🚛' />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/groups/:groupId" element={<GroupView />} />
          <Route path="/groups/:groupId/devices/:deviceId" element={<Dashboard />} />
        </Routes>
      </div>
    </>
  );
}

export default App;
