<script setup>
import { ref, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useEventCategory } from "../stores/event.js"
const { params } = useRoute();

const allCategory = useEventCategory();

//get event
const getEventCategory = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories/${params.id}`,
        {
            method: "GET",
        }
    );
    if (res.status === 200) {
        displayCategory.value = await res.json();
        console.log("get successfully");
    } else console.log("error, cannot get category");
};

//edit event
const saveEventCategory = async (displayCategory, editCategory) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories/${params.id}`, {
        method: "PUT",
        headers: { "content-type": "application/json" },
        body: JSON.stringify({
            eventCategoryName: editCategory.eventCategoryName === "" ? displayCategory.eventCategoryName : editCategory.eventCategoryName,
            eventDuration: editCategory.eventDuration === null ? displayCategory.eventDuration : editCategory.eventDuration,
            eventCategoryDescription: editCategory.eventCategoryDescription === "" ? displayCategory.eventCategoryDescription : editCategory.eventCategoryDescription,
        }),
    });
    if (res.status === 200) {
        let category = await res.json();
        console.log(category);
        if (editCategory.eventCategoryName !== "" || editCategory.eventDuration !== null || editCategory.eventCategoryDescription !== "") {
            showEditPopUp()
        }
        popUp.value = false
        reset()
        getEventCategory()
        console.log("edit successfully");
    } else {
        console.log("error, cannot edit");
        alert("error, cannot edit");
    }
};

const displayCategory = ref({
    eventCategoryName: "",
    eventDuration: 0,
    eventCategoryDescription: ""
});

const myRouter = useRouter();
const goBack = () => {
    myRouter.go(-1);
};

const editCategory = ref({
    eventCategoryName: "",
    eventDuration: null,
    eventCategoryDescription: ""
})

const popUp = ref(false);
const showPopUp = () => {
    popUp.value = true;
    console.log(popUp.value);
};

const editPopUp = ref(false);
const showEditPopUp = () => {
    editPopUp.value = true;
    console.log(editPopUp.value);
};


const reset = () => {
    editCategory.value = {
        eventCategoryName: "",
        eventDuration: null,
        eventCategoryDescription: ""
    },
    showNameSame.value = false
}

const showNameSame = ref(false)
const checkName = (eventCategoryName) => {
    for (let i = 0; i < allCategory.categoryLists.length; i++) {
        if (eventCategoryName !== allCategory.categoryLists[i].eventCategoryName) {
            showNameSame.value = true
        }
    }
}

onBeforeMount(async () => {
    await getEventCategory();
    await allCategory.getEventCategory();
});


</script>

<template>
    <div class="bg-fixed bg-detail bg-no-repeat bg-auto bg-cover bg-center h-screen w-screen pt-12 px-36 pb-36">
        <div class="w-full h-full overflow-auto">
            <div class="text-4xl gap-y-10 break-all">
                <p class="text-8xl text-center col-span-2 ">
                    Detail
                    <button @click="showPopUp()">
                        <img src="../assets/images/Edit.png" width="40" />
                    </button>
                    <button @click="goBack">
                        <img src="../assets/images/Exit.png" width="60" class="absolute top-3 right-36" />
                    </button>
                </p>
                <p class="pl-10 py-6" v-show="!popUp">Name : {{ displayCategory.eventCategoryName }}</p>
                <p class="pl-10 py-6" v-show="!popUp">Duration : {{ displayCategory.eventDuration }} min.</p>
                <p class="pl-10 py-6" v-show="!popUp">
                    Description : </p>
                <div class="ml-10 mr-72" v-show="!popUp">
                    <p>{{ displayCategory.eventCategoryDescription }}
                    </p>
                </div>

                <p class="pl-10 py-6" v-show="popUp">Name : <input type="text" class="bg-white border border-slate-300 rounded-lg h-10 w-3/5 text-3xl 
        placeholder:italic placeholder:text-2xl " placeholder=" Enter Category Name"
                        v-model="editCategory.eventCategoryName" /></p>

                <p v-if="showNameSame && editCategory.eventCategoryName !== ''" class="text-lg text-red-500 pl-36 -mt-4">*Category name is not unique.</p>
                <p v-if="editCategory.eventCategoryName.length > 100" class="text-lg text-red-500 pl-36 -mt-4">
                    *Category Name can't be longer than 100 characters.</p>

                <p class="pl-10 py-6" v-show="popUp">Duration : <input type="number"
                        class="bg-white border border-slate-300 rounded-lg h-10 w-24 text-3xl text-center"
                        v-model="editCategory.eventDuration" max='480' min='1' /> min.</p>
                <p class="col-span-2 pl-10 pr-80" v-show="popUp">
                    Description :
                <p v-if="editCategory.eventCategoryDescription.length > 500" class="text-lg text-red-500 pl-52 -mt-8">
                    *Description can't be longer than 500 characters.</p>
                <textarea
                    class="bg-white border border-slate-300 rounded-lg h-10 col-span-2 w-full h-28 mt-5 p-3 text-3xl resize-none"
                    placeholder="add your note " v-model="editCategory.eventCategoryDescription"></textarea>
                </p>
            </div>
        </div>
        <div class="flex text-white justify-center text-2xl" v-show="popUp">
            <button class="bg-red-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl" @click="popUp = false, reset()">
                Cancel
            </button>
            <button class="bg-green-500 rounded-3xl w-36 py-2 mx-2 drop-shadow-xl"
                @click="saveEventCategory(displayCategory, editCategory), checkName(editCategory.eventCategoryName)">
                Save
            </button>
        </div>

        <div v-show="editPopUp" class="flex justify-center absolute bg-black/50 h-screen w-screen inset-0 top-0">
            <div class="grid grid-rows-3.5 bg-white w-2/6 h-80 place-self-center rounded-3xl">
                <div class="grid row-span-1.5 bgPopUp rounded-t-3xl place-items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
                        aria-hidden="true" role="img" class="iconify iconify--ep animate-bounce" width="100" height="100"
                        preserveAspectRatio="xMidYMid meet" viewBox="0 0 1024 1024">
                        <path fill="#ffff"
                            d="M512 64a448 448 0 1 1 0 896a448 448 0 0 1 0-896zm-55.808 536.384l-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z">
                        </path>
                    </svg>
                </div>
                <div class="grid text-4xl place-items-center">
                    <p>Edit successfully</p>
                </div>
                <div class="grid place-items-center">
                    <button class="text-4xl px-5 text-white bgPopUp rounded-3xl w-36 py-2 mx-2 hover:text-pink-700 hover:border-2 border-pink-700"
                        @click="editPopUp = false, popUp = false">
                        OK
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style>
</style>
