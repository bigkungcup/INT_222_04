<script setup>
import { onBeforeMount } from "vue";
import ClinicList from '../components/ClinicList.vue';
import { useClinics } from "../stores/Clinics.js";

const clinic = useClinics();

onBeforeMount(async () => {
    await clinic.getClinics();
});
</script>
 
<template>
    <div class="bg-Bg-Plain bg-cover h-screen">
        <ClinicList :currentClinics="clinic.clinicList"/>
    </div>
</template>
 
<style>

</style>