<script setup>
import {  onBeforeMount  } from "vue";
import CreateEvent from "../components/CreateEvent.vue";
import NoAuthentication from "../components/NoAuthentication.vue"
import { useEvent, useEventCategory } from "../stores/event.js";

const event = useEvent();
const category = useEventCategory();

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
    <CreateEvent
      @create="event.createEvent"
      @close="event.disShowPopUp"
      :textPopUp="event.textPopUp"
      :popUp="event.popUp"
      :currentCategory="category.categoryLists"
    />
    <div class="grid" v-show="!event.noAuthentication">
      <NoAuthentication/>
    </div>
  </div>
</template>

<style></style>
