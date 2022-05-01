<script setup>
import { ref, onBeforeMount, onMounted } from "vue";
import EventList from "../components/EventList.vue";
import EventEmptyList from "../components/EventEmptyList.vue";

//GET
const lists = ref([]);
const getEventLists = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/event`, {
    method: "GET",
  });
  if (res.status === 200) {
    lists.value = await res.json();
  } else console.log("error, cannot get event lists");
};

const currentEvent = ref({});
onBeforeMount(async () => {
  await getEventLists();
});



</script>

<template>
  <div>
  <div class="grid" v-show="!(lists.length===0)">
    <EventList :currentEvent="lists" />
  </div>
  <div class="grid" v-show="lists.length===0">
    <EventEmptyList />
  </div>
  </div>
</template>

<style></style>
