<script setup>
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { formatDate, formatTime } from "../main.js";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import moment from "moment"
import { useEvent } from "../stores/event.js"
const { params } = useRoute();

const event = useEvent()

//get event
const getEvent = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${params.id}`);
  if (res.status === 200) {
    displayEvent.value = await res.json();
    console.log("get successfully");
  } else console.log("error, cannot get event");
};

//edit event
const saveEvent = async (displayEvent,editEvent) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${params.id}`, {
    method: "PUT",
    headers: { "content-type": "application/json" },
    body: JSON.stringify({
      bookingName: displayEvent.bookingName,
      bookingEmail: displayEvent.bookingEmail,
      eventCategory: displayEvent.eventCategory,
      eventStartTime: editEvent.eventStartTime === "" ? displayEvent.eventStartTime : event.getOverlapTime(editEvent.eventStartTime,displayEvent.eventCategory.id) ? editEvent.eventStartTime = "overlap":editEvent.eventStartTime,
      eventNotes: editEvent.eventNotes === "" ? displayEvent.eventNotes : editEvent.eventNotes,
      eventDuration: displayEvent.eventDuration,
    }),
  });
  if (res.status === 200) {
    let event = await res.json();
    console.log(event);
    if (editEvent.eventStartTime !== "" || editEvent.eventNotes !== "") {
      showEditPopUp()
    }
    popUp.value = false
    reset()
    getEvent()
    console.log("edit successfully");
  } else {
    console.log("error, cannot edit");
    alert("error, cannot edit");
  }
};

const displayEvent = ref({
  bookingName: "",
  bookingEmail: "",
  eventCategory: {},
  eventStartTime: "",
  eventNotes: "",
  eventDuration: 0,
});

const myRouter = useRouter();

const goBack = () => {
  myRouter.go(-1);
  window.localStorage.clear();
};

const editEvent = ref({
  eventStartTime: "",
  eventNotes: "",
})

const popUp = ref(false);
const showPopUp = () => {
  popUp.value = true;
  console.log(popUp.value);
};

const editPopUp = ref(false);
const showEditPopUp = () => {
  editPopUp.value = true;
  console.log(editPopUp.value);
};

const reset = () => {
  editEvent.value = {
    eventStartTime: "",
    eventNotes: ""
  }
}

const setMinTime = (eventStartTime) => {
  editEvent.value.eventStartTime = moment(eventStartTime).isAfter(moment(new Date())) ? eventStartTime : "a"
  console.log(editEvent.value.eventStartTime);
}

onBeforeMount(async () => {
  await event.getAllEventLists();
  await getEvent();
});


</script>

<template>
  <div class="bg-fixed bg-detail bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen pt-12 px-36 pb-36">
    <div class="w-full h-full overflow-auto">
      <div class="grid grid-cols-2 text-4xl gap-y-10 break-all">
        <p class="text-8xl text-center col-span-2">
          Detail
          <button @click="showPopUp()">
            <img src="../assets/images/Edit.png" width="40" class="transition duration-150 ease-in-out hover:scale-125" />
          </button>
          <button @click="goBack">
            <img src="../assets/images/Exit.png" width="60" class="absolute top-3 right-36" />
          </button>
        </p>
        <p class="pl-10">Name : {{ displayEvent.bookingName }}</p>
        <p class="pl-10">Email : {{ displayEvent.bookingEmail }}</p>
        <p class="pl-10">
          Category : {{ displayEvent.eventCategory.eventCategoryName }}
        </p>

        <div class="flex pl-10 " v-show="popUp">
          Date/Time :
          <Datepicker :minDate="new Date()" @closed="setMinTime(editEvent.eventStartTime)"
            v-model="editEvent.eventStartTime" class="w-52 ml-3 -mt-1"></Datepicker>
          <div v-if="editEvent.eventStartTime === 'a'">
            <br>
            <p class="absolute text-lg text-red-500 -ml-52 break-words">*Not be able to select
              the previous date and time.</p>
          </div>
          <div v-if="editEvent.eventStartTime === 'overlap'">
            <br>
            <p class="absolute text-lg text-red-500 -ml-52 break-words">*This time has already been used.</p>
          </div>
        </div>

        <p class="pl-10">Duration : {{ displayEvent.eventDuration }}</p>
        <p class="pl-10" v-show="!popUp">
          Date : {{ formatDate(displayEvent.eventStartTime) }}
        </p>
        <p class="pl-10" v-show="!popUp">
          Start Time : {{ formatTime(displayEvent.eventStartTime) }}
        </p>
        <p class="col-span-2 pl-10 pr-80" v-show="!popUp">
          Description : {{ displayEvent.eventNotes }}
        </p>

        <p class="col-span-2 pl-10 pr-80" v-show="popUp">
          Description :
          <p v-if="editEvent.eventNotes.length > 500" class="text-lg text-red-500 pl-52 -mt-8">*Description can't be longer than 500 characters.</p>
          <textarea
            class="bg-white border border-slate-300 rounded-lg h-10 col-span-2 w-full h-28 mt-5 p-3 text-3xl resize-none"
            placeholder="add your note " v-model="editEvent.eventNotes"></textarea><span class="absolute -ml-16 -mt-2 text-gray-500 text-lg">{{editEvent.eventNotes.length}}/500</span>
        </p>
      </div>
    </div>
    <div class="flex text-white justify-center text-2xl" v-show="popUp">
      <button class="bg-red-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-red-700" @click="popUp = false,reset()">
        Cancel
      </button>
      <button class="bg-green-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-green-700" @click="saveEvent(displayEvent,editEvent)">
        Save
      </button>
    </div>

    <div v-show="editPopUp" class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
      <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
        <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
          <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true"
            role="img" class="iconify iconify--ep animate-bounce" width="100" height="100" preserveAspectRatio="xMidYMid meet"
            viewBox="0 0 1024 1024">
            <path fill="#ffff"
              d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
            </path>
          </svg>
        </div>
        <div class="grid text-4xl place-items-center">
          <p>Edit successfully</p>
        </div>
        <div class="grid place-items-center">
          <button class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2 hover:text-pink-700 hover:border-2 border-pink-700"
            @click="editPopUp = false, popUp = false">
            OK
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
</style>
