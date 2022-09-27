import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import "@vuepic/vue-datepicker/dist/main.css";
import moment from "moment";
import router from "../router";

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
  const noAuthentication = ref(true);

  //Get Event
  const getEventLists = async (page = 0) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events?page=${page}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      }
    );
    if (res.status === 200) {
      noAuthentication.value = true;
      eventLists.value = await res.json();
      console.log("get event lists successfully");
    } else if (res.status === 401) {
      noAuthentication.value = false;
    } else console.log("error, cannot get event lists");
  };

  //Get All Event
  const getAllEventLists = async () => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventAll`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      }
    );
    if (res.status === 200) {
      eventListAll.value = await res.json();
      console.log("get all event lists successfully");
    } else console.log("error, cannot get event lists");
  };

  //  Get Filter Event
  const getFilterEvent = async (page = 0) => {
    let res;
    if (filter.value == 1) {
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/pastEvents?page=${page}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwt")}`,
          },
        }
      );
    } else if (filter.value == 2) {
      res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/events/upComingEvents?page=${page}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwt")}`,
          },
        }
      );
    } else if (filter.value == 3) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/1?page=${page}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwt")}`,
          },
        }
      );
    } else if (filter.value == 4) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/2?page=${page}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwt")}`,
          },
        }
      );
    } else if (filter.value == 5) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/3?page=${page}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwt")}`,
          },
        }
      );
    } else if (filter.value == 6) {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/4?page=${page}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwt")}`,
          },
        }
      );
    } else {
      res = await fetch(
        `${
          import.meta.env.VITE_BASE_URL
        }/events/eventByCategory/5?page=${page}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwt")}`,
          },
        }
      );
    }
    if (res.status === 200) {
      filterEventLists.value = await res.json();
      console.log("get filter event lists successfully");
    } else console.log("error, cannot get event lists");
  };

  //Create Event
  const createEvent = async (newEvent) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("jwt")}`,
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
    noAuthentication,
  };
});

//-----------------------------------------------------------------------------------
export const useEventCategory = defineStore("eventCategory", () => {
  const categoryLists = ref([]);
  const noAuthentication = ref(true);

  //Get Category
  const getEventCategory = async () => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/eventCategories`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      }
    );
    if (res.status === 200) {
      noAuthentication.value = true;
      categoryLists.value = await res.json();
      console.log("get category lists successfully");
    } else if (res.status === 401) {
      noAuthentication.value = false;
    } else console.log("error, cannot get event category lists");
  };
  return { categoryLists, getEventCategory, noAuthentication };
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
  const validEmail =
    /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  const noAuthentication = ref(true);
  const passwordMatchText = ref(false);
  const passwordNoMatchText = ref(false);

  //Get User
  const getUserList = async (page = 0) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users?page=${page}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      }
    );
    if (res.status === 200) {
      noAuthentication.value = true;
      userList.value = await res.json();
      console.log("get user lists successfully");
    } else if (res.status === 401) {
      noAuthentication.value = false;
    } 
    // else if (res.status === 403) {} 
    else console.log("error, cannot get user lists");
  };

  //Get All User
  const getUserAll = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/userAll`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("jwt")}`,
      },
    });
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
        Authorization: `Bearer ${localStorage.getItem("jwt")}`,
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
      getUserAll();
      showPopUp();
      console.log("created successfully");
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
        Authorization: `Bearer ${localStorage.getItem("jwt")}`,
      },
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    });
    if (res.status === 200) {
      passwordMatchText.value = true;
      passwordNoMatchText.value = false
      console.log("Password match");
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
    noAuthentication,
    checkPassword,
    passwordMatchText,
    passwordNoMatchText,
    resetMatchText
  };
});

