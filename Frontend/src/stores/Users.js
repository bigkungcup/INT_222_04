import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import { useLogin } from "./Login.js";

export const useUsers = defineStore("Users", () => {
  const login = useLogin()
  const deletePopup = ref(false);
  const editUserField = ref(false);
  const userList = ref([]);
  const userListAll = ref([]);
  const newUserClinic = ref('');
  const clinicList = ref([]);
  const confirmPassword = ref('');
  const lecturerClinic = ref(1);
  const validEmail =
    /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  const signUpValidate = ref(true)
  const editValidate = ref(true)
  const userUnique = ref(true)
  const signUpSuccessfully = ref(false);
  const editUserSuccessfully = ref(false);
  const checkEvent = ref(false);
  const undeletePopup = ref(false);

  const newUser = ref({
    name: "",
    email: "",
    role: "student",
    password: "",
  });

  const displayUser = ref({
    id: 0,
    name: "",
    email: "",
    role: "",
    eventCategories: [],
  });

  const editUser = ref({
    name: "",
    email: "",
    role: "",
    eventCategories: [],
  });

  const resetNewUser = () => {
    newUser.value = {
      name: "",
      email: "",
      role: "student",
      password: "",
    };
    confirmPassword.value = "";
    lecturerClinic.value = 1;
    signUpValidate.value = true;
    userUnique.value = true;
  };

  const resetEditUser = () =>{
    editUser.value = {
      name: "",
      email: "",
      role: displayUser.value.role,
      eventCategories: [],
    },
    newUserClinic.value = ""
    editUserField.value = false;
    signUpValidate.value = true;
    editValidate.value = true;
    userUnique.value = true;
  };

  //Get User Page
  const getUserList = async (page = 0) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users?page=${page}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      } : {
        method: "GET",
      }
    );
    if (res.status === 200) {
      userList.value = await res.json();
      deletePopup.value = false;
      undeletePopup.value = false;
      console.log("get user list successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserList());
    } else if (res.status === 401 && login.logoutIcon == false) {
    }
    // else if (res.status === 403) {}
    else console.log("error, cannot get user list");
  };

  //Get All User
  const getUserAll = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/userAll`, 
    localStorage.getItem("msal.idtoken") != null ? {
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
      }
    } : {
      method: "GET",
    });
    if (res.status === 200) {
      userListAll.value = await res.json();
      console.log("get all user list successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserAll());
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot get all user list");
  };

  //Get User Detail
  const getUserDetail = async (userId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users/${userId}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      } : {
        method: "GET",
      }
    );
    if (res.status === 200) {
      displayUser.value = await res.json();
      console.log("get user detail successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserDetail(userId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot get user user detail");
  };

  //Create User
  const signUp = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/register`, 
    localStorage.getItem("msal.idtoken") != null ? {
      method: "POST",
      headers: {
        "content-type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
      },
      body: JSON.stringify({
        name: newUser.value.name,
        email: newUser.value.email.match(validEmail)
          ? newUser.value.email
          : null,
        role: newUser.value.role,
        password: newUser.value.password == confirmPassword.value ? newUser.value.password : null,
      }),
    }:{
      method: "POST",
      headers: {
        "content-type": "application/json",
      },
      body: JSON.stringify({
        name: newUser.value.name,
        email: newUser.value.email.match(validEmail)
          ? newUser.value.email
          : null,
        role: newUser.value.role,
        password: newUser.value.password == confirmPassword.value ? newUser.value.password : null,
      }),
    });
    if (res.status === 201) {
      const addUser = await res.json();
      // if (newUser.value.role == "lecturer") {
      //   addLecturerClinic(addUser.id, lecturerClinic.value);
      // }
      signUpSuccessfully.value = true;
      resetNewUser();
      console.log("created user successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(signUp());
    } else if (res.status === 400) {
      signUpValidate.value = false;
    } else if (res.status === 500) {
      userUnique.value = false;
    } 
    else {
      console.log("error, cannot create user");
    }
  };

    //Edit User
    const saveUser = async (userId) => {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${userId}`, 
      localStorage.getItem("msal.idtoken") != null ? {
        method: "PUT",
        headers: {
          "content-type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        },
        body: JSON.stringify({
          name: editUser.value.name === "" ? displayUser.value.name : editUser.value.name,
          email: editUser.value.email === "" ? displayUser.value.email : 
                  editUser.value.email.match(validEmail) ? editUser.value.email : null,
          role: editUser.value.role === "" ? displayUser.value.role : editUser.value.role
        }),
      } : {
        method: "PUT",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          name: editUser.value.name === "" ? displayUser.value.name : editUser.value.name,
          email: editUser.value.email === "" ? displayUser.value.email : 
                  editUser.value.email.match(validEmail) ? editUser.value.email : null,
          role: editUser.value.role === "" ? displayUser.value.role : editUser.value.role
        }),
      });
      if (res.status === 200) {
        displayUser.value = await res.json();
        if (displayUser.value.role == "lecturer" && newUserClinic.value != '') {
          addLecturerClinic(userId,newUserClinic.value);
        }
        editUserSuccessfully.value = true;
        console.log("edit user successfully");
      } else if (res.status === 401 && login.logoutIcon == true) {
        login.getRefresh(saveUser(userId));
      } else if(res.status === 401 && login.logoutIcon == false){
      } else if (res.status === 400) {
        editValidate.value = false;
      } else if (res.status === 500) {
        userUnique.value = false;
      } else {
        console.log("error, cannot edit user");
      }
    };

  //Add Lecturer Clinics
  const addLecturerClinic = async (userId,lecturerClinicId) => {
    const res = fetch(
      `${
        import.meta.env.VITE_BASE_URL
      }/users/registerCategory/${userId}/${lecturerClinicId}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "POST",
        headers: {
          "content-type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        },
      } : {
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
      }
    );
    if (res.status === 200) {
      console.log("created lecturer clinic successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(addLecturerClinic(userId,lecturerClinicId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else {
      console.log("error, cannot create lecturer clinic");
    }
  };

  //Delete Lecturer Clinics
  const deleteLecturerClinic = (userId, userClinicId) => {
    const res = fetch(
      `${
        import.meta.env.VITE_BASE_URL
      }/users/${userId}/eventCategory/${userClinicId}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      } : {
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      displayUser.value = displayUser.value.eventCategories.filter(
        (category) => category.eventCategoryId !== userClinicId
      );
      console.log("Delete lecturer category successfully");
      }else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(deleteLecturerClinic(userId, userClinicId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot delete lecturer category");
  };


  //Delete User
  const removeUser = async (userId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users/${userId}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      } : {
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      userList.value.content = userList.value.content.filter(
        (user) => user.id !== userId
      );
      getUserList(userList.value.pageNumber);
      console.log("deletet user succesfully");
    }else if(res.status === 400 && login.logoutIcon == true){
      undeletePopup.value = true;
  }  else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(removeUser(userId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot delete user");
    if(userList.value.content.length == 0 && userList.value.pageNumber > 0){
      userList.value.pageNumber = userList.value.pageNumber-1;
      getUserList(userList.value.pageNumber);
    }
  };

      //Check event of user
      const checkUserEvent = async (userId) => {
        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/users/checkEvent/${userId}`,
          localStorage.getItem("msal.idtoken") != null ? {
            method: "GET",
            headers: {
              Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
            }
          } : {
            method: "GET",
          }
        );
        if (res.status === 200) {
          checkEvent.value = await res.json();
          deletePopup.value = true;
          console.log("check user event succesfully");
        } else if (res.status === 401 && login.logoutIcon == true) {
          login.getRefresh(checkUserEvent(userId));
        } else if (res.status === 401 && login.logoutIcon == false) {
        }
        else console.log("error, cannot check user event");
      };

  const checkBeforeEdit = () => {
    let check = false
    if((editUser.value.name == displayUser.value.name || editUser.value.name == "") && (editUser.value.email == displayUser.value.email || editUser.value.email == "") && (editUser.value.role == displayUser.value.role || editUser.value.role == "")){
      if (displayUser.value.role == "lecturer" && newUserClinic.value != '') {
        addLecturerClinic(displayUser.value.id,newUserClinic.value);
        editUserSuccessfully.value = true;
      }
      check = true
    };
    return check
  }

    //Page
    const NextPage = () => {
      if (userList.value.pageNumber < 0) {
        userList.value.pageNumber = 0;
      }
      getUserList(userList.value.pageNumber + 1);
    };
    const BackPage = () => {
      if (userList.value.pageNumber < 0) {
        userList.value.pageNumber = 0;
      }
      getUserList(userList.value.pageNumber - 1);
    };

  return {
    getUserList,
    getUserDetail,
    signUp,
    saveUser,
    addLecturerClinic,
    deleteLecturerClinic,
    removeUser,
    resetNewUser,
    resetEditUser,
    checkUserEvent,
    checkBeforeEdit,
    NextPage,
    BackPage,
    userList,
    newUser,
    displayUser,
    editUser,
    newUserClinic,
    clinicList,
    validEmail,
    confirmPassword,
    lecturerClinic,
    signUpValidate,
    editValidate,
    userUnique,
    signUpSuccessfully,
    editUserSuccessfully,
    deletePopup,
    editUserField,
    checkEvent,
    undeletePopup
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot));
}
