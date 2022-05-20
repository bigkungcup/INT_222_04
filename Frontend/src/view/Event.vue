<script setup>
import { ref, onBeforeMount, onBeforeUpdate } from "vue";
import EventList from "../components/EventList.vue";
import EventEmptyList from "../components/EventEmptyList.vue";
import { useEvent,useEventCategory } from "../stores/event.js"
import moment from "moment"

const event = useEvent()
const category = useEventCategory()

  //Delete Event
  const removeEvent = async (eventId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/${eventId}`,
      {
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      event.eventLists.content = event.eventLists.content.filter(
        (event) => event.id !== eventId
      );
      console.log("deleteted succesfully");
    } else console.log("error, cannot delete");
  };

onBeforeMount(async () => {
  await event.getEventLists();
  await category.getEventCategory();
  // await event.getUpcomingEvent();
  event.page = event.eventLists.pageNumber;
});

onBeforeUpdate(async () => {
  await event.getEmptyEvent();
});

</script>

<template>
  <div
    class="bg-fixed bg-schedule bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen overflow-auto no-scrollbar">
    <div class="grid" v-show="!event.showEmptyEvent">
      <EventList :currentEvent="event.filter == 0 ? event.eventLists.content : event.filterEventLists" @delete="removeEvent" @next="event.NextPage"
        @back="event.BackPage" :page="event.page"/>
    </div>
    <div class="grid" v-show="event.showEmptyEvent">
      <EventEmptyList />
    </div>
  </div>
</template>

<style>
</style>
