import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia"
import "./index.css";


createApp(App).use(router).use(createPinia()).mount("#app");


export function formatDate(date) {
  const options = { year: "numeric", month: "long", day: "numeric" };
  return new Date(date).toLocaleString("en-TH", options);
}

export function formatTime(date) {
  const options = { hour: "numeric", minute: "numeric" };
  return new Date(date).toLocaleString("th-TH", options);
}

