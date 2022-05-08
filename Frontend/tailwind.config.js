module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'home': "url('./assets/images/BgHome.png')",
        'schedule': "url('./src/assets/images/BgListsAll.png')",
        'detail':"url('./src/assets/images/BgDetail.png')",
        'booking':"url('./src/assets/images/BgCreate.png')"
      },
      width:{
        '100':'30rem'
      }
    },
  },
  plugins: [],
}
