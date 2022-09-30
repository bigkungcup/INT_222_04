import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import "@vuepic/vue-datepicker/dist/main.css";
import moment from "moment";
import router from "../router";

//-----------------------------------------------------------------------------------
export const useLogin2 = defineStore("login", () => {
    const token = ref();
    const popUp = ref(false);
    const matchPassword = ref(true);
    const matchEmail = ref(true);
    const logoutPopup = ref(false);
    const logoutIcon = ref(false);
    // const accessTimeLimit = ref();
    // const refreshTimeLimit = ref();
    // const timeCheck = ref();
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
  
    const resetAccessToken = () => {
      localStorage.removeItem("jwt");
      localStorage.removeItem("refreshToken");
    }
  
    //log out
    const logout = () => {
      userPage.value = false;
      logoutPopup.value = false;
      logoutIcon.value = false;
      // window.clearInterval();
      resetJwtToken();
      // resetTimeLimit();
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
        // setTimeLimit();
        // window.setInterval(() => {
        //   getRefresh();
        // }, 10*1000);
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
        resetAccessToken();
        setJwtToken(token.value.access_token);
        setRefreshToken(token.value.refresh_token);
        console.log("Refresh token success");
        // console.log(getJwtToken()); 
      } else if (res.status === 401) {
        logout();
        console.log("Refresh token not success");
      } 
    };
  
    // const setTimeLimit = () => {
    //   const startTime = new Date();
    //   accessTimeLimit.value = new Date(startTime.getTime() + 10 * 1000);
    //   refreshTimeLimit.value = new Date(startTime.getTime() + 30 * 1000);
    // };
  
    // const resetTimeLimit = () => {
    //   accessTimeLimit.value = null;
    //   refreshTimeLimit.value = null;
    // };
  
    // const checkTokenExpired = () => {
    //   const now = new Date();
    //   const accessExpired = accessTimeLimit.value < now.getTime();
    //   const refreshExpired = refreshTimeLimit.value < now.getTime();
    //   if (token.value != null) {
    //     if (!refreshExpired) {
    //       console.log(accessExpired, refreshExpired);
    //       getRefresh();
    //     } 
    //     else if (refreshExpired) {
    //       console.log(refreshExpired);
    //       logout();
    //     }
    //   }
    // };
  
    return {
      handleLogin,
      getJwtToken,
      getRoleToken,
      getEmailToken,
      logout,
      getRefresh,
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
        useLogin2,
        import.meta.hot
      )
    );
  }