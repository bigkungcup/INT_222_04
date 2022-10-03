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
const category = useEventCategory();
const checkLecturer = ref('lecturer')
const addPopUp = ref(false);
const deletePopUp = ref(false)
const deleteCategoryId = ref()

const toggleAddPopUp = () => {
  addPopUp.value = !addPopUp.value
}

const toggleDeletePopUp = () => {
  deletePopUp.value = !deletePopUp.value
}

const deleteCategory = (id) => {
  deleteCategoryId.value = id;
  toggleDeletePopUp();
}

  // get user
  const getUser = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${login.getIdToken()}`,
      {
        method: "GET",
      })
    if (res.status === 200) {
      displayUser.value = await res.json();
      login.noAuthentication = true;
      checkLecturer.value = displayUser.value.role == 'lecturer' ? true : false;
      console.log("get successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUser());
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else console.log("error, cannot get user");
  };

  const displayUser = ref({
    id: 0,
    name: "",
    email: "",
    role: "",
    eventCategories: []
  });

  const newEventCategory = ref()
  // const categoryLists = ref([]);
  // const userCategories = ref([]);

  // const filterCategories = () => {
  //   for(let i = 0; i <= categoryLists.length; i++ ) {
  //     for (let j = 0; j < userCategories.length; j++) {
  //       if(categoryLists[i].id == userCategories[j].id){
  //         delete categoryLists[0];
  //       }
  //     }
  // }


onBeforeMount(async () => {
  await getUser();
  await category.getEventCategory();
  // console.log(displayUser.value.eventCategories);
  // console.log(category.categoryLists);
  // categoryLists.value = category.categoryLists;
  // userCategories.value = displayUser.value.eventCategories;
  // categoryLists.value = categoryLists.value.filter(userCategories.value);
  // filterCategories();
  // console.log(categoryLists.value);
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

        <p class="mx-36">Name : {{ displayUser.name }}</p>

        <p class="mx-36">Email : {{ displayUser.email }}</p>

        <p class="mx-36">Role : {{ displayUser.role }}</p>

        <div class="mx-36" v-show="checkLecturer">
          <p>Clinics :</p>
          <li v-for="category in displayUser.eventCategories" class="ml-36">
              {{ category.eventCategoryName }}              
              <button @click="deleteCategory(category.id)" class="float-right mr-40">
                <svg width="40" viewBox="0 0 24 24"><path fill="#FF3366" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10s10-4.48 10-10S17.52 2 12 2zm5 11H7v-2h10v2z"/></svg>
            </button>
          </li>

          <select class="px-3 rounded-lg text-3xl mt-6 ml-36" v-model="newEventCategory">
            <option v-for="list in category.categoryLists" :value="list.id">
              {{ list.eventCategoryName }}            
            </option>
          </select>
          <button @click="login.addLecturerCategory(displayUser.id,newEventCategory),toggleAddPopUp()" class="absolute ml-8 mt-4">
          <svg width="50" viewBox="0 0 24 24">
            <path
              fill="#00CC66"
              d="M11 17h2v-4h4v-2h-4V7h-2v4H7v2h4Zm1 5q-2.075 0-3.9-.788q-1.825-.787-3.175-2.137q-1.35-1.35-2.137-3.175Q2 14.075 2 12t.788-3.9q.787-1.825 2.137-3.175q1.35-1.35 3.175-2.138Q9.925 2 12 2t3.9.787q1.825.788 3.175 2.138q1.35 1.35 2.137 3.175Q22 9.925 22 12t-.788 3.9q-.787 1.825-2.137 3.175q-1.35 1.35-3.175 2.137Q14.075 22 12 22Z"
            />
          </svg></button>
        </div>
      </div>
    </div>

    <div v-show="addPopUp" class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
    <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
      <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" aria-hidden="true" role="img"
          class="iconify iconify--ep animate-bounce pt-2" width="100" height="100" preserveAspectRatio="xMidYMid meet"
          viewBox="0 0 1024 1024">
          <path fill="#ffff"
            d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
          </path>
        </svg>
      </div>
      <div class="grid text-4xl place-items-center">
        <p>Created successfully</p>
      </div>
      <div class="grid place-items-center">
        <button class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2 hover:text-pink-700 hover:border-2 border-pink-700" @click="toggleAddPopUp(),getUser()">
          OK
        </button>
      </div>
    </div>
  </div>

  <div
      v-show="deletePopUp"
      class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0"
    >
      <div
        class="grid grid-rows-6 bg-white w-2/5 h-96 place-self-center rounded-3xl"
      >
        <div class="grid row-span-2 bgPopUp rounded-t-3xl place-items-center">
          <img
            src="../assets/images/Warning.png"
            width="90"
            class="grid absolute animate-bounce"
          />
        </div>
        <div class="grid row-span-2 place-items-center">
          <p class="text-5xl">Are you sure?</p>
        </div>
        <div>
          <p class="grid text-2xl text-gray-500 place-items-center">
            This clicnics will be deleted. Do you confirm that?
          </p>
        </div>
        <div class="flex justify-center mb-6">
          <button
            class="text-4xl px-5 text-green-500 hover:text-green-700"
            @click="login.deleteLecturerCategory(displayUser.id,deleteCategoryId),getUser(),toggleDeletePopUp()"
          >
            Yes
          </button>
          <button class="text-4xl px-5 text-red-500 hover:text-red-700" @click="toggleDeletePopUp()">
            No
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style></style>
