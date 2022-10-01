import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import "@vuepic/vue-datepicker/dist/main.css";
import moment from "moment";
import router from "../router";
import { useLogin } from "./login.js";

export const useEvent = defineStore("event", () => {
  const eventLists = ref([]);
  const eventListAll = ref([]);
  const filterEventLists = ref([]);
  const listsNewEvent = ref([]);
  const showEmptyEvent = ref(false);
  const showEmptyFilterEvent = ref(false);
  const filter = ref(0);
  const page = ref(0);
  const popUp = ref(false);
  const textPopUp = ref(false);
  const validEmail =
    /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  // const noAuthentication = ref(true);
  const login = useLogin();

  //Get Event
  const getEventLists = async (page = 0) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events?page=${page}`,
      {
        method: "GET",
      }
    );
    if (res.status === 200) {
      login.noAuthentication = true;
      eventLists.value = await res.json();
      console.log("get event lists successfully");
    }
    else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getEventLists(page = 0));
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    }console.log("error, cannot get event lists");
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
      login.noAuthentication = true;
      console.log("get all event lists successfully");
    }else if (res.status === 401 && login.logoutIcon == true) {
        login.getRefresh(getAllEventLists());
        login.noAuthentication = false;
      } else if(res.status === 401 && login.logoutIcon == false){
        login.noAuthentication = false;
      }else console.log("error, cannot get event lists");
  };

  //  Get Filter Event
  const getFilterEvent = async (page = 0) => {
    let res;
    if (filter.value == 1) {
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/pastEvents?page=${page}`,
        {
          method: "GET",
        }
      );
    } else if (filter.value == 2) {
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/upComingEvents?page=${page}`,
        {
          method: "GET",
        }
      );
    } else if (filter.value == 3) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/1?page=${page}`,
        {
          method: "GET",
        }
      );
    } else if (filter.value == 4) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/2?page=${page}`,
        {
          method: "GET",
        }
      );
    } else if (filter.value == 5) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/3?page=${page}`,
        {
          method: "GET",
        }
      );
    } else if (filter.value == 6) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/4?page=${page}`,
        {
          method: "GET",
        }
      );
    } else {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/5?page=${page}`,
        {
          method: "GET",
        }
      );
    }
    if (res.status === 200) {
      filterEventLists.value = await res.json();
      login.noAuthentication = true;
      console.log("get filter event lists successfully");
    }else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getFilterEvent());
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else console.log("error, cannot get event lists");
  };

  //Create Event
  const createEvent = async (newEvent) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        bookingName: newEvent.bookingName,
        bookingEmail: newEvent.bookingEmail.match(validEmail)
          ? newEvent.bookingEmail
          : null,
        eventCategoryId: newEvent.eventCategory.id,
        eventStartTime: getOverlapTime(
          newEvent.eventStartTime,
          newEvent.eventCategory.id
        )
          ? (newEvent.eventStartTime = "overlap")
          : newEvent.eventStartTime,
        eventNotes: newEvent.eventNotes,
        eventDuration: newEvent.eventDuration,
      }),
    });
    if (res.status === 201) {
      const addEvent = await res.json();
      login.noAuthentication = true;
      listsNewEvent.value.push(addEvent);
      showPopUp();
      getAllEventLists();
      console.log("created successfully");
    }else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(createEvent(newEvent));
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else {
      console.log("error, cannot create");
      showText();
    }
  };

  //ShowEmpty
  const getEmptyEvent = async () => {
    if (eventLists.value.content.length === 0) {
      showEmptyEvent.value = true;
    } else if (eventLists.value.content.length !== 0) {
      showEmptyEvent.value = false;
    }
  };

  const getEmptyFilterEvent = async (filterEvent = []) => {
    if (filterEvent.length === 0) {
      showEmptyFilterEvent.value = true;
    } else if (filterEvent.length !== 0) {
      showEmptyFilterEvent.value = false;
    }
  };

  //Page
  const NextPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value += 1;
    getEventLists(page.value);
    getFilterEvent(page.value);
  };
  const BackPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value -= 1;
    getEventLists(page.value);
    getFilterEvent(page.value);
  };

  //pop-up
  const showPopUp = () => {
    popUp.value = true;
  };
  const disShowPopUp = () => {
    popUp.value = false;
    textPopUp.value = false;
  };

  //Text pop-up
  const showText = () => {
    textPopUp.value = true;
  };

  // Get Overlap Time
  const getOverlapTime = (eventStartTime, category) => {
    let listAll;
    listAll = eventListAll.value.filter((a) => a.eventCategory.id == category);
    return listAll.some((event) => {
      if (
        moment(eventStartTime).toLocaleString("th-TH") <=
          moment(event.eventStartTime)
            .add(event.eventDuration, "m")
            .toLocaleString("th-TH") &&
        moment(eventStartTime)
          .add(event.eventDuration, "m")
          .toLocaleString("th-TH") >=
          moment(event.eventStartTime).toLocaleString("th-TH")
      )
        return true;
      else return false;
    });
  };

  //Sort
  const getSortAsc = (event = []) => {
    return event.sort(
      (a, b) => moment(a.eventStartTime) - moment(b.eventStartTime)
    );
  };
  const getSortDesc = (event = []) => {
    return event.sort(
      (a, b) => moment(b.eventStartTime) - moment(a.eventStartTime)
    );
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
    popUp,
    textPopUp,
    showPopUp,
    disShowPopUp,
    showText,
    filter,
    getSortAsc,
    getSortDesc,
    // noAuthentication,
  };
});

