<script setup>
import { onBeforeMount } from "vue";
import EventCategoryList from "../components/EventCategoryList.vue";
import NoAuthentication from "../components/NoAuthentication.vue"
import { useEventCategory } from "../stores/event.js";
import { useLogin } from "../stores/login.js";
import Logout from "../components/Logout.vue";

const category = useEventCategory();
const login = useLogin()

onBeforeMount(async () => {
  await category.getEventCategory();
});
</script>

<template>
  <div
    class="bg-home bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen"
  >
    <EventCategoryList :currentCategory="category.categoryLists" />
    <div class="grid" v-show="!login.noAuthentication">
      <NoAuthentication/>
  </div>
  <div class="grid" v-show="login.logoutPopup">
      <Logout/>
    </div>
  </div>
</template>

<style></style>
