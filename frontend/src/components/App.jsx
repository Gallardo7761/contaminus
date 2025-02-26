import '../css/App.css'
import 'leaflet/dist/leaflet.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import { ThemeProvider } from '../contexts/ThemeContext.jsx'

import Home from '../pages/Home.jsx'

const App = () => {
  return (
    <>
      <ThemeProvider>
          <Home />
      </ThemeProvider>
    </>
  );
}

export default App;
