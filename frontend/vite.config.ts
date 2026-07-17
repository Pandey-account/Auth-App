import tailwindcss from '@tailwindcss/vite' // Make sure this is imported
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from "path"

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(), // This must be here for @import "tailwindcss" to work
  ],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
})
