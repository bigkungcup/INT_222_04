<script setup>
import { ref, onBeforeMount, onMounted, onUpdated } from "vue";
import CreateEvent from "../components/CreateEvent.vue";

//GET
const listEvent = ref([]);
const getEventLists = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/event`, {
    method: "GET",
  });
  if (res.status === 200) {
    listEvent.value = await res.json();
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
    console.log(lists);
  } else console.log("error, cannot get event lists");
};

//Create Schedule
const listsSchedule = ref([]);
const createSchedule = async (newEvent) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/event`, {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify({
      bookingName: newEvent.bookingName,
      bookingEmail: newEvent.bookingEmail.match(validEmail)?newEvent.bookingEmail:null,
      eventCategory: newEvent.eventCategory,
      eventStartTime: newEvent.eventStartTime,
      eventNotes: newEvent.eventNotes,
      eventDuration: newEvent.eventDuration,
    }),
  });
  if (res.status === 201) {
    const addedSchedule = await res.json();
    listsSchedule.value.push(addedSchedule);
    showPopUp();
    console.log("created successfully");
  } else {
    console.log("error, cannot create");
    showText();
  }console.log(newEvent.bookingEmail.match(validEmail)?newEvent.bookingEmail:null);
};

const popUp = ref(false);
const showPopUp = () => {
  popUp.value = true;
  console.log(popUp.value);
};
const disShowPopUp = () => {
  popUp.value = false;
  textPopUp.value = false;
};

const textPopUp = ref(false);
const showText = () => {
  textPopUp.value = true;
  console.log(popUp.value);
};

const validEmail = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;


onMounted(async () => {
  await getCategoryLists();
  await getEventLists();
});

</script>

<template>
  <div class="bg-fixed bg-booking bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen">
    <CreateEvent @create="createSchedule" @close="disShowPopUp" :textPopUp="textPopUp" :popUp="popUp"
      :currentCategory="lists" />
  </div>
</template>

<style>
</style>
