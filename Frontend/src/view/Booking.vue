<script setup>
import { ref, onBeforeMount } from "vue";
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

//GET Schedule
const listsSchedule = ref([]);
const getEventLists = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/event`, {
    method: "GET",
  });
  if (res.status === 200) {
    listsSchedule.value = await res.json();
  } else console.log("error, cannot get event lists");
};

//Get Category
const lists = ref([]);
const getCategoryLists = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategory`, {
    method: "GET",
  });
  if (res.status === 200) {
    lists.value = await res.json();
    console.log(lists)
  } else console.log("error, cannot get event lists");
};

//Create Schedule
const createSchedule = async (currentSchedule) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/event`, {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify({   
    bookingName: currentSchedule.bookingName,
    bookingEmail: currentSchedule.bookingEmail,
    eventCategory: {
      id: currentSchedule.id,
    },
    eventStartTime: currentSchedule.eventStartTime,
    eventNotes: currentSchedule.eventNotes,
    eventDuration: currentSchedule.eventDuration,
    }),
  });
  if (res.status === 201) {
    const addedSchedule = await res.json();
    listsSchedule.value.push(addedSchedule);
    console.log("created successfully");
  } else console.log("error, cannot create");
  reset();
};

const reset = () => {
  currentSchedule.value = {};
};
const currentSchedule = ref({});


onBeforeMount(async () => {
  await getEventLists();
  await getCategoryLists();
});
</script>

<template>
  <div
    class="bg-fixed bg-booking bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen"
  >
    <div class="w-full h-full pt-12 px-36 pb-36">
      <div class="grid grid-cols-2 text-4xl gap-y-10 break-all">
        <p class="col-span-2 pl-10">
          Name :
          <input
            type="text"
            class="bg-white border border-slate-300 rounded-lg h-10 w-3/5 text-3xl"
            v-model="currentSchedule.bookingName"
          />
        </p>
        <p class="pl-10">
          Email :
          <input
            type="text"
            class="bg-white border border-slate-300 rounded-lg h-10 text-3xl"
            v-model="currentSchedule.bookingEmail"
          />
        </p>
        <p class="pl-10">Date/Time :   
        <Datepicker class="aaa" v-model="currentSchedule.eventStartTime"></Datepicker> </p>
        <p>  
          Category :
          <select v-model="currentSchedule.id" class="px-3 rounded-lg text-3xl">
            <option v-for="list in lists" :value = "list.id" >{{list.eventCategoryName}}</option>
          </select>
        </p>
        <p class="pl-10">
          Duration :
          <input
            type="text"
            class="bg-white border border-slate-300 rounded-lg h-10 col-span-2 w-1/6 text-3xl"
            v-model="currentSchedule.eventDuration"
          />
          min.
        </p>
        <p class="col-span-2 pl-10 pr-80">
          Description :
          <textarea
            class="bg-white border border-slate-300 rounded-lg h-10 col-span-2 w-full h-60 mt-5 p-3 text-3xl"
            placeholder="add your note "
            v-model="currentSchedule.eventNotes"
          ></textarea>
        </p>
        <div></div>
        <div class="text-white">
        <button class="bg-red-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl" v-on:click="reset">Cancle</button>
        <button class="bg-green-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl" v-on:click="createSchedule(currentSchedule)">Save</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.aaa{
  padding-left: 16px;
  padding-right: 16px;
}
</style>
