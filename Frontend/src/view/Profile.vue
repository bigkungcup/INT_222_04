<script setup>
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useEventCategory } from "../stores/event.js";
import { useLogin } from "../stores/login.js";
import Logout from "../components/Logout.vue";

const myRouter = useRouter();
const goBack = () => {
  myRouter.go(-1);
};

const login = useLogin();
const role = ref(login.getRoleToken());
// const showCategory = role.value = '[lecturer]' ? true : false;
const category = useEventCategory();

onBeforeMount(async () => {
  await category.getEventCategory();
});

</script>

<template>
  <div
    class="bg-fixed bg-user bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen pt-12 px-36 pb-36"
  >
    <div class="w-full h-full overflow-auto">
      <div class="grid absolute z-40 top-0 left-0" v-show="login.logoutPopup">
        <Logout />
      </div>
      <div class="grid text-4xl gap-y-6 break-all">
        <p class="text-8xl text-center">Profile</p>

        <p class="mx-36">Name :</p>

        <p class="mx-36">Email :</p>

        <p class="mx-36">Role : {{ role.slice(1, -1) }}</p>

        <div class="mx-36" v-show="login.checkLecture">
          <p>Clinics :</p>
          <!-- <li v-for="item in items">
      {{ item.message }}
      </li> -->

          <select class="px-3 rounded-lg text-3xl mt-6">
            <option v-for="list in category.categoryLists" :value="list">
              {{ list.eventCategoryName }}
            </option>
          </select>
          <svg class="absolute ml-99 -mt-11" width="50" viewBox="0 0 24 24">
            <path
              fill="#FF3366"
              d="M11 17h2v-4h4v-2h-4V7h-2v4H7v2h4Zm1 5q-2.075 0-3.9-.788q-1.825-.787-3.175-2.137q-1.35-1.35-2.137-3.175Q2 14.075 2 12t.788-3.9q.787-1.825 2.137-3.175q1.35-1.35 3.175-2.138Q9.925 2 12 2t3.9.787q1.825.788 3.175 2.138q1.35 1.35 2.137 3.175Q22 9.925 22 12t-.788 3.9q-.787 1.825-2.137 3.175q-1.35 1.35-3.175 2.137Q14.075 22 12 22Z"
            />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<style></style>
