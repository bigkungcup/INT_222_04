<script setup>
import {  ref,onBeforeMount  } from "vue";
import CreateEvent from "../components/CreateEvent.vue";
import NoAuthentication from "../components/NoAuthentication.vue"
import { useEvent, useEventCategory } from "../stores/event.js";
import { useLogin } from "../stores/login.js";
import Logout from "../components/Logout.vue";

const event = useEvent();
const category = useEventCategory();
const login = useLogin()
const asGuest = ref();
asGuest.value = localStorage.getItem('role') == null ? true : false;

onBeforeMount(async () => {
  event.textPopUp = false;
  await category.getEventCategory();
  // await event.getEventLists();
  if(localStorage.getItem('role') !== null){
  await event.getAllEventLists();
  }
});

</script>

<template>
  <div
    class="bg-fixed bg-booking bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen "
  >
  <div class="grid">
    <CreateEvent
      @create="event.createEvent"
      @createWithGuest="event.createEventWithGuest"
      @close="event.disShowPopUp"
      :textPopUp="event.textPopUp"
      :popUp="event.popUp"
      :currentCategory="category.categoryLists"
    />
  </div>
    <!-- <div class="grid" v-show="login.noAuthentication">
      <NoAuthentication/>
    </div> -->
    <div class="grid" v-show="login.logoutPopup">
      <Logout/>
    </div>

    <div class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0" v-show="asGuest">
    <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
      <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img"
          class="iconify iconify--ep animate-bounce pt-2" width="100" height="100" preserveAspectRatio="xMidYMid meet"
          viewBox="0 0 1024 1024">
          <path fill="#ffff"
            d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
          </path>
        </svg>
      </div>
      <div class="grid text-4xl place-items-center px-4 text-center">
        <p>Do you want to create event as guest?</p>
      </div>
      <div class="flex justify-center mb-6">
          <button
            class="text-4xl px-5 text-green-500 hover:text-green-700"
            @click="asGuest = false"
          >
            Yes
          </button>
          <button class="text-4xl px-5 text-red-500 hover:text-red-700" @click="$router.push('/')">
            No
          </button>
        </div>
    </div>
  </div>
  </div>
</template>

<style></style>
