<script setup>
import Navbar from "./components/Navbar.vue";
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
  background-color: #7F8EFA;
}

.devopInfra {
  background-color: #AF7AFA;
}

.database {
  background-color: #7FE0F7;
}

.clientSide {
  background-color: #7FC987;
}

.serverSide {
  background-color: #FC7A7B;
}

.no-scrollbar::-webkit-scrollbar {
  display: none;
}

.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}


</style>
