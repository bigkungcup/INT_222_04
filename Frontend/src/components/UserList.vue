<script setup>
import { ref } from "vue";
import { useUser } from "../stores/event.js";
defineEmits(["next","back"]);

defineProps({
  currentUser: {
    type: Array,
    require: true,
  },
  page:{
    type: Number,
    require: true,
  }
});

const user = useUser()

</script>
 
<template>
<div class="grid place-items-center">
<table class="table-fixed w-4/6 bg-white/70  my-16 text-3xl rounded-3xl" >
  <thead>
    <tr>
      <th class="py-4">Name</th>
      <th >Email</th>
      <th class="w-28">Role</th>
      <th class="w-20"></th>
      <th class="w-20"></th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="list in currentUser">
      <td class="px-8 py-4">{{ list.name }}</td>
      <td class="px-8">{{ list.email }}</td>
      <td class="px-6">{{ list.role }}</td>
      <td class="px-10">
        <svg width="32" height="32" viewBox="0 0 24 24"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"><path fill="currentColor" d="M7 12.5a.5.5 0 1 0 0-1a.5.5 0 0 0 0 1Zm5 0a.5.5 0 1 0 0-1a.5.5 0 0 0 0 1Zm5 0a.5.5 0 1 0 0-1a.5.5 0 0 0 0 1Z"/><path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2S2 6.477 2 12s4.477 10 10 10Z"/></g></svg>
      </td>
      <td class="px-6">
        <svg width="32" height="32" viewBox="0 0 1024 1024"><path fill="currentColor" d="M696 480H328c-4.4 0-8 3.6-8 8v48c0 4.4 3.6 8 8 8h368c4.4 0 8-3.6 8-8v-48c0-4.4-3.6-8-8-8z"/><path fill="currentColor" d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448s448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372s372 166.6 372 372s-166.6 372-372 372z"/></svg>
      </td>
    </tr>   
  </tbody>
</table>

<div class="">
        <button class="bg-white rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:text-white hover:bg-black" v-show="page > 0" @click="$emit('back')">back</button>
          <input type="text" class="rounded-lg bg-white h-12 text-3xl w-16 text-center text-amber-900" disabled readonly
          :value="page+1">
 <button class="bg-white rounded-3xl w-36 py-2 mx-2 drop-shadow-xl hover:text-white hover:bg-black" v-show="page+1 < user.userList.totalPages " @click="$emit('next')">next</button>
</div>

</div>
</template>
 
<style>

</style>