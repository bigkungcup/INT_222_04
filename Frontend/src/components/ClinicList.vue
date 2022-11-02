<script setup>
defineProps({
  currentClinics: {
    type: Array,
    require: true,
  },
});

</script>

<template>
  <div class="grid grid-cols-5 mx-28">
    <div v-for="list in currentClinics" class="grid font-bold text-white text-xl pt-28 h-screen hover-trigger">
        <div :class="['w-56 h-full hover:bg-white',
        list.id == 1
            ? `bg-gradient-to-b from-bg-projectManagement/70 to-bg-projectManagement/0`
            : list.id == 2
              ? `bg-gradient-to-b from-bg-devopInfra/70 to-bg-devopInfra/0`
              : list.id == 3
                ? `bg-gradient-to-b from-bg-database/70 to-bg-database/0`
                : list.id == 4
                  ? `bg-gradient-to-b from-bg-clientSide/70 to-bg-clientSide/0`
                  : `bg-gradient-to-b from-bg-serverSide/70 to-bg-serverSide/0`]" 
        >
        <div class="text-center pt-12">
              {{ list.eventCategoryName }}
        </div>
      </div>
      <div class="absolute pt-32 bottom-0 hover-target">
      <div :class="['grid grid-rows-6 pt-6 px-4 bg-white/80 w-56 h-100 rounded-t-xl',
        list.id == 1
            ? `text-bg-projectManagement`
            : list.id == 2
              ? `text-bg-devopInfra`
              : list.id == 3
                ? `text-bg-database`
                : list.id == 4
                  ? `text-bg-clientSide`
                  : `text-bg-serverSide` ]" >
        <div class="row-span-5">
        {{ list.eventCategoryDescription }}
      </div>
      <div class="text-right">
        {{ list.eventDuration }} min.
      </div>
      <img src="/favicon.png" class="grid place-self-center absolute opacity-20" width="100" />
      </div></div>
        </div>
  </div>
</template>

<style>
.hover-trigger .hover-target {
  position: fixed;
  transition: bottom 0.4s ease-in-out;
  -o-transition: bottom 0.4s ease-in-out;
  -ms-transition: bottom 0.4s ease-in-out;
  -moz-transition: bottom 0.4s ease-in-out;
  -webkit-transition: bottom 0.4s ease-in-out;
  bottom: -600px;
}

.hover-trigger:hover .hover-target {
  bottom: 0px;
}
</style>
