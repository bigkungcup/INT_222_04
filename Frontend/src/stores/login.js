import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import router from "../router";

export const useLogin = defineStore("Login", () => {
  const loginSuccessfully = ref(false);
  const token = ref();
  const logoutIcon = ref(false)
  const logoutPopup = ref(false)

  const loginAccount = ref({
    email: "",
    password: "",
  });
  
  const resetLoginAccount = () => {
    loginAccount.value = {
      email: "",
      password: "",
    };
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
    //
      console.log("Password NOT Matched");
    } else if (res.status === 404) {
      //
      console.log("A user with the specified email DOES NOT exist");
    }
  };

  //Get refresh
  const getRefresh = async (getAll=null) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token/refresh`, {
      method: "GET",
      headers: {
        "content-type": "application/json",
      },
    });
    if (res.status === 200) {
      token.value = await res.json();
      resetToken();
      setToken(token.value)
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
          router.push({ name: "Login" });
        } else console.log();
      };

  return { handleLogin,getRefresh,logout,resetLoginAccount,getIdToken,loginAccount,loginSuccessfully,logoutPopup,logoutIcon,token };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useLogin, import.meta.hot));
}
