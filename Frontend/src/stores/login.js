import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import router from "../router";
import { myMSALObj,loginRequest } from "../services/authConfig.js";


export const useLogin = defineStore("Login", () => {
  const loginSuccessfully = ref(false);
  const token = ref();
  const logoutIcon = ref(localStorage.getItem("name") != null ? true : false);
  const userName = ref(localStorage.getItem("name"));
  const msLogoutIcon = ref(localStorage.getItem("msal.idtoken") != null ? true : false)
  const userPageIcon = ref(
    localStorage.getItem("role") == "admin" ? true : false
  );
  const logoutPopup = ref(false);
  const loginValidate = ref(true);
  const noAuthentication = ref(false);
  
  //เดี๋ยวก็ลบ
  const noAuthorization = ref(false);

  const loginAccount = ref({
    email: "",
    password: "",
  });

  const resetLoginAccount = () => {
    loginAccount.value = {
      email: "",
      password: "",
    };
    loginValidate.value = true;
  };

  const getRoleToken = () => {
    return localStorage.getItem("role");
  };

  const getEmailToken = () => {
    return localStorage.getItem("email");
  };

  const getNameToken = () => {
    return localStorage.getItem("name");
  };

  const getIdToken = () => {
    return localStorage.getItem("id");
  };


  const setToken = (token) => {
    // localStorage.setItem("id", token.id);
    localStorage.setItem("name", token.name);
    localStorage.setItem("email", token.email);
    localStorage.setItem("role", token.role);
  };

  const resetToken = () => {
    localStorage.removeItem("role");
    localStorage.removeItem("email");
    localStorage.removeItem("name");
    // localStorage.removeItem("id");
  };

  const delete_cookie = (name) => {
    document.cookie =
      name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  };

  //Login
  const handleLogin = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/login`, {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({
        email: loginAccount.value.email,
        password: loginAccount.value.password,
      }),
    });
    if (res.status === 200) {
      getRefresh();
      resetLoginAccount();
      loginSuccessfully.value = true;
      logoutIcon.value = true;
      console.log("Password Matched");
    } else if (res.status === 401) {
      loginValidate.value = false;
      console.log("Password NOT Matched");
    } else if (res.status === 404) {
      loginValidate.value = false;
      console.log("A user with the specified email DOES NOT exist");
    }
  };

  //Get refresh
  const getRefresh = async (getAll = null) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token/refresh`, {
      method: "GET",
      headers: {
        "content-type": "application/json",
      },
    });
    if (res.status === 200) {
      token.value = await res.json();
      resetToken();
      localStorage.removeItem("id");
      setToken(token.value);
      localStorage.setItem("id", token.value.id);
      if (localStorage.getItem("role") == "admin") {
        userPageIcon.value = true;
      }
      userName.value = getNameToken();
      getAll;
      console.log("Refresh token success");
    } else if (res.status === 401) {
      logout();
      console.log("Refresh token not success");
    }
  };

  //log out
  const logout = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token/remove`, {
      method: "POST",
      headers: { "content-type": "application/json" },
    });
    if (res.status === 200) {
      delete_cookie("refresh_token");
      delete_cookie("access_token");
      resetToken();
      localStorage.removeItem("id");
      logoutPopup.value = false;
      logoutIcon.value = false;
      userPageIcon.value = false;
      router.push({ name: "Login" });
    } else console.log();
  };

  //Microsoft
  const msSignIn = async () => {
    let response = await myMSALObj.loginPopup(loginRequest);
    console.log(response);
    resetToken();
    // localStorage.setItem("name", response.account.name);
    // localStorage.setItem("email", response.account.userName);
    // localStorage.setItem("role", response.account.idTokenClaims.roles == undefined ? "guest" : response.account.idTokenClaims.roles[0] ); 
    // console.log(localStorage.getItem("email"));
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/loginWithMS`, {
      method: "GET",
      headers: { 
        "content-type": "application/json" ,
        Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`
      },
      // body: JSON.stringify({
      //   role: localStorage.getItem("role"),
      //   name: localStorage.getItem("name"),
      //   email: localStorage.getItem("email") 
      // }),
    });
    if (res.status === 200) {
      token.value = await res.json();
      resetToken();
      setToken(token.value);
    if (localStorage.getItem("role") == "admin") {
      userPageIcon.value = true;
    };
    userName.value = getNameToken();
    loginSuccessfully.value = true;
    logoutIcon.value = true; 
  }};

  const msSignOut = async () => {
    myMSALObj.logout()
    logoutPopup.value = false;
    logoutIcon.value = false;
    userPageIcon.value = false;
    router.push({ name: "Login" });
    delete_cookie("refresh_token");
    delete_cookie("access_token");
    resetToken();
  };

  return {
    getRoleToken,
    getNameToken,
    getEmailToken,
    handleLogin,
    getRefresh,
    logout,
    resetLoginAccount,
    getIdToken,
    msSignIn,
    msSignOut,
    loginAccount,
    loginSuccessfully,
    logoutPopup,
    loginValidate,
    logoutIcon,
    msLogoutIcon,
    userPageIcon,
    token,
    noAuthentication,
    noAuthorization,
    userName
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
