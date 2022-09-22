import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  base: '/kw4'
})

// export default defineConfig({
//   server: {
//     proxy: {
//       "/api": {
//         target: "https://localhost:8080/",
//         changeOrigin: true,
//         secure: false,
//         rewrite: (path) => path.replace(/^\/api/, ""),
//       },
//     },
//   },
//   plugins: [vue()],
//   base: '/kw4'
// })