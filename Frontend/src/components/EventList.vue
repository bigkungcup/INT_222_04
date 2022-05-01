<script setup>
import { ref } from 'vue'

defineProps({
  currentEvent: {
    type: Array,
    require: true,
  },
});

const color = ref()
</script>

<template>
  <div
    class="overflow-y-auto no-scrollbar bg-white h-128 w-128 place-self-center rounded-3xl py-3"
  >
    <div class="grid grid-cols-3 place-items-center gap-3 py-3">
      <div
        v-for="list in currentEvent"
        :class="[`grid w-auto h-auto text-white p-5 justify-center`, list.eventCategoryName == 'Project Management Clinic' ? `pmc`
        :list.eventCategoryName == 'DevOps/Infra Clinic'? `dic` : list.eventCategoryName == 'Database Clinic' ? `dc` 
        : list.eventCategoryName == 'Client-side Clinic' ? `cc`:`sc`]" 
      >
        <router-link :to="{ name: 'ScheduleDetail', params: { id: list.id } }">
          <div class="text-center text-xl mb-3">
            {{ list.eventCategoryName }}
          </div>
          <div
            class="bg-white w-60 h-52 rounded-3xl text-black space-y-3 pt-5 pl-4"
          >
            <p>Name : {{ list.bookingName }}</p>
            <p>Date : {{ list.eventStartTime.slice(0, 10) }}</p>
            <p>Start Time : {{ list.eventStartTime.slice(11, 16) }}</p>
            <p>Duration : {{ list.eventDuration }}</p>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<style></style>
