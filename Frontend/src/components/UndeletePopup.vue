<script setup>
defineEmits(["toggle"]);
</script>
 
<template>
  <div class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
    <div class="grid grid-rows-3.5 bg-Bg-Plain bg-cover w-3/6 h-80 place-self-center rounded-3xl border-4 border-Web-pink">
      <div class="grid row-span-1.5 rounded-t-3xl place-items-center">
        <img src="../assets/images/Exclamation.png" class="animate-bounce mt-4" width="80" />
      </div>
      <div class="grid font-bold text-white text-4xl place-items-center">
        <p>You cannot delete this user account</p>
        <p>since the lecturer is the only owner of</p>
        <p>event category.</p>
      </div>
      <div class="grid place-items-center">
          <button
            class="text-4xl px-5 text-white rounded-3xl w-36 py-2 mx-2 bg-Web-pink hover:bg-white hover:text-Web-pink"
            @click="$emit('toggle')">
            OK
          </button>
      </div>
    </div>
  </div>
</template>
 
<style>

</style>