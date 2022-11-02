import { defineStore, acceptHMRUpdate } from "pinia";
import { ref } from "vue";

export const useClinics = defineStore("Clinics", () => {
  const clinicList = ref([]);

  //Get Clinics
  const getClinics = async () => {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/eventCategories`,
      {
        method: "GET",
      }
    );
    if (res.status === 200) {
      clinicList.value = await res.json();
      console.log("get clinic list successfully");
    } else if (res.status === 401 && login.logoutIcon == true) {
    } else if (res.status === 401 && login.logoutIcon == false) {
    } else console.log("error, cannot get event clinics list");
  };
  return { getClinics,clinicList };
});

//-----------------------------------------------------------------------------------
if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useClinics, import.meta.hot));
}
