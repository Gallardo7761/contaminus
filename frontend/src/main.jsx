import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './css/index.css'
import App from './components/App.jsx'

import { ThemeProvider } from './contexts/ThemeContext.jsx'
import { ConfigProvider } from './contexts/ConfigContext.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ThemeProvider>
        <ConfigProvider>
          <App />
        </ConfigProvider>
      </ThemeProvider>
  </StrictMode>,
)
