<script setup>
import { ref, onBeforeMount } from "vue";
import { useClinics } from "../stores/Clinics.js";
import { useEvents } from "../stores/Events.js";
import { useUsers } from "../stores/Users.js";
import { useLogin } from "../stores/Login.js";
import EventList from "../components/EventList.vue";
import EventEmptyListVue from "../components/EventEmptyList.vue";

const clinic = useClinics();
const event = useEvents();
const user = useUsers();
const login = useLogin();
const eventId = ref();
const clinicFilter = ref()
const selectClinic = ref(0)
const selectTime = ref('all')

const checkRole = async () => {
  await event.getFilterEventList();
    if(localStorage.getItem('role') == 'lecturer'){
      await user.getUserDetail(localStorage.getItem('id'));
      clinicFilter.value = user.displayUser.eventCategories;
    }else clinicFilter.value = clinic.clinicList;
  }

onBeforeMount(async () => {
  event.eventList = {};
  await clinic.getClinics();
  await checkRole();
});
</script>

<template>
  <div
    class="grid bg-Bg bg-cover h-screen grid-rows-8 pt-24 px-32 overflow-auto no-scrollbar"
  >
    <div class="grid font-bold text-white text-2xl">
      <div class="space-x-4">
        <span>Clinic:</span>
        <select
          class="rounded-lg h-12 w-80 font-bold text-white text-xl bg-black/30 border-4 border-Web-pink padding-select"
         v-model="selectClinic" @change="event.getFilterEventList(selectClinic,selectTime)">
          <option default value="0">All</option>
          <option v-for="list in clinicFilter" :value="list.id">
            {{ list.eventCategoryName }}
          </option>
        </select>
        <span>Time:</span>
        <select
          class="rounded-lg h-12 w-60 font-bold text-white text-xl bg-black/30 border-4 border-Web-pink padding-select"
        v-model="selectTime" @change="event.getFilterEventList(selectClinic,selectTime)">
          <option default value="all">All</option>
          <option value="past">Past Events</option>
          <option value="upComing">Up-coming Events</option>
        </select>
      </div>
    </div>
    <div class="grid row-span-7" v-show="event.eventList.numberOfElements != 0">
      <EventList
        :eventList="event.eventList"
        :deletePopUp="event.deletePopup"
        :eventId="eventId"
        @delete="event.removeEvent"
      />
      <div class="grid grid-cols-3 py-6 place-items-center" v-show="event.eventList.numberOfElements != 0 && event.eventList.numberOfElements != null">
        <div>
          <button
            class="text-xl text-white rounded-3xl w-28 h-12 mx-2 bg-Web-pink hover:bg-white hover:text-Web-pink"
            v-show="event.eventList.pageNumber > 0"
            @click="event.BackPage()"
          >
            Back
          </button>
        </div>
        <span
          class="rounded-lg h-12 w-16 font-bold text-white text-xl text-center bg-black/30 border-4 border-Web-pink py-1"
          >{{ event.eventList.pageNumber + 1 }}</span
        >
        <button
          class="text-xl text-white rounded-3xl w-28 h-12 mx-2 bg-Web-pink hover:bg-white hover:text-Web-pink"
          v-show="event.eventList.pageNumber + 1 < event.eventList.totalPages"
          @click="event.NextPage()"
        >
          Next
        </button>
      </div>
    </div>
    <div class="grid row-span-8" v-show="event.eventList.numberOfElements == 0">
        <EventEmptyListVue />
    </div>
  </div>
</template>

<style></style>
