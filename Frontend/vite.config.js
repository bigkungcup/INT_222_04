import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  devserver: {
    proxy: {
      '/api' : {
        target: 'http://backendclinic',
        changeOrigin: true,
        secure: false,
        transpileDependencies: true
        // rewrite: (path) => path.replace(/^\/api/,'') 
      },
    },
    port: 80,
  }
})

