<script setup>
import { onMounted } from "vue";
import Navbar from "./components/Navbar.vue";
import { useLogin } from "./stores/Login.js";
import Logout from "./components/Logout.vue";
import NoAuthorization from "./components/NoAuthorization.vue";
import NoAuthorizationAsGuest from "./components/noAuthorizationAsGuest.vue";

const login = useLogin();

const togglenoAuthorizationPopup = () => {
  login.noAuthorization = !login.noAuthorization
}

//เดี๋ยวก็ลบ
const togglenoAuthorizationAsGuestPopup = () => {
  login.noAuthorizationAsGuest = !login.noAuthorizationAsGuest
}

// onMounted(async () => {
//   login.checkToken();
// });

</script>

<template>
  <div class="inter">
    <Navbar :userName="login.getNameToken()" :logoutIcon="login.logoutIcon" :userPageIcon="login.userPageIcon" @toggle="togglePopup()" @logout="login.logoutPopup"/>
    <router-view></router-view>
    <div v-show="login.noAuthorization && login.getRoleToken() == null">
        <NoAuthorization @toggle="togglenoAuthorizationPopup()"/>
    </div> 

    <div v-show="login.noAuthorizationAsGuest">
        <NoAuthorizationAsGuest @toggle="togglenoAuthorizationAsGuestPopup()"/>
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
