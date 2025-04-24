import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import cleanPlugin from 'vite-plugin-clean'

// https://vite.dev/config/
export default defineConfig({
  server: {
    port: 5173,
  },
  plugins: [react(), cleanPlugin()],
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          "react-vendors": ["react", "react-dom"],
          "leaflet": ["leaflet"],
          "chartjs": ["chart.js"]
        }
      }
    }
  },
  publicDir: 'public',
})
