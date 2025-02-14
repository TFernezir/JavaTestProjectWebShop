/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#ff6f61",   // Warm coral
        secondary: '#ffe066',  // Soft yellow
        dark: '#3b3a3f',       // Deep charcoal
        secondDark: '#6a515e', // Rich plum
        accent: '#98d1c2',     // Cool mint
        highlight: '#fff4e6',  // Light peach
        contrast: '#282828',   // Dark onyx     
      },
    },
  },
  plugins: [],
}
