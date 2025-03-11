import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  server: {
    port: 8080,
  },
  plugins: [react()],
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          "react-vendors": ["react", "react-dom"],
          "leaflet": ["leaflet"],
          "chartjs": ["chart.js"]
        }
      }
    },
    outDir: 'dist',
  },
  publicDir: 'public',
})
