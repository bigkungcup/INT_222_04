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
    <div v-for="list in currentCategory" class="relative hover-trigger ">
      <div class="grid grid-cols-5 bg-white/70 w-100 h-28 h-auto rounded-3xl z-0 ">
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
          class="grid col-span-4 text-center text-4xl mb-3 text-amber-800 place-content-center hover-target2"
        >
          {{ list.eventCategoryName }}
        </div>
          <div
          class="hover-target grid col-span-4 text-2xl mb-3 text-amber-800 place-content-center p-4"
        >
          {{ list.eventCategoryDescription }}
        </div>
      </div>
      
      <!-- <div class="absolute hover-target bg-white w-99 h-auto z-10 text-2xl p-4 mx-5  mt-5">
        {{ list.eventCategoryDescription }}
        </div> -->
    </div>
  </div>
</template>

<style>
.hover-trigger .hover-target {
    display: none;
}

.hover-trigger:hover .hover-target {
    display: block;
}

.hover-trigger:hover .hover-target2 {
    display: none;
}
</style>