//-----------------------------------------------------------------------------------
export const useLogin = defineStore("login", () => {
  const token = ref();
  const popUp = ref(false);
  const matchPassword = ref(true);
  const matchEmail = ref(true);
  const logoutPopup = ref(false);
  const logoutIcon = ref(false);
  const accessTimeLimit = ref();
  const refreshTimeLimit = ref();
  const timeCheck = ref();
  const userPage = ref(false);

  const getJwtToken = () => {
    return localStorage.getItem("jwt");
  };

  const setJwtToken = (token) => {
    localStorage.setItem("jwt", token);
  };

  const getRefreshToken = () => {
    return localStorage.getItem("refreshToken");
  };

  const setRefreshToken = (token) => {
    localStorage.setItem("refreshToken", token);
  };

  const getRoleToken = () => {
    return localStorage.getItem("role");
  };

  const setRoleToken = (token) => {
    localStorage.setItem("role", token);
  };

  const getEmailToken = () => {
    return localStorage.getItem("email");
  };

  const setEmailToken = (token) => {
    localStorage.setItem("email", token);
  };

  const resetJwtToken = () => {
    localStorage.removeItem("jwt");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("role");
    localStorage.removeItem("email");
  };

  //log out
  const logout = () => {
    userPage.value = false;
    logoutPopup.value = false;
    logoutIcon.value = false;
    clearInterval(timeCheck.value);
    resetJwtToken();
    resetTimeLimit();
    router.push({ name: 'Login'});
  };

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
      matchPassword.value = true;
      matchEmail.value = true;
      logoutIcon.value = true;
      popUp.value = true;
      token.value = await res.json();
      resetJwtToken();
      setJwtToken(token.value.access_token);
      setRoleToken(token.value.role);
      setRefreshToken(token.value.refresh_token);
      setEmailToken(token.value.email);
      setTimeLimit();
      timeCheck.value = setInterval(() => {
        checkTokenExpired();
      }, 30 * 60 * 1000 + 1);
      if(getRoleToken() == '[admin]'){
        userPage.value = true;
      }
      console.log("Password Matched");
    } else if (res.status === 401) {
      // matchText.value = false;
      matchPassword.value = false;
      matchEmail.value = true;
      console.log("Password NOT Matched");
    } else if (res.status === 404) {
      // matchText.value = false;
      matchEmail.value = false;
      matchPassword.value = true;
      console.log("A user with the specified email DOES NOT exist");
    }
  };

  const getRefresh = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token/refresh`, {
      method: "GET",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("refreshToken")}`,
      },
    });
    if (res.status === 200) {
      token.value = await res.json();
      resetJwtToken();
      setJwtToken(token.value.access_token);
      setRefreshToken(token.value.refresh_token);
      console.log("Refresh token success");
    } else if (res.status === 401) {
      console.log("Refresh token not success");
    }
  };

  const setTimeLimit = () => {
    const startTime = new Date();
    accessTimeLimit.value = new Date(startTime.getTime() + 30 * 60 * 1000);
    refreshTimeLimit.value = new Date(startTime.getTime() + 24 * 60 * 60 * 1000);
  };

  const resetTimeLimit = () => {
    accessTimeLimit.value = null;
    refreshTimeLimit.value = null;
  };

  const checkTokenExpired = () => {
    const now = new Date();
    const accessExpired = accessTimeLimit.value < now.getTime();
    const refreshExpired = refreshTimeLimit.value < now.getTime();
    if (token.value != null) {
      if (accessExpired && !refreshExpired) {
        console.log(accessExpired, refreshExpired);
        getRefresh();
      } else if (refreshExpired) {
        console.log(refreshExpired);
        logout();
      }
    }
  };

  return {
    handleLogin,
    getJwtToken,
    getRoleToken,
    getEmailToken,
    logout,
    popUp,
    matchEmail,
    matchPassword,
    token,
    logoutPopup,
    logoutIcon,
    userPage,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(
    acceptHMRUpdate(
      useEvent,
      useEventCategory,
      useUser,
      useLogin,
      import.meta.hot
    )
  );
}
