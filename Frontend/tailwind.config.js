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
        'booking':"url('./assets/images/BgCreate.png')",
        'aboutUs':"url('./assets/images/BgAboutUs.png')",
        'user':"url('./assets/images/BgUser.png')"
      },
      width:{
        '110':'40rem',
        '100':'30rem',
        '99':'28em'
      }
    },      
  },
  plugins: [],
}
