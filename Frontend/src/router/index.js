import { createRouter, createWebHashHistory } from "vue-router";
import Home from "../view/Home.vue";
import Event from "../view/Event.vue";
import Booking from "../view/Booking.vue";
import EventDetail from "../view/EventDetail.vue";
import NotFound from "../view/NotFound.vue";

const history = createWebHashHistory('/kw4');
// const history = createWebHashHistory();
const routes = [
  {
    path: "/:catchNotMatchPath(.*)",
    name: "NotFound",
    component: NotFound,
  },
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/event",
    name: "Event",
    component: Event,
  },
  {
    path: "/booking",
    name: "Booking",
    component: Booking,
  },
  {
    path: "/event/:id",
    name: "EventDetail",
    component: EventDetail,
  },
];

const router = createRouter({ history, routes });
export default router;
