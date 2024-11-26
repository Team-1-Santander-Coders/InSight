/** @type {import('tailwindcss').Config} */
import tailwindcssPrimeui from 'tailwindcss-primeui';
export default {
  darkMode: 'class',
  content: [
    './index.html',
    './src/**/*.{vue,ts,js,jsx,tsx}',
  ],
  theme: {
    extend: {
      fontFamily: {
        'sans': ['Roboto', 'Helvetica', 'Arial', 'sans-serif', 'Bebas Neue', 'Anton']
      }
    },
  },
  plugins: [tailwindcssPrimeui],
}