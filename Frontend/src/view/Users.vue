<script setup>
import { ref,onBeforeMount } from "vue";
import { useUsers } from "../stores/Users.js";
import UserList from "../components/UserList.vue";
import UserEmptyList from "../components/UserEmptyList.vue";

const user = useUsers();

onBeforeMount(async () => {
    await user.getUserList();
});
</script>

<template>
  <div
    class="grid bg-Bg bg-cover h-screen grid-rows-7 pt-24 px-32 overflow-auto no-scrollbar"
  >
    <div class="grid row-span-7 font-bold text-white text-2xl pt-6">
      <UserList
        :userList="user.userList"
        :deletePopUp="user.deletePopup"
        :checkEvent="user.checkEvent"
        :undeletePopup="user.undeletePopup"
        @delete="user.removeUser"
        @check="user.checkUserEvent"
      />

      <div class="grid grid-cols-3 py-6 place-items-center">
        <div>
          <button
            class="text-xl text-white rounded-3xl w-28 h-12 mx-2 bg-Web-pink hover:bg-white hover:text-Web-pink"
            v-show="user.userList.pageNumber > 0"
            @click="user.BackPage()"    
          >
            Back
          </button>
        </div>
        <span
          class="rounded-lg h-12 w-16 font-bold text-white text-xl text-center bg-black/30 border-4 border-Web-pink py-1"
          >{{ user.userList.pageNumber + 1 }}</span
        >
        <button
          class="text-xl text-white rounded-3xl w-28 h-12 mx-2 bg-Web-pink hover:bg-white hover:text-Web-pink"
          v-show="user.userList.pageNumber + 1 < user.userList.totalPages"
          @click="user.NextPage()"
        >
          Next
        </button>
      </div>
    </div>

      <div class="grid row-span-7" v-show="user.userList.numberOfElements == 0">
        <UserEmptyList />
    </div>
  </div>
</template>

<style></style>
