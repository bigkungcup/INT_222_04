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
    <div v-for="list in currentCategory" class="relative hover-trigger">
      <div class="grid grid-cols-5 bg-white/70 w-100 h-28 h-auto rounded-3xl z-0">
        <div :class="[
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
        ]">
          <img src="../assets/images/Star.png" class="p-4 mt-2" />
        </div>
        <div class="grid col-span-4 text-center text-4xl mb-3 text-amber-800 place-content-center hover-target2">
          {{ list.eventCategoryName }}
        </div>
        <div
          class="hover-target grid col-span-4 text-xl mb-3 text-amber-800 place-content-center p-6 hover:scale-110 transition ease-in duration-300">
          {{ list.eventCategoryDescription }}
        </div>
        <router-link :to="{ name: 'CategoryDetail', params: { id: list.id } }" class="absolute right-2 bottom-2">
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true"
              role="img" class="iconify iconify--ic" width="32" height="32" preserveAspectRatio="xMidYMid meet"
              viewBox="0 0 24 24">
              <path fill="#92400e" class="hover:fill-amber-500"
                d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04a.996.996 0 0 0 0-1.41l-2.34-2.34a.996.996 0 0 0-1.41 0l-1.83 1.83l3.75 3.75l1.83-1.83z">
              </path>
            </svg>
        </router-link>
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
