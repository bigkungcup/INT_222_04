import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useClinics = defineStore("Clinics", () => {
  const clinicList = ref([]);

  //Get Clinics
  const getClinics = async () => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/eventCategories`,
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
      clinicList.value = await res.json();
      console.log("get clinic list successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot get clinic list");
  };

  const filterClinics = (allClinicList,userClinicList) => {
    allClinicList = clinicList.value;
    for(let i = 0; i < userClinicList.eventCategories.length; i++ ) {
      allClinicList = allClinicList.filter(x => x.id != userClinicList.eventCategories[i].id);
      }
      return allClinicList
  }

  return { getClinics,filterClinics,clinicList };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useClinics, import.meta.hot));
}
