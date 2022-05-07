import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './index.css'
// import Datepicker from '@vuepic/vue-datepicker';
// import '@vuepic/vue-datepicker/dist/main.css'

createApp(App).use(router).mount('#app')
// App.component('Datepicker', Datepicker);

export function formatDate(date){
    const options = { year: 'numeric', month: 'long', day: 'numeric'}
    return new Date(date).toLocaleString('en-TH',options)
}

export function formatTime (date){
const options = { hour:'numeric',minute:'numeric'}
return new Date(date).toLocaleString('th-TH',options)
}
