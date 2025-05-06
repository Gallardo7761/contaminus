import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import './css/index.css'
import App from './components/App.jsx'

import { ThemeProvider } from './context/ThemeContext.jsx'
import { ConfigProvider } from './context/ConfigContext.jsx'

/**
 * main.jsx
 * 
 * Este archivo es el punto de entrada principal de la aplicación React.
 * 
 * Importaciones:
 * - StrictMode: Un componente de React que ayuda a identificar problemas potenciales en la aplicación.
 * - createRoot: Una función de ReactDOM que crea una raíz para renderizar la aplicación.
 * - './css/index.css': Archivo CSS que contiene los estilos globales de la aplicación.
 * - App: El componente principal de la aplicación.
 * - ThemeProvider: Un proveedor de contexto que maneja el tema de la aplicación.
 * - ConfigProvider: Un proveedor de contexto que maneja la configuración de la aplicación.
 * 
 * Funcionalidad:
 * - El archivo utiliza `createRoot` para obtener el elemento con el id 'root' del DOM y renderizar la aplicación React dentro de él.
 * - La aplicación se envuelve en `StrictMode` para ayudar a detectar problemas potenciales.
 * - `ThemeProvider` y `ConfigProvider` envuelven el componente `App` para proporcionar los contextos de tema y configuración a toda la aplicación.
 */

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ThemeProvider>
        <ConfigProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </ConfigProvider>
      </ThemeProvider>
  </StrictMode>,
)
