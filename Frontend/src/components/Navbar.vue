<script setup></script>

<template>
  <div class="fixed z-50 w-screen drop-shadow-md text-white appBg">
    <div class="grid grid-cols-3 gap-4 h-16">
      <div class="grid grid-cols-2 gap-4 place-items-center text-xl">
        <router-link :to="{ name: 'Schedule' }">Project Management</router-link>
        <router-link :to="{ name: 'Schedule' }">DevOps/Infra</router-link>
      </div>
      <div class="grid place-items-center text-xl">
        <router-link :to="{ name: 'Schedule' }"
          ><img src="../assets/images/logo.png" width="150"
        /></router-link>
      </div>
      <div class="grid grid-cols-3 gap-4 place-items-center text-xl">
        <router-link :to="{ name: 'Schedule' }">Database</router-link>
        <router-link :to="{ name: 'Schedule' }">Server-side</router-link>
        <router-link :to="{ name: 'Schedule' }">Client-side</router-link>
      </div>
    </div>
  </div>
</template>

<style></style>
