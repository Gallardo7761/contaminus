import '../css/App.css'
import 'leaflet/dist/leaflet.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import Home from '../pages/Home.jsx'
import MenuButton from './MenuButton.jsx'
import SideMenu from './SideMenu.jsx'
import ThemeButton from '../components/ThemeButton.jsx'
import Header from '../components/Header.jsx'

import { useState } from 'react'

/**
 * App.jsx
 * 
 * Este archivo define el componente App, que es el componente principal de la aplicación.
 * 
 * Importaciones:
 * - '../css/App.css': Archivo CSS que contiene los estilos globales de la aplicación.
 * - 'leaflet/dist/leaflet.css': Archivo CSS que contiene los estilos para los mapas de Leaflet.
 * - 'bootstrap/dist/css/bootstrap.min.css': Archivo CSS que contiene los estilos de Bootstrap.
 * - 'bootstrap/dist/js/bootstrap.bundle.min.js': Archivo JS que contiene los scripts de Bootstrap.
 * - Header: Componente que representa el encabezado de la página.
 * - Home: Componente que representa la página principal de la aplicación.
 * - MenuButton: Componente que representa el botón del menú lateral.
 * - SideMenu: Componente que representa el menú lateral.
 * - ThemeButton: Componente que representa el botón de cambio de tema.
 * 
 * Funcionalidad:
 * - App: Componente principal que renderiza la página Home.
 *   - Planea añadir un React Router en el futuro.
 * - El componente Header muestra el título y subtítulo de la página.
 * - El componente MenuButton muestra un botón para abrir el menú lateral.
 * - El componente SideMenu muestra un menú lateral con opciones de navegación.
 * - El componente ThemeButton muestra un botón para cambiar el tema de la aplicación.
 * - El componente Home contiene el contenido principal de la aplicación.
 * 
 */

const App = () => {
  const [isSideMenuOpen, setIsSideMenuOpen] = useState(false);

  const toggleSideMenu = () => {
    setIsSideMenuOpen(!isSideMenuOpen);
  };

  return (
    <>
      {/* Planeo añadir un React Router */}
      <Header title='Contamin' subtitle='Midiendo la calidad del aire y las calles en Sevilla 🌿🚛' />
      <MenuButton onClick={toggleSideMenu} />
      <SideMenu isOpen={isSideMenuOpen} onClose={toggleSideMenu} />
      <ThemeButton />
      <div className={isSideMenuOpen ? 'blur m-0 p-0' : 'm-0 p-0'}>
        <Home />
      </div>
    </>
  );
}

export default App;
