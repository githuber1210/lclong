import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/commmon/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/commmon/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/commmon/404.vue')
  },
  {
    path: '/mailLogin',
    name: '/MailLogin',
    component: () => import('../views/commmon/MailLogin.vue')
  },
  {
    path: '/telLogin',
    name: '/TelLogin',
    component: () => import('../views/commmon/TelLogin.vue')
  },
  {
    path: '/',
    name: 'Index',
    redirect: '/home',
    component: () => import('../views/home/container/container.vue'),
    children:  [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/home/index/index.vue')
      },
      {
        path: '/blogs',
        name: 'Blogs',
        component: () => import('../views/home/blogs/index.vue')
      },
      {
        path: '/blogsDetail',
        name: 'BlogsDetail',
        component: () => import('../views/home/blogsDetail/index.vue')
      },
      {
        path: '/product',
        name: 'Product',
        component: () => import('../views/home/product/index.vue')
      },
      {
        path: '/product',
        name: 'Product',
        component: () => import('../views/home/product/index.vue')
      },
      {
        path: '/material',
        name: 'Material',
        component: () => import('../views/home/material/index.vue')
      },
      {
        path: '/material',
        name: 'Material',
        component: () => import('../views/home/material/index.vue')
      },
      {
        path: '/notice',
        name: 'Notice',
        component: () => import('../views/home/notice/index.vue')
      },
      {
        path: '/description',
        name: 'Description',
        component: () => import('../views/home/description/index.vue')
      },
      { path: '/noticeDetail',
        name: 'NoticeDetail',
        component:()=> import('../views/home/noticeDetail/index.vue')
      }


    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Container')) {
      const manageRoute =
          {
            path: '/admin',
            name: 'Container',
            component: () => import('../views/management/Layout/Container.vue'),
            redirect: "/admin/home",
            children: [
              { path: 'person',
                name: '个人中心',
                component: () => import('../views/management/person/index.vue')
              },
              { path: 'noticeDetail',
                name:'博客详情页',
                component:()=> import('../views/management/noticeDetail/index.vue')
              }
            ] }
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        if (item.path) {
          let itemMenu = {
            path: item.path,
            name: item.name,
            component: () => import('../views/management/' + item.component + '/index.vue')
          }
          manageRoute.children.push(itemMenu)
        } else if(item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              let itemMenu = {
                path: item.path,
                name: item.name,
                component: () => import('../views/management/' + item.component + '/index.vue')
              }
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      router.addRoute(manageRoute)
    }

  }
}

setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentMenu", to.name)
  store.commit("setPath")
  if (!to.matched.length) {
    const menus = localStorage.getItem("menus")
    if (menus) {
      next("/404")
    } else {
      next("/login")
    }
  }
  next()
})
export default router
