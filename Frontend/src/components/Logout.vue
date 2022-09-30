<script setup>
import { useLogin } from "../stores/login.js";

const login = useLogin();
</script>
 
<template>
 <div class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
    <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
      <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
        <!-- <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img"
          class="iconify iconify--ep animate-bounce pt-2" width="100" height="100" preserveAspectRatio="xMidYMid meet"
          viewBox="0 0 1024 1024">
          <path fill="#ffff"
            d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
          </path>
        </svg> -->
        <img
            src="../assets/images/Warning.png"
            width="100" height="100"
            class="iconify iconify--ep animate-bounce pt-2"
          />
      </div>
      <div class="grid text-4xl place-items-center">
        <p>Are you sure to logout?</p>
      </div>
      <div class="flex justify-center mb-6">
          <button
            class="text-4xl px-5 text-green-500 hover:text-green-700"
            @click="login.logout(),login.logoutPopup = false,$router.push('Login')"
          >
            Yes
          </button>
          <button class="text-4xl px-5 text-red-500 hover:text-red-700" @click="login.logoutPopup = false">
            No
          </button>
        </div>
    </div>
  </div>
</template>
 
<style>

</style>
<!-- <div
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
            class="text-4xl px-5 text-green-500 hover:text-green-700"
            @click="$emit('delete', deleteId, (popUp = false))"
          >
            Yes
          </button>
          <button class="text-4xl px-5 text-red-500 hover:text-red-700" @click="popUp = false">
            No
          </button>
        </div>
      </div>
    </div> -->