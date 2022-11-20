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

// export const msalConfig = {
//   auth: {
//       clientId: "90842470-8277-47a8-86fd-4fe694176219",
//       authority: "https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d",
//       redirectUri: "http://localhost:3000",
//   },
//   cache: {
//       cacheLocation: "localStorage", // This configures where your cache will be stored
//       storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
//   },
// };

// const apiConfig = {
//   uri: "http://localhost:5000/api", // e.g. http://localhost:5000/api
//   scopes: ["api://90842470-8277-47a8-86fd-4fe694176219/access_as_user"] // e.g. ["scp1", "scp2"]
// };

// const loginRequest = {
//   scopes: ["openid", "profile"]
// };

// const tokenRequest = {
//   scopes: [...apiConfig.scopes],
// };
