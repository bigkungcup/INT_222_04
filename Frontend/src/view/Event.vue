<script setup>
import { ref, onBeforeMount, onBeforeUpdate } from "vue";
import EventList from "../components/EventList.vue";
import EventEmptyList from "../components/EventEmptyList.vue";
import { useEvent } from "../stores/event.js"

const event = useEvent()

onBeforeMount(async () => {
  await event.getEventLists();
});

onBeforeUpdate(async () => {
  await event.getEmptyEvent();
});

</script>

<template>
  <div
    class="bg-fixed bg-schedule bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen overflow-auto no-scrollbar">
    <div class="grid" v-show="!event.showEmptyEvent">
      <EventList :currentEvent="event.eventLists" @delete="event.removeEvent" @next="event.NextPage"
        @back="event.BackPage" :page="event.page" />
    </div>
    <div class="grid" v-show="event.showEmptyEvent">
      <EventEmptyList />
    </div>
  </div>
</template>

<style>
</style>
