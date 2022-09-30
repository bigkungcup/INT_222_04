<script setup>
import {  onBeforeMount  } from "vue";
import CreateEvent from "../components/CreateEvent.vue";
import NoAuthentication from "../components/NoAuthentication.vue"
import { useEvent, useEventCategory } from "../stores/event.js";
import { useLogin } from "../stores/login.js";
import Logout from "../components/Logout.vue";

const event = useEvent();
const category = useEventCategory();
const login = useLogin()

onBeforeMount(async () => {
  event.textPopUp = false;
  await category.getEventCategory();
  // await event.getEventLists();
  await event.getAllEventLists();
});

</script>

<template>
  <div
    class="bg-fixed bg-booking bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen"
  >
  <div class="grid" v-if="login.noAuthentication">
    <CreateEvent
      @create="event.createEvent"
      @close="event.disShowPopUp"
      :textPopUp="event.textPopUp"
      :popUp="event.popUp"
      :currentCategory="category.categoryLists"
    />
  </div>
    <div class="grid" v-show="!login.noAuthentication">
      <NoAuthentication/>
    </div>
    <div class="grid" v-show="login.logoutPopup">
      <Logout/>
    </div>
  </div>
</template>

<style></style>
