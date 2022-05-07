<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { formatDate,formatTime } from '../main.js'

const { params } = useRoute();
const getEvent = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/event`);
  if (res.status === 200) {
    let result = await res.json();
    console.log(result);
    displayEvent.value = result.filter((x) => x.id == params.id)[0];
    console.log(displayEvent.value);
  } else console.log("error, cannot get word");
};
getEvent();
const displayEvent = ref();
const myRouter = useRouter();
const goBack = () => {
  getEvent();
  myRouter.go(-1);
};
</script>

<template>
  <div class="bg-fixed bg-detail bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen pt-12 px-36 pb-36">
    <div
      class="w-full h-full overflow-auto"
    >
      <div class="grid grid-cols-2 text-4xl gap-y-10 break-all">
        <p class="text-8xl text-center col-span-2">Detail</p>
        <p class="pl-10">Name : {{ displayEvent.bookingName }}</p>
        <p class="pl-10">Email : {{ displayEvent.bookingEmail }}</p>
        <p class="pl-10">Category : {{ displayEvent.eventCategory.eventCategoryName }}</p>
        <p class="pl-10">
          Date : {{ formatDate(displayEvent.eventStartTime) }}
        </p>
        <p class="pl-10">Duration : {{ displayEvent.eventDuration }}</p>
        <p class="pl-10">
          Start Time : {{ formatTime(displayEvent.eventStartTime) }}
        </p>
        <p class="col-span-2 pl-10 pr-80">Description : {{ displayEvent.eventNotes }}</p>
      </div>
          <button @click="goBack"><img class="absolute bottom-16 left-20" src="../assets/images/back.png" width="70" ></button>
    </div>
  </div>
</template>

<style></style>
