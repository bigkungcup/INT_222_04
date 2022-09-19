<script setup>
import Navbar from "./components/Navbar.vue";
import router from "./router";
import { onMounted } from "vue";
import { useLogin } from "./stores/event.js"

const login = useLogin();

const checkToken = () => {
  if(localStorage.getItem('jwt') === null){
    router.push("../login")
  }else{
    login.logoutIcon = true;
  }
}

onMounted(async () => {
  checkToken();
});

</script>

<template>
  <div class="itim">
    <Navbar />
    <router-view></router-view>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css2?family=Itim&display=swap");
.itim {
  font-family: "Itim", cursive;
}

.appBg {
  background-color: #23162b;
  background-attachment: fixed;
  background-size: cover;
}

.projectManagement {
  background-color: #7f8efa;
}

.devopInfra {
  background-color: #af7afa;
}

.database {
  background-color: #7fe0f7;
}

.clientSide {
  background-color: #7fc987;
}

.serverSide {
  background-color: #fc7a7b;
}

.no-scrollbar::-webkit-scrollbar {
  display: none;
}

.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.bgPopUp {
  background-color: #f5c6cd;
}

</style>
