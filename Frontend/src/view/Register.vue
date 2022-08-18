<script setup>
import { ref } from "vue";
import {  onBeforeMount ,onUpdated } from "vue";
import { useUser } from "../stores/event.js";

const user = useUser();
const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

const newUser = ref({
  name: "",
  email: "",
  role: ""
})

const reset = () => {
  newUser.value = {
  name: "",
  email: "",
  role: ""
  };
  showNameSame.value=false;
  showEmailSame.value=false;
};

const showNameSame = ref(false)
const showEmailSame = ref(false)

const checkName = (newName) => {
    for (let i = 0; i < user.userAll.length; i++) {
        if (newName !== user.userAll[i].name) {
            showNameSame.value = true
        }
    }
}

const checkEmail = (newEmail) => {
    for (let i = 0; i < user.userAll.length; i++) {
        if (newEmail !== user.userAll[i].email) {
            showEmailSame.value = true
        }
    }
}

onBeforeMount(async () => {
  user.textPopUp = false;
});

onUpdated(async () => {
  await user.getUserAll();
});

</script>
 
<template>
    <div class="bg-rose-200 h-screen w-screen">
        <div class="grid place-items-center">
        <div class="bg-white/50 w-100 h-96 my-36 rounded-3xl">
            <div class="p-6 text-rose-800 text-3xl">
            Register
            </div>
            <div class="grid grid-cols-4 gap-y-8">
            <div class="place-self-center"><svg width="32" height="32" viewBox="0 0 24 24"><path fill="#9f1239" d="M20 22H4v-2a5 5 0 0 1 5-5h6a5 5 0 0 1 5 5v2zm-8-9a6 6 0 1 1 0-12a6 6 0 0 1 0 12z"/></svg></div>
            <div class="col-span-3"><input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-4/5 text-3xl 
        placeholder:italic placeholder:text-2xl " placeholder=" Enter Your Name" v-model.trim="newUser.name"
            /><span class="text-gray-500 text-lg ml-0.5">{{newUser.name.length}}/100</span>
        <div v-if="newUser.name === '' || newUser.name === null || newUser.name.value === 0">
        <p v-show="user.textPopUp" class="text-lg text-red-500 absolute top-62">*Please enter your name.</p>
      </div>
      <div v-if="newUser.name.length > 100">
        <p  class="text-lg text-red-500 absolute top-62">*Name can't be longer than 100 characters.</p>
    </div>
    <p v-if="showNameSame && newUser.name !== '' && newUser.name.length <= 100" class="text-lg text-red-500 absolute top-62">*User name is not unique.</p>
    </div>

            <div class="place-self-center"><svg width="32" height="32" viewBox="0 0 24 24"><path fill="#9f1239" d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm-.4 4.25l-7.07 4.42c-.32.2-.74.2-1.06 0L4.4 8.25a.85.85 0 1 1 .9-1.44L12 11l6.7-4.19a.85.85 0 1 1 .9 1.44z"/></svg></div>
            <div class="col-span-3"><input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-4/5 text-3xl 
        placeholder:italic placeholder:text-2xl " placeholder=" Enter Your Email" v-model.trim="newUser.email"
            /><span class="text-gray-500 text-lg ml-0.5">{{newUser.email.length}}/50</span>
      <div v-if="newUser.email === '' || newUser.email.value === 0">
        <p v-show="user.textPopUp" class="text-lg text-red-500 absolute top-83">*Please enter your email.</p>
      </div>
      <div v-if="newUser.email.match(validEmail) === null && newUser.email !== ''">
        <p v-show="user.textPopUp" class="text-lg text-red-500 absolute top-83">*Invalid Email.</p>
      </div>
      <div v-if="newUser.email.length > 50">
        <p  class="text-lg text-red-500 absolute top-83">*Email can't be longer than 50 characters.</p>
      </div>
      <p v-if="showEmailSame && newUser.email !== '' && newUser.email.match(validEmail) !== null && newUser.email.length <= 50" class="text-lg text-red-500 absolute top-83">*User email is not unique.</p>
            </div>

        <div class="col-span-4 place-self-center space-x-4 text-rose-800 text-2xl mt-2">
            <input type="radio" id="1" value="student" v-model="newUser.role" />
            <label for="1">Student</label>

            <input type="radio" id="2" value="lecturer" v-model="newUser.role" />
            <label for="2">Lecturer</label>

            <input type="radio" id="3" value="admin" v-model="newUser.role" />
            <label for="3">Admin</label>

        <div v-if="newUser.role === '' || newUser.role.value === 0">
        <p v-show="user.textPopUp" class="text-lg text-red-500 ml-10">*Please choose your role.</p>
      </div>
        </div>

            <div class="col-span-4 place-self-center absolute bottom-56">
                <button class="bg-rose-400 text-white rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-white hover:text-rose-800" @click="user.createUser(newUser),checkName(newUser.name),checkEmail(newUser.email)">
                Register
                </button>
                <button class="bg-white text-rose-800 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-stone-200 hover:text-rose-400" @click="reset()">
                Cancel
                </button>       
            </div>

            </div>
        </div>
        </div>
          <div v-show="user.popUp" class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
    <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
      <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img"
          class="iconify iconify--ep animate-bounce pt-2" width="100" height="100" preserveAspectRatio="xMidYMid meet"
          viewBox="0 0 1024 1024">
          <path fill="#ffff"
            d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
          </path>
        </svg>
      </div>
      <div class="grid text-4xl place-items-center">
        <p>Created successfully</p>
      </div>
      <div class="grid place-items-center">
        <button class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2 hover:text-pink-700 hover:border-2 border-pink-700" @click="reset(),user.disShowPopUp()">
          OK
        </button>
      </div>
    </div>
  </div>
    </div>
</template>
 
<style>

</style>