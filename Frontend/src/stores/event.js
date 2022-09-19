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
  const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  const noAuthentication = ref(true);

  //Get Event
  const getEventLists = async (page=0) => {
    const res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events?page=${page}`,
        {
          method: "GET",
          headers: {
            "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
        })
      if (res.status === 200) {
        noAuthentication.value = true
        eventLists.value = await res.json();
        console.log("get event lists successfully");
      }else if (res.status === 401) {
        noAuthentication.value = false;
      }  else console.log("error, cannot get event lists");
  };

    //Get All Event
    const getAllEventLists = async () => {
      const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/events/eventAll`,
          {
            method: "GET",
            headers: {
              "Authorization":`Bearer ${localStorage.getItem('jwt')}`},
          })
        if (res.status === 200) {
          eventListAll.value = await res.json();
          console.log("get all event lists successfully");
        } else console.log("error, cannot get event lists");
    };

    //  Get Filter Event
  const getFilterEvent = async (page=0) => {
    let res 
    if(filter.value == 1){
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/pastEvents?page=${page}`,
        {
          method: "GET",
          headers: {
            "Authorization":`Bearer ${localStorage.getItem('jwt')}`},
        })
    }
    else if(filter.value == 2){
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/upComingEvents?page=${page}`,
        {
          method: "GET",
          headers: {
            "Authorization":`Bearer ${localStorage.getItem('jwt')}`},
        })
    }
    else if(filter.value == 3){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/1?page=${page}`,
      {
        method: "GET",
        headers: {
          "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
      })
    }
    else if(filter.value == 4){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/2?page=${page}`,
      {
        method: "GET",
        headers: {
          "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
      })
    }
    else if(filter.value == 5){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/3?page=${page}`,
      {
        method: "GET",
        headers: {
          "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
      })
    }
    else if(filter.value == 6){
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/4?page=${page}`,
      {
        method: "GET",
        headers: {
          "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
      })
    }
    else{
      res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventByCategory/5?page=${page}`,
      {
        method: "GET",
        headers: {
          "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
      })
    }
    if (res.status === 200) {
      filterEventLists.value = await res.json();
      console.log("get filter event lists successfully");
    } else console.log("error, cannot get event lists")
};

  //Create Event
  const createEvent = async (newEvent) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`, {
      method: "POST",
      headers: { 
        "content-type": "application/json",
        "Authorization":`Bearer ${localStorage.getItem('jwt')}`},
      body: JSON.stringify({
        bookingName: newEvent.bookingName,
        bookingEmail: newEvent.bookingEmail.match(validEmail)
          ? newEvent.bookingEmail
          : null,
        eventCategoryId: newEvent.eventCategory.id,
        eventStartTime: getOverlapTime(newEvent.eventStartTime,newEvent.eventCategory.id) ? newEvent.eventStartTime = "overlap" : newEvent.eventStartTime,
        eventNotes: newEvent.eventNotes,
        eventDuration: newEvent.eventDuration,
      }),
    });
    if (res.status === 201) {
      const addEvent = await res.json();
      listsNewEvent.value.push(addEvent);
      showPopUp();
      getAllEventLists();
      console.log("created successfully");
    } else {
      console.log("error, cannot create");
      showText();
    }
  };

  //ShowEmpty
  const getEmptyEvent = async () => {
    if (eventLists.value.content.length === 0 ) {
      showEmptyEvent.value = true;
    } 
    else if (eventLists.value.content.length !== 0) {
      showEmptyEvent.value = false;
    }
  };

  const getEmptyFilterEvent = async (filterEvent=[]) => {
    if (filterEvent.length === 0 ) {
      showEmptyFilterEvent.value = true;
    } 
    else if (filterEvent.length !== 0) {
      showEmptyFilterEvent.value = false;
    }
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
  const getOverlapTime = (eventStartTime,category) => {
    let listAll
    listAll = eventListAll.value.filter(a => a.eventCategory.id == category);
    return listAll.some((event) => {
    if(moment(eventStartTime).toLocaleString("th-TH") <= moment(event.eventStartTime).add(event.eventDuration,'m').toLocaleString("th-TH") && moment(eventStartTime).add(event.eventDuration,'m').toLocaleString("th-TH") >= moment(event.eventStartTime).toLocaleString("th-TH"))
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
    getSortAsc,getSortDesc,
    noAuthentication
  };
});

//-----------------------------------------------------------------------------------
export const useEventCategory = defineStore("eventCategory", () => {
  const categoryLists = ref([]);
  const noAuthentication = ref(true);

  //Get Category
  const getEventCategory = async () => {
    console.log(localStorage.getItem('jtw'));
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories`, {
      method: "GET",
      headers: {
        "Authorization":`Bearer ${localStorage.getItem('jwt')}`},
    })
    if (res.status === 200) {
      noAuthentication.value = true;
      categoryLists.value = await res.json();
      console.log("get category lists successfully");
    }else if (res.status === 401) {
      noAuthentication.value = false;
    } 
    else console.log("error, cannot get event category lists");
  };
  return { categoryLists, getEventCategory,noAuthentication };
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
  const noAuthentication = ref(true);

  //Get User
  const getUserList = async (page=0) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users?page=${page}`, {
      method: "GET",
      headers: {
        "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
    })
    if (res.status === 200) {
      noAuthentication.value = true;
      userList.value = await res.json();
      console.log("get user lists successfully");
    }else if (res.status === 401) {
      noAuthentication.value = false;
    }  else console.log("error, cannot get user lists");
  };

    //Get All User
    const getUserAll = async () => {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/userAll`, {
        method: "GET",
        headers: {
          "Authorization":`Bearer ${localStorage.getItem('jwt')}`}
      })
      if (res.status === 200) {
        userAll.value = await res.json();
        console.log("get all user lists successfully");
      } else console.log("error, cannot get user lists");
    };

    //Create User
    const createUser = async (newUser) => {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/register`, {
        method: "POST",
        headers: { 
          "content-type": "application/json",
          "Authorization":`Bearer ${localStorage.getItem('jwt')}`},
        body: JSON.stringify({
          name: newUser.name,
          email: newUser.email.match(validEmail)
          ? newUser.email
          : null,
          role: newUser.role,
          password: newUser.password
        }),
      });
      if (res.status === 201) {
        const addUser = await res.json();
        listsNewUser.value.push(addUser);
        getUserAll();
        showPopUp();
        console.log("created successfully");
      } else {
        console.log("error, cannot create");
        showText();
      }
    };

  //ShowEmpty
  const getEmptyUser = async () => {
    if (userList.value.content.length === 0 ) {
      showEmptyUser.value = true;
    } 
    else if (userList.value.content.length !== 0) {
      showEmptyUser.value = false;
    }
  };

  //Page
  const NextPage = () => {
      if (page.value < 0) {
        page.value = 0;
      }
      page.value += 1
      getUserList((page.value));
    };
    const BackPage = () => {
      if (page.value < 0) {
        page.value = 0;
      }
      page.value -= 1
      getUserList((page.value));
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
    noAuthentication };
});

