import { createRouter, createWebHashHistory } from "vue-router";
import Home from "../view/Home.vue";
import Event from "../view/Event.vue";
import Booking from "../view/Booking.vue";
import EventDetail from "../view/EventDetail.vue";
import CategoryDetail from "../view/CategoryDetail.vue"
import AboutUs from "../view/AboutUs.vue"
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
  {
    path: "/category/:id",
    name: "CategoryDetail",
    component: CategoryDetail,
  },
  {
    path: "/aboutUs",
    name: "AboutUs",
    component: AboutUs,
  },
];

const router = createRouter({ history, routes });
export default router;
