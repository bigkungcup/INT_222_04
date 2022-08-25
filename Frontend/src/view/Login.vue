<script setup>
import { ref } from "vue";
import {  onBeforeMount ,onUpdated } from "vue";
import { useUser } from "../stores/event.js";

const user = useUser();
const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

const userAccount = ref({
  email: "",
  password: ""
})

const reset = () => {
  userAccount.value = {
  email: "",
  password: ""
  };
  showNameSame.value=false;
  showEmailSame.value=false;
};

const showNameSame = ref(false)
const showEmailSame = ref(false)

const checkName = (newName) => {
    for (let i = 0; i < user.userAll.length; i++) {
        if (newName.lowercase === user.userAll[i].name.lowercase) {
            showNameSame.value = true;
            break;
        } 
    }
}

const checkEmail = (newEmail) => {
    for (let i = 0; i < user.userAll.length; i++) {
        if (newEmail.lowercase === user.userAll[i].email.lowercase) {
            showEmailSame.value = true;
            break;
        }
    }
}

onBeforeMount(async () => {
  user.textPopUp = false;
});

// onUpdated(async () => {
//   await user.getUserAll();
// });

</script>
 
<template>
    <div class="bg-rose-200 h-screen w-screen">
        <div class="grid place-items-center">
        <div class="bg-white/50 place-self-center my-24 pb-10 rounded-3xl">
            <div class="pt-4 pb-6 px-8 text-3xl text-rose-800">
            <span class="text-rose-500"> Login </span> | 
            <router-link :to="{ name: 'Register'}">
            <span class="text-rose-800 hover:text-rose-500"> Register </span>
            </router-link>
            </div>
            <div class="grid grid-cols-4 gap-y-8">
            

            <div class="place-self-center"><svg width="32" height="32" viewBox="0 0 24 24"><path fill="#9f1239" d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm-.4 4.25l-7.07 4.42c-.32.2-.74.2-1.06 0L4.4 8.25a.85.85 0 1 1 .9-1.44L12 11l6.7-4.19a.85.85 0 1 1 .9 1.44z"/></svg></div>
            <div class="col-span-3"><input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-5/6 text-3xl 
        placeholder:italic placeholder:text-2xl " placeholder=" Enter Your Email" v-model.trim="userAccount.email" @input="showEmailSame=false"
            />
      <div v-if="userAccount.email === '' || userAccount.email.value === 0">
        <p v-show="user.textPopUp" class="text-lg text-red-500 absolute">*Please enter your email.</p>
      </div>

            </div>

        <div class="place-self-center"><svg width="32" height="32" viewBox="0 0 24 24"><path fill="#9f1239" d="M4 22V8h3V6q0-2.075 1.463-3.538Q9.925 1 12 1t3.538 1.462Q17 3.925 17 6v2h3v14ZM9 8h6V6q0-1.25-.875-2.125T12 3q-1.25 0-2.125.875T9 6Zm3 9q.825 0 1.413-.587Q14 15.825 14 15q0-.825-.587-1.413Q12.825 13 12 13q-.825 0-1.412.587Q10 14.175 10 15q0 .825.588 1.413Q11.175 17 12 17Z"/></svg></div>
            <div class="col-span-3"><input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-5/6 text-3xl 
        placeholder:italic placeholder:text-2xl " placeholder=" Enter Your Password" 
            />
                    <div v-if="userAccount.password === '' || userAccount.password.value === 0">
        <p v-show="user.textPopUp" class="text-lg text-red-500 absolute">*Please enter your password.</p>
      </div>
            </div>
        

             


            <div class="col-span-4 place-self-center">
                <button class="bg-rose-400 text-white rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-white hover:text-rose-800" @click="user.createUser(userAccount),checkName(userAccount.name),checkEmail(userAccount.email)">
                Login
                </button>
                <button class="bg-white text-rose-800 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-stone-200 hover:text-rose-400" @click="reset()">
                Cancel
                </button>       
            </div>

            </div>
        </div>
        </div>
          
    </div>
</template>
 
<style>

</style>