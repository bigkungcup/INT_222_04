<script setup>
import { onBeforeMount } from "vue";
import { useClinics } from "../stores/Clinics.js";
import { useUsers } from "../stores/Users";
import SignUpSuccessfully from "../components/SignUpSuccessfully.vue";

const clinic = useClinics();
const user = useUsers();

const getClinic = () => {
    clinic.getClinics();
}

const togglePopup = () => {
    user.signUpSuccessfully = !user.signUpSuccessfully;
}

onBeforeMount(async () => {
    user.resetNewUser();
});

</script>

<template>
    <div class="grid bg-Bg-Login bg-cover h-screen grid-cols-7">
        <div class="grid col-span-4"></div>
        <div class="grid col-span-3 content-center grid-cols-2 gap-y-4 mt-16">
            <router-link :to="{ name: 'Login' }">
                <div class="grid font-bold text-white text-5xl underline underline-offset-10 pl-8">
                    Login
                </div>
            </router-link>
            <div class="grid font-bold text-Web-pink text-5xl underline underline-offset-10">
                Sign Up
            </div>
            <div class="grid col-span-2">
                <svg width="32" height="36" viewBox="0 0 32 32" class="absolute my-8 mx-4">
                    <path fill="#462A6C"
                        d="M6 30h20v-5a7.008 7.008 0 0 0-7-7h-6a7.008 7.008 0 0 0-7 7zM9 9a7 7 0 1 0 7-7a7 7 0 0 0-7 7z" />
                </svg>
                <input type="text"
                    class="padding-input bg-white border border-slate-300 rounded-lg h-16 w-5/6 text-3xl placeholder:italic placeholder:text-2xl mt-4"
                    placeholder="Enter Your Name" v-model.trim="user.newUser.name"/>
                <p class="text-Web-pink pt-3" v-show="user.newUser.name == '' && !user.signUpValidate">*Please enter your user name.</p>
                <p class="text-Web-pink pt-3" v-show="user.newUser.name.length > 100 && !user.signUpValidate">*Name can't be longer than 100 characters.</p>
            </div>

            <div class="grid col-span-2">
                <svg width="32" height="36" viewBox="0 0 24 24" class="absolute my-4 mx-4">
                    <path fill="#462A6C"
                        d="M4 20q-.825 0-1.412-.587Q2 18.825 2 18V6q0-.825.588-1.412Q3.175 4 4 4h16q.825 0 1.413.588Q22 5.175 22 6v12q0 .825-.587 1.413Q20.825 20 20 20Zm8-7l8-5V6l-8 5l-8-5v2Z" />
                </svg>
                <input type="text"
                    class="padding-input bg-white border border-slate-300 rounded-lg h-16 w-5/6 text-3xl placeholder:italic placeholder:text-2xl"
                    placeholder="Enter Your Email" v-model.trim="user.newUser.email"/>
                    <p class="text-Web-pink pt-3" v-show="user.newUser.email == '' && !user.signUpValidate">*Please enter your user email.</p>
                    <p class="text-Web-pink pt-3" v-show="user.newUser.email.match(user.validEmail) && user.newUser.email.length > 50 && !user.signUpValidate">*Email can't be longer than 50 characters.</p>
                    <p class="text-Web-pink pt-3" v-show="!(user.newUser.email.match(user.validEmail)) && user.newUser.email != '' && !user.signUpValidate">*Please enter a valid email address.</p>
                    <p class="text-Web-pink pt-3" v-show="!user.userUnique">*Username or email already exists.</p>
            </div>

            <form class="grid col-span-2">
                <svg width="32" height="34" viewBox="0 0 24 24" class="absolute my-4 mx-4">
                    <path fill="#462A6C"
                        d="M6 22q-.825 0-1.412-.587Q4 20.825 4 20V10q0-.825.588-1.413Q5.175 8 6 8h1V6q0-2.075 1.463-3.538Q9.925 1 12 1t3.538 1.462Q17 3.925 17 6v2h1q.825 0 1.413.587Q20 9.175 20 10v10q0 .825-.587 1.413Q18.825 22 18 22Zm6-5q.825 0 1.413-.587Q14 15.825 14 15q0-.825-.587-1.413Q12.825 13 12 13q-.825 0-1.412.587Q10 14.175 10 15q0 .825.588 1.413Q11.175 17 12 17ZM9 8h6V6q0-1.25-.875-2.125T12 3q-1.25 0-2.125.875T9 6Z" />
                </svg>
                <input type="password" autocomplete="off"
                    class="bg-white border padding-input border-slate-300 rounded-lg h-16 w-5/6 text-3xl placeholder:italic placeholder:text-2xl"
                    placeholder="Enter Your Password" v-model="user.newUser.password"/>
                    <p class="text-Web-pink pt-3" v-show="user.newUser.password == '' && !user.signUpValidate">Please enter your user password.</p>
                    <p class="text-Web-pink pt-3" v-show="(user.newUser.password.length > 14 || user.newUser.password.length < 8) && user.newUser.password != '' && !user.signUpValidate">*Use 8-14 characters for password.</p>
            </form>

            <form class="grid col-span-2">
                <svg width="32" height="34" viewBox="0 0 24 24" class="absolute my-4 mx-4">
                    <path fill="#462A6C"
                        d="M19 13c.34 0 .67.04 1 .09V10a2 2 0 0 0-2-2h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6a2 2 0 0 0-2 2v10c0 1.11.89 2 2 2h7.81c-.51-.88-.81-1.9-.81-3c0-3.31 2.69-6 6-6M9 6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9V6m3 11a2 2 0 1 1 2-2c0 1.11-.89 2-2 2m10.5.25L17.75 22L15 19l1.16-1.16l1.59 1.59l3.59-3.59l1.16 1.41Z" />
                </svg>
                <input type="password" autocomplete="off"
                    class="bg-white border padding-input border-slate-300 rounded-lg h-16 w-5/6 text-3xl placeholder:italic placeholder:text-2xl"
                    placeholder="Confirm Your Password" v-model="user.confirmPassword"/>
                    <p class="text-Web-pink pt-3" v-show="user.confirmPassword == '' && !user.signUpValidate">*Please enter your confirm password.</p>
                    <p class="text-Web-pink pt-3" v-show="user.confirmPassword != user.newUser.password && !user.signUpValidate && user.confirmPassword != ''">*The password confirmation does not match.</p>
            </form>

            <div class="grid col-span-2 font-bold text-white text-xl">
                <div>
                <input type="radio" id="1" value="student" v-model="user.newUser.role" checked/>
                <label for="1" class="ml-2">Student</label>

                <input type="radio" id="2" value="lecturer" class="ml-6" v-model="user.newUser.role" @click="getClinic()"/>
                <label for="2" class="ml-2">Lecturer</label>

                <input type="radio" id="3" value="admin" class="ml-6" v-model="user.newUser.role"/>
                <label for="3" class="ml-2">Admin</label>
            </div>

            <div class="grid col-span-2 mt-4" v-show="user.newUser.role == 'lecturer'">
                <select class="rounded-lg h-12 w-5/6 font-bold text-white text-xl bg-black/30 border-4 border-Web-pink padding-select" v-model="user.lecturerClinic">
                    <option v-for="list in clinic.clinicList" :value="list.id">
                        {{ list.eventCategoryName }}            
                    </option>
                </select>
            </div>
            </div>
            <div class="grid">
                <button class="rounded-full bg-white font-bold text-Web-pink text-center text-xl py-2 w-2/5 " @click="user.resetNewUser()">
                    Cancle
                </button>
            </div>
            <div class="grid">
                <button class="rounded-full bg-Web-pink font-bold text-white text-center text-xl py-2 w-2/5 ml-24" @click="user.signUp()">
                    Sign Up
                </button>
            </div>
            <div v-show="user.signUpSuccessfully">
                <SignUpSuccessfully @toggle="togglePopup()"/>
            </div>
        </div>
    </div>
</template>

<style>
.padding-input {
    padding-left: 4rem;
}

.padding-select{
    padding-left: 1rem;
}

</style>
