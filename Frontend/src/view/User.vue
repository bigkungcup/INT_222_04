<script setup>
import { onBeforeMount,onBeforeUpdate } from "vue";
import UserList from "../components/UserList.vue";
import UserEmptyList from "../components/UserEmptyList.vue";
import { useUser } from "../stores/event.js"

const user = useUser()

onBeforeMount(async () => {
  await user.getUserList();
  user.page = user.userList.pageNumber;
});

onBeforeUpdate(async () => {
  await user.getEmptyUser();
});

</script>
 
<template>
    <div class="bg-fixed bg-schedule bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen">
    <div class="grid" v-show="!user.showEmptyUser">
      <UserList :currentUser="user.userList.content" @next="user.NextPage"
        @back="user.BackPage" :page="user.page"/>
    </div>
    <div class="grid" v-show="user.showEmptyUser">
      <UserEmptyList />
    </div>
    </div>
</template>
 
<style>

</style>