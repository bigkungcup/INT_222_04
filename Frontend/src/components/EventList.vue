<script setup>
import {ref} from 'vue'
import { formatDate, formatTime } from "../main.js";
import EventDeleteConfirm from '../components/EventDeleteConfirm.vue'
defineEmits(["delete"]);

defineProps({
  currentEvent: {
    type: Array,
    require: true,
  },
});

const popUp = ref(false)

const showPopUp = () => {
  popUp.value = true
  console.log(popUp.value)
}

</script>

<template>
<div>
  <div class="grid grid-cols-2 place-items-center gap-9 py-16 px-52 text-xl">
    <div v-for="list in currentEvent">
        <div
          class="grid grid-cols-5 bg-white/70 w-100 h-auto rounded-3xl gap-4 break-all z-0"
        >
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
              <img src="../assets/images/Star.png" class="p-4" />
            </div>
          </div>
          <div class="grid col-span-3 p-3">
            <p>{{ list.eventCategory.eventCategoryName }}</p>
            <p>{{ list.bookingName }}</p>
            <p>
              {{
                formatDate(list.eventStartTime) +
                "   " +
                formatTime(list.eventStartTime)
              }}
            </p>
            <router-link :to="{ name: 'ScheduleDetail', params: { id: list.id } }"><p class="underline underline-offset-4 text-pink-600">more details</p></router-link>
          </div>
          <div class="grid col-span-1 pt-3">
            <p>{{ list.eventDuration }} min.</p>
            <button @click="showPopUp">
              <img
                src="../assets/images/Trash.png"
                width="30"
                class="ml-8 mt-2 z-10"
              />
            </button>
          </div>
        </div>
    </div>
  </div>
  <div v-show="popUp" class="absolute bg-black/50 h-screen w-screen inset-0 top-0">
  <div class="bg-white w-2/5 h-2/5">

  </div>
  </div>
  </div>
</template>

<style></style>
