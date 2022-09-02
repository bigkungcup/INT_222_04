<script setup>
import { ref } from "vue";
import { onBeforeMount } from "vue";
import { useUser,useLogin } from "../stores/event.js";

const user = useUser();
const login = useLogin();

// const popUp = ref(false);
// // const matchText = ref(true);
// const matchPassword = ref(true);
// const matchEmail = ref(true);

// //Match
// const passwordMatch = async (userAccount) => {
//   const res = await fetch(`${import.meta.env.VITE_BASE_URL}/match`, {
//     method: "POST",
//     headers: { "content-type": "application/json" },
//     body: JSON.stringify({
//       email: userAccount.email,
//       password: userAccount.password,
//     }),
//   });
//   if (res.status === 200) {
//     // matchText.value = true;
//     matchPassword.value=true;
//     matchEmail.value=true;
//     popUp.value = true;
//     console.log("Password Matched");
//   } else if (res.status === 401) {
//     // matchText.value = false;
//     matchPassword.value=false;
//     matchEmail.value=true;
//     console.log("Password NOT Matched");
//   }else if (res.status === 404) {
//     // matchText.value = false;
//     matchEmail.value=false;
//     matchPassword.value=true;
//     console.log("A user with the specified email DOES NOT exist");
//   }
// };

const userAccount = ref({
  email: "",
  password: "",
});

const reset = () => {
  userAccount.value = {
    email: "",
    password: "",
  };
  // // matchText.value=true;
  //   matchPassword.value=true;
  //   matchEmail.value=true;
  login.matchPassword=true;
  login.matchEmail=true;
};

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
          <router-link :to="{ name: 'Register' }">
            <span class="text-rose-800 hover:text-rose-500"> Register </span>
          </router-link>
        </div>
        <div class="grid grid-cols-4 gap-y-8">
          <div class="place-self-center">
            <svg width="32" height="32" viewBox="0 0 24 24">
              <path
                fill="#9f1239"
                d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm-.4 4.25l-7.07 4.42c-.32.2-.74.2-1.06 0L4.4 8.25a.85.85 0 1 1 .9-1.44L12 11l6.7-4.19a.85.85 0 1 1 .9 1.44z"
              />
            </svg>
          </div>
          <div class="col-span-3">
            <input
              type="text"
              class="bg-white border border-slate-300 rounded-lg h-10 w-5/6 text-3xl placeholder:italic placeholder:text-2xl"
              placeholder=" Enter Your Email"
              v-model.trim="userAccount.email"
            />
            <!-- <div
              v-if="userAccount.email === '' || userAccount.email.value === 0"
            >
              <p v-show="user.textPopUp" class="text-lg text-red-500 absolute">
                *Please enter your email.
              </p>
            </div> -->
          </div>

          <div class="place-self-center">
            <svg width="32" height="32" viewBox="0 0 24 24">
              <path
                fill="#9f1239"
                d="M4 22V8h3V6q0-2.075 1.463-3.538Q9.925 1 12 1t3.538 1.462Q17 3.925 17 6v2h3v14ZM9 8h6V6q0-1.25-.875-2.125T12 3q-1.25 0-2.125.875T9 6Zm3 9q.825 0 1.413-.587Q14 15.825 14 15q0-.825-.587-1.413Q12.825 13 12 13q-.825 0-1.412.587Q10 14.175 10 15q0 .825.588 1.413Q11.175 17 12 17Z"
              />
            </svg>
          </div>
          <div class="col-span-3">
            <form>
            <input
              type="password"
              class="bg-white border border-slate-300 rounded-lg h-10 w-5/6 text-3xl placeholder:italic placeholder:text-2xl"
              placeholder=" Enter Your Password"
              autocomplete="off"
              v-model="userAccount.password"
            /></form>
            <div
              v-if="
                userAccount.password === '' || userAccount.password.value === 0
              "
            >
              <p v-show="user.textPopUp" class="text-lg text-red-500 absolute">
                *Please enter your password.
              </p>
            </div>
              <!-- <p v-show="!matchText" class="text-lg text-red-500 absolute">
                *Email or password did not match.
              </p> -->
                            <p v-show="!login.matchPassword" class="text-lg text-red-500 absolute">
                *Password NOT Matched
              </p>
                            <p v-show="!login.matchEmail" class="text-lg text-red-500 -ml-8 absolute">
                *A user with the specified email DOES NOT exist
              </p>
          </div>

          <div class="col-span-4 place-self-center">
            <button
              class="bg-rose-400 text-white rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-white hover:text-rose-800"
              @click="login.handleLogin(userAccount)"
            >
              Login
            </button>
            <button
              class="bg-white text-rose-800 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:bg-stone-200 hover:text-rose-400"
              @click="reset()"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>

    <div
      v-show="login.popUp"
      class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0"
    >
      <div
        class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl"
      >
        <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            aria-hidden="true"
            role="img"
            class="iconify iconify--ep animate-bounce pt-2"
            width="100"
            height="100"
            preserveAspectRatio="xMidYMid meet"
            viewBox="0 0 1024 1024"
          >
            <path
              fill="#ffff"
              d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z"
            ></path>
          </svg>
        </div>
        <div class="grid text-4xl place-items-center">
          <p>Login successfully</p>
        </div>
        <div class="grid place-items-center">
          <button
            class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2 hover:text-pink-700 hover:border-2 border-pink-700"
            @click="reset(), (login.popUp = false)"
          >
            OK
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style></style>
