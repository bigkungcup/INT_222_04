import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import router from "../router";
import { myMSALObj,loginRequest } from "../services/authConfig.js";


export const useLogin = defineStore("Login", () => {
  const loginSuccessfully = ref(false);
  const token = ref();
  const logoutIcon = ref(localStorage.getItem("id") != null ? true : false);
  const msLogoutIcon = ref(false)
  const userPageIcon = ref(
    localStorage.getItem("role") == "admin" ? true : false
  );
  const logoutPopup = ref(false);
  const loginValidate = ref(true);
  const noAuthorization = ref(false);
  
  //เดี๋ยวก็ลบ
  const noAuthorizationAsGuest = ref(false);

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
    localStorage.setItem("id", token.id);
    localStorage.setItem("name", token.name);
    localStorage.setItem("email", token.email);
    localStorage.setItem("role", token.role);
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
      setToken(token.value);
      if (localStorage.getItem("role") == "admin") {
        userPageIcon.value = true;
      }
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
    localStorage.setItem("name", response.account.name);
    localStorage.setItem("email", response.account.userName);
    localStorage.setItem("role", response.account.idTokenClaims.roles == undefined ? "guest" : response.account.idTokenClaims.roles[0] ); 
    console.log(localStorage.getItem("email"));
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/loginWithMS`, {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({
        role: localStorage.getItem("role"),
        name: localStorage.getItem("name"),
        email: localStorage.getItem("email") 
      }),
    });
    if (res.status === 200) {
      token.value = await res.json();
      resetToken();
      setToken(token.value);
    if (localStorage.getItem("role") == "admin") {
      userPageIcon.value = true;
    }
    loginSuccessfully.value = true;
    logoutIcon.value = true; 
    msLogoutIcon.value = true;
  }};

  const msSignOut = () => {
    // const logoutRequest = {
    //   account: myMSALObj.getAccountByUsername(username),
    // };
    myMSALObj.logout();
    delete_cookie("refresh_token");
    delete_cookie("access_token");
    resetToken();
    logoutPopup.value = false;
    logoutIcon.value = false;
    userPageIcon.value = false;
    router.push({ name: "Login" });
  };

//   const msHandleResponse = (response) => {
//     console.log(response);

//     if (response !== null) {
//       resetToken();
//       localStorage.setItem("name", response.account.name);
//       localStorage.setItem("email", response.account.username);
//       localStorage.setItem("role", response.account.idTokenClaims.roles[0]);  
//       if (localStorage.getItem("role") == "admin") {
//         userPageIcon.value = true;
//       }
//       loginSuccessfully.value = true;
//       logoutIcon.value = true; 
//       msLogoutIcon.value = true;
//       console.log(response.account);
//     } else {
//       msSelectAccount();
//     }
//   };

//   const msSelectAccount = () => {
//     const currentAccounts = myMSALObj.getAllAccounts();

//     if (!currentAccounts  || currentAccounts.length < 1) {
//         return;
//     } else if (currentAccounts.length > 1) {
//         // Add your account choosing logic here
//         console.warn("Multiple accounts detected.");
//     } else if (currentAccounts.length === 1) {
//         username = currentAccounts[0].username;
//         // welcomeUser(username);
//     }
// }

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
    noAuthorization,
    noAuthorizationAsGuest
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
