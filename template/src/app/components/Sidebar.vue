<template>
    <aside class="main-sidebar sidebar-light-primary elevation-4">
        <router-link :to="dashboardRoute" class="brand-link navbar-white">
            <img src="@assets/logo.png" :alt="env.app_name" class="brand-image bg-gray img-circle elevation-1"
            style="opacity: .8">
            <span class="brand-text font-weight-light">{{ env.app_name }}</span>
        </router-link>

        <div class="sidebar">
<!--            <div class="user-panel mt-3 pb-3 mb-3 d-flex">-->
<!--                <div class="image">-->
<!--                    <img src="@assets/unknown.jpeg" v-if="!user_image" class="img-circle elevation-2" alt="User Image">-->
<!--                    <img :src="user_image" v-if="user_image" class="img-circle elevation-1" alt="User Image">-->
<!--                </div>-->
<!--                <div class="info">-->
<!--                    <router-link :to="dashboardRoute" class="d-block ellipsis capitalize">{{ user.full_name }}</router-link>-->
<!--                </div>-->
<!--            </div>-->
            <nav class="mt-2">
                <ul class="nav nav-pills nav-sidebar flex-column nav-flat nav-child-indent" data-widget="treeview" role="menu" data-accordion="false">
                    <nav-item v-for="(item, index) in items" :key="index" :item="item" :routeName="routeName"></nav-item>
                </ul>
            </nav>
        </div>
    </aside>
</template>

<script>
import NavItem from './NavItem.vue'
import SidebarConfig from '@config/sidebar.json'
const {console} = window