//-----------------------------------------------------------------------------------
export const useLogin = defineStore("login", () => {
  const token = ref();
  const popUp = ref(false);
  const matchPassword = ref(true);
  const matchEmail = ref(true);
  const logoutPopup = ref(false);
  const logoutIcon = ref(false);

  const getJwtToken = () => {
    return localStorage.getItem("jwt")
}

const setJwtToken = (token) => {
  localStorage.setItem("jwt", token)
}

const resetJwtToken = () => {
  localStorage.removeItem("jwt")
}

const logout = () => {
  logoutPopup.value = false;
  logoutIcon.value = false;
  resetJwtToken();
}

    
  //Login
    const handleLogin = async (userAccount) => {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/login`, {
        method: "POST",
        headers: { "content-type": "application/json" },
        body: JSON.stringify({
          email: userAccount.email,
          password: userAccount.password,
        }),
      });
      if (res.status === 200) {
        // matchText.value = true;
        matchPassword.value=true;
        matchEmail.value=true;
        logoutIcon.value = true;
        popUp.value = true;
        token.value = await res.json()
        resetJwtToken()
        setJwtToken(token.value.token)
        console.log("Password Matched");
      } else if (res.status === 401) {
        // matchText.value = false;
        matchPassword.value=false;
        matchEmail.value=true;
        console.log("Password NOT Matched");
      }else if (res.status === 404) {
        // matchText.value = false;
        matchEmail.value=false;
        matchPassword.value=true;
        console.log("A user with the specified email DOES NOT exist");
      }
    };

    return { 
      handleLogin, getJwtToken,logout,popUp,matchEmail,matchPassword,token,logoutPopup,logoutIcon }
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(
    acceptHMRUpdate(useEvent, useEventCategory, useUser, useLogin, import.meta.hot)
  );
}