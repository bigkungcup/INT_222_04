<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";

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
  <div class="grid appBg h-screen">
    <div
      class="bg-white h-128 w-128 place-self-center rounded-3xl py-3 space-y-6"
    >
      <div class="grid grid-cols-2 text-4xl space-y-6">
        <p class="text-8xl text-center col-span-2">Detail</p>
        <p class="pl-10">Name : {{ displayEvent.bookingName }}</p>
        <p class="pl-10">Email : {{ displayEvent.bookingEmail }}</p>
        <p class="pl-10">Category : {{ displayEvent.eventCategoryName }}</p>
        <p class="pl-10">
          Date : {{ displayEvent.eventStartTime.slice(0, 10) }}
        </p>
        <p class="pl-10">Duration : {{ displayEvent.eventDuration }}</p>
        <p class="pl-10">
          Start Time : {{ displayEvent.eventStartTime.slice(11, 16) }}
        </p>
      </div>
      <div class="text-4xl break-words">
        <p class="pl-10">Description : {{ displayEvent.eventNotes }}</p>
      </div>
          <button @click="goBack"><img class="absolute bottom-16 left-20" src="../assets/images/back.png" width="70" ></button>
    </div>
  </div>
</template>

<style></style>
