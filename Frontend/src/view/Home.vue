<script setup>
import { ref, onBeforeMount } from "vue";
import EventCategoryList from "../components/EventCategoryList.vue";

//GET
const lists = ref([]);
const getEventCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategory`, {
    method: "GET",
  });
  if (res.status === 200) {
    lists.value = await res.json();
    console.log(lists.value);
  } else console.log("error, cannot get event category lists");
};

onBeforeMount(async () => {
  await getEventCategory();
});



</script>

<template>
  <div
    class="bg-home bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen"
  >
    <EventCategoryList :currentCategory="lists" />
  </div>
</template>

<style></style>
