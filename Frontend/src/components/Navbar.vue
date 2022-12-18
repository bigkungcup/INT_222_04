<script setup>
// import { useLogin } from "../stores/Login.js";
// const login = useLogin();
defineEmits(["logout","toggle"]);

defineProps({
    logoutIcon: {
    type: Boolean,
    require: true,
  },
    userPageIcon: {
    type: Boolean,
    require: true,
  },
  userName: {
    type: String,
    require: true,
  }
});
</script>

<template>
    <div class="grid fixed h-24 bg-Menu w-screen grid-cols-8 font-bold text-white text-2xl">
        <div class="grid col-span-2 place-content-center">
            <router-link :to="{ name: 'Home' }">
                <p class="hover:animate-bounce">
                    <img src="/favicon.png" class="float-left" width="25" />ASIP-KW4
                </p>
            </router-link>
        </div>
        <div class="place-content-center" v-show="!userPageIcon"></div>
        <div class="grid place-content-center">
            <router-link :to="{ name: 'Clinics' }">
                <p class="hover:underline underline-offset-8">Clinics</p>
            </router-link>
        </div>
        <div class="grid place-content-center">
            <router-link :to="{ name: 'Events' }">
                <p class="hover:underline underline-offset-8">Events</p>
            </router-link>
        </div>
        <div class="grid place-content-center">
            <router-link :to="{ name: 'Booking' }">
                <p class="hover:underline underline-offset-8">Booking</p>
            </router-link>
        </div>
        <div class="grid place-content-center">
            <router-link :to="{ name: 'AboutUs' }">
                <p class="hover:underline underline-offset-8">About Us</p>
            </router-link>
        </div>
        <div class="grid place-content-center" v-show="userPageIcon">
            <router-link :to="{ name: 'Users' }">
                <p class="hover:underline underline-offset-8">Users</p>
            </router-link>
        </div>
        <div class="grid place-content-center">
            <router-link :to="{ name: 'Login' }" v-show="!logoutIcon">
            <span class="rounded-full bg-Web-pink py-2 px-9 font-bold hover:bg-white hover:text-Web-pink">
                Sign In
            </span>
            </router-link>
            <!-- <button @click="login.msSignOut()">logout</button> -->
            <!-- <button class="font-bold hover:underline underline-offset-8" v-show="logoutIcon" @click="$emit('toggle')">Log Out</button> -->
            <router-link :to="{ name: 'Profile' }" v-show="logoutIcon">
            <button class="rounded-full border-4 border-Web-pink px-4 font-bold hover:bg-Web-pink w-40">
                <div class="flex">
                <svg width="30" viewBox="0 0 496 512" class="mr-2">
                    <path fill="#FFFFFF"
                        d="M248 8C111 8 0 119 0 256s111 248 248 248s248-111 248-248S385 8 248 8zm0 96c48.6 0 88 39.4 88 88s-39.4 88-88 88s-88-39.4-88-88s39.4-88 88-88zm0 344c-58.7 0-111.3-26.6-146.5-68.2c18.8-35.4 55.6-59.8 98.5-59.8c2.4 0 4.8.4 7.1 1.1c13 4.2 26.6 6.9 40.9 6.9c14.3 0 28-2.7 40.9-6.9c2.3-.7 4.7-1.1 7.1-1.1c42.9 0 79.7 24.4 98.5 59.8C359.3 421.4 306.7 448 248 448z" />
                </svg>
                <p class="my-2 truncate">{{ userName }}</p>
            </div>
            </button>
            </router-link>
        </div>
    </div>
</template>

<style>

</style>
