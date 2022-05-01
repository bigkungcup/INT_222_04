import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api' : {
        target: 'http://backendclinic:8080',
        // target: 'http://10.4.84.58:8080',
        changeOrigin: true,
        secure: false,
        // transpileDependencies: true
        // rewrite: (path) => path.replace(/^\/api/,'') 
      }
    }
  }
})

