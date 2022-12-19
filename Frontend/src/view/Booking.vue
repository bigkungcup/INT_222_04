<script setup>
import { ref,onBeforeMount } from "vue";
import { useClinics } from "../stores/Clinics.js";
import { useEvents } from "../stores/Events.js";
import { useLogin } from "../stores/Login.js";
import BookingSeccessfully from "../components/BookingSuccessfully.vue";
import ContinueAsGuest from "../components/ContinueAsGuest.vue";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import NoAuthorization from "../components/NoAuthorization.vue";

const clinic = useClinics();
const event = useEvents();
const login = useLogin();
const checkGuest = ref(false);
const asGuest = ref(false);
event.newEvent.eventCategory = [];

onBeforeMount(async () => {
    await clinic.getClinics();
    // await event.getAllEventList();
    event.resetNewEvent();
    checkGuest.value = login.getRoleToken() == null ? true : false;
});
</script>

<template>
  <div class="bg-Bg bg-cover h-screen pt-24 px-32 overflow-auto no-scrollbar">
      <p class="font-bold text-white text-2xl my-3">Add your details :</p>
      <div class="grid grid-cols-2 bg-white rounded-2xl h-56">
        <div class="grid grid-rows-2">
          <div>
          <div class="flex">
            <p class="text-Web-violet text-xl font-bold my-10 ml-11 mr-3">Name :</p>
            <input
              type="text"
              class="padding-input-detail bg-Bg-Plain rounded-lg h-16 w-9/12 text-1xl text-white placeholder:italic placeholder:text-1xl place-self-center"
              v-model="event.newEvent.bookingName"
              placeholder="Enter Your Name"
            />
          </div>
          <p class="text-Web-pink -mt-5 ml-32" v-show="event.newEvent.bookingName == '' && !event.bookingValidate">*Please enter your name.</p>
          <p class="text-Web-pink -mt-5 ml-32" v-show="event.newEvent.bookingName.length > 100 && !event.bookingValidate">*Name can't be longer than 100 characters.</p>
        </div>

        <div>
          <div class="flex">
            <p class="text-Web-violet text-xl font-bold my-10 ml-11 mr-4">Email :</p>
            <input
              type="text"
              class="padding-input-detail bg-Bg-Plain rounded-lg h-16 w-9/12 text-1xl text-white placeholder:italic placeholder:text-1xl place-self-center"
              v-show="login.getRoleToken() == null || login.getRoleToken() == 'admin'"
              v-model="event.newEvent.bookingEmail"
              placeholder="Enter Your Email"
            />
            <p class="text-Web-violet text-xl font-bold my-10" v-show="!(login.getRoleToken() == 'admin')">{{ login.getEmailToken() }}</p>
          </div>
        </div>
        <p class="text-Web-pink -mt-6 ml-32" v-show="event.newEvent.bookingEmail == '' && !event.bookingValidate">*Please enter your email.</p>
        <p class="text-Web-pink -mt-6 ml-32" v-show="!(event.newEvent.bookingEmail.match(event.validEmail)) && event.newEvent.bookingEmail != '' && !event.bookingValidate">*Invalid email.</p>
        <p class="text-Web-pink -mt-6 ml-32" v-show="event.newEvent.bookingEmail.length > 255 && !event.bookingValidate">*Email can't be longer than 255 characters.</p>
      </div>

        <div class="p-6">
        <textarea
          class="padding-input-note bg-Bg-Plain rounded-lg h-full w-full text-1xl text-white resize-none placeholder:italic placeholder:text-1xl"
          v-model="event.newEvent.eventNotes"
          placeholder="Add your note (optional)"></textarea>
          <p class="text-Web-pink -mt-1.5" v-show="event.newEvent.eventNotes.length > 500 && !event.bookingValidate">*Note can't be longer than 500 characters.</p>
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
           <p class="text-Web-pink text-lg -mt-2 ml-40" v-show="event.newEvent.eventStartTime == '' && !event.bookingValidate">*Please enter your select time.</p>
           <p class="text-Web-pink text-lg -mt-2 ml-40" v-show="event.newEvent.eventStartTime == 'previous time' && !event.bookingValidate">*Not be able to select the previous date and time.</p>
           <p class="text-Web-pink text-lg -mt-2 ml-40" v-show="event.newEvent.eventStartTime == 'overlap' && !event.bookingValidate">*This select time has already been used.</p>
  </div>

  <div class="grid mt-6 text-white text-2xl">
        <p class="flex font-bold my-3">Select file : 
          <label for="file" >
                <div v-show="!event.showFileName" class="border-2 border-white rounded-lg ml-6 text-white hover:bg-white hover:text-Web-violet text-center text-lg py-1 px-2">
                <input id="file" type="file" @change="event.chooseFile"/>
                Choose File
                </div>
                <div v-show="event.showFileName" class="bg-white text-Web-violet rounded-lg ml-6 text-white hover:bg-white/70 text-center text-lg py-1 px-2">
                    <input id="file" type="file" @change="event.chooseFile"/>
                    <p id="filename"></p>
                </div>
          </label>
          
          <div v-show="event.showFileName">
          <button class="ml-6 -mt-1" @click="event.resetNewEventFile"><svg width="40" height="40" viewBox="0 0 512 512" >
                  <path fill="none" d="M296 64h-80a7.91 7.91 0 0 0-8 8v24h96V72a7.91 7.91 0 0 0-8-8Z"/>
                  <path fill="#EB4C84" d="M432 96h-96V72a40 40 0 0 0-40-40h-80a40 40 0 0 0-40 40v24H80a16 16 0 0 0 0 32h17l19 304.92c1.42 26.85 22 47.08 48 47.08h184c26.13 0 46.3-19.78 48-47l19-305h17a16 16 0 0 0 0-32ZM192.57 416H192a16 16 0 0 1-16-15.43l-8-224a16 16 0 1 1 32-1.14l8 224A16 16 0 0 1 192.57 416ZM272 400a16 16 0 0 1-32 0V176a16 16 0 0 1 32 0Zm32-304h-96V72a7.91 7.91 0 0 1 8-8h80a7.91 7.91 0 0 1 8 8Zm32 304.57A16 16 0 0 1 320 416h-.58A16 16 0 0 1 304 399.43l8-224a16 16 0 1 1 32 1.14Z"/>
          </svg></button></div>
        </p>
        <p v-show="event.showErrorFileText" class="text-Web-pink text-lg pl-40">*The file size cannot be larger than 10 MB.</p>
  </div>

  <div class="flex my-12 text-2xl justify-center">
    <button class="rounded-2xl bg-white py-2 w-1/5 text-Web-pink font-bold mx-24" @click="event.resetNewEvent()">
                    Cancle
    </button>
    <button class="rounded-2xl bg-Web-pink py-2 w-1/5 text-white font-bold mx-24" @click="asGuest ? event.createEventWithGuest() : event.createEvent()">
                    Book Now
    </button>
  </div>

  <div v-show="event.bookingSeccessfully">
    <BookingSeccessfully @reset="event.resetNewEvent()" @toggle="event.bookingSeccessfully = false"/>
  </div>
  
  <div v-show="checkGuest">
    <ContinueAsGuest @toggle="checkGuest = false,asGuest = true"/>
  </div>

  <div v-show="login.getRoleToken() == 'lecturer'">
    <NoAuthorization @toggle="checkGuest = false,asGuest = true"/>
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

input[type="file"] {
    display: none;
}

/* .custom-file-upload {
    border: 1px solid #ccc;
    display: inline-block;
    padding: 6px 12px;
    cursor: pointer;
} */

</style>
