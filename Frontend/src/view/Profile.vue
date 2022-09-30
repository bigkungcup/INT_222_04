<script setup>
  import { ref, onBeforeMount } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { useLogin,useEventCategory} from "../stores/event.js"
import Logout from "../components/Logout.vue";
  
const myRouter = useRouter();
const goBack = () => {
    myRouter.go(-1);
  };

const login = useLogin();
const role = ref(login.getRoleToken());
const category = useEventCategory();

onBeforeMount(async () => {
  await category.getEventCategory();
});

</script>
 
<template>
<div class="bg-fixed bg-user bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen pt-12 px-36 pb-36">
    <div class="w-full h-full overflow-auto">
        <div class="grid" v-show="login.logoutPopup">
      <Logout/>
    </div>  
        <div class="grid text-4xl gap-y-6 break-all">
          <p class="text-8xl text-center">
            Profile
            <!-- <button @click="showPopUp()">
              <img src="../assets/images/Edit.png" width="40"
                class="transition duration-150 ease-in-out hover:scale-125" />
            </button> -->
            <button @click="goBack">
              <img src="../assets/images/Exit.png" width="60" class="absolute top-3 right-36" />
            </button>
          </p>
          
          <p class="mx-36">Name : </p>
 
  
  
          <p class="mx-36">Email : </p>
         
   
  
          <p class="mx-36">Role : {{ role.slice(1,-1) }} </p>
          <!-- <p class="mx-36" v-show="popUp">Role :
            <span class="space-x-4 text-rose-800 text-4xl mt-2 ml-6">
              <input type="radio" id="1" value="student" v-model="editUser.role"
                v-bind:checked="displayUser.role =='student'" />
              <label for="1">Student</label>
  
              <input type="radio" id="2" value="lecturer" v-model="editUser.role"
                v-bind:checked="displayUser.role =='lecturer'" />
              <label for="2">Lecturer</label>
  
              <input type="radio" id="3" value="admin" v-model="editUser.role"
                v-bind:checked="displayUser.role =='admin'" />
              <label for="3">Admin</label>
            </span>
          </p> -->
  
        
          <!-- <p class="mx-36"><form>Password : 
          <input type="password" 
          class="bg-white border border-slate-300 rounded-lg h-10 w-2/5 text-3xl placeholder:italic placeholder:text-2xl " 
          placeholder=" Enter password" 
          v-model="passwordCheck" 
          autocomplete="off" 
          @input="user.resetMatchText()"/>
          </form>
          <button class="text-2xl mx-2 text-white bgPopUp rounded-3xl w-28 py-2 hover:text-pink-700 hover:border-2 border-pink-700 absolute ml-100 -mt-11" @click="user.checkPassword(displayUser.email,passwordCheck)">
              Check
          </button>
            <p v-if="passwordCheck !== '' && user.passwordMatchText"
          class="text-lg text-green-700 pl-40">*Password match!!</p>
          <p v-if="passwordCheck !== '' && user.passwordNoMatchText"
          class="text-lg text-red-500 pl-40">*Password did not match!!</p>
      </p> -->

      <div class="mx-36">
      <p>Clinics : </p>
      <!-- <li v-for="item in items">
      {{ item.message }}
      </li> -->

      <select class="px-3 rounded-lg text-3xl mt-6" >
          <option v-for="list in category.categoryLists" :value="list">
            {{ list.eventCategoryName }}
          </option>
      </select>
      <svg class="mb-5" width="50" viewBox="0 0 24 24"><path fill="#ffffff" d="M11 17h2v-4h4v-2h-4V7h-2v4H7v2h4Zm1 5q-2.075 0-3.9-.788q-1.825-.787-3.175-2.137q-1.35-1.35-2.137-3.175Q2 14.075 2 12t.788-3.9q.787-1.825 2.137-3.175q1.35-1.35 3.175-2.138Q9.925 2 12 2t3.9.787q1.825.788 3.175 2.138q1.35 1.35 2.137 3.175Q22 9.925 22 12t-.788 3.9q-.787 1.825-2.137 3.175q-1.35 1.35-3.175 2.137Q14.075 22 12 22Z"/></svg>
    </div>

        </div>
      </div>
<!--   
      <div class="flex text-white justify-center text-2xl" v-show="popUp">
        <button class="bg-red-500 rounded-3xl w-36 py-2 mx-2" @click="popUp = false, reset()">
          Cancel
        </button>
        <button class="bg-green-500 rounded-3xl w-36 py-2 mx-2"
          @click="checkAll(displayUser,editUser)?(popUp = false, reset()):saveUser(displayUser, editUser), checkName(editUser.name),checkEmail(editUser.email)">
          Save
        </button>
      </div>
  
      <div v-show="editPopUp" class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
        <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
          <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true"
              role="img" class="iconify iconify--ep animate-bounce" width="100" height="100"
              preserveAspectRatio="xMidYMid meet" viewBox="0 0 1024 1024">
              <path fill="#ffff"
                d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
              </path>
            </svg>
          </div>
          <div class="grid text-4xl place-items-center">
            <p>Edit successfully</p>
          </div>
          <div class="grid place-items-center">
            <button
              class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2 hover:text-pink-700 hover:border-2 border-pink-700"
              @click="editPopUp = false, popUp = false">
              OK
            </button>
          </div> -->
        <!-- </div> -->
      <!-- </div> -->
    </div>
</template>
 
<style>

</style>