import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import "@vuepic/vue-datepicker/dist/main.css";
import moment from "moment"

export const useEvent = defineStore("event", () => {
  const eventLists = ref([]);
  const eventListAll = ref([]);
  const filterEventLists = ref([]);
  const listsNewEvent = ref([]);
  const showEmptyEvent = ref(false);
  const showEmptyFilterEvent = ref(false);
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
  };

    //Get All Event
    const getAllEventLists = async () => {
      const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/events/eventAll`,
          {
            method: "GET",
          }
        );
        if (res.status === 200) {
          eventListAll.value = await res.json();
        } else console.log("error, cannot get event lists");
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
        eventStartTime: getOverlapTime(newEvent.eventStartTime,newEvent.eventCategory.id) ? newEvent.eventStartTime = "overlap" : newEvent.eventStartTime,
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
      console.log(getOverlapTime(newEvent.eventStartTime,newEvent.eventCategory.id));
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

  const getEmptyFilterEvent = async (filterEvent=[]) => {
    if (filterEvent.length === 0 ) {
      showEmptyFilterEvent.value = true;
    } 
    else if (filterEvent.length !== 0) {
      showEmptyFilterEvent.value = false;
    }
    console.log(showEmptyFilterEvent.value);
  };

  //Page
  const NextPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value += 1
    getEventLists((page.value));
    getFilterEvent((page.value));
  };
  const BackPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value -= 1
    getEventLists((page.value));
    getFilterEvent((page.value));
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

  // Get Overlap Time
  const getOverlapTime = (eventStartTime,category) => {
    getAllEventLists();
    let listAll
    listAll = eventListAll.value.filter(a => a.eventCategory.id == category);
    console.log(listAll);
    return listAll.some((event) => {
    if(moment(eventStartTime).toLocaleString("th-TH") < moment(event.eventStartTime).add(event.eventDuration,'m').toLocaleString("th-TH") && moment(eventStartTime).add(event.eventDuration,'m').toLocaleString("th-TH") > moment(event.eventStartTime).toLocaleString("th-TH"))
      return true;
    else
      return false
    })
  }

  //Sort
  const getSortAsc = (event=[]) => {
    return event.sort((a,b) => moment(a.eventStartTime) - moment(b.eventStartTime))
  };
  const getSortDesc = (event=[]) => {
    return event.sort((a,b) => moment(b.eventStartTime) - moment(a.eventStartTime))
  };

  return {
    eventLists,
    filterEventLists,
    getEventLists,
    getAllEventLists,
    getFilterEvent,
    listsNewEvent,
    createEvent,
    showEmptyEvent,
    showEmptyFilterEvent,
    getEmptyEvent,
    getEmptyFilterEvent,
    getOverlapTime,
    page,
    NextPage,
    BackPage,
    popUp,textPopUp,showPopUp,disShowPopUp,showText,filter,
    getSortAsc,getSortDesc
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