<script setup>
import { ref } from "vue";
import { formatDate, formatTime } from "../main.js";
import moment from "moment"
import { useEvent } from "../stores/event.js";
defineEmits(["delete","next","back"]);

defineProps({
  currentEvent: {
    type: Array,
    require: true,
  },
  page:{
    type: Number,
    require: true,
  }
});

const event = useEvent()
const popUp = ref(false);
const deleteId = ref();

const showPopUp = (id) => {
  popUp.value = true;
  deleteId.value = id;
  console.log(popUp.value);
  console.log(deleteId.value);
};

const filter = ref(0)

const filterPast = (currentEvent) => {
    return currentEvent.filter(a => moment(a.eventStartTime).isBefore(moment(new Date())))
}

const filterFuture = (currentEvent) => {
    return currentEvent.filter(a => moment(a.eventStartTime).isAfter(moment(new Date())))
}

</script>

<template>
  <div>
    <div class="grid grid-cols-2 place-items-center gap-9 py-16 px-52 text-xl">
      <select v-model="filter" class="col-span-2 px-3 rounded-lg text-3xl -mt-8">
          <option default value="0">Lists All</option>
          <option value="1">Past Events</option>
          <option value="2">Up-coming Events</option>
        </select>
      <div v-for="list in filter == 1 ? filterPast(currentEvent) : filter == 2 ? filterFuture(currentEvent) :currentEvent">
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
            <router-link
              :to="{ name: 'EventDetail', params: { id: list.id } }"
              ><p class="text-pink-600 hover:text-pink-700 hover:underline underline-offset-4">
                more details »
              </p></router-link
            >
          </div>
          <div class="grid col-span-1 pt-3">
            <p>{{ list.eventDuration }} min.</p>
            <button @click="showPopUp(list.id)">
              <img
                src="../assets/images/Trash.png"
                width="30"
                class="ml-8 mt-2 z-10"
              />
            </button>
          </div>
        </div>
      </div>
        <div class="flex gap-x-16 ">
          <button class="bg-white rounded-3xl w-36 py-2 mx-2 drop-shadow-xl" v-show="page > 0" @click="$emit('back')">back</button>
          <input type="text" class="bg-white border border-slate-300 rounded-lg h-10 text-3xl w-10 text-center" disabled readonly
          :value="page+1">
          <button class="bg-white rounded-3xl w-36 py-2 mx-2 drop-shadow-xl" v-show="page+1 < event.eventLists.totalPages" @click="$emit('next')">next</button>
        </div>
    </div>
    <div
      v-show="popUp"
      class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0"
    >
      <div
        class="grid grid-rows-6 bg-white w-2/5 h-96 place-self-center rounded-3xl"
      >
        <div class="grid row-span-2 bgPopUp rounded-t-3xl place-items-center">
          <img
            src="../assets/images/Warning.png"
            width="90"
            class="grid absolute animate-bounce"
          />
        </div>
        <div class="grid row-span-2 place-items-center">
          <p class="text-5xl">Are you sure?</p>
        </div>
        <div>
          <p class="grid text-2xl text-gray-500 place-items-center">
            This schedule will be deleted. Do you confirm that?
          </p>
        </div>
        <div class="flex justify-center mb-6">
          <button
            class="text-4xl px-5 text-green-500"
            @click="$emit('delete', deleteId, (popUp = false))"
          >
            Yes
          </button>
          <button class="text-4xl px-5 text-red-500" @click="popUp = false">
            No
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.bgPopUp {
  background-color: #f5c6cd;
}
</style>
