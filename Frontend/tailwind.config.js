module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'Bg': "url('./assets/images/Background.png')",
        'Bg-Plain': "url('./assets/images/Background_Plain.png')",
        'Bg-Login': "url('./assets/images/Background_Login.png')",
        'Menu':"url('./assets/images/Menubar.png')"
      },
      colors:{
        'Web-pink':'#EB4C84',
        'Web-violet':'#462A6C',
        'bg-projectManagement':'#00A1FC',
        'bg-devopInfra':'#B960FF',
        'bg-database':'#FF1515',
        'bg-clientSide':'#00DA30',
        'bg-serverSide':'#FF6610'
      },
      height: {
        '100': '32rem',
      },
      margin: {
        '88' : '22rem',
        '104': '28rem'
      },
      textUnderlineOffset: {
        10: '18px',
      },
      gridTemplateRows: {
        '7': 'repeat(7, minmax(0, 1fr))',
        '8': 'repeat(8, minmax(0, 1fr))',
        'layout': '200px minmax(900px, 1fr) 100px',
      },
      gridRow: {
        'span-7': 'span 7 / span 7',
      }
    },      
  },
  plugins: [],
}
