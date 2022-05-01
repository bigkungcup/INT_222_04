import { createRouter, createWebHashHistory } from "vue-router";
import Schedule from "../view/Schedule.vue";
import Booking from "../view/Booking.vue";
import ScheduleDetail from "../view/ScheduleDetail.vue";
import NotFound from "../view/NotFound.vue";

const history = createWebHashHistory();
const routes = [
  {
    path: "/:catchNotMatchPath(.*)",
    name: "NotFound",
    component: NotFound,
  },
  {
    path: "/",
    name: "Schedule",
    component: Schedule,
  },
  {
    path: "/booking",
    name: "Booking",
    component: Booking,
  },
  {
    path: "/schedule/:id",
    name: "ScheduleDetail",
    component: ScheduleDetail,
  },
];

const router = createRouter({ history, routes });
export default router;
