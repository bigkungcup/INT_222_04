<script setup>
defineProps({
  currentCategory: {
    type: Array,
    require: true,
  },
});
</script>

<template>
  <div class="grid grid-cols-2 place-items-center gap-9 pt-20 px-52">
    <div v-for="list in currentCategory">
      <div class="grid grid-cols-5 bg-white/70 w-100 h-28 rounded-3xl">
        <div
          :class="[
            `w-full h-full rounded-l-3xl`,
            list.id == 1
              ? `projectManagement`
              : list.id == 2
              ? `devopInfra`
              : list.id == 3
              ? `database`
              : list.id == 4
              ? `clientSide`
              : `serverSide`,
          ]"
        >
          <img src="../assets/images/Star.png" class="p-4 mt-2" />
        </div>
        <div
          class="grid col-span-4 text-center text-4xl mb-3 text-amber-800 place-content-center"
        >
          {{ list.eventCategoryName }}
        </div>
      </div>
    </div>
  </div>
</template>

<style></style>
