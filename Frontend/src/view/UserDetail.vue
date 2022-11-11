<script setup>
import { ref, onBeforeMount, onBeforeUpdate } from "vue";
import { useRoute, useRouter } from "vue-router";
import { formatDate, formatTime } from "../main.js";
import { useUsers } from "../stores/Users";
import { useClinics } from "../stores/Clinics";
import EditSuccessfully from "../components/EditSuccessfully.vue"

const { params } = useRoute();
const user = useUsers();
const clinic = useClinics();

onBeforeMount(async () => {
  await user.getUserDetail(params.id);
  await clinic.getClinics();
  user.editUserField = false;
  user.clinicList = clinic.filterClinics(user.clinicList,user.displayUser)
  user.editUser.role = user.displayUser.role;
});

onBeforeUpdate(async () => {
  user.clinicList = clinic.filterClinics(user.clinicList,user.displayUser)
});

</script>
 
<template>
  <div class="bg-Bg bg-cover h-screen pt-24 px-32 overflow-auto no-scrollbar">
    <div class="grid grid-cols-2 bg-white rounded-2xl h-144 mt-6">
        <div class="py-24 px-28"><svg width="360" viewBox="0 0 496 512"><path fill="#502763" d="M248 8C111 8 0 119 0 256s111 248 248 248s248-111 248-248S385 8 248 8zm0 96c48.6 0 88 39.4 88 88s-39.4 88-88 88s-88-39.4-88-88s39.4-88 88-88zm0 344c-58.7 0-111.3-26.6-146.5-68.2c18.8-35.4 55.6-59.8 98.5-59.8c2.4 0 4.8.4 7.1 1.1c13 4.2 26.6 6.9 40.9 6.9c14.3 0 28-2.7 40.9-6.9c2.3-.7 4.7-1.1 7.1-1.1c42.9 0 79.7 24.4 98.5 59.8C359.3 421.4 306.7 448 248 448z"/></svg></div>
        <div class="grid grid-rows-7 pt-8 pb-2 text-2xl font-bold text-Web-violet">
            <div class="grid row-span-6 content-center">

            <div class="mb-2" v-show="!user.editUserField"><span class="bg-Web-pink p-2 text-white rounded-xl -ml-2">{{ user.displayUser.role }}</span></div>
            <div class="flex space-x-3 text-white mb-6" v-show="user.editUserField">
            <div v-for="list in [{ role:'student' },{ role:'lecturer' },{ role:'admin' }]" class="grid">
              <button :class="['p-2 rounded-xl',list.role == user.editUser.role && list.role == user.displayUser.role ? 'bg-Web-pink'
                                                :list.role == user.editUser.role && list.role != user.displayUser.role ? 'bg-Web-pink'
                                                :list.role != user.editUser.role && list.role == user.displayUser.role ? 'bg-Web-violet' : 'bg-Web-violet'  ]" @click="user.editUser.role = list.role">{{list.role}}</button>
            </div>
          </div>

            <p class="mb-2" v-show="!user.editUserField">{{ user.displayUser.name }}</p>
            <div class="flex mb-6" v-show="user.editUserField">
            <p class="text-Web-violet font-bold mr-3">Name :</p>
            <input
              type="text"
              class="padding-input-detail bg-Web-violet rounded-lg h-10 w-9/12 text-1xl text-white place-self-center"
              v-model="user.editUser.name"
              placeholder="Enter Your Name"
            />
          </div>

            <p class="font-light mb-2" v-show="!user.editUserField">{{ user.displayUser.email }}</p>
            <div class="flex mb-6" v-show="user.editUserField">
            <p class="text-Web-violet font-bold mr-3">Email :</p>
            <input
              type="text"
              class="padding-input-detail bg-Web-violet rounded-lg h-10 w-9/12 text-1xl text-white place-self-center"
              v-model="user.editUser.email"
              placeholder="Enter Your Email"
            />
          </div>

            <div class="mb-2" v-show="user.displayUser.role == 'lecturer'">
              <div v-show="!user.editUserField">
              <p class="mr-3">Clinics :</p>
              <ul class="no-bullets font-light">
              <li v-for="clinic in user.displayUser.eventCategories" class="ml-28" v-show="!user.editUserField">
                <div class="flex">
                  <!-- <button @click="user.deleteLecturerClinic(user.displayUser.id,clinic.id)" class="mr-4">
                <svg width="40" viewBox="0 0 24 24"><path fill="#EB4C84" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10s10-4.48 10-10S17.52 2 12 2zm5 11H7v-2h10v2z"/></svg>
              </button> -->
              <p class="mt-1">
                {{ clinic.eventCategoryName }}     
              </p>         
          </div>
          </li>
        </ul></div>

          <div class="flex" v-show="user.editUserField">
          <p class="mr-3">Clinics :</p>
            <select class="rounded-lg h-10 w-9/12 font-bold text-white text-xl bg-Web-violet padding-select mr-1" v-model="user.newUserClinic">
            <option v-for="list in user.clinicList" :value="list.id">
              {{ list.eventCategoryName }}            
            </option>
          </select>
          <!-- <button @click="user.addLecturerClinic(user.displayUser.id,user.newUserClinic)" class="">
          <svg width="50" viewBox="0 0 24 24" class="-mt-1">
            <path
              fill="#EB4C84"
              d="M11 17h2v-4h4v-2h-4V7h-2v4H7v2h4Zm1 5q-2.075 0-3.9-.788q-1.825-.787-3.175-2.137q-1.35-1.35-2.137-3.175Q2 14.075 2 12t.788-3.9q.787-1.825 2.137-3.175q1.35-1.35 3.175-2.138Q9.925 2 12 2t3.9.787q1.825.788 3.175 2.138q1.35 1.35 2.137 3.175Q22 9.925 22 12t-.788 3.9q-.787 1.825-2.137 3.175q-1.35 1.35-3.175 2.137Q14.075 22 12 22Z"
            />
          </svg>
        </button> -->
      </div>
      </div>

            <p class="flex mb-2" v-show="!user.editUserField">Created on : <p class="font-light">{{formatDate(user.displayUser.createdOn)}} | {{ formatTime(user.displayUser.createdOn) }}</p></p>
            <p class="flex" v-show="!user.editUserField">Updated on : <p class="font-light">{{formatDate(user.displayUser.updatedOn)}} | {{ formatTime(user.displayUser.updatedOn) }}</p></p>
            </div>
            <div class="grid place-items-end">
                <button
                class="rounded-2xl bg-Web-pink py-2 w-1/6 text-white text-lg font-bold mx-14" @click="user.editUserField = !user.editUserField" v-show="!user.editUserField">
                    Edit
                </button>
                <div class="flex" v-show="user.editUserField">
                    <button class="rounded-2xl bg-white w-24 h-12 text-Web-pink border-2 border-Web-pink text-lg font-bold mr-7" @click="user.editUserField = false,user.resetEditUser()">
                    Cancle
                    </button>
                    <button class="rounded-2xl bg-Web-pink w-24 text-white text-lg font-bold mr-7" 
                    @click="user.checkBeforeEdit()?user.resetEditUser():user.saveUser(user.displayUser.id)">
                    Save
                    </button>
                </div>
          </div>
        </div>
    </div>
    <div v-show="user.editUserSuccessfully">
      <EditSuccessfully @toggle="user.editUserSuccessfully = false" @refresh="user.getUserDetail(params.id)" @reset="user.resetEditUser"/>
    </div>
  </div>
</template>
 
<style>
ul.no-bullets {
  list-style-type: none; /* Remove bullets */
  padding: 0; /* Remove padding */
  margin: 0; /* Remove margins */
}
</style>