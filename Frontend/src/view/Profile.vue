<script setup>
import { ref, onBeforeMount, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import Logout from "../components/Logout.vue";
import { formatDate, formatTime } from "../main.js";
import { useUsers } from "../stores/Users.js";
import { useLogin } from "../stores/Login.js";

const { params } = useRoute();
const user = useUsers();
const login = useLogin();

const displayUser = ref({
    name: localStorage.getItem("name"),
    email: localStorage.getItem("email"),
    role: localStorage.getItem("role"),
});

login.msLogoutIcon = localStorage.getItem("msal.idtoken") != null ? true : false;

onBeforeMount(async () => {
    await user.getUserDetail(localStorage.getItem('id'));
    displayUser.value = localStorage.getItem("msal.idtoken") != null ? displayUser.value : user.displayUser;
    console.log(displayUser.value);
});


</script>
 
<template>
    <div class="bg-Bg bg-cover h-screen pt-24 px-32 overflow-auto no-scrollbar">
        <div class="grid grid-cols-2 bg-white rounded-2xl h-144 mt-6">
            <div class="py-24 px-28"><svg width="360" viewBox="0 0 496 512">
                    <path fill="#502763"
                        d="M248 8C111 8 0 119 0 256s111 248 248 248s248-111 248-248S385 8 248 8zm0 96c48.6 0 88 39.4 88 88s-39.4 88-88 88s-88-39.4-88-88s39.4-88 88-88zm0 344c-58.7 0-111.3-26.6-146.5-68.2c18.8-35.4 55.6-59.8 98.5-59.8c2.4 0 4.8.4 7.1 1.1c13 4.2 26.6 6.9 40.9 6.9c14.3 0 28-2.7 40.9-6.9c2.3-.7 4.7-1.1 7.1-1.1c42.9 0 79.7 24.4 98.5 59.8C359.3 421.4 306.7 448 248 448z" />
                </svg></div>

            <div class="grid pt-8 pb-2 text-2xl font-bold text-Web-violet content-center">
                <div class="mb-4"><span class="bg-Web-violet p-2 text-white rounded-xl -ml-2">{{ displayUser.role }}</span></div>
                <p class="mb-4">{{ displayUser.name }}</p>
                <p class="font-light mb-4">{{ displayUser.email }}</p>

                <div class="mb-4" v-show="displayUser.role == 'lecturer'">
                        <p class="mr-3">Clinics :</p>
                        <ul class="no-bullets font-light">
                            <li v-for="clinic in displayUser.eventCategories" class="ml-28"
                                v-show="!user.editUserField">
                                <div class="flex">
                                    <p class="mt-1">
                                        {{ clinic.eventCategoryName }}
                                    </p>
                                </div>
                            </li>
                        </ul>
                </div>

                <p class="flex mb-4" v-if="!login.msLogoutIcon">Created on :
                <p class="font-light">{{formatDate(displayUser.createdOn)}} | {{
                    formatTime(displayUser.createdOn) }}</p>
                </p>
                <p class="flex mb-4" v-if="!login.msLogoutIcon">Updated on :
                <p class="font-light">{{formatDate(displayUser.updatedOn)}} | {{
                    formatTime(displayUser.updatedOn) }}</p>
                </p>
                <div class="grid mt-2 justify-items-end">
                <button class="rounded-2xl bg-Web-pink py-2 w-1/6 text-white text-lg font-bold mx-14"
                    @click="login.logoutPopup = true">
                    Log Out
                </button>
            </div>
            </div>
        </div>
        <div v-show="login.logoutPopup && login.msLogoutIcon">
            <Logout v-show="login.msLogoutIcon" @toggle="login.logoutPopup = !login.logoutPopup" @logout="login.msSignOut()" />
        </div>
        <div v-show="login.logoutPopup && !login.msLogoutIcon">
            <Logout v-show="!login.msLogoutIcon" @toggle="login.logoutPopup = !login.logoutPopup" @logout="login.logout()" />
        </div>
    </div>
</template>
 
<style>
ul.no-bullets {
    list-style-type: none;
    /* Remove bullets */
    padding: 0;
    /* Remove padding */
    margin: 0;
    /* Remove margins */
}
</style>