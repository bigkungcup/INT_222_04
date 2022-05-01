<script setup>
import Navbar from "./components/Navbar.vue";
</script>

<template> 
  <div class="grid appBg h-screen itim ">
    <Navbar bgColor="" />
    <router-view class="pt-16"></router-view>
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

.pmc {
  background-color: #173f5f;
}

.dic {
  background-color: #1F639C;
}

.dc {
  background-color: #3CAEA3;
}

.cc {
  background-color: #ED563B;
}

.sc {
  background-color: #F6D65D;
}

.no-scrollbar::-webkit-scrollbar {
  display: none;
}

.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}


</style>
