import '@/css/App.css'
import 'leaflet/dist/leaflet.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import Home from '@/pages/Home.jsx'
import Dashboard from '@/pages/Dashboard.jsx'
import MenuButton from './MenuButton.jsx'
import SideMenu from './SideMenu.jsx'
import ThemeButton from '@/components/ThemeButton.jsx'
import Header from '@/components/Header.jsx'

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
        <Header title='Contamin' subtitle='Midiendo la calidad del aire y las calles en Sevilla 🌿🚛' />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/dashboard/:deviceId" element={<Dashboard />} />
        </Routes>
      </div>
    </>
  );
}

export default App;
