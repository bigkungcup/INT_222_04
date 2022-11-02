import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useUsers = defineStore("Users", () => {
  const userList = ref([]);
  const userListAll = ref([]);
  const confirmPassword = ref();
  const lecturerClinic = ref();
  const validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  const signUpSuccessfully = ref(false)

  const newUser = ref({
    name: "",
    email: "",
    role: "student",
    password: "",
  });
  
  const resetNewUser = () => {
    newUser.value = {
      name: "",
      email: "",
      role: "student",
      password: "",
    };
    confirmPassword.value = "";
    lecturerClinic.value = "";
  };

  //Get All User
  const getUserAll = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/userAll`, {
      method: "GET",
    });
    if (res.status === 200) {
      userListAll.value = await res.json();
      console.log("get all user lists successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserAll());
    } else if(res.status === 401 && login.logoutIcon == false){
    } else console.log("error, cannot get user lists");
  };

  //Create User
  const signUp = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/register`, {
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        name: newUser.value.name,
        email: newUser.value.email.match(validEmail) ? newUser.value.email : null,
        role: newUser.value.role,
        password: newUser.value.password,
      }),
    });
    if (res.status === 201) {
      const addUser = await res.json();
      userList.value.push(addUser);
      if(newUser.value.role == 'lecturer'){
        addLecturerClinic(addUser.id,lecturerClinic.value)
      };
      signUpSuccessfully.value = true;
      resetNewUser();
      getUserAll();
      console.log("created successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(signUp(newUser));
    } else if(res.status === 401){
    } else {
      console.log("error, cannot create");
    }
  };

  //Add lecturer category
  const addLecturerClinic = async (userId, lecturerClinicId) => {
    const res = fetch(
      `${
        import.meta.env.VITE_BASE_URL
      }/users/register/${userId}/${lecturerClinicId}`,
      {
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
      }
    );
    if (res.status === 200) {
      console.log("created successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(addLecturerClinic(userId,lecturerClinicId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else {
      console.log("error, cannot create");
    }
  };
    
  return { signUp,addLecturerClinic,resetNewUser,newUser,confirmPassword,lecturerClinic,signUpSuccessfully };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot));
}
