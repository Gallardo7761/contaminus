import '../css/App.css'
import 'leaflet/dist/leaflet.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import Home from '../pages/Home.jsx'

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
 * - Home: Componente que representa la página principal de la aplicación.
 * 
 * Funcionalidad:
 * - App: Componente principal que renderiza la página Home.
 *   - Planea añadir un React Router en el futuro.
 * 
 */

const App = () => {
  return (
    <>
      {/* Planeo añadir un React Router */}
      <Home />
    </>
  );
}

export default App;
