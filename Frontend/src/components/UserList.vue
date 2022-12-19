<script setup>
import { ref } from "vue";
import DeleteConfirmation from "./DeleteConfirmation.vue"
import UndeletePopup from "./UndeletePopup.vue";
defineEmits(["delete","check"]);

defineProps({
    userList: {
        type: Object,
        require: true
    },
    deletePopUp: {
        type: Boolean,
        require: true
    },
    // userId: {
    //     type: Number,
    //     require: true
    // },
    checkEvent: {
      type: Boolean,
        require: true
    },
    undeletePopup:{
      type: Boolean,
      require: true
    }
});

const userId = ref();
</script>
 
<template>
<div class="grid grid-rows-6 space-y-4">
    <div class="bg-white p-4 rounded-2xl" v-for="list in userList.content">
        <div class="grid grid-cols-4 font-bold text-black text-xl">
            <p class="grid place-content-center">{{ list.name }}</p>
            <p class="grid place-content-center font-light">{{ list.email }}</p>
            <p class="grid place-content-center text-Web-pink">{{ list.role }}</p>
            <div class="flex justify-center space-x-16">
          <router-link
              :to="{ name: 'UserDetail', params: { id: list.id } }">
            <svg width="50" height="50" viewBox="0 0 24 24"><g fill="none"><path d="M0 0h24v24H0z"/><path fill="#3C2D75" d="M12 2c5.523 0 10 4.477 10 10s-4.477 10-10 10S2 17.523 2 12S6.477 2 12 2Zm0 2a8 8 0 1 0 0 16a8 8 0 0 0 0-16Zm-4.5 6.5a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3Zm4.5 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3Zm4.5 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3Z"/></g></svg>
          </router-link>
            <button @click="$emit('check',list.id),userId = list.id,deletePopUp = true"><svg width="50" height="50" viewBox="0 0 512 512" >
              <path fill="none" d="M296 64h-80a7.91 7.91 0 0 0-8 8v24h96V72a7.91 7.91 0 0 0-8-8Z"/>
              <path fill="#EB4C84" d="M432 96h-96V72a40 40 0 0 0-40-40h-80a40 40 0 0 0-40 40v24H80a16 16 0 0 0 0 32h17l19 304.92c1.42 26.85 22 47.08 48 47.08h184c26.13 0 46.3-19.78 48-47l19-305h17a16 16 0 0 0 0-32ZM192.57 416H192a16 16 0 0 1-16-15.43l-8-224a16 16 0 1 1 32-1.14l8 224A16 16 0 0 1 192.57 416ZM272 400a16 16 0 0 1-32 0V176a16 16 0 0 1 32 0Zm32-304h-96V72a7.91 7.91 0 0 1 8-8h80a7.91 7.91 0 0 1 8 8Zm32 304.57A16 16 0 0 1 320 416h-.58A16 16 0 0 1 304 399.43l8-224a16 16 0 1 1 32 1.14Z"/>
            </svg></button>
        </div>
        </div>
    </div>
    <div v-show="deletePopUp">
      <DeleteConfirmation @toggle="deletePopUp = false" @delete="$emit('delete',userId)" :data="'user'" :checkEvent="checkEvent"/>
    </div>
    <div v-show="undeletePopup">
      <UndeletePopup @toggle="undeletePopup = false,deletePopUp = false"/>
    </div>
</div>
</template>
 
<style>

</style>