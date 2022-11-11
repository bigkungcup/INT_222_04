<script setup>
import { ref } from "vue";
import { useLogin } from "../stores/Login.js";
import LoginSuccessfully from "../components/LoginSuccessfully.vue";

const login = useLogin();

const togglePopup = () => {
    login.loginSuccessfully = !login.loginSuccessfully
}

</script>
 
<template>
    <div class="grid bg-Bg-Login bg-cover h-screen grid-cols-7">
        <div class="grid col-span-4"></div>
        <div class="grid col-span-3 content-center grid-cols-2 gap-y-10">
            <div class="grid font-bold text-Web-pink text-5xl underline underline-offset-10 pl-8">
                Login
            </div>
            <router-link :to="{ name: 'SignUp' }">
                <div class="grid font-bold text-white text-5xl underline underline-offset-10">
                    Sign Up
                </div>
            </router-link>
            <div class="grid col-span-2">
                <svg width="32" height="36" viewBox="0 0 24 24" class="absolute my-8 mx-4">
                    <path fill="#462A6C"
                        d="M4 20q-.825 0-1.412-.587Q2 18.825 2 18V6q0-.825.588-1.412Q3.175 4 4 4h16q.825 0 1.413.588Q22 5.175 22 6v12q0 .825-.587 1.413Q20.825 20 20 20Zm8-7l8-5V6l-8 5l-8-5v2Z" />
                </svg>
                <input type="text"
                    class="padding-input bg-white border border-slate-300 rounded-lg h-16 w-5/6 text-3xl placeholder:italic placeholder:text-2xl mt-4"
                    v-model.trim="login.loginAccount.email" placeholder="Enter Your Email" />
            </div>
            <form class="grid col-span-2">
                <svg width="32" height="34" viewBox="0 0 24 24" class="absolute my-4 mx-4">
                    <path fill="#462A6C"
                        d="M6 22q-.825 0-1.412-.587Q4 20.825 4 20V10q0-.825.588-1.413Q5.175 8 6 8h1V6q0-2.075 1.463-3.538Q9.925 1 12 1t3.538 1.462Q17 3.925 17 6v2h1q.825 0 1.413.587Q20 9.175 20 10v10q0 .825-.587 1.413Q18.825 22 18 22Zm6-5q.825 0 1.413-.587Q14 15.825 14 15q0-.825-.587-1.413Q12.825 13 12 13q-.825 0-1.412.587Q10 14.175 10 15q0 .825.588 1.413Q11.175 17 12 17ZM9 8h6V6q0-1.25-.875-2.125T12 3q-1.25 0-2.125.875T9 6Z" />
                </svg>
                <input type="password" autocomplete="off"
                    class="bg-white border padding-input border-slate-300 rounded-lg h-16 w-5/6 text-3xl placeholder:italic placeholder:text-2xl"
                    v-model="login.loginAccount.password" placeholder="Enter Your Password"/>
            </form>
            <div class="grid content-center font-bold text-Web-pink text-xl">
                Forgot Password?
            </div>
            <div class="grid content-center font-bold text-white text-xl text-center">
                <button class="rounded-full bg-Web-pink py-2 w-2/5 hover:bg-white hover:text-Web-pink mx-24" @click="login.handleLogin()">
                    Login
                </button>
            </div>

        <div v-show="login.loginSuccessfully">
            <LoginSuccessfully @toggle="togglePopup()"/>
        </div>
        </div>
    </div>
</template>
 
<style>
.padding-input {
    padding-left: 4rem;
}
</style>