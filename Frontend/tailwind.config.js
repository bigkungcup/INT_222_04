module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'home': "url('./assets/images/BgHome.png')",
        'schedule': "url('./assets/images/BgListsAll.png')",
        'detail':"url('./assets/images/BgDetail.png')",
        'booking':"url('./assets/images/BgCreate.png')"
      },
      width:{
        '100':'30rem',
        '99':'28em'
      }
    },
  },
  plugins: [],
}