//-----------------------------------------------------------------------------------
export const useEventCategory = defineStore("eventCategory", () => {
  const categoryLists = ref([]);
  // const noAuthentication = ref(true);
  const login = useLogin();

  //Get Category
  const getEventCategory = async () => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/eventCategories`,
      {
        method: "GET",
      }
    );
    if (res.status === 200) {
      // noAuthentication.value = true;
      categoryLists.value = await res.json();
      login.noAuthentication = true;
      console.log("get category lists successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getEventCategory());
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else console.log("error, cannot get event category lists");
  };
  return { categoryLists, getEventCategory, 
    // noAuthentication 
  };
});

//-----------------------------------------------------------------------------------
export const useUser = defineStore("user", () => {
  const userList = ref([]);
  const userAll = ref([]);
  const page = ref(0);
  const showEmptyUser = ref(false);
  const listsNewUser = ref([]);
  const popUp = ref(false);
  const textPopUp = ref(false);
  const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  // const noAuthentication = ref(true);
  const passwordMatchText = ref(false);
  const passwordNoMatchText = ref(false);
  const login = useLogin();

  //Get User
  const getUserList = async (page = 0) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users?page=${page}`,
      {
        method: "GET",
      }
    );
    if (res.status === 200) {
      login.noAuthentication = true;
      userList.value = await res.json();
      console.log("get user lists successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserList());
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } 
    // else if (res.status === 403) {} 
    else console.log("error, cannot get user lists");
  };

  //Get All User
  const getUserAll = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/userAll`, {
      method: "GET",
    });
    if (res.status === 200) {
      userAll.value = await res.json();
      login.noAuthentication = true;
      console.log("get all user lists successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserAll());
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else console.log("error, cannot get user lists");
  };

  //Create User
  const createUser = async (newUser) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/register`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        name: newUser.name,
        email: newUser.email.match(validEmail) ? newUser.email : null,
        role: newUser.role,
        password: newUser.password,
      }),
    });
    if (res.status === 201) {
      const addUser = await res.json();
      listsNewUser.value.push(addUser);
      login.noAuthentication = true;
      getUserAll();
      showPopUp();
      console.log("created successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(createUser(newUser));
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else {
      console.log("error, cannot create");
      showText();
    }
  };

  // Check Password(Admin)
  const checkPassword = async (email,password) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/match`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    });
    if (res.status === 200) {
      passwordMatchText.value = true;
      passwordNoMatchText.value = false
      login.noAuthentication = true;
      console.log("Password match");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(checkPassword(email,password));
      login.noAuthentication = false;
    } else if(res.status === 401 && login.logoutIcon == false){
      login.noAuthentication = false;
    } else {
      passwordNoMatchText.value = true;
      passwordMatchText.value = false;
      console.log("Password did not match");
    }
  };

  const resetMatchText = () => {
    passwordNoMatchText.value = false;
    passwordMatchText.value = false;
  }

  //ShowEmpty
  const getEmptyUser = async () => {
    if (userList.value.content.length === 0) {
      showEmptyUser.value = true;
    } else if (userList.value.content.length !== 0) {
      showEmptyUser.value = false;
    }
  };

  //Page
  const NextPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value += 1;
    getUserList(page.value);
  };
  const BackPage = () => {
    if (page.value < 0) {
      page.value = 0;
    }
    page.value -= 1;
    getUserList(page.value);
  };

  //pop-up
  const showPopUp = () => {
    popUp.value = true;
  };
  const disShowPopUp = () => {
    popUp.value = false;
    textPopUp.value = false;
  };

  //Text pop-up
  const showText = () => {
    textPopUp.value = true;
  };

  return {
    userList,
    getUserList,
    userAll,
    getUserAll,
    page,
    NextPage,
    BackPage,
    getEmptyUser,
    showEmptyUser,
    createUser,
    showPopUp,
    disShowPopUp,
    showText,
    listsNewUser,
    popUp,
    textPopUp,
    // noAuthentication,
    checkPassword,
    passwordMatchText,
    passwordNoMatchText,
    resetMatchText
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(
    acceptHMRUpdate(
      useEvent,
      useEventCategory,
      useUser,
      import.meta.hot
    )
  );
}
