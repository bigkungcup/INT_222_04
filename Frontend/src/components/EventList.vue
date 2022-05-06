<script setup>
import { ref } from "vue";

defineProps({
  currentEvent: {
    type: Array,
    require: true,
  },
});
</script>

<template>
  <div class="grid grid-cols-2 place-items-center gap-9 py-16 px-52 text-xl">
    <div v-for="list in currentEvent">
      <router-link :to="{ name: 'ScheduleDetail', params: { id: list.id } }">
        <div class="grid grid-cols-5 bg-white/70 w-100 h-auto rounded-3xl gap-4 break-all">
          <div class="grid col-span-1">
            <div
              :class="[
                `grid rounded-full w-20 h-20 -ml-20 place-self-center`,
                list.eventCategory.id == 1
                  ? `projectManagement`
                  : list.eventCategory.id == 2
                  ? `devopInfra`
                  : list.eventCategory.id == 3
                  ? `database`
                  : list.eventCategory.id == 4
                  ? `clientSide`
                  : `serverSide`,
              ]"
            >
            <img src="../assets/images/Star.png" class="p-4">
            </div>
          </div>
          <div class="grid col-span-3 p-3">
           <p>{{ list.eventCategory.eventCategoryName }}</p> 
            <p>{{ list.bookingName }}</p> 
            <p>{{ list.eventStartTime.slice(0, 10) + " " +list.eventStartTime.slice(11, 16) }}</p> 
          </div>
          <div class="grid col-span-1 pt-3">
          <p>{{ list.eventDuration }} min.</p>
          </div>
        </div>
      </router-link>
    </div>
  </div>
</template>

<style></style>
