import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";
import moment from "moment";
import { useLogin } from "./Login.js";
import { useClinics } from "./Clinics.js";

export const useEvents = defineStore("Events", () => {
  const login = useLogin()
  const clinic = useClinics()
  const bookingSeccessfully = ref(false);
  const bookingValidate = ref(true)
  const editValidate = ref(true)
  const deletePopup = ref(false);
  const editField = ref(false);
  const editTime = ref(false);
  const editFile = ref(false)
  const editFileName = ref()
  const validEmail =
    /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  const eventFile = ref()
  const editEventFile = ref()
  const fileUrl = ref()
  const showFileName = ref(false)
  const showErrorFileText = ref(false)
  const eventList = ref([]);
  const eventListAll = ref([]);
  const deleteFileCheck = ref(false)

  const newEvent = ref({
    bookingName: "",
    bookingEmail: login.getRoleToken() == null || login.getRoleToken() == 'admin' ? "" : login.getEmailToken(),
    eventCategory: clinic.clinicList[0],
    eventStartTime: "",
    eventNotes: "",
    eventDuration: 0,
  });

  const displayEvent = ref({
    bookingName: "",
    bookingEmail: "",
    eventCategory: {},
    eventStartTime: "",
    eventNotes: "",
    eventDuration: 0,
  });

  const editEvent = ref({
    eventCategory: {},
    eventStartTime: "",
    eventNotes: "",
  });

  //Get Event
  const getEventList = async (page = 0) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events?page=${page}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      }:{
        method: "GET",
      }
    );
    if (res.status === 200) {
      eventList.value = await res.json();
      if(eventList.value.numberOfElements == 0 && eventList.value.pageNumber > 0){
        getEventList(eventList.value.pageNumber - 1);
      }
      console.log("get event list successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getEventList((page = 0)));
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = true;
    }
    console.log("error, cannot get event list");
  };

  //Get All Event
  const getAllEventList = async () => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/eventAll`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      }:{
        method: "GET",
      }
    );
    if (res.status === 200) {
      eventListAll.value = await res.json();
      console.log("get all event list successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getAllEventList());
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot get all event list");
  };

  //Get Filter Event
  const getFilterEventList = async (clinicId = 0,time = 'all',page = 0) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/filter/?eventCategoryId=${clinicId}&time=${time}&page=${page}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`}
      }:{
        method: "GET",
      }
    );
    if (res.status === 200) {
      eventList.value = await res.json();
      console.log("get event lists successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getFilterEventList((page = 0)));
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = true;
    } 

    //เดี๋ยวก็ลบ
    else if (res.status === 403 && login.logoutIcon == true) {
      login.noAuthorization = true;
    } 
    
    console.log("error, cannot get event lists");
  };

  //get event detail
  const getEventDetail = async (id) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${id}`, 
    localStorage.getItem("msal.idtoken") != null ?{
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
      }
    }:{
      method: "GET",
    });
    if (res.status === 200) {
      displayEvent.value = await res.json();
      console.log(displayEvent.value);
      console.log("get successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(getEventDetail(id));
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = true;
    } else console.log("error, cannot get event");
  };

    //Download File
    const getDownloadFile = async (eventId,fileName) => {
      const res = await fetch(
        `${import.meta.env.VITE_BASE_URL}/files/${eventId}/${fileName}`,
        localStorage.getItem("msal.idtoken") != null ?{
          method: "GET",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
          }
        }:{
          method: "GET",
        }
      );
      if (res.status === 200) {
        window.open(res.url,'_blank')
        console.log("Download file successfully");
      } else if (res.status === 401 && login.logoutIcon == true) {
        login.getRefresh(getDownloadFile(eventId,fileName));
      } else if (res.status === 401 && login.logoutIcon == false) {
      } else console.log("error, cannot get all event list");
    };

  //Create Event
  const createEvent = async () => {
    console.log(getOverlapTime(
      newEvent.value.eventStartTime,
      newEvent.value.eventCategory.id
    ));
    const event = {
        bookingName: newEvent.value.bookingName,
        bookingEmail: newEvent.value.bookingEmail.match(validEmail)
          ? newEvent.value.bookingEmail
          : null,
        eventCategoryId: newEvent.value.eventCategory.id,
        eventStartTime: getOverlapTime(
          newEvent.value.eventStartTime,
          newEvent.value.eventCategory.id
        )
          ? (newEvent.value.eventStartTime = "overlap")
          : newEvent.value.eventStartTime,
        eventNotes: newEvent.value.eventNotes,
        eventDuration: newEvent.value.eventCategory.eventDuration,
      }
    
    const formData = new FormData();
    formData.append('event',new Blob([JSON.stringify(event)],{ type: 'application/json' }))
    formData.append('file',eventFile.value == null ? document.getElementById("file").setCustomValidity('') : eventFile.value)
    console.log(formData);

    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`, 
    localStorage.getItem("msal.idtoken") != null ?{
      method: "POST",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
      },
      body: formData
    }:{
      method: "POST",
      body: formData
    });
    if (res.status === 201) {
      const addEvent = await res.json();
      // eventListAll.value.push(addEvent);
      // getAllEventList();
      bookingSeccessfully.value = true;
      console.log("created successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(createEvent());
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = true;
    } else if (res.status === 417) {
    }else if (res.status === 400) {
      bookingValidate.value = false;
    }else {
      console.log("error, cannot create");
    }
  };

      //Create Event With Guest
      const createEventWithGuest = async () => {
        const event = {
          bookingName: newEvent.value.bookingName,
          bookingEmail: newEvent.value.bookingEmail.match(validEmail)
            ? newEvent.value.bookingEmail
            : null,
          eventCategoryId: newEvent.value.eventCategory.id,
          eventStartTime: getOverlapTime(
            newEvent.value.eventStartTime,
            newEvent.value.eventCategory.id
          )
            ? (newEvent.value.eventStartTime = "overlap")
            : newEvent.value.eventStartTime,
          eventNotes: newEvent.value.eventNotes,
          eventDuration: newEvent.value.eventCategory.eventDuration,
        }
      
      const formData = new FormData();
      formData.append('event',new Blob([JSON.stringify(event)],{ type: 'application/json' }))
      formData.append('file',eventFile.value == null ? undefined : eventFile.value)
      console.log(formData);

        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/guest`, 
        localStorage.getItem("msal.idtoken") != null ? {
          method: "POST",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
          },
          body: formData,
        }:{
          method: "POST",
          body: formData,
        });
        if (res.status === 201) {
          const addEvent = await res.json();
          bookingSeccessfully.value = true;
          // listsNewEvent.value.push(addEvent);
          console.log("created successfully");
        }else if (res.status === 401 && login.logoutIcon == true) {
          login.getRefresh(createEventWithGuest());
        } else if(res.status === 401 && login.logoutIcon == false){
          login.noAuthentication = true;
        }else if (res.status === 400) {
          bookingValidate.value = false;
        }
         else {
          console.log("error, cannot create");
        }
      };

  //Delete Event
  const removeEvent = async (selectClinic,selectTime,eventId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/${eventId}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      }:{
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      eventList.value.content = eventList.value.content.filter(
        (event) => event.id !== eventId
      );
      deletePopup.value = false;
      getFilterEventList(selectClinic,selectTime,eventList.value.pageNumber);
      if(eventList.value.content.length == 0 && eventList.value.pageNumber > 0){
        eventList.value.pageNumber = eventList.value.pageNumber-1;
        getFilterEventList(selectClinic,selectTime,eventList.value.pageNumber);
      }
      console.log("deleteted succesfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(removeEvent(eventId));
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = true;
    } else console.log("error, cannot delete");
  };

  //Delete File
  const removeFile = async (eventId) => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/files/${eventId}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        }
      }:{
        method: "DELETE",
      }
    );
    if (res.status === 200) {
      // eventList.value.content = eventList.value.content.filter(
      //   (event) => event.id !== eventId
      // );
      // if (editEvent.eventStartTime !== "" || editEvent.eventNotes !== "") {
      //   showEditPopUp();
      // }
      await getEventDetail(eventId);
      editFile.value = false;
      resetEditFile();
      editValidate.value = true;
      deleteFileCheck.value = false;
      editFileName.value = displayEvent.value.fileName;
      console.log("edit successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(removeFile(eventId));
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = true;
    } else if (res.status === 400) {
      editValidate.value = false;
    }else {
      console.log("error, cannot edit");
    }
  };

  //Edit Event
  const saveEvent = async (id) => {
    const event = {
      // bookingName: displayEvent.value.bookingName,
      // bookingEmail: displayEvent.value.bookingEmail,
      eventCategory: editEvent.value.eventCategory === {} ? displayEvent.value.eventCategory : editEvent.value.eventCategory,
      eventStartTime:
        editEvent.value.eventStartTime === ""
          ? displayEvent.value.eventStartTime
          : getOverlapTime(
              editEvent.value.eventStartTime,
              displayEvent.value.eventCategory.id
            )
          ? (editEvent.value.eventStartTime = "overlap")
          : editEvent.value.eventStartTime,
      eventNotes:
        editEvent.value.eventNotes === ""
          ? displayEvent.value.eventNotes
          : editEvent.value.eventNotes,
      eventDuration: displayEvent.value.eventDuration,
    }

    console.log(editEventFile.value);
    const formData = new FormData();
    formData.append('event',new Blob([JSON.stringify(event)],{ type: 'application/json' }))
    formData.append('file',editEventFile.value == null ? undefined : editEventFile.value)
    console.log(editEventFile.value);
    console.log(formData);

    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/events/${id}`,
      localStorage.getItem("msal.idtoken") != null ? {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("msal.idtoken")}`,
        },
        body: formData
      }:{
        method: "PUT",
        body: formData
      }
    );
    if (res.status === 200) {
      let event = await res.json();
      // if (editEvent.eventStartTime !== "" || editEvent.eventNotes !== "") {
      //   showEditPopUp();
      // }
      await getEventDetail(id);
      editFile.value = false;
      resetEditField();
      resetEditTime();
      resetEditFile();
      editValidate.value = true;
      editFileName.value = displayEvent.value.fileName;
      console.log("edit successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
      login.getRefresh(saveEvent(id));
    } else if (res.status === 401 && login.logoutIcon == false) {
      login.noAuthentication = true;
    } else if (res.status === 400) {
      editValidate.value = false;
    }else {
      console.log("error, cannot edit");
    }
  };

  const resetNewEvent = () => {
    newEvent.value = {
      bookingName: "",
      bookingEmail: login.getRoleToken() == null || login.getRoleToken() == 'admin' ? "" : login.getEmailToken(),
      eventCategory: clinic.clinicList[0],
      eventStartTime: "",
      eventNotes: "",
      eventDuration: 0,
    };
    resetNewEventFile();
    bookingValidate.value = true;
  };

  const resetNewEventFile = () => {
    eventFile.value = null;
    showFileName.value = false;
    showErrorFileText.value = false;
    document.getElementById("file").value = null;
  }

  const resetEditField = () => {
    editEvent.value.eventCategory = displayEvent.value.eventCategory;
    editEvent.value.eventNotes = "";
    editField.value = false;
  }

  const resetEditTime = () => {
    editEvent.value.eventStartTime = "";
    editTime.value = false;
  }

  const resetEditFile = () => {
        if(editFileName.value != ''){
      editFileName.value = '';
    }
    editEventFile.value = null;
    showFileName.value = false;
    showErrorFileText.value = false;
    document.getElementById("file").value = null;
  }

  //Choose File
  const chooseFile = () => {
    let fileName = document.getElementById("filename")
    let inputFile = document.querySelector("input[type=file]").files[0];
    let fileSize = ((inputFile.size/1024)/1024); //Mb

    if(fileSize <= 10.00 && fileSize > 0){
      eventFile.value = inputFile;
      fileName.innerText = inputFile.name;
      showErrorFileText.value = false;
      showFileName.value = true;
      deleteFileCheck.value = false;
    } else {
      showErrorFileText.value = true;
      if(eventFile.value == null){
        showFileName.value = false;
      } else {showFileName.value = true;} 
    }
  }

    //Choose File
    const chooseEditFile = () => {
      let fileName = document.getElementById("filename")
      let inputFile = document.querySelector("input[type=file]").files[0];
      let fileSize = ((inputFile.size/1024)/1024); //Mb
  
      if(fileSize <= 10.00 && fileSize > 0){
        editEventFile.value = inputFile;
        fileName.innerText = inputFile.name;
        showErrorFileText.value = false;
        showFileName.value = true;
        editFileName.value = '';
        deleteFileCheck.value = false;
      } else {
        showErrorFileText.value = true;
        if(editEventFile.value == null){
          showFileName.value = false;
        } else {showFileName.value = true;} 
      }
    }

  const setMinTime = (eventStartTime) => {
    newEvent.value.eventStartTime = moment(eventStartTime).isAfter(
      moment(new Date())
    )
      ? eventStartTime
      : "previous time";
    console.log(newEvent.value.eventStartTime);
  };

  // Get Overlap Time
  const getOverlapTime = (eventStartTime, category) => {
    let listAll;
    getAllEventList();
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

  //Page
  const NextPage = (selectClinic,selectTime) => {
    if (eventList.value.pageNumber < 0) {
      eventList.value.pageNumber = 0;
    }
    getFilterEventList(selectClinic,selectTime,eventList.value.pageNumber + 1);
  };
  const BackPage = (selectClinic,selectTime) => {
    if (eventList.value.pageNumber < 0) {
      eventList.value.pageNumber = 0;
    }
    getFilterEventList(selectClinic,selectTime,eventList.value.pageNumber - 1);
  };

  return {
    getAllEventList,
    getEventList,
    getFilterEventList,
    getEventDetail,
    getDownloadFile,
    createEvent,
    createEventWithGuest,
    chooseFile,
    chooseEditFile,
    // createFileUrl,
    resetNewEvent,
    resetNewEventFile,
    resetEditField,
    resetEditTime,
    resetEditFile,
    removeEvent,
    removeFile,
    saveEvent,
    setMinTime,
    NextPage,
    BackPage,
    eventList,
    eventListAll,
    newEvent,
    displayEvent,
    editEvent,
    bookingSeccessfully,
    bookingValidate,
    editValidate,
    deletePopup,
    editField,
    editTime,
    editFile,
    editFileName,
    showFileName,
    showErrorFileText,
    eventFile,
    editEventFile,
    fileUrl,
    validEmail,
    deleteFileCheck 
  };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useEvents, import.meta.hot));
}
