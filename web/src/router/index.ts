import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from '../views/Home.vue'
import Doc from '../views/Doc.vue'
import AdminUser from '../views/admin/admin-user.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'
import store from "@/store";
import {Tool} from "@/until/tool";
import {message} from "ant-design-vue";


const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    },
    {
        path: '/admin/user',
        name: 'AdminUser',
        component: AdminUser,
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/ebook',
        name: 'AdminEbook',
        component: AdminEbook,
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/category',
        name: 'AdminCategory',
        component: AdminCategory,
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/doc',
        name: 'AdminDoc',
        component: AdminDoc,
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/doc',
        name: 'Doc',
        component: Doc,
        meta: {
            loginRequire: true
        }
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

//路由拦截器
router.beforeEach((to, from, next) => {
    //要不要对meta.loginRequire属性做监控拦截
    if (to.matched.some(function (item) {
        console.log(item, "是否需要登录校验", item.meta.loginRequire);
        return item.meta.loginRequire
    })) {
        const loginUser = store.state.user;
        if (Tool.isEmpty(loginUser)) {
            console.log("用户未登录！");
            message.error("用户未登录");
            next('/');
        } else {
            next();
        }
    } else {
        next();
    }
});
export default router