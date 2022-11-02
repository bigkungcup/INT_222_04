<script setup>
defineEmits(["logout","toggle"]);

</script>
 
<template>
 <div class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
    <div class="grid grid-rows-3.5 bg-Bg-Plain bg-cover w-2/6 h-80 place-self-center rounded-3xl border-4 border-Web-pink">
      <div class="grid row-span-1.5 rounded-t-3xl place-items-center">
        <img
            src="../assets/images/Exclamation.png"
            width="100" height="100"
            class="animate-pulse"
          />
      </div>
      <div class="grid font-bold text-white text-4xl place-items-center">
        <p>Are you sure to logout?</p>
      </div>
      <div class="flex justify-center my-4">
        <button class="text-2xl text-Web-pink rounded-3xl h-16 w-28  mx-2 bg-white" @click="$emit('toggle')">
            No
        </button>
          <button
            class="text-2xl text-white rounded-3xl h-16 w-28  mx-2 bg-Web-pink"
            @click="$emit('logout'),$router.push('Login')"
          >
            Yes
          </button>
        </div>
    </div>
  </div>
</template>
 
<style></style>