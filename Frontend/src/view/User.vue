<script setup>
import { onBeforeMount,onBeforeUpdate, ref } from "vue";
import UserList from "../components/UserList.vue";
import UserEmptyList from "../components/UserEmptyList.vue";
import NoAuthentication from "../components/NoAuthentication.vue"
import { useUser } from "../stores/event.js"

const user = useUser()

  //Delete User
  const removeUser = async (userId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users/${userId}`,
      {
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      user.userList.content = user.userList.content.filter(
        (user) => user.id !== userId
      );
      console.log("deleteted succesfully");
    } else console.log("error, cannot delete");
    user.getUserList();
  };

onBeforeMount(async () => {
  await user.getUserList();
  user.page = user.userList.pageNumber;
});

onBeforeUpdate(async () => {
  await user.getEmptyUser();
});

// onUpdated(async () => {
//    user.page = user.userList.pageNumber;
// })

</script>
 
<template>
    <div class="bg-fixed bg-schedule bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen">
    <div class="grid" v-show="!user.showEmptyUser && user.noAuthentication">
      <UserList :currentUser="user.userList.content" @delete="removeUser" @next="user.NextPage"
        @back="user.BackPage" :page="user.page"/>
    </div>
    <div class="grid" v-show="user.showEmptyUser">
      <UserEmptyList />
    </div>
    <div class="grid" v-show="!user.noAuthentication">
      <NoAuthentication/>
    </div>
    </div>
</template>
 
<style>

</style>