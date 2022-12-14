import { createRouter, createWebHashHistory } from "vue-router";
import NotFound from "../view/NotFound.vue";
import Home from "../view/Home.vue";
import Clinics from "../view/Clinics.vue";
import Events from "../view/Events.vue";
import Booking from "../view/Booking.vue";
import EventDetail from "../view/EventDetail.vue";
import AboutUs from "../view/AboutUs.vue"
import Users from "../view/Users.vue"
import UserDetail from "../view/UserDetail.vue"
import Login from "../view/Login.vue"
import SignUp from "../view/SignUp.vue"
import Profile from "../view/Profile.vue"

const history = createWebHashHistory('/kw4');
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
    path: "/clinics",
    name: "Clinics",
    component: Clinics,
  },
  {
    path: "/events",
    name: "Events",
    component: Events,
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
    path: "/aboutUs",
    name: "AboutUs",
    component: AboutUs,
  },
  {
    path: "/users",
    name: "Users",
    component: Users,
  },
  {
    path: "/user/:id",
    name: "UserDetail",
    component: UserDetail,
  },
  {
    path: "/signUp",
    name: "SignUp",
    component: SignUp
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
