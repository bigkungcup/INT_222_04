<script setup>
import { onBeforeMount, onBeforeUpdate } from "vue";
import { useRoute } from "vue-router";
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
            <div class="flex space-x-3 text-white" v-show="user.editUserField">
            <div v-for="list in [{ role:'student' },{ role:'lecturer' },{ role:'admin' }]" class="grid">
              <button :class="['p-2 rounded-xl',list.role == user.editUser.role && list.role == user.displayUser.role ? 'bg-Web-pink'
                                                :list.role == user.editUser.role && list.role != user.displayUser.role ? 'bg-Web-pink'
                                                :list.role != user.editUser.role && list.role == user.displayUser.role ? 'bg-Web-violet' : 'bg-Web-violet'  ]" @click="user.editUser.role = list.role">{{list.role}}</button>
            </div>
          </div>

            <p class="mb-2" v-show="!user.editUserField">{{ user.displayUser.name }}</p>
            <div class="flex mt-6" v-show="user.editUserField">
            <p class="text-Web-violet font-bold mr-3">Name :</p>
            <input
              type="text"
              class="padding-input-detail bg-Web-violet rounded-lg h-10 w-9/12 text-1xl text-white place-self-center"
              v-model="user.editUser.name"
              placeholder="Enter Your Name"
            />
          </div>
          <p class="text-Web-pink text-lg text-light pt-3 ml-24" v-show="user.editUser.name.length > 100 && !user.editValidate">*Name can't be longer than 100 characters.</p>

            <p class="font-light mb-2" v-show="!user.editUserField">{{ user.displayUser.email }}</p>
            <div class="flex mt-6" v-show="user.editUserField">
            <p class="text-Web-violet font-bold mr-3">Email :</p>
            <input
              type="text"
              class="padding-input-detail bg-Web-violet rounded-lg h-10 w-9/12 text-1xl text-white place-self-center"
              v-model="user.editUser.email"
              placeholder="Enter Your Email"
            />
          </div>
          <p class="text-Web-pink text-lg text-light pt-3 ml-24" v-show="user.editUser.email.match(user.validEmail) && user.editUser.email.length > 50 && !user.editValidate">*Email can't be longer than 50 characters.</p>
          <p class="text-Web-pink text-lg text-light pt-3 ml-24" v-show="!(user.editUser.email.match(user.validEmail)) && user.editUser.email != '' && !user.editValidate">*Please enter a valid email address.</p>
          <p class="text-Web-pink text-lg text-light pt-3 ml-24" v-show="!user.userUnique">*Username or email already exists.</p>

            <div class="mb-2" v-show="user.displayUser.role == 'lecturer'">
              <div v-show="!user.editUserField">
              <p class="mr-3">Clinics :</p>
              <ul class="no-bullets font-light">
              <li v-for="clinic in user.displayUser.eventCategories" class="ml-28" v-show="!user.editUserField">
                <div class="flex">
              <p class="mt-1">
                {{ clinic.eventCategoryName }} 
              </p>         
          </div>
          </li>
        </ul></div>

          <div class="flex mt-6" v-show="user.editUserField">
          <p class="mr-3">Clinics :</p>
            <select class="rounded-lg h-10 w-9/12 font-bold text-white text-xl bg-Web-violet padding-select mr-1" v-model="user.newUserClinic">
            <option v-for="list in user.clinicList" :value="list.id">
              {{ list.eventCategoryName }}            
            </option>
          </select>
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
  list-style-type: none;
  padding: 0;
  margin: 0; 
}
</style>