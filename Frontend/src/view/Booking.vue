<script setup>
import { ref, onBeforeMount } from 'vue'

//GET
const lists = ref([])
const getEventLists = async () => {
  const res = await fetch('http://localhost:5000/events')
  if (res.status === 200) {
    lists.value = await res.json()
  } else console.log('error, cannot get event lists')
}
onBeforeMount(async () => {
  await getEventLists()
})
</script>
 
<template>
<!-- <div>
    <div v-for= "x in lists">
    {{ x }}
    </div>
</div> -->
<div><h1>Hello</h1></div>
</template>
 
<style>

</style>