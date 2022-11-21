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
      {
        method: "GET",
      }
    );
    if (res.status === 200) {
      userList.value = await res.json();
      console.log("get user lists successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserList());
    } else if (res.status === 401 && login.logoutIcon == false) {
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
      userListAll.value = await res.json();
      console.log("get all user lists successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserAll());
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot get user lists");
  };

  //Get User Detail
  const getUserDetail = async (userId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users/${userId}`,
      {
        method: "GET",
      }
    );
    if (res.status === 200) {
      displayUser.value = await res.json();
      console.log("get successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getUserDetail(userId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot get user");
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
        email: newUser.value.email.match(validEmail)
          ? newUser.value.email
          : null,
        role: newUser.value.role,
        password: newUser.value.password == confirmPassword.value ? newUser.value.password : null,
      }),
    });
    if (res.status === 201) {
      const addUser = await res.json();
      userList.value.push(addUser);
      if (newUser.value.role == "lecturer") {
        addLecturerClinic(addUser.id, lecturerClinic.value);
      }
      signUpSuccessfully.value = true;
      resetNewUser();
      getUserAll();
      console.log("created successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(signUp());
    } else if (res.status === 400) {
      signUpValidate.value = false;
    } else if (res.status === 500) {
      userUnique.value = false;
    } 
    else {
      console.log("error, cannot create");
    }
  };

    //Edit User
    const saveUser = async (userId) => {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${userId}`, {
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
        console.log(newUserClinic.value);
        if (displayUser.value.role == "lecturer" && newUserClinic.value != '') {
          addLecturerClinic(userId,newUserClinic.value);
        }
        editUserSuccessfully.value = true;
        console.log("edit successfully");
      } else if (res.status === 401 && login.logoutIcon == true) {
        login.getRefresh(saveUser(userId));
      } else if(res.status === 401 && login.logoutIcon == false){
      } else if (res.status === 400) {
        editValidate.value = false;
      } else if (res.status === 500) {
        userUnique.value = false;
      } else {
        console.log("error, cannot edit");
      }
    };

  //Add Lecturer Clinics
  const addLecturerClinic = async (userId,lecturerClinicId) => {
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

  //Delete Lecturer Clinics
  const deleteLecturerClinic = (userId, userClinicId) => {
    const res = fetch(
      `${
        import.meta.env.VITE_BASE_URL
      }/users/${userId}/eventCategory/${userClinicId}`,
      {
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      displayUser.value = displayUser.value.eventCategories.filter(
        (category) => category.eventCategoryId !== userClinicId
      );
      console.log("Delete category success");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(deleteLecturerClinic(userId, userClinicId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("Delete category not success");
  };


  //Delete User
  const removeUser = async (userId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/users/${userId}`,
      {
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      userList.value.content = userList.value.content.filter(
        (user) => user.id !== userId
      );
      console.log("deleteted succesfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(removeUser(userId));
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot delete");
    getUserList(userList.value.pageNumber);
    if(userList.value.content.length == 0 && userList.value.pageNumber > 0){
      userList.value.pageNumber = userList.value.pageNumber-1;
      getUserList(userList.value.pageNumber);
    }
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
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot));
}
