<script setup>
import { ref, onBeforeMount, onBeforeUpdate } from "vue";
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

const showEmpty = ref();

const getEmpty = async () => {
  if(lists.value.length===0){
    showEmpty.value = true
  } 
  else if (lists.value.length!==0){showEmpty.value = false}
  console.log(showEmpty.value)
}
onBeforeUpdate(async () => {
  await getEmpty();
});

</script>

<template>
  <div class="bg-fixed bg-schedule bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen overflow-auto no-scrollbar">
  <div class="grid" v-show="!showEmpty">
    <EventList :currentEvent="lists" />
  </div>
  <div class="grid" v-show="showEmpty">
    <EventEmptyList />
  </div>
  </div>
</template>

<style></style>
