import LoginPage from "./components/LoginPage";
import CalcInfo from "./components/CalcInfo";
import SEAGrid from "./components/SEAGrid";

import {createRouter, createWebHistory} from 'vue-router';

const routes = [
    {
        name: 'Login',
        component: LoginPage,
        path:'/'
    },
    {
        name: 'CalcInfo',
        component: CalcInfo,
        path:'/calcinfo'
    },
    {
        name: 'Home',
        component: SEAGrid,
        path:'/SEAGrid'
    },
];

const router = createRouter(
    {
        history:createWebHistory(),
        routes
    }
)


export default router