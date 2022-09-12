import Vue from 'vue'
import Vuex from 'vuex'
import custosStore from "airavata-custos-portal/src/lib/store";
import App from './App.vue'
import router from "./router";
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'
import JsonCSV from 'vue-json-csv'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'


Vue.use(Vuex);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.component('downloadCsv', JsonCSV)
new Vue({
    router,
    custosStore,
    render: h => h(App),
}).$mount('#app')

