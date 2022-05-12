<script setup>
import { computed } from "vue";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
defineEmits(['create', 'reset', 'show'])

const props = defineProps({
  currentCategory: {
    type: Object,
    require:{},
  },
  currentEvent: {
    type: Object,
    require: {},
  },
  popUp: {
    type: Boolean,
    require: true,
  }
});

const newEvent = computed(() => {
    return {
      bookingName: props.currentEvent.bookingName,
      bookingEmail: props.currentEvent.bookingEmail,
      eventCategory: props.currentEvent.eventCategory,
      eventStartTime: props.currentEvent.eventStartTime,
      eventNotes: props.currentEvent.eventNotes,
      eventDuration: props.currentEvent.eventDuration,
    }
})

// const a = (b) =>{
//     newEvent.eventDuration = b
// }

const a = (b) =>{
    console.log("ads");
}
</script>
 
<template>
  <div class="w-full h-full pt-12 px-36 pb-36">
      <div class="grid grid-cols-2 text-4xl gap-y-10 break-all">
        <p class="col-span-2 pl-10">
          Name :
          <input
            type="text"
            class="bg-white border border-slate-300 rounded-lg h-10 w-3/5 text-3xl"
            v-model="newEvent.bookingName"
          />
          <router-link :to="{ name: 'Home' }"
            ><img
              src="../assets/images/Exit.png"
              width="60"
              class="absolute top-3 right-36"
          /></router-link>
        </p>
        <p class="pl-10">
          Email :
          <input
            type="text"
            class="bg-white border border-slate-300 rounded-lg h-10 text-3xl"
            v-model="newEvent.bookingEmail"
          />
        </p>

        <p class="flex pl-10">
          Date/Time :
          <Datepicker :minDate="new Date()" :minTime="{ hour:new Date().getHours(), minutes: new Date().getMinutes() } " 
            v-model="newEvent.eventStartTime"
            class="w-52 ml-3 -mt-1"
          />
        </p>

        <p class="pl-10">
          Category :
          <select
            v-model="newEvent.eventCategory"
            class="px-3 rounded-lg text-3xl"
          >
            <option v-for="list in currentCategory" :value="list" @ended="a()">
            {{ list.eventCategoryName }}
            </option>
          </select>
        </p>
        <p class="pl-10"  >
          <!-- Duration :
          <span v-for="list in lists" v-show="list == currentSchedule.category" :v-model="currentSchedule.eventDuration">
          {{list.eventDuration}}</span>
          min. -->
          <!-- <input
            type="text"
            class="bg-white border border-slate-300 rounded-lg h-10 text-3xl w-1/5"
            :v-model="currentSchedule.eventDuration"
            v-for="list in lists" v-show="list == currentSchedule.category"
            disabled readonly :value="list.eventDuration"
          >  min. -->
          <!-- <select
            v-model="currentSchedule.eventDuration"
            class="px-3 rounded-lg text-3xl"
          >
            <option v-for="list in lists" :value="list" v-show="list == currentSchedule.category">
            {{ list.eventDuration }} 
            </option> 
          </select> mins. -->
          <!-- {{ newEvent.eventDuration }} -->
            <input
            type="text"
            class="bg-white border border-slate-300 rounded-lg h-10 text-3xl w-1/5"
            v-model="newEvent.eventDuration"
          >  min.
        </p>
        <p class="col-span-2 pl-10 pr-80">
          Description :
          <textarea
            class="bg-white border border-slate-300 rounded-lg h-10 col-span-2 w-full h-60 mt-5 p-3 text-3xl resize-none"
            placeholder="add your note "
            v-model="newEvent.eventNotes"
          ></textarea>
        </p>
        <div></div>
        <div class="text-white">
          <button
            class="bg-red-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl"
            @click="$emit('reset')"
          >
            Cancel
          </button>
          <button
            class="bg-green-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl"
            @click="$emit('create',newEvent)"
          >
            Save
          </button>
        </div>
      </div>
    </div>
    <div
      v-show="popUp"
      class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0"
    >
      <div
        class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl"
      >
        <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            aria-hidden="true"
            role="img"
            class="iconify iconify--ep"
            width="100"
            height="100"
            preserveAspectRatio="xMidYMid meet"
            viewBox="0 0 1024 1024"
          >
            <path
              fill="#ffff"
              d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z"
            ></path>
          </svg>
        </div>
        <div class="grid text-4xl place-items-center">
          <p>Created successfully</p>
        </div>
        <div class="grid place-items-center">
          <button
            class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2"
             @click="popUp = false"
          >
            OK
          </button>
        </div>
      </div>
    </div>
</template>
 
<style>

</style>