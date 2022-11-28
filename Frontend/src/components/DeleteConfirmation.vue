<script setup>
defineEmits(["toggle","delete"]);
defineProps({
    data: {
        type: String,
        require: true
    },
    checkEvent: {
        type: Boolean,
        require: true
    }
  })

</script>
 
<template>
  <div class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
    <div class="grid grid-rows-3.5 bg-Bg-Plain bg-cover w-2/6 h-80 place-self-center rounded-3xl border-4 border-Web-pink">
      <div class="grid row-span-1.5 rounded-t-3xl place-items-center">
        <img src="../assets/images/Exclamation.png" class="animate-pulse" width="80" />
      </div>
      <div class="grid font-bold text-white text-4xl place-items-center">
        <p v-show="checkEvent">This user has an event.</p>
        <p>Are you sure to delete</p>
        <p>this {{ data }}?</p>
      </div>
      <div class="flex place-content-center py-2">
          <button
            class="text-2xl px-5 text-Web-pink rounded-3xl h-12 w-36 py-2 mx-2 bg-white"
            @click="$emit('toggle')">
            No
          </button>
          <button
            class="text-2xl px-5 text-white rounded-3xl h-12 w-36 py-2 mx-2 bg-Web-pink"
            @click="$emit('delete')">
            Yes
          </button>
      </div>
    </div>
  </div>
</template>
 
<style>

</style>