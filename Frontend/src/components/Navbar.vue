<script setup>
defineEmits(["logout","toggle"]);

defineProps({
    logoutIcon: {
    type: Boolean,
    require: true,
  },
    userPageIcon: {
    type: Boolean,
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
            <router-link :to="{ name: 'Home' }">
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
            <button class="font-bold hover:underline underline-offset-8" v-show="logoutIcon" @click="$emit('toggle')">Log Out</button>
            <!-- <button class="font-bold hover:underline underline-offset-8"  @click="$emit('toggle')">Log Out</button> -->
        </div>
    </div>
</template>

<style>

</style>
