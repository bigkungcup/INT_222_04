<script setup>
  import { ref, onBeforeMount } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { formatDate, formatTime } from "../main.js";
  import { useUser,useLogin } from "../stores/event.js"
  import Logout from "../components/Logout.vue";
  const { params } = useRoute();
  
  const user = useUser()
  const login = useLogin()
  const passwordCheck = ref("")
  
  //get user
  const getUser = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${params.id}`,
      {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem('jwt')}`
        }
      })
    if (res.status === 200) {
      displayUser.value = await res.json();
      console.log("get successfully");
    } else console.log("error, cannot get user");
  };
  
  //edit event
  const saveUser = async (displayUser, editUser) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${params.id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem('jwt')}`
      },
      body: JSON.stringify({
        name: editUser.name === "" ? displayUser.name : editUser.name,
        email: editUser.email === "" ? displayUser.email : editUser.email,
        role: editUser.role === "" ? displayUser.role : editUser.role
      }),
    });
    if (res.status === 200) {
      let userEdit = await res.json();
      console.log(userEdit);
      showEditPopUp()
      popUp.value = false
      reset()
      getUser()
      console.log("edit successfully");
    } else {
      console.log("error, cannot edit");
      user.showText();
    }
  };
  
  const displayUser = ref({
    name: "",
    email: "",
    role: ""
  });
  
  const editUser = ref({
    name: "",
    email: "",
    role: ""
  });
  
  const myRouter = useRouter();
  
  const popUp = ref(false);
  const showPopUp = () => {
    popUp.value = true;
    console.log(popUp.value);
  };
  
  const editPopUp = ref(false);
  const showEditPopUp = () => {
    editPopUp.value = true;
    console.log(editPopUp.value);
  };
  
  const goBack = () => {
    myRouter.go(-1);
    // window.localStorage.clear();
  };
  
  const reset = () => {
    editUser.value = {
      name: "",
      email: "",
      role: ""
    };
    showNameSame.value = false;
    showEmailSame.value = false;
  };
  
  const showNameSame = ref(false)
  const showEmailSame = ref(false)
  
  const checkName = (newName) => {
    showNameSame.value = false
    for (let i = 0; i < user.userAll.length; i++) {
      // if(newName !== displayUser.value.name){
      if (newName === user.userAll[i].name) {
        showNameSame.value = true;
        break;
      }
    }
    // }
  }
  
  const checkEmail = (newEmail) => {
    showEmailSame.value = false
    for (let i = 0; i < user.userAll.length; i++) {
      // if(newEmail !== displayUser.value.email){
      if (newEmail === user.userAll[i].email) {
        showEmailSame.value = true;
        break;
      }
      // }
    }
  }
  
  const checkAll = (displayUser, editUser) => {
    let check = false;
    if (editUser.name === "" && editUser.email === "" && editUser.role === "") {
      check = true;
    }
    else if ((editUser.name === displayUser.name || editUser.name === "") && (editUser.email === displayUser.email || editUser.email === "") && editUser.role === "") {
      check = true;
    }
  
    return check;
  }
  
  
  const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  
  onBeforeMount(async () => {
    await getUser();
    await user.getUserAll();
    user.textPopUp = false;
  });
  
  </script>
   
  <template>
    <div class="bg-fixed bg-user bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen pt-12 px-36 pb-36">
      <div class="grid" v-show="login.logoutPopup">
        <Logout/>
      </div>
      <div class="w-full h-full overflow-auto">
        <div class="grid text-4xl gap-y-8 break-all">
          <p class="text-8xl text-center">
            Detail
            <button @click="showPopUp()">
              <img src="../assets/images/Edit.png" width="40"
                class="transition duration-150 ease-in-out hover:scale-125" />
            </button>
            <button @click="goBack">
              <img src="../assets/images/Exit.png" width="60" class="absolute top-3 right-36" />
            </button>
  
          </p>
          <p class="mx-36" v-show="!popUp">Name : {{ displayUser.name }}</p>
          <p class="mx-36" v-show="popUp">Name : <input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-3/5 text-3xl 
          placeholder:italic placeholder:text-2xl " placeholder=" Enter your name" @input="showNameSame=false"
              v-model.trim="editUser.name" /><span
              class="text-gray-500 text-lg ml-0.5">{{editUser.name.length}}/100</span>
            <!-- <div v-if="editUser.name === '' || editUser.name === null || editUser.name.value === 0">
          <p v-show="user.textPopUp" class="text-lg text-red-500 absolute">*Please enter your name.</p>
        </div> -->
          <div v-if="editUser.name.length > 100">
            <p class="text-lg text-red-500 absolute">*Name can't be longer than 100 characters.</p>
          </div>
          <p v-if="showNameSame && editUser.name !== '' && editUser.name.length <= 100"
            class="text-lg text-red-500 absolute">*User name is not unique.</p>
          </p>
  
  
          <p class="mx-36" v-show="!popUp">Email : {{ displayUser.email }}</p>
          <p class="mx-36" v-show="popUp">Email : <input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-3/5 text-3xl 
          placeholder:italic placeholder:text-2xl " placeholder=" Enter your email" @input="showEmailSame=false"
              v-model.trim="editUser.email" /><span
              class="text-gray-500 text-lg ml-0.5">{{editUser.email.length}}/50</span>
            <!-- <div v-if="editUser.email === '' || editUser.email.value === 0">
          <p v-show="user.textPopUp" class="text-lg text-red-500 absolute">*Please enter your email.</p>
        </div> -->
          <div v-if="editUser.email.match(validEmail) === null && editUser.email !== ''">
            <p v-show="user.textPopUp" class="text-lg text-red-500 absolute">*Invalid Email.</p>
          </div>
          <div v-if="editUser.email.length > 50">
            <p class="text-lg text-red-500 absolute">*Email can't be longer than 50 characters.</p>
          </div>
          <p v-if="showEmailSame && editUser.email !== '' && editUser.email.match(validEmail) !== null && editUser.email.length <= 50"
            class="text-lg text-red-500 absolute">*User email is not unique.</p>
          </p>
  
          <p class="mx-36" v-show="!popUp">Role : {{ displayUser.role }}</p>
          <p class="mx-36" v-show="popUp">Role :
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
          </p>
  
          <p class="mx-36">Created on : {{formatDate(displayUser.createdOn)}} | {{ formatTime(displayUser.createdOn) }}
          </p>
          <p class="mx-36">Updated on : {{formatDate(displayUser.updatedOn)}} | {{ formatTime(displayUser.updatedOn) }}
          </p>
          <form><p class="mx-36">Password : <input type="password" class="bg-white border border-slate-300 rounded-lg h-10 w-2/5 text-3xl 
        placeholder:italic placeholder:text-2xl " placeholder=" Enter password" v-model="passwordCheck" autocomplete="off" @input="user.resetMatchText()"/>
               <button class="text-2xl mx-2 text-white bgPopUp rounded-3xl w-28 py-2 hover:text-pink-700 hover:border-2 border-pink-700" @click="user.checkPassword(displayUser.email,passwordCheck)">
              Check
            </button>
            <p v-if="passwordCheck !== '' && user.passwordMatchText"
          class="text-lg text-green-700 pl-40">*Password match!!</p>
          <p v-if="passwordCheck !== '' && user.passwordNoMatchText"
          class="text-lg text-red-500 pl-40">*Password did not match!!</p>
      </p></form>

        </div>
      </div>
  
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
          </div>
        </div>
      </div>
    </div>
  </template>
   
  <style>
  </style>