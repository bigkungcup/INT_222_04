<script setup></script>

<template>
  <div class="fixed w-20 h-screen drop-shadow-md bg-black/25">
    <div class="p-3">
      <router-link :to="{ name: 'Home' }"
        ><img src="../assets/images/Home.png" width="50" class="pb-8 pt-4 transition duration-150 ease-in-out hover:scale-125"
      /></router-link>
      <router-link :to="{ name: 'Event' }"
        ><img src="../assets/images/Lists.png" width="50" class="pb-8 transition duration-150 ease-in-out hover:scale-125"
      /></router-link>
      <router-link :to="{ name: 'Booking' }"
        ><svg width="2.5em" height="2.5em" viewBox="0 0 26 26" class="ml-2 transition duration-150 ease-in-out hover:scale-125">
          <path
            fill="#ffffff"
            d="M22.438-.063c-.375 0-.732.17-1.032.47l-.718.687l4.218 4.218l.688-.718c.6-.6.6-1.494 0-2.094L23.5.406c-.3-.3-.688-.469-1.063-.469zM20 1.688l-1.094.907l4.5 4.5l1-1L20 1.687zm-1.688 1.625l-9.03 8.938a.975.975 0 0 0-.126.125l-.062.031a.975.975 0 0 0-.219.438l-1.219 4.281a.975.975 0 0 0 1.219 1.219l4.281-1.219a.975.975 0 0 0 .656-.531l8.876-8.782L21 6v.094l-1.188-1.188h.094l-1.593-1.593zM.813 4A1 1 0 0 0 0 5v20a1 1 0 0 0 1 1h20a1 1 0 0 0 1-1V14a1 1 0 1 0-2 0v10H2V6h10a1 1 0 1 0 0-2H1a1 1 0 0 0-.094 0a1 1 0 0 0-.094 0zm9.813 9.813l1.375.093l.094 1.5l-1.375.406l-.531-.53l.437-1.47z"
          ></path>
        </svg>
      </router-link>
      <router-link :to="{ name: 'User' }">
      <img src="../assets/images/aboutUs.png" width="50" class="pt-8 transition duration-150 ease-in-out hover:scale-125">
      </router-link>
      <router-link :to="{ name: 'AboutUs' }">
      <svg width="50" class="pt-8 transition duration-150 ease-in-out hover:scale-125" viewBox="0 0 32 32"><path fill="#ffffff" d="M17 22v-8h-4v2h2v6h-3v2h8v-2h-3zM16 8a1.5 1.5 0 1 0 1.5 1.5A1.5 1.5 0 0 0 16 8z"/><path fill="#ffffff" d="M16 30a14 14 0 1 1 14-14a14 14 0 0 1-14 14Zm0-26a12 12 0 1 0 12 12A12 12 0 0 0 16 4Z"/></svg>
      </router-link>
      <router-link :to="{ name: 'Register'}">
        <svg width="50" class="mt-64 transition duration-150 ease-in-out hover:scale-125" viewBox="0 0 15 15"><path fill="#ffffff" d="M5 5.5a2.5 2.5 0 1 1 5 0a2.5 2.5 0 0 1-5 0Z"/><path fill="#ffffff" fill-rule="evenodd" d="M7.5 0a7.5 7.5 0 1 0 0 15a7.5 7.5 0 0 0 0-15ZM1 7.5a6.5 6.5 0 1 1 10.988 4.702A3.5 3.5 0 0 0 8.5 9h-2a3.5 3.5 0 0 0-3.488 3.202A6.482 6.482 0 0 1 1 7.5Z" clip-rule="evenodd"/></svg>
      </router-link>
    </div>
  </div>
</template>

<style></style>
