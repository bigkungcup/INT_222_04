<script setup>
import { ref } from "vue";
import { useLogin } from "../stores/login.js";

const login = useLogin();
const bookingIcon = ref();
bookingIcon.value = login.getRoleToken() == 'lecturer' ? false : true; 
</script>

<template>
  <div class="fixed w-20 h-screen drop-shadow-md bg-black/25">
    <div class="p-3">
      <router-link :to="{ name: 'Home' }"
        ><img src="../assets/images/Home.png" width="50" class="pt-8 pt-4 transition duration-150 ease-in-out hover:scale-125"
      /></router-link>
      <router-link :to="{ name: 'Event' }"
        ><img src="../assets/images/Lists.png" width="50" class="pt-8 transition duration-150 ease-in-out hover:scale-125"
      /></router-link>
      <router-link :to="{ name: 'Booking' }" v-show="bookingIcon"
        ><svg width="40" viewBox="0 0 26 26" class="ml-2 pt-8 transition duration-150 ease-in-out hover:scale-125">
          <path
            fill="#ffffff"
            d="M22.438-.063c-.375 0-.732.17-1.032.47l-.718.687l4.218 4.218l.688-.718c.6-.6.6-1.494 0-2.094L23.5.406c-.3-.3-.688-.469-1.063-.469zM20 1.688l-1.094.907l4.5 4.5l1-1L20 1.687zm-1.688 1.625l-9.03 8.938a.975.975 0 0 0-.126.125l-.062.031a.975.975 0 0 0-.219.438l-1.219 4.281a.975.975 0 0 0 1.219 1.219l4.281-1.219a.975.975 0 0 0 .656-.531l8.876-8.782L21 6v.094l-1.188-1.188h.094l-1.593-1.593zM.813 4A1 1 0 0 0 0 5v20a1 1 0 0 0 1 1h20a1 1 0 0 0 1-1V14a1 1 0 1 0-2 0v10H2V6h10a1 1 0 1 0 0-2H1a1 1 0 0 0-.094 0a1 1 0 0 0-.094 0zm9.813 9.813l1.375.093l.094 1.5l-1.375.406l-.531-.53l.437-1.47z"
          ></path>
        </svg>
      </router-link>
      <router-link :to="{ name: 'User' }" v-show="login.userPage">
      <img src="../assets/images/aboutUs.png" width="50" class="pt-8 transition duration-150 ease-in-out hover:scale-125">
      </router-link>
      <router-link :to="{ name: 'AboutUs' }">
      <svg width="50" class="pt-8 transition duration-150 ease-in-out hover:scale-125" viewBox="0 0 32 32"><path fill="#ffffff" d="M17 22v-8h-4v2h2v6h-3v2h8v-2h-3zM16 8a1.5 1.5 0 1 0 1.5 1.5A1.5 1.5 0 0 0 16 8z"/><path fill="#ffffff" d="M16 30a14 14 0 1 1 14-14a14 14 0 0 1-14 14Zm0-26a12 12 0 1 0 12 12A12 12 0 0 0 16 4Z"/></svg>
      </router-link>
      <span>
      <router-link :to="{ name: login.logoutIcon?'Profile':'Login'}">
        <svg width="50" v-show="!login.logoutIcon" class="absolute bottom-36 transition duration-150 ease-in-out hover:scale-125" viewBox="0 0 15 15"><path fill="#ffffff" d="M5 5.5a2.5 2.5 0 1 1 5 0a2.5 2.5 0 0 1-5 0Z"/><path fill="#ffffff" fill-rule="evenodd" d="M7.5 0a7.5 7.5 0 1 0 0 15a7.5 7.5 0 0 0 0-15ZM1 7.5a6.5 6.5 0 1 1 10.988 4.702A3.5 3.5 0 0 0 8.5 9h-2a3.5 3.5 0 0 0-3.488 3.202A6.482 6.482 0 0 1 1 7.5Z" clip-rule="evenodd"/></svg>
        <svg width="50" v-show="login.logoutIcon" class="absolute bottom-36 transition duration-150 ease-in-out hover:scale-125" viewBox="0 0 24 24"><path fill="#ffffff" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10s10-4.48 10-10S17.52 2 12 2zm0 4c1.93 0 3.5 1.57 3.5 3.5S13.93 13 12 13s-3.5-1.57-3.5-3.5S10.07 6 12 6zm0 14c-2.03 0-4.43-.82-6.14-2.88a9.947 9.947 0 0 1 12.28 0C16.43 19.18 14.03 20 12 20z"/></svg>
      </router-link>
      <button v-show="login.logoutIcon" @click="login.logoutPopup = true">
      <svg width="50" class="absolute bottom-16 transition duration-150 ease-in-out hover:scale-125" viewBox="0 0 1024 1024"><path fill="#e11d48" d="M868 732h-70.3c-4.8 0-9.3 2.1-12.3 5.8c-7 8.5-14.5 16.7-22.4 24.5a353.84 353.84 0 0 1-112.7 75.9A352.8 352.8 0 0 1 512.4 866c-47.9 0-94.3-9.4-137.9-27.8a353.84 353.84 0 0 1-112.7-75.9a353.28 353.28 0 0 1-76-112.5C167.3 606.2 158 559.9 158 512s9.4-94.2 27.8-137.8c17.8-42.1 43.4-80 76-112.5s70.5-58.1 112.7-75.9c43.6-18.4 90-27.8 137.9-27.8c47.9 0 94.3 9.3 137.9 27.8c42.2 17.8 80.1 43.4 112.7 75.9c7.9 7.9 15.3 16.1 22.4 24.5c3 3.7 7.6 5.8 12.3 5.8H868c6.3 0 10.2-7 6.7-12.3C798 160.5 663.8 81.6 511.3 82C271.7 82.6 79.6 277.1 82 516.4C84.4 751.9 276.2 942 512.4 942c152.1 0 285.7-78.8 362.3-197.7c3.4-5.3-.4-12.3-6.7-12.3zm88.9-226.3L815 393.7c-5.3-4.2-13-.4-13 6.3v76H488c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h314v76c0 6.7 7.8 10.5 13 6.3l141.9-112a8 8 0 0 0 0-12.6z"/></svg>
    </button>
  </span>
    </div>
  </div>
</template>

<style></style>
