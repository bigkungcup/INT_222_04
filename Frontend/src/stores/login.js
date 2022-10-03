import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import "@vuepic/vue-datepicker/dist/main.css";
import moment from "moment";
import router from "../router";

//-----------------------------------------------------------------------------------
export const useLogin = defineStore("login", () => {
  const token = ref();
  const popUp = ref(false);
  const matchPassword = ref(true);
  const matchEmail = ref(true);
  const logoutPopup = ref(false);
  const logoutIcon = ref(false);
  const noAuthentication = ref(true);
  const userPage = ref(false);
  const categoryList = ref();
  const listsNewLecturerCategory = ref([]);

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

  const getNameToken = () => {
    return localStorage.getItem("name");
  };

  const setNameToken = (token) => {
    localStorage.setItem("name", token);
  };

  const getIdToken = () => {
    return localStorage.getItem("id");
  };

  const setIdToken = (token) => {
    localStorage.setItem("id", token);
  };

  const resetToken = () => {
    localStorage.removeItem("role");
    localStorage.removeItem("email");
    localStorage.removeItem("name");
    localStorage.removeItem("id");
  };

  const delete_cookie = (name) => {
    document.cookie =
      name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  };

  //log out
  const logout = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token/remove`, {
      method: "POST",
      headers: { "content-type": "application/json" },
    });
    if (res.status === 200) {
      userPage.value = false;
      logoutPopup.value = false;
      logoutIcon.value = false;
      delete_cookie("refresh_token");
      delete_cookie("access_token");
      resetToken();
      router.push({ name: "Login" });
    } else console.log();
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
      getRefresh();
      matchPassword.value = true;
      matchEmail.value = true;
      logoutIcon.value = true;
      popUp.value = true;
      console.log("Password Matched");
    } else if (res.status === 401) {
      matchPassword.value = false;
      matchEmail.value = true;
      console.log("Password NOT Matched");
    } else if (res.status === 404) {
      matchEmail.value = false;
      matchPassword.value = true;
      console.log("A user with the specified email DOES NOT exist");
    }
  };

  const getRefresh = async (getAll) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token/refresh`, {
      method: "GET",
      headers: {
        "content-type": "application/json",
      },
    });
    if (res.status === 200) {
      token.value = await res.json();
      console.log(token.value);
      resetToken();
      setRoleToken(token.value.role);
      setEmailToken(token.value.email);
      setNameToken(token.value.name);
      setIdToken(token.value.id);
      if (localStorage.getItem('role') == "admin") {
        userPage.value = true;
      }
      getAll;
      console.log("Refresh token success");
    } else if (res.status === 401) {
      logout();
      console.log("Refresh token not success");
    }
  };


  const addLecturerCategory = async (userId, eventCategoryId) => {
    const res = fetch(
      `${
        import.meta.env.VITE_BASE_URL
      }/users/register/${userId}/${eventCategoryId}`,
      {
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
      }
    );
    if (res.status === 200) {
      const addCategory = await res.json();
      listsNewLecturerCategory.value.push(addCategory);
      login.noAuthentication = true;
      console.log("created successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(addLecturerCategory(userId, eventCategory, x));
      login.noAuthentication = false;
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = false;
    } else {
      console.log("error, cannot create");
    }
  };

  const deleteLecturerCategory = (userId, eventCategoryId) => {
    const res = fetch(
      `${
        import.meta.env.VITE_BASE_URL
      }/users/${userId}/eventCategory/${eventCategoryId}`,
      {
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      categoryList.value.content = categoryList.value.content.filter(
        (category) => category.eventCategoryId !== eventCategoryId
      );
      noAuthentication.value = true;
      console.log("delete category success");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(removeUser());
      noAuthentication.value = false;
    } else if (res.status === 401 && login.logoutIcon == false) {
      noAuthentication.value = false;
    } else console.log("Get category list not success");
  };

  return {
    handleLogin,
    getIdToken,
    getRoleToken,
    getEmailToken,
    getNameToken,
    logout,
    getRefresh,
    addLecturerCategory,
    deleteLecturerCategory,
    popUp,
    matchEmail,
    matchPassword,
    token,
    logoutPopup,
    logoutIcon,
    userPage,
    noAuthentication,
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
