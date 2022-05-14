import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  base: '/kw4',
  server: {
    proxy: {
      '/api' : {
        target: 'http://intproj21.sit.kmutt.ac.th',
        // target: 'http://10.4.84.58:8080',
        changeOrigin: true,
        secure: false,
        // transpileDependencies: true
        // rewrite: (path) => path.replace(/^\/api/,'') 
      }
    }
  }
})

