import LoginPage from "./components/LoginPage";
import CalcInfo from "./components/CalcInfo";

import {createRouter, createWebHistory} from 'vue-router';

const routes = [
    {
        name: 'Home',
        component: LoginPage,
        path:'/'
    },
    {
        name: 'CalcInfo',
        component: CalcInfo,
        path:'/calcinfo'
    }
];

const router = createRouter(
    {
        history:createWebHistory(),
        routes
    }
)

export default router