export default {
    components: {
        NavItem
    },
    data: () => ({
        routeName: 'undefined',
        layout: 'fixed',
        user: {},
        user_image: null,
        items: [],
        dashboardRoute: { name: 'dashboard' }
    }),
    mounted() {

        this.pushMenuSetup()

        // $('body').attr('class', 'sidebar-mini layout-fixed text-sm')
        $('body').attr('class', 'sidebar-mini layout-navbar-fixed text-sm')
        $('[data-widget="pushmenu"]').closest('.nav-item').show()

        this.routeName = this.$route.name
        this.setTitle(this.$route)
        this.$router.afterHooks = []
        this.$router.afterEach((nw, old) => {
            $('.modal-backdrop').remove()
            $('.sidebar-open [data-widget=pushmenu]').PushMenu('toggle')
            this.setTitle(nw)
            this.routeName = nw.name
        })

        setTimeout(() => {
            try {
                if (!$('[data-widget=treeview]', this.$el).data('lte.treeview')) {
                    //this.overlayScrollbarsSetup()
                    $(window).trigger('load.lte.treeview')
                }
            } catch (e) {}
        }, 500)


        this.Api.get('/users/me').then(res => {
            this.$store.state.user = res.data
            this.user = {
                full_name: this.$store.state.user.full_name || 'User'
            }

            console.log("ini role sidebar : " +  this.$store.state.user.roles.at(0).name);

            // Determine user role and set appropriate menu items
            if (this.$store.state.user && this.$store.state.user.roles) {
                const roles = this.$store.state.user.roles;
                if (roles.some(role => role.name === 'ROLE_SUPERADMIN')) {
                    this.items = SidebarConfig.owner;
                    this.dashboardRoute = { name: 'dashboard' };
                } else if (roles.some(role => role.name === 'ROLE_ADMIN_GUDANG')) {
                    this.items = SidebarConfig.warehouse;
                    this.dashboardRoute = { name: 'dashboard_gudang' };
                } else if (roles.some(role => role.name === 'ROLE_ADMIN_TOKO')) {
                    this.items = SidebarConfig.boutique;
                    this.dashboardRoute = { name: 'dashboard_toko' };
                } else if (roles.some(role => role.name === 'ROLE_ADMIN') || roles.some(role => role.name === 'ROLE_SUPERUSER')) {
                    this.items = SidebarConfig.admin;
                    this.dashboardRoute = { name: 'dashboard' };
                } else {
                    this.items = SidebarConfig.admin; // Default to admin menu
                    this.dashboardRoute = { name: 'dashboard' };
                }
            } else {
                // If no roles or user data, default to admin menu
                this.items = SidebarConfig.owner; // For development, using owner menu as default
                this.dashboardRoute = { name: 'dashboard' };
            }

            console.log('User data:', res.data);
        }).catch(err => {
            console.error('Error fetching user data:', err);
            // For development, using owner menu as default
            this.items = SidebarConfig.owner;
            this.dashboardRoute = { name: 'dashboard' };
        })
        $(window).trigger('resize')
    },
    methods: {
        setTitle(route) {
            if (this.$route.meta && this.$route.meta.title) {
                this.$store.state.title = this.$route.meta.title
                if (this.$route.meta.subtitle) {
                    this.$store.state.subtitle = this.$route.meta.subtitle
                } else {
                    this.$store.state.subtitle = null
                }
            } else {
                this.$store.state.title = this.$route.name
                this.$store.state.subtitle = null
            }
        },
        pushMenuSetup() {
            $(document).off('collapsed.lte.pushmenu').on('collapsed.lte.pushmenu', () => {
                localStorage.setItem('_PUSH_MENU', 'collapse')
            })
            $(document).off('shown.lte.pushmenu').on('shown.lte.pushmenu', () => {
                localStorage.setItem('_PUSH_MENU', 'expand')
            })

            this.$nextTick(() => {
                if (localStorage.getItem('_PUSH_MENU') == 'collapse') {
                        $('[data-widget=pushmenu]').PushMenu('collapse')
                } else if (localStorage.getItem('_PUSH_MENU') == 'expand') {
                    $('[data-widget=pushmenu]').PushMenu('expand')
                }
            })
        },
        overlayScrollbarsSetup() {
            new adminlte.Layout('body', {
                scrollbarTheme : 'os-theme-light',
                scrollbarAutoHide: 'l'
            })

            // $('.sidebar', this.$el).overlayScrollbars({
            //     className: "os-theme-light",
            //     sizeAutoCapable: true,
            //     scrollbars: {
            //         autoHide: "l",
            //         clickScrolling: true
            //     }
            // })
        },
        topnav() {
            this.routeName = 'topnav'
            $('body').attr('class', 'layout-top-nav text-sm')
            this.layout = 'top-nav'
            $('[data-widget="pushmenu"]').closest('.nav-item').hide()
        },
        boxed() {
            this.routeName = 'boxed'
            $('body').attr('class', 'sidebar-mini layout-boxed text-sm')
            this.layout = 'boxed'
            $('[data-widget="pushmenu"]').closest('.nav-item').show()
            new adminlte.Layout('body', {
                scrollbarTheme : 'os-theme-light',
                scrollbarAutoHide: 'l'
            })
        },
        fixed() {
            this.routeName = 'sidebar'
            $('body').attr('class', 'sidebar-mini layout-fixed text-sm')
            this.layout = 'fixed'
            $('[data-widget="pushmenu"]').closest('.nav-item').show()
            new adminlte.Layout('body', {
                scrollbarTheme : 'os-theme-light',
                scrollbarAutoHide: 'l'
            })
        },
        fixednavbar() {
            this.routeName = 'navbar'
            $('body').attr('class', 'sidebar-mini layout-navbar-fixed text-sm')
            $('[data-widget="pushmenu"]').closest('.nav-item').show()
            new adminlte.Layout('body', {
                scrollbarTheme : 'os-theme-light',
                scrollbarAutoHide: 'l'
            })
        },
        fixedfooter() {
            this.routeName = 'footer'
            $('body').attr('class', 'sidebar-mini layout-footer-fixed text-sm')
            $('[data-widget="pushmenu"]').closest('.nav-item').show()
            new adminlte.Layout('body', {
                scrollbarTheme : 'os-theme-light',
                scrollbarAutoHide: 'l'
            })
        },
        collapsed() {
            this.routeName = 'collapsed'
            $('body').attr('class', 'sidebar-mini sidebar-collapse text-sm')
            $('[data-widget="pushmenu"]').closest('.nav-item').show()
            new adminlte.Layout('body', {
                scrollbarTheme : 'os-theme-light',
                scrollbarAutoHide: 'l'
            })
        },
    }
}
</script>
