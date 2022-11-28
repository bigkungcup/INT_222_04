<script setup>
import { onBeforeMount } from "vue";
import Navbar from "./components/Navbar.vue";
import { useLogin } from "./stores/Login.js";
import Logout from "./components/Logout.vue";
import NoAuthentication from "./components/NoAuthentication.vue";
import NoAuthorization from "./components/NoAuthorization.vue";

const login = useLogin();

//เดี๋ยวก็ลบ
const toggleNoAuthorizationPopup = () => {
  login.noAuthorization = !login.noAuthorization
}


</script>

<template>
  <div class="inter">
    <Navbar :userName="login.userName" :logoutIcon="login.logoutIcon" :userPageIcon="login.userPageIcon" @toggle="togglePopup()" @logout="login.logoutPopup"/>
    <router-view></router-view>
    <div v-show="login.noAuthentication && login.getRoleToken() == null">
        <NoAuthentication @toggle="toggleNoAuthenticationPopup()"/>
    </div> 

    <div v-show="login.noAuthorization">
        <NoAuthorization @toggle="togglenoAuthorizationPopup()"/>
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
