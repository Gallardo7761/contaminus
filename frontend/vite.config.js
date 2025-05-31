import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import cleanPlugin from 'vite-plugin-clean'

// https://vite.dev/config/
export default defineConfig({
  server: {
    host: "0.0.0.0",
    port: 3000,
  },
  resolve: {
    alias: {
      '@/': '/src/',
    },
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
