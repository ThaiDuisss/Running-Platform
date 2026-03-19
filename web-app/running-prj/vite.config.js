import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { fileURLToPath } from 'url'
import path from 'path'

const __dirname = fileURLToPath(new URL('.', import.meta.url))

/**
 * Sửa lỗi "Invalid hook call" / "Cannot read properties of null (reading 'useState')"
 * do react-toastify (hoặc thư viện khác) dùng bản React khác với app → hai bản React.
 *
 * Cách sửa:
 * 1. resolve.alias: ép mọi import 'react' và 'react-dom' trỏ về đúng node_modules của project,
 *    để toàn bộ (app + react-toastify, ...) dùng chung một bản React.
 * 2. resolve.dedupe: bảo Vite gom (dedupe) các bản react/react-dom thành một instance.
 *
 * Sau khi sửa: tắt dev server, xóa cache (rm -rf node_modules/.vite), chạy lại npm run dev.
 */
// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
      'react': path.resolve(__dirname, 'node_modules/react'),
      'react-dom': path.resolve(__dirname, 'node_modules/react-dom'),
    },
    dedupe: ['react', 'react-dom'],
  },
})