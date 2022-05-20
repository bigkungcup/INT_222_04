import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import "@vuepic/vue-datepicker/dist/main.css";


export const useEvent = defineStore("event", () => {
  const eventLists = ref([]);
  const filterEventLists = ref([]);
  // const pastEventLists = ref([]);
  // const upComingEventLists = ref([]);
  const listsNewEvent = ref([]);
  const showEmptyEvent = ref(false);
  const filter = ref(0)
  const page = ref(0);
  const popUp = ref(false);
  const textPopUp = ref(false);
  const validEmail = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

  //Get Event
  const getEventLists = async (page=0) => {
    const res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events?page=${page}`,
        {
          method: "GET",
        }
      );
      if (res.status === 200) {
        eventLists.value = await res.json();
      } else console.log("error, cannot get event lists");
      // console.log(eventLists.value.content.length);
  };

    //  Get Filter Event
  const getFilterEvent = async (page=0) => {
    let res 
    if(filter.value == 1){
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/pastEvent?page=${page}`,
        {
          method: "GET",
        }
      )
    }
    else if(filter.value == 2){
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/upComingEvent?page=${page}`,
        {
          method: "GET",
        }
      )
    }
    else if(filter.value == 3){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/1?page=${page}`,
      {
        method: "GET",
      })
    }
    else if(filter.value == 4){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/2?page=${page}`,
      {
        method: "GET",
      })
    }
    else if(filter.value == 5){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/3?page=${page}`,
      {
        method: "GET",
      })
    }
    else if(filter.value == 6){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/4?page=${page}`,
      {
        method: "GET",
      })
    }
    else{
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/5?page=${page}`,
      {
        method: "GET",
      })
    }
    if (res.status === 200) {
      filterEventLists.value = await res.json();
    } else console.log("error, cannot get event lists")
    console.log(eventLists.value);
};

//    //Get Past Event
//   const getPastEvent = async (page=0) => {
//     const res = await fetch(
//       `${import.meta.env.VITE_BASE_URL}/events/pastEvent?page=${page}`,
//       {
//         method: "GET",
//       }
//     );
//     if (res.status === 200) {
//       pastEventLists.value = await res.json();
//     } else console.log("error, cannot get event lists");
//     console.log(eventLists.value);
// };

// //Get Up-coming Event
// const getUpcomingEvent = async (page=0) => {
//   const res = await fetch(
//     `${import.meta.env.VITE_BASE_URL}/events/upComingEvent?page=${page}`,
//     {
//       method: "GET",
//     }
//   );
//   if (res.status === 200) {
//     upComingEventLists.value = await res.json();
//   } else console.log("error, cannot get event lists");
//   console.log(eventLists.value);
// };

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
    if (eventLists.value.content.length === 0 ) {
      showEmptyEvent.value = true;
    } 
    else if (eventLists.value.content.length !== 0) {
      showEmptyEvent.value = false;
    }
    console.log(showEmptyEvent.value);
  };

  //Page
  const NextPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value += 1
    getEventLists((page.value));
    getFilterEvent((page.value));
    // getUpcomingEvent((page.value));
    // getPastEvent((page.value));
    // window.localStorage.setItem("page",page.value);
  };
  const BackPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value -= 1
    getEventLists((page.value));
    getFilterEvent((page.value));
    // getUpcomingEvent((page.value));
    // getPastEvent((page.value));
    // window.localStorage.setItem("page",page.value);
  };

  // const checkFilterPage = (page) => {
  //   getFilterEvent(page) 
  // }

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
    filterEventLists,
    // pastEventLists,
    // upComingEventLists,
    getEventLists,
    getFilterEvent,
    // getPastEvent,
    // getUpcomingEvent,
    listsNewEvent,
    createEvent,
    // removeEvent,
    showEmptyEvent,
    getEmptyEvent,
    page,
    NextPage,
    BackPage,
    popUp,textPopUp,showPopUp,disShowPopUp,showText,filter
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

  // const sortEventCategory = () => {

  // }

  return { categoryLists, getEventCategory };
});

  

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(
    acceptHMRUpdate(useEvent, useEventCategory, import.meta.hot)
  );
}