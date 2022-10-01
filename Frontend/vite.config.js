import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  base: '/kw4',
  server: {
    proxy: {
        "/api": {
            // target: "https://intproj21.sit.kmutt.ac.th/us1",
            target: `http://localhost:8080`,
            changeOrigin: true,
            secure: false,
        },
    },
},
})
