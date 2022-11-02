<script setup>
import Navbar from "./components/Navbar.vue";
import { useLogin } from "../src/stores/Login.js";
import Logout from "./components/Logout.vue";

const login = useLogin();

const togglePopup = () => {
    login.logoutPopup = !login.logoutPopup
}

if(login.getIdToken != null){
  login.logoutIcon = true;
} 
login.logoutIcon = false;

</script>

<template>
  <div class="inter">
    <Navbar :logoutIcon="login.logoutIcon" @toggle="togglePopup()" @logout="login.logoutPopup"/>
    <router-view></router-view>
    <div v-show="login.logoutPopup">
        <Logout @toggle="togglePopup()" @logout="login.logout()"/>
    </div>
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&display=swap%27');
.inter {
  font-family: 'Inter', sans-serif;
}

.no-scrollbar::-webkit-scrollbar {
  display: none;
}

.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
