import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import "@vuepic/vue-datepicker/dist/main.css";


export const useEvent = defineStore("event", () => {
  const eventLists = ref([]);
  const listsNewEvent = ref([]);
  const showEmptyEvent = ref();
  const page = ref(0);
  const popUp = ref(false);
  const textPopUp = ref(false);
  const validEmail = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

  //Get Event
  const getEventLists = async () => {
      const res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events?page=${page.value}`,
        {
          method: "GET",
        }
      );
      if (res.status === 200) {
        eventLists.value = await res.json();
      } else console.log("error, cannot get event lists");
      console.log(eventLists.value);
  };

  //Create Event
  const createEvent = async (newEvent) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`, {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({
        bookingName: newEvent.bookingName,
        bookingEmail: newEvent.bookingEmail.match(validEmail)
          ? newEvent.bookingEmail
          : null,
        eventCategory: newEvent.eventCategory,
        eventStartTime: newEvent.eventStartTime,
        eventNotes: newEvent.eventNotes,
        eventDuration: newEvent.eventDuration,
      }),
    });
    if (res.status === 201) {
      const addEvent = await res.json();
      listsNewEvent.value.push(addEvent);
      showPopUp();
      console.log("created successfully");
    } else {
      console.log("error, cannot create");
      showText();
    }
    console.log(
      newEvent.bookingEmail.match(validEmail) ? newEvent.bookingEmail : null
    );
  };

  //ShowEmpty
  const getEmptyEvent = async () => {
    if (eventLists.value.length === 0) {
      showEmptyEvent.value = true;
    } else if (eventLists.value.length !== 0) {
      showEmptyEvent.value = false;
    }
    console.log(showEmptyEvent.value);
  };

  //Page
  const NextPage = () => {
    if (page.value < 0) {
      page.value= 0;
    }
    getEventLists((page.value += 1));
    // window.localStorage.setItem("page",page.value);
  };
  const BackPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    getEventLists((page.value -= 1));
    // window.localStorage.setItem("page",page.value);
  };

  //pop-up
  const showPopUp = () => {
    popUp.value = true;
    console.log(popUp.value);
  };
  const disShowPopUp = () => {
    popUp.value = false;
    textPopUp.value = false;
  };

  //Text pop-up
  const showText = () => {
    textPopUp.value = true;
    console.log(popUp.value);
  };

  return {
    eventLists,
    getEventLists,
    listsNewEvent,
    createEvent,
    // removeEvent,
    showEmptyEvent,
    getEmptyEvent,
    page,
    NextPage,
    BackPage,
    popUp,textPopUp,showPopUp,disShowPopUp,showText
  };
});

//-----------------------------------------------------------------------------------
export const useEventCategory = defineStore("eventCatergory", () => {
  const categoryLists = ref([]);

  //Get Category
  const getEventCategory = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategory`, {
      method: "GET",
    });
    if (res.status === 200) {
      categoryLists.value = await res.json();
      console.log(categoryLists.value);
    } else console.log("error, cannot get event category lists");
  };
  return { categoryLists, getEventCategory };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(
    acceptHMRUpdate(useEvent, useEventCategory, import.meta.hot)
  );
}