<script setup>
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { formatDate, formatTime } from "../main.js";
import Datepicker from "@vuepic/vue-datepicker";
import { useEvents } from "../stores/Events.js";

const { params } = useRoute();
const event = useEvents();

onBeforeMount(async () => {
  await event.getEventDetail(params.id);
});
</script>

<template>
  <div class="bg-Bg bg-cover h-screen pt-24 px-32 overflow-auto no-scrollbar">
    <div class="grid grid-cols-2 bg-white rounded-2xl h-80 mt-6">
      <div class="grid grid-rows-2 pl-14">
        <div class="text-3xl font-bold text-Web-violet">
            <p class="mt-6">
                {{ event.displayEvent.bookingName }}
            </p>
        </div>
        <div class="text-Web-violet">
          By {{ event.displayEvent.bookingEmail }}
        </div>
      </div>
      <div class="grid grid-rows-2 pt-2">
        <div
          :class="['text-center text-white font-bold w-3/5 rounded-2xl place-self-center ml-32',
            event.displayEvent.eventCategory.id == 1 
              ? `bg-bg-projectManagement`
              : event.displayEvent.eventCategory.id == 2 
              ? `bg-bg-devopInfra`
              : event.displayEvent.eventCategory.id == 3
              ? `bg-bg-database`
              : event.displayEvent.eventCategory.id == 4
              ? `bg-bg-clientSide`
              : `bg-bg-serverSide`
          ]"
        >
        <p class="py-3">
          {{ event.displayEvent.eventCategory.eventCategoryName }}
        </p>
        </div>
        <div class="grid text-center text-stone-500 mb-6 ml-104">
          {{ event.displayEvent.eventCategory.eventDuration }} min.
        </div>
      </div>
      <hr class="col-span-2 h-3 -mt-6"/>
      <div class="col-span-2 text-1xl text-black px-14 pb-2 -mt-12 h-20 resize-none break-words">
        <p v-show="!event.editField">{{ event.displayEvent.eventNotes }}</p>
        <textarea
          class="padding-input-note bg-Bg-Plain rounded-lg h-28 w-full text-1xl text-white resize-none placeholder:italic placeholder:text-1xl"
          v-model="event.editEvent.eventNotes"
          v-show="event.editField"
          placeholder="Add your note (optional)"></textarea>
      </div>
      <div class="col-span-2 h-6">
        <div class="grid place-items-end">
          <button
            class="rounded-2xl bg-Web-pink py-2 w-1/12 text-white font-bold hover:bg-Web-pink hover:text-white mx-14"
            v-show="!event.editField"
            @click="event.editField = true"
          >
            Edit
          </button>
        <div class="flex w-3/6 -mt-5" v-show="event.editField">
            <button
            class="rounded-2xl bg-white py-2 w-2/12 text-Web-pink border-2 border-Web-pink text-lg font-bold ml-88 my-6"
            @click="event.editField = false, event.resetEditField()"
          >
            Cancle
          </button>
          <button
            class="rounded-2xl bg-Web-pink py-2 w-2/12 text-white text-lg font-bold ml-6 my-6"
            @click="event.saveEvent(params.id)"
          >
            Submit
          </button>
        </div>
      </div>
      </div>
      
      </div>

    <div class="grid grid-cols-2 h-24 mt-6 text-white text-2xl bg-white rounded-2xl">
        <p class="flex text-Web-violet font-bold my-8 mx-14">Booking time : 
          <p class="ml-2" v-show="!event.editTime">{{ formatDate(event.displayEvent.eventStartTime) }} || {{ formatTime(event.displayEvent.eventStartTime) }}</p>
          <Datepicker @closed="event.setMinTime(event.newEvent.eventStartTime)" :minDate="new Date()" 
           class="ml-6" v-show="event.editTime" v-model="event.editEvent.eventStartTime"></Datepicker>
        </p>
        <div v-show="!event.editTime" class="grid place-items-end">
        <button
            class="rounded-2xl bg-Web-pink py-2 w-1/6 text-white text-lg font-bold mx-14 my-6"
            @click="event.editTime = true"
          >
            Edit
          </button>
        </div>
        <div class="grid place-items-end" v-show="event.editTime">
          <div class="flex w-4/6">
            <button
            class="rounded-2xl bg-white py-2 w-1/4 text-Web-pink border-2 border-Web-pink text-lg font-bold ml-32 my-6"
            @click="event.editTime = false, event.resetEditTime()"
          >
            Cancle
          </button>
          <button
            class="rounded-2xl bg-Web-pink py-2 w-1/4 text-white text-lg font-bold ml-6 my-6"
            @click="event.saveEvent(params.id)"
          >
            Submit
          </button>
        </div>
          </div>
  </div>
  </div>
</template>

<style></style>
