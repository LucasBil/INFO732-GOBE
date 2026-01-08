/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#6d28d9', // purple-700
        secondary: '#4c1d95', // purple-900
        accent: '#8b5cf6', // purple-500
      },
      backdropBlur: {
        xs: '2px',
      }
    },
  },
  plugins: [],
}
