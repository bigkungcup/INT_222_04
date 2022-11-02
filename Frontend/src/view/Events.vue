<script setup>
import { ref,onBeforeMount } from "vue";
import { useClinics } from "../stores/Clinics.js";
import { useEvents } from "../stores/Events.js";
import EventList from "../components/EventList.vue";

const clinic = useClinics();
const event = useEvents();
const eventId = ref()

onBeforeMount(async () => {
    await clinic.getClinics();
    await event.getEventList();
});
</script>
 
<template>
    <div class="grid bg-Bg bg-cover h-screen grid-rows-8 pt-24 px-32 overflow-auto no-scrollbar">
    <div class="grid font-bold text-white text-2xl">
        <div class="space-x-4"><span>Clinic:</span>
            <select class="rounded-lg h-12 w-80 font-bold text-white text-xl bg-black/30 border-4 border-Web-pink padding-select">
                    <option default value="0">All</option>
                    <option v-for="list in clinic.clinicList" :value="list.id">
                        {{ list.eventCategoryName }}            
                    </option>
                </select>
           <span>Time:</span>
           <select class="rounded-lg h-12 w-60 font-bold text-white text-xl bg-black/30 border-4 border-Web-pink padding-select">
                    <option default value="all">All</option>
                    <option value="past">Past Events</option>
                    <option value="upComing">Up-coming Events</option>
            </select>
        </div>
    </div>
    <div class="grid row-span-7">
        <EventList :eventList="event.eventList" :deletePopUp="event.deletePopup" :eventId="eventId" @delete="event.removeEvent"/>
        <div class="grid grid-cols-3 py-6 place-items-center">
        <div><button class="text-xl text-white rounded-3xl w-28 h-12 mx-2 bg-Web-pink hover:bg-white hover:text-Web-pink" v-show="event.eventList.pageNumber > 0" @click="event.BackPage()">Back</button></div>
        <span class="rounded-lg h-12 w-16 font-bold text-white text-xl text-center bg-black/30 border-4 border-Web-pink py-1">{{event.eventList.pageNumber + 1}}</span>
        <button class="text-xl text-white rounded-3xl w-28 h-12 mx-2 bg-Web-pink hover:bg-white hover:text-Web-pink" v-show="event.eventList.pageNumber + 1 < event.eventList.totalPages" @click="event.NextPage()">Next</button>
    </div>
    </div>
    </div>
</template>
 
<style>

</style>