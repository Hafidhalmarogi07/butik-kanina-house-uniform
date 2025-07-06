<template>
    <div class="login-box">
        <!-- /.login-logo -->
            <!-- <img src="@assets/logo.png" class="login-banner" :alt="env.app_name"> -->
            <div class="card">
                <div class="login-logo">
                    <a href="./">
                        <b>{{ env.app_name }}</b>
                    </a>
                </div>
                <div class="card-body login-card-body">
                <p class="login-box-msg">Lupa kata sandi? Di sini Anda dapat dengan mudah mendapatkan kata sandi baru.</p>

                <div class="alert alert-danger alert-dismissible" v-if="errorRequest">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <h5>
                        <i class="icon fa fa-ban"></i>
                        Gagal meminta kata sandi baru!
                    </h5>
                    <p>{{ this.errorRequest }}</p>
                </div>
                <form @submit.prevent="submit" method="post">
                    <div class="input-group mb-3">
                        <input type="email" class="form-control" autocomplete="off" v-model="form.email" placeholder="Surel" :disabled="loading" required>
                        <div class="input-group-append">
                            <div class="input-group-text">
                                <span class="fas fa-envelope"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <!-- <button type="submit" class="btn btn-primary btn-block">Request new password</button> -->
                            <button type="submit" class="btn btn-secondary btn-block btn-flat" :disabled="loading">
                                <i class="fas fa-spinner fa-spin" v-if="loading"></i>
                                {{ loading ? 'Meminta ...' : 'Minta Kata Sandi Baru' }}
                            </button>
                        </div>
                        <!-- /.col -->
                    </div>
                </form>
                <div class="modal" id="myModal1">
                        <div class="modal-dialog modal-lg-4">
                            <div class="modal-content">
                                <!-- Modal Header -->
                                <div class="modal-header">
                                    <h4 class="modal-title">Formulir Atur Ulang Kata Sandi</h4>
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                </div>
                                <!-- Modal body -->
                                <div class="modal-body">
                                    <div class="alert alert-danger alert-dismissible" v-if="error">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <h5>
                                            <i class="icon fa fa-ban"></i>
                                            Gagal mengatur ulang kata sandi!
                                        </h5>
                                        <p>{{ this.error }}</p>
                                    </div>
                                    <form @submit.prevent="reset" class="card">
                                        <div class="card-body">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label>Kode OTP</label>
                                                        <input type="number" class="form-control" v-model="formreset.otp" :disabled="loading" required>
                                                    </div>
                                                    <div class="form-group">
                                                        <label>Kata Sandi Baru</label>
                                                        <input type="password" class="form-control" v-model="formreset.password" :disabled="loading" required>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-footer">
                                            <button type="submit" class="btn btn-primary" :disabled="loading">
                                                <i class="fas fa-spinner fa-spin" v-if="loading"></i>
                                                {{ loading ? 'Memperbarui ...' : 'Perbarui' }}
                                            </button>
                                        </div>
                                    </form>
                                </div>
                                <!-- Modal footer -->
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Tutup</button>
                                </div>
                            </div>
                        </div>
                    </div>

                <p class="mt-3 mb-1">
                    <router-link :to="{name: 'login'}">Masuk</router-link>
                </p>
                <!-- <p class="mb-0">
                    <router-link :to="{name: 'register'}" class="text-center">Register a new membership</router-link>
                </p> -->
            </div>
            <!-- /.login-card-body -->
        </div>
    </div>
    <!-- /.login-box -->
</template>

<script>
const {console} = window
export default {
    data: () => ({
        prevRoute: {
            name: 'login'
        },
        form: {
            email: null
        },
        formreset : {
            otp: null,
            password: null
        },
        error: undefined,
        errorRequest: undefined,
        loading: false
    }),
    mounted() {
        $('body').attr('class', 'hold-transition login-page')
        $('body,html').removeAttr('style')
        $('body').Layout('fixLayoutHeight')
    },
    methods: {
        submit() {
            this.loading = true
            this.Api.forgot('/forgot_password/request',this.form)
            .then(res => {
                this.loading = false
                alert("Silakan periksa email Anda");
                $('#myModal1').modal('show');
            }).catch(err => {
                this.loading = false
                this.errorRequest = err.response.data.message
            })
        },
        reset(){
            this.loading = true
            this.Api.reset('/forgot_password/reset', this.formreset).then(res => {
                this.loading = false
                alert("Berhasil mengatur ulang kata sandi");
                $('#myModal1').modal('hide');
                this.$router.push(this.prevRoute)
            }).catch(err => {
                this.loading = false
                this.error = err.response.data.message
            })
        }
    }
}
</script>

<style>
.login-banner {
    width: 100px;
    margin: auto;
    display: block;
}
.login-page {
    background: #333333 !important;
}
.login-page .login-logo{
    padding-top: 20px;
    margin-bottom: 0px;
}
.login-page .login-logo img{
    background-position: center center;
    width: 26px;
    height: 26px;
    margin-top: -6px;
    margin-right: 6px;
}
</style>
