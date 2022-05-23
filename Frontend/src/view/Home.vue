<script setup>
import { onBeforeMount } from "vue";
import EventCategoryList from "../components/EventCategoryList.vue";
import { useEventCategory } from "../stores/event.js";

const category = useEventCategory();

onBeforeMount(async () => {
  await category.getEventCategory();
});
</script>

<template>
  <div
    class="bg-home bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen"
  >
    <EventCategoryList :currentCategory="category.categoryLists" />
  </div>
</template>

<style></style>
