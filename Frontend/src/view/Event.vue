<script setup>
import { ref,onBeforeMount, onBeforeUpdate } from "vue";
import EventList from "../components/EventList.vue";
import EventEmptyList from "../components/EventEmptyList.vue";
import { useEvent,useEventCategory,useUser } from "../stores/event.js"
import { useLogin } from "../stores/login.js";
import NoAuthentication from "../components/NoAuthentication.vue"
import Logout from "../components/Logout.vue";

const event = useEvent();
const category = useEventCategory();
const login = useLogin();
const user = useUser();
const categoryFilter = ref();

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
      login.noAuthentication = true;
      console.log("deleteted succesfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(removeEvent());
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else console.log("error, cannot delete");
    event.filter == 0 ? event.getEventLists() : event.getFilterEvent();
  };

  const checkRole = async () => {
    if(localStorage.getItem('role') == 'lecturer'){
      await event.getLecturerEventLists();
    }
      await event.getEventLists();
  }


onBeforeMount(async () => {
  await checkRole();
  await category.getEventCategory();
  await user.getUser();
  // await event.getEventLists();
  await event.getFilterEvent();
  categoryFilter.value = login.getRoleToken() == 'admin' ? category.categoryLists : user.displayUser.eventCategories;
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
    <div class="grid absolute" v-show="login.logoutPopup">
      <Logout/>
    </div>
    <div class="absolute place-self-center top-10">      
      <select v-show="!login.noAuthentication" v-model="event.filter" class="col-span-2 px-3 rounded-lg text-3xl -mt-8" @change="event.getFilterEvent(),event.page=0">
          <option default value="0">Lists All</option>
          <option value="1">Past Events</option>
          <option value="2">Up-coming Events</option>
          <option v-for="list in categoryFilter" :value="list.id + 2">
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
    <div class="grid" v-show="login.noAuthentication">
      <NoAuthentication/>
    </div>
    <div class="grid" v-show="login.logoutPopup">
      <Logout/>
    </div>
  </div>
</template>

<style>
</style>
