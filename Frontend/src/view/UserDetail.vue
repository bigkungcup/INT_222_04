<script setup>
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { formatDate, formatTime } from "../main.js";
import { useUser } from "../stores/event.js"
const { params } = useRoute();

const user = useUser()

//get user
const getUser = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${params.id}`);
  if (res.status === 200) {
    displayUser.value = await res.json();
    console.log("get successfully");
  } else console.log("error, cannot get user");
};

const displayUser = ref({
  bookingName: "",
  bookingEmail: "",
  eventCategory: {},
  eventStartTime: "",
  eventNotes: "",
  eventDuration: 0,
});

const myRouter = useRouter();

const goBack = () => {
  myRouter.go(-1);
  window.localStorage.clear();
};

onBeforeMount(async () => {
  await getUser();
});

</script>
 
<template>
   <div class="bg-fixed bg-user bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen pt-12 px-36 pb-36">
       <div class="w-full h-full overflow-auto">
      <div class="grid text-4xl gap-y-10 break-all">
        <p class="text-8xl text-center">
          Detail
          <button @click="showPopUp()">
            <img src="../assets/images/Edit.png" width="40" class="transition duration-150 ease-in-out hover:scale-125" />
          </button>
          <button @click="goBack">
            <img src="../assets/images/Exit.png" width="60" class="absolute top-3 right-36" />
          </button>
        </p>
        <p class="mx-36">Name : {{ displayUser.name }}</p>
        <p class="mx-36">Email : {{ displayUser.email }}</p>
        <p class="mx-36">Role : {{ displayUser.role }}</p>
        <p class="mx-36">Created on : {{formatDate(displayUser.createdOn)}} | {{ formatTime(displayUser.createdOn) }}  </p>
        <p class="mx-36">Updated on : {{formatDate(displayUser.updatedOn)}} | {{ formatTime(displayUser.updatedOn) }}  </p>
</div>
</div>
    </div>
</template>
 
<style>

</style>