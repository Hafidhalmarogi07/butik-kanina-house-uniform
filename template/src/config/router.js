const Dashboard = () => import('@pages/Dashboard.vue')
const DashboardGudang = () => import('@pages/DashboardGudang.vue')
const DashboardToko = () => import('@pages/DashboardToko.vue')
const Error404 = () => import('@pages/Error404.vue')
const Error500 = () => import('@pages/Error500.vue')
const Login = () => import('@pages/Login.vue')
const Register = () => import('@pages/Register.vue')
const ForgotPassword = () => import('@pages/ForgotPassword.vue')
const RecoverPassword = () => import('@pages/RecoverPassword.vue')
const Main = () => import('@pages/Main.vue')
const Profile = () => import('@pages/Profile.vue')
const Navbar = () => import('@components/Navbar.vue')
const Sidebar = () => import('@components/Sidebar.vue')
const Header = () => import('@components/Header.vue')
const Footer = () => import('@components/Footer.vue')

// Owner Dashboard Pages
const Products = () => import('@pages/Products.vue')
const Category = () => import('@pages/Category.vue')
const Sales = () => import('@pages/Sales.vue')
const Orders = () => import('@pages/Orders.vue')
const Production = () => import('@pages/Production.vue')
const Expenses = () => import('@pages/Expenses.vue')
const Customers = () => import('@pages/Customers.vue')
const Reports = () => import('@pages/Reports.vue')
const Users = () => import('@pages/Users.vue')
const Returns = () => import('@pages/Returns.vue')

const mainPages = [
    {
        path: '/',
        name: 'dashboard',
        component: Dashboard,
        meta: {
            title: "Dashboard",
            subtitle: "overview"
        }
    }, {
        path: '/dashboard-gudang',
        name: 'dashboard_gudang',
        component: DashboardGudang,
        meta: {
            title: "Warehouse Dashboard",
            subtitle: "warehouse overview"
        }
    }, {
        path: '/dashboard-toko',
        name: 'dashboard_toko',
        component: DashboardToko,
        meta: {
            title: "Store Dashboard",
            subtitle: "store overview"
        }
    }, {
        path: '/products',
        name: 'products',
        component: Products,
        meta: {
            title: "Products",
            subtitle: "manage products"
        }
    }, {
        path: '/category',
        name: 'category',
        component: Category,
        meta: {
            title: "Categories",
            subtitle: "manage categories"
        }
    }, {
        path: '/sales',
        name: 'sales',
        component: Sales,
        meta: {
            title: "Sales",
            subtitle: "manage sales"
        }
    }, {
        path: '/orders',
        name: 'orders',
        component: Orders,
        meta: {
            title: "Orders",
            subtitle: "manage orders"
        }
    }, {
        path: '/production',
        name: 'production',
        component: Production,
        meta: {
            title: "Production",
            subtitle: "manage production"
        }
    }, {
        path: '/expenses',
        name: 'expenses',
        component: Expenses,
        meta: {
            title: "Expenses",
            subtitle: "manage expenses"
        }
    }, {
        path: '/customers',
        name: 'customers',
        component: Customers,
        meta: {
            title: "Customers",
            subtitle: "manage customers"
        }
    }, {
        path: '/reports',
        name: 'reports',
        component: Reports,
        meta: {
            title: "Reports",
            subtitle: "view reports"
        }
    }, {
        path: '/users',
        name: 'users',
        component: Users,
        meta: {
            title: "Users",
            subtitle: "manage admins"
        }
    }, {
        path: '/returns',
        name: 'returns',
        component: Returns,
        meta: {
            title: "Returns",
            subtitle: "manage returns"
        }
    }, {
        path: '/error404',
        name: 'error404',
        component: Error404
    }, {
        path: '/error500',
        name: 'error500',
        component: Error500
    }, {
        path: '/users/me',
        name: 'profile',
        component: Profile,
        meta: {
            title: "Profile",
            subtitle: "my profile"
        }
    }
]

export default {
    routes: [
        {
            path: '',
            component: {
                template: '<router-view></router-view>'
            },
            children: [
                {
                    path: '',
                    component: Main,
                    children: [
                        {
                            path: '',
                            components: {
                                default: {
                                    template: '<router-view></router-view>'
                                },
                                sidebar: Sidebar,
                                navbar: Navbar,
                                header: Header,
                                footer: Footer
                            },
                            children: mainPages
                        }
                    ]
                }, {
                    path: '/login',
                    name: 'login',
                    component: Login
                }, {
                    path: '/register',
                    name: 'register',
                    component: Register
                }, {
                    path: '/forgot-password',
                    name: 'forgot_password',
                    component: ForgotPassword
                }, {
                    path: '/recover-password',
                    name: 'recover_password',
                    component: RecoverPassword
                }
            ]
        }
    ]
}
