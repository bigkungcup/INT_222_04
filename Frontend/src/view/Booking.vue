<script setup>
import {  onBeforeMount ,onUpdated } from "vue";
import CreateEvent from "../components/CreateEvent.vue";
import { useEvent, useEventCategory } from "../stores/event.js";

const event = useEvent();
const category = useEventCategory();

onBeforeMount(async () => {
  event.textPopUp = false;
  await category.getEventCategory();
  await event.getEventLists();
  await event.getAllEventLists();
});

// onUpdated(async () => {
//   await event.getAllEventLists();
//   });
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
  </div>
</template>

<style></style>
