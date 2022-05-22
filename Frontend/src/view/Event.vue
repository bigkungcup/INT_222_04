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
    event.filter == 0 ? event.getEventLists() : event.getFilterEvent();
  };

onBeforeMount(async () => {
  await event.getEventLists();
  await event.getFilterEvent();
  await category.getEventCategory();
  event.page = event.eventLists.pageNumber;
});

onBeforeUpdate(async () => {
  await event.getEmptyEvent();
  await event.getEmptyFilterEvent(event.filterEventLists.content);
});

</script>

<template>
  <div
    class="grid bg-fixed bg-schedule bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen overflow-auto no-scrollbar">
    <div class="absolute place-self-center top-10">      
      <select v-model="event.filter" class="col-span-2 px-3 rounded-lg text-3xl -mt-8" @change="event.getFilterEvent(),event.page=0">
          <option default value="0">Lists All</option>
          <option value="1">Past Events</option>
          <option value="2">Up-coming Events</option>
          <option v-for="list in category.categoryLists" :value="list.id + 2">
            {{ list.eventCategoryName }}
          </option>
        </select>
        </div>
    <div class="grid mt-8" v-show="event.filter == 0 ? !event.showEmptyEvent : !event.showEmptyFilterEvent">
      <EventList :currentEvent="event.filter == 0 ? event.eventLists.content : event.filterEventLists.content" @delete="removeEvent" @next="event.NextPage"
        @back="event.BackPage" :page="event.page"/>
    </div>
    <div class="grid" v-show="event.filter == 0 ? event.showEmptyEvent : event.showEmptyFilterEvent">
      <EventEmptyList />
    </div>
  </div>
</template>

<style>
</style>
