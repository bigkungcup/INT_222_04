import { createRouter, createWebHistory } from "vue-router";
import Home from "../view/Home.vue";
import Event from "../view/Event.vue";
import Booking from "../view/Booking.vue";
import EventDetail from "../view/EventDetail.vue";
import CategoryDetail from "../view/CategoryDetail.vue"
import AboutUs from "../view/AboutUs.vue"
import NotFound from "../view/NotFound.vue";
import User from "../view/User.vue"
import UserDetail from "../view/UserDetail.vue"
import Register from "../view/Register.vue"
import Login from "../view/Login.vue"
import Profile from "../view/Profile.vue"

const history = createWebHistory('/kw4');
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
  {
    path: "/user",
    name: "User",
    component: User,
  },
  {
    path: "/user/:id",
    name: "UserDetail",
    component: UserDetail,
  },
  {
    path: "/register",
    name: "Register",
    component: Register
  },
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile
  }
];

const router = createRouter({ history, routes });
export default router;
