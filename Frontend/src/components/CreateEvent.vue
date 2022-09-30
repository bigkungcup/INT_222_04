<script setup>
import { ref } from "vue";
import { useLogin } from "../stores/login.js";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import moment from "moment"
defineEmits(['create','close'])

defineProps({
  currentCategory: {
    type: Object,
    require: {},
  },
  popUp: {
    type: Boolean,
    require: true,
  },
  textPopUp: {
    type: Boolean,
    require: true,
  },
});

const login = useLogin();

const userEmail = ref('')
userEmail.value = login.getEmailToken();
console.log(userEmail.value);

const studentEvent = ref({
  bookingName: "",
  bookingEmail: userEmail.value,
  eventCategory: {},
  eventStartTime: "",
  eventNotes: "",
  eventDuration: 0,
})

const adminEvent = ref({
  bookingName: "",
  bookingEmail: "",
  eventCategory: {},
  eventStartTime: "",
  eventNotes: "",
  eventDuration: 0,
})

const newEvent = ref();

const check = () => {
if(login.getRoleToken() == '[admin]'){
  newEvent.value = adminEvent.value
  console.log(adminEvent.value);
}else{
  newEvent.value = studentEvent.value;
  console.log(studentEvent.value);
}
}

const reset = () => {
  newEvent.value = {
    bookingName: "",
    bookingEmail: "",
    eventCategory: {},
    eventStartTime: "",
    eventNotes: "",
    eventDuration: 0,
  };
};


const textPopUpDate = ref(false)
const setMinTime = (eventStartTime) => {
  newEvent.value.eventStartTime = moment(eventStartTime).isAfter(moment(new Date())) ? eventStartTime : "a"
  console.log(newEvent.value.eventStartTime);
  textPopUpDate.value = true;
}

const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

check();

</script>
 
<template>
  <div class="w-full h-full pt-12 px-36 pb-36">
    <div class="grid grid-cols-2 text-4xl gap-y-6 break-all">
      <p class="col-span-2 pl-10">
        Name :
        <input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-3/5 text-3xl 
        placeholder:italic placeholder:text-2xl " placeholder=" Enter Your Name"
          v-model="newEvent.bookingName" /><span class="text-gray-500 text-lg">{{newEvent.bookingName.length}}/100</span>
        <!-- <router-link :to="{ name: 'Home' }"><img src="../assets/images/Exit.png" width="60"
            class="absolute top-3 right-36" /></router-link> -->
      <div v-if="newEvent.bookingName === '' || newEvent.bookingName === null || newEvent.bookingName.length === 0">
        <p v-show="textPopUp" class="text-lg text-red-500 pl-28">*Please enter your name.</p>
      </div>
      <div v-if="newEvent.bookingName.length > 100">
        <p  class="text-lg text-red-500 pl-28">*Name can't be longer than 100 characters.</p>
      </div>

      </p>
      <p class="pl-10">
        Email :
        <input type="email" class="bg-white border border-slate-300 rounded-lg h-10 text-3xl 
        placeholder:italic placeholder:text-2xl" placeholder=" you@example.com"
          v-model="newEvent.bookingEmail" v-if="login.getRoleToken() == '[admin]'"/><span class="text-gray-500 text-lg" v-if="login.getRoleToken() == '[admin]'">{{newEvent.bookingEmail.length}}/255</span>
         <span v-if="!(login.getRoleToken() === '[admin]')">{{ userEmail }}</span> 
      <div v-if="newEvent.bookingEmail === '' || newEvent.bookingEmail.length === 0">
        <p v-show="textPopUp" class="text-lg text-red-500 pl-28">*Please enter your email.</p>
      </div>
      <div v-if="newEvent.bookingEmail.match(validEmail) === null && newEvent.bookingEmail !== '' ">
        <p v-show="textPopUp" class="text-lg text-red-500 pl-28">*Invalid Email.</p>
      </div>
      <div v-if="newEvent.bookingEmail.length > 255">
        <p  class="text-lg text-red-500 pl-28">*Email can't be longer than 255 characters.</p>
      </div>
      </p>

      <p class="flex pl-10">
        Date/Time :
        <Datepicker @closed="setMinTime(newEvent.eventStartTime)" :minDate="new Date()"
          v-model="newEvent.eventStartTime" class="w-52 ml-3 -mt-1">
        </Datepicker>
      <div
        v-if="newEvent.eventStartTime === '' || newEvent.eventStartTime === null || newEvent.eventStartTime.value === 0 && textPopUpDate === false">
        <br>
        <p v-show="textPopUp" class="absolute text-lg text-red-500 -ml-52">*Please enter your start time.</p>
      </div>
      <div v-if="newEvent.eventStartTime === 'a'">
        <br>
        <p v-show="textPopUpDate" class="absolute text-lg text-red-500 -ml-52 break-words">*Not be able to select the
          previous date and time.</p>
      </div>
      <div
        v-if="newEvent.eventStartTime === 'overlap'">
        <br>
        <p class="absolute text-lg text-red-500 -ml-52 break-words">*This time has already been used.</p>
      </div>
      </p>

      <p class="pl-10">
        Clinic :
        <select v-model="newEvent.eventCategory" class="px-3 rounded-lg text-3xl" >
          <option v-for="list in currentCategory" :value="list">
            {{ list.eventCategoryName }}
          </option>
        </select>
      <div
        v-if="newEvent.eventCategory === {} || newEvent.eventCategory === null || Object.keys(newEvent.eventCategory).length === 0">
        <p v-show="textPopUp" class="text-lg text-red-500 pl-28">*Please enter your clinic.</p>
      </div>
      </p>
      <p class="pl-10">
        Duration :
        <input type="text" class="bg-white border border-slate-300 rounded-lg h-10 text-3xl w-1/5" disabled readonly
          :value="newEvent.eventDuration = newEvent.eventCategory.eventDuration"> min.
      </p>
      <p class="col-span-2 pl-10 pr-80">
        Description :
        <textarea
          class="bg-white border border-slate-300 rounded-lg h-10 col-span-2 w-full h-60 mt-5 p-3 text-3xl resize-none placeholder:italic placeholder:text-2xl"
          placeholder="Add your note" v-model="newEvent.eventNotes"></textarea><span class="absolute -ml-16 -mt-2 text-gray-500 text-lg">{{newEvent.eventNotes.length}}/500</span>
      <div v-if="newEvent.eventNotes.length > 500">
        <p class="text-lg text-red-500 pl-28">*Description can't be longer than 500 characters.</p>
      </div>
      </p>
      <div></div>
      <div class="text-white">
        <router-link :to="{ name: 'Home' }">
          <button class="bg-red-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-red-700">
            Cancel
          </button>
        </router-link>
        <button class="bg-green-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-green-700" @click="$emit('create', newEvent)">
          Save
        </button>
      </div>
    </div>
  </div>
  <div v-show="popUp" class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
    <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
      <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img"
          class="iconify iconify--ep animate-bounce pt-2" width="100" height="100" preserveAspectRatio="xMidYMid meet"
          viewBox="0 0 1024 1024">
          <path fill="#ffff"
            d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
          </path>
        </svg>
      </div>
      <div class="grid text-4xl place-items-center">
        <p>Created successfully</p>
      </div>
      <div class="grid place-items-center">
        <button class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2 hover:text-pink-700 hover:border-2 border-pink-700" @click="reset(), $emit('close')">
          OK
        </button>
      </div>
    </div>
  </div>
</template>
 
<style>
</style>