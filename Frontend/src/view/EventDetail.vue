<script setup>
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { formatDate, formatTime } from "../main.js";
import Datepicker from "@vuepic/vue-datepicker";
import { useEvents } from "../stores/Events.js";
import { useClinics } from "../stores/Clinics.js";

const { params } = useRoute();
const event = useEvents();
const clinic = useClinics();
const setEditFileName = () => {
  event.editFileName = event.displayEvent.fileName
}


onBeforeMount(async () => {
  await event.getEventDetail(params.id)
  clinic.getClinics();
  event.resetEditField();
  event.resetEditTime();
  event.editFile = false;
  event.createFileUrl(params.id,event.displayEvent.fileName)
  setEditFileName()
  // if(window.location.host == 'localhost:3000'){
  // fileUrl.value = window.location.protocol + "//" + window.location.host +"/api/files/" + params.id  + "/" + event.displayEvent.fileName
  // } else { fileUrl.value = 'https://10.4.56.93/api/files/' + params.id  + "/" + event.displayEvent.fileName }
});
</script>

<template>
  <div class="bg-Bg bg-cover h-screen pt-24 px-32 overflow-auto no-scrollbar">
    <div class="grid grid-cols-2 bg-white rounded-2xl h-80 mt-6">
      <div class="grid grid-rows-2 pl-14">
        <div class="text-3xl font-bold text-Web-violet">
            <p class="mt-6">
                {{ event.displayEvent.bookingName }}
            </p>
        </div>
        <div class="text-Web-violet">
          By {{ event.displayEvent.bookingEmail }}
        </div>
      </div>
      <div class="grid grid-rows-2 pt-2" v-show="!event.editField">
        <div
          :class="['text-center text-white font-bold w-3/5 rounded-2xl place-self-center ml-32',
            event.displayEvent.eventCategory.id == 1 
              ? `bg-bg-projectManagement`
              : event.displayEvent.eventCategory.id == 2 
              ? `bg-bg-devopInfra`
              : event.displayEvent.eventCategory.id == 3
              ? `bg-bg-database`
              : event.displayEvent.eventCategory.id == 4
              ? `bg-bg-clientSide`
              : `bg-bg-serverSide`
          ]"
        >
        <p class="py-3">
          {{ event.displayEvent.eventCategory.eventCategoryName }}
        </p>
        </div>
        <div class="grid text-center text-stone-500 mb-6 ml-104">
          {{ event.displayEvent.eventCategory.eventDuration }} min.
        </div>
      </div>
      
      <div class="grid grid-rows-2 pt-2" v-show="event.editField">
      <select class="bg-Web-violet text-center text-white font-bold w-3/5 rounded-2xl place-self-center py-3 ml-32" v-model="event.editEvent.eventCategory">
        <option v-for="list in clinic.clinicList" :value="list">
            {{ list.eventCategoryName }}            
        </option>
        </select>
        <div class="grid text-center text-stone-500 mb-6 ml-104">
          {{ event.editEvent.eventCategory.eventDuration }} min.
        </div>
    </div>

      <hr class="col-span-2 h-3 -mt-6"/>
      <div class="col-span-2 text-1xl text-black px-14 pb-2 -mt-12 h-20 resize-none break-words">
        <p v-show="!event.editField">{{ event.displayEvent.eventNotes }}</p>
        <textarea
          class="padding-input-note bg-Bg-Plain rounded-lg h-28 w-full text-1xl text-white resize-none placeholder:italic placeholder:text-1xl"
          v-model="event.editEvent.eventNotes"
          v-show="event.editField"
          placeholder="Add your note (optional)"></textarea>
          <p class="text-Web-pink " v-show="event.editEvent.eventNotes.length > 500 && !event.editValidate && event.editField">*Note can't be longer than 500 characters.</p>
      </div>
      <div class="col-span-2 h-6">
        <div class="grid place-items-end">
          <button
            class="rounded-2xl bg-Web-pink py-2 w-1/12 text-white font-bold hover:bg-Web-pink hover:text-white mx-14"
            v-show="!event.editField"
            @click="event.editField = true"
          >
            Edit
          </button>
        <div class="flex w-3/6 -mt-5" v-show="event.editField">
            <button
            class="rounded-2xl bg-white py-2 w-2/12 text-Web-pink border-2 border-Web-pink text-lg font-bold ml-88 my-6"
            @click="event.resetEditField()"
          >
            Cancle
          </button>
          <button
            class="rounded-2xl bg-Web-pink py-2 w-2/12 text-white text-lg font-bold ml-6 my-6"
            @click="event.saveEvent(params.id)"
          >
            Submit
          </button>
        </div>
      </div>
      </div>
      </div>

    <div class="grid grid-cols-2 h-26 mt-6 text-white text-2xl bg-white rounded-2xl">
        <p class="flex text-Web-violet font-bold my-8 mx-14">Booking time : 
          <p class="ml-2" v-show="!event.editTime">{{ formatDate(event.displayEvent.eventStartTime) }} || {{ formatTime(event.displayEvent.eventStartTime) }}</p>
          <Datepicker @closed="event.setMinTime(event.editEvent.eventStartTime)" :minDate="new Date()" 
           class="ml-6" v-show="event.editTime" v-model="event.editEvent.eventStartTime"></Datepicker>
        </p>
        <div v-show="!event.editTime" class="grid place-items-end">
        <button
            class="rounded-2xl bg-Web-pink py-2 w-1/6 text-white text-lg font-bold mx-14 my-6"
            @click="event.editTime = true"
          >
            Edit
          </button>
        </div>
        <div class="grid place-items-end" v-show="event.editTime">
          <div class="flex w-4/6">
            <button
            class="rounded-2xl bg-white py-2 w-1/4 text-Web-pink border-2 border-Web-pink text-lg font-bold ml-32 my-6"
            @click="event.resetEditTime()"
          >
            Cancle
          </button>
          <button
            class="rounded-2xl bg-Web-pink py-2 w-1/4 text-white text-lg font-bold ml-6 my-6"
            @click="event.saveEvent(params.id)"
          >
            Submit
          </button>
        </div>
          </div>
          <p class="col-span-2 text-Web-pink text-lg -mt-7 ml-60" v-show="event.editEvent.eventStartTime == 'previous time' && !event.editValidate">*Not be able to select the previous date and time.</p>
          <p class="col-span-2 text-Web-pink text-lg -mt-7 ml-60" v-show="event.editEvent.eventStartTime == 'overlap' && !event.editValidate">*This select time has already been used.</p>
  </div>

  <div class="grid grid-cols-2 h-26 mt-6 text-white text-2xl bg-white rounded-2xl">
        <p class="flex text-Web-violet font-bold my-8 mx-14">File :
        <a :href="event.fileUrl" v-show="!event.editFile && event.displayEvent.fileName != ''" download>
          <div class="border-2 border-Web-violet rounded-lg ml-6 text-Web-violet hover:bg-Web-violet hover:text-white text-center text-lg py-1 px-2 -mt-1">
            {{ event.displayEvent.fileName }}
          </div>
        </a>
        <p class="ml-2" v-show="event.displayEvent.fileName == '' && !event.editFile">No File</p>
        <label for="file" v-show="event.editFile">
                <div v-show="!event.showFileName" class="border-2 border-Web-violet rounded-lg ml-6 text-Web-violet hover:bg-Web-violet hover:text-white text-center text-lg py-1 px-2">
                <input id="file" type="file" @change="event.chooseEditFile"/>
                <p v-show="!(event.editFileName == '')">{{ event.editFileName }}</p><p v-show="event.editFileName == ''">Choose File</p>
                </div>
                <div v-show="event.showFileName" class="bg-Web-violet text-white rounded-lg ml-6 hover:bg-Web-violet/70 text-center text-lg py-1 px-2">
                    <input id="file" type="file" @change="event.chooseEditFile"/>
                    <p id="filename"></p>
                </div>
          </label>
          <div v-show="event.editFile && (event.showFileName || !(event.editFileName == ''))">
          <button class="ml-6 -mt-1" @click="event.resetEditFile(),event.deleteFileCheck = true"><svg width="40" height="40" viewBox="0 0 512 512" >
                  <path fill="none" d="M296 64h-80a7.91 7.91 0 0 0-8 8v24h96V72a7.91 7.91 0 0 0-8-8Z"/>
                  <path fill="#EB4C84" d="M432 96h-96V72a40 40 0 0 0-40-40h-80a40 40 0 0 0-40 40v24H80a16 16 0 0 0 0 32h17l19 304.92c1.42 26.85 22 47.08 48 47.08h184c26.13 0 46.3-19.78 48-47l19-305h17a16 16 0 0 0 0-32ZM192.57 416H192a16 16 0 0 1-16-15.43l-8-224a16 16 0 1 1 32-1.14l8 224A16 16 0 0 1 192.57 416ZM272 400a16 16 0 0 1-32 0V176a16 16 0 0 1 32 0Zm32-304h-96V72a7.91 7.91 0 0 1 8-8h80a7.91 7.91 0 0 1 8 8Zm32 304.57A16 16 0 0 1 320 416h-.58A16 16 0 0 1 304 399.43l8-224a16 16 0 1 1 32 1.14Z"/>
          </svg></button></div>
      </p>
      
      <div class="grid place-items-end" v-show="!event.editFile" >
        <button
            class="rounded-2xl bg-Web-pink py-2 w-1/6 text-white text-lg font-bold mx-14 my-7"
            @click="event.editFile = true"
          >
            Edit
          </button>
        </div>
        <div class="grid place-items-end" v-show="event.editFile" >
          <div class="flex w-4/6">
            <button
            class="rounded-2xl bg-white py-2 w-1/4 text-Web-pink border-2 border-Web-pink text-lg font-bold ml-32 my-6"
            @click="event.editFile = false,event.resetEditFile(),setEditFileName(),event.deleteFileCheck = false"
          >
            Cancle
          </button>
          <button
            class="rounded-2xl bg-Web-pink py-2 w-1/4 text-white text-lg font-bold ml-6 my-6"
            @click="event.editFileName == '' && event.deleteFileCheck == false ? event.saveEvent(params.id) : event.editFileName == '' && event.deleteFileCheck == true ? event.removeFile(params.id) : (event.editFile = false,event.showErrorFileText = false)"
          >
            Submit
          </button>
        </div>
          </div>     
          <p v-show="event.showErrorFileText" class="text-Web-pink text-lg pl-14 -mt-8">*The file size cannot be larger than 10 MB.</p>
  </div>
  </div>
</template>

<style></style>
