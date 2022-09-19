<script setup>

</script>
 
<template>
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
    </div>
</template>
 
<style>

</style>