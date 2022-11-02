<script setup>
import { ref,onBeforeMount } from "vue";
import { useClinics } from "../stores/Clinics.js";
import { useEvents } from "../stores/Events";
import BookingSeccessfully from "../components/BookingSuccessfully.vue";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

const clinic = useClinics();
const event = useEvents();


onBeforeMount(async () => {
    await clinic.getClinics();
});
</script>

<template>
  <div class="bg-Bg bg-cover h-screen pt-24 px-32 overflow-auto no-scrollbar">
      <p class="font-bold text-white text-2xl my-3">Add your details :</p>
      <div class="grid grid-cols-2 bg-white rounded-2xl h-56">
        <div class="grid grid-rows-2">
          <div class="flex">
            <p class="text-Web-violet text-xl font-bold my-10 ml-11 mr-3">Name :</p>
            <input
              type="text"
              class="padding-input-detail bg-Bg-Plain rounded-lg h-16 w-9/12 text-1xl text-white placeholder:italic placeholder:text-1xl place-self-center"
              v-model="event.newEvent.bookingName"
              placeholder="Enter Your Name"
            />
          </div>
          <div class="flex">
            <p class="text-Web-violet text-xl font-bold my-10 ml-11 mr-4">Email :</p>
            <input
              type="text"
              class="padding-input-detail bg-Bg-Plain rounded-lg h-16 w-9/12 text-1xl text-white placeholder:italic placeholder:text-1xl place-self-center"
              v-model="event.newEvent.bookingEmail"
              placeholder="Enter Your Email"
            />
          </div>
        </div>
        <div class="p-6">
        <textarea
          class="padding-input-note bg-Bg-Plain rounded-lg h-full w-full text-1xl text-white resize-none placeholder:italic placeholder:text-1xl"
          v-model="event.newEvent.eventNotes"
          placeholder="Add your note (optional)"></textarea>
        </div>
      </div>
    <div class="grid mt-6 text-white text-2xl">
        <p class="font-bold my-3">Select clinic :</p>
        <div class="grid grid-cols-2">
        <div v-for="list in clinic.clinicList" class="grid">
            <button :class="['text-left rounded-lg w-11/12 my-2 p-2',
            list.id == 1 
            ? `text-bg-projectManagement` 
              : list.id == 2 
              ? `text-bg-devopInfra`
                : list.id == 3
                  ? `text-bg-database`
                  : list.id == 4
                    ? `text-bg-clientSide`
                    : `text-bg-serverSide` ,event.newEvent.eventCategory.id == list.id ? `bg-Web-pink`:'bg-white']" @click="event.newEvent.eventCategory = list">
                  <p :class="['text-xl pl-6 font-bold',event.newEvent.eventCategory.id == list.id ? `text-white`:'']">
                  {{ list.eventCategoryName }}
                  </p>
                  <p :class="['text-lg pl-6',event.newEvent.eventCategory.id == list.id ? 'text-white':'text-stone-600']">
                    {{ list.eventDuration }} min.
                  </p>
            </button>
        </div>
    </div>
  </div>
  <div class="grid mt-6 text-white text-2xl">
        <p class="flex font-bold my-3">Select time : 
          <Datepicker @closed="event.setMinTime(event.newEvent.eventStartTime)" :minDate="new Date()" 
           class="ml-6" v-model="event.newEvent.eventStartTime"></Datepicker></p>
  </div>
  <div class="flex my-12 text-2xl justify-center">
    <button class="rounded-2xl bg-white py-2 w-1/5 text-Web-pink font-bold hover:bg-white hover:text-Web-pink mx-24" @click="event.resetNewEvent()">
                    Cancle
    </button>
    <button class="rounded-2xl bg-Web-pink py-2 w-1/5 text-white font-bold hover:bg-Web-pink hover:text-white mx-24" @click="event.createEvent()">
                    Book Now
    </button>
  </div>
  <div v-show="event.bookingSeccessfully">
    <BookingSeccessfully @reset="event.resetNewEvent()" @toggle="event.bookingSeccessfully = false"/>
  </div>
</div>
</template>

<style>
.padding-input-detail {
    padding-left: 1rem;
}

.padding-input-note {
    padding-left: 1rem;
    padding-top: 0.5rem;
}

</style>
