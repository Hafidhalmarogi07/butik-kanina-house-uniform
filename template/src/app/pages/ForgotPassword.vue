<template>
    <div class="login-box">
        <!-- /.login-logo -->
        <div class="login-header">
            <img src="@assets/logo.png" class="login-banner" :alt="env.app_name">
        </div>
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
                        <input type="email" class="form-control" autocomplete="off" v-model="form.email" placeholder="Email" :disabled="loading" required>
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

                <p class="mt-3 mb-1">
                    <router-link :to="{name: 'login'}">Masuk</router-link>
                </p>
                <!-- <p class="mb-0">
                    <router-link :to="{name: 'register'}" class="text-center">Register a new membership</router-link>
                </p> -->
            </div>
            <!-- /.login-card-body -->
        </div>

        <!-- Modal moved outside of card -->
        <div class="modal" id="myModal1" data-backdrop="false"  backdrop="false" data-keyboard="false">
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
                        <div class="card">
                            <form @submit.prevent="reset">
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
                    </div>
                    <!-- Modal footer -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Tutup</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /.login-box -->
</template>

<script>
import {alert} from "ionicons/icons";

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
            email: null,
            otp: null,
            password: null
        },
        error: undefined,
        errorRequest: undefined,
        loading: false
    }),
    mounted() {
        $('body').attr('class', 'hold-transition login-page text-sm')
        $('body,html').removeAttr('style')
        new adminlte.Layout('body', {})

        // Initialize modal properly
        $('#myModal1').on('shown.bs.modal', function () {
            $(this).find('input:first').focus()
        })

        // Ensure modal is properly cleaned up when hidden
        $('#myModal1').on('hidden.bs.modal', function () {
            $('body').removeClass('modal-open')
        })
    },
    methods: {
        submit() {
            this.loading = true
            this.Api.forgot('/forgot-password/request',this.form)
            .then(res => {
              this.loading = false
              if(res.data.success == true) {
                // Set the email in formreset to the email from the first form
                this.formreset.email = this.form.email;
                $('#myModal1').modal('show');
              }else{
                this.errorRequest = res.data.message;
              }
            }).catch(err => {
                this.loading = false
                this.errorRequest = err.response.data.message
            })
        },
        reset(){
            this.loading = true
            this.Api.reset('/forgot-password/reset', this.formreset).then(res => {
                this.loading = false
                console.log("ini response ", res.data.message);
                if(res.data.success == true) {
                  $('#myModal1').modal('hide');
                  $('body').removeClass('modal-open');
                  this.$router.push(this.prevRoute)
                }else {
                  this.error = res.data.message;
                }
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
    width: 120px;
    margin: auto;
    display: block;
    margin-bottom: 20px;
    filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.5));
    animation: fadeInDown 1s ease-out;
}

@keyframes fadeInDown {
    from {
        opacity: 0;
        transform: translateY(-20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.login-header {
    text-align: center;
    padding: 30px 0;
}

.login-page {
    background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%) !important;
    background-size: cover !important;
    background-attachment: fixed !important;
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    overflow: hidden;
}

.login-page::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%23ffffff' fill-opacity='0.1' fill-rule='evenodd'/%3E%3C/svg%3E");
    opacity: 0.5;
    z-index: 0;
}

.login-box {
    width: 400px;
    max-width: 90%;
    margin: 0 auto;
    position: relative;
    z-index: 1;
}

/* Responsive adjustments */
@media (max-width: 576px) {
    .login-box {
        width: 320px;
    }

    .login-banner {
        width: 100px;
    }

    .login-logo a {
        font-size: 20px;
    }

    .login-box-msg {
        font-size: 14px;
    }

    .btn-secondary {
        padding: 8px 15px;
    }

    .card {
        border-radius: 10px;
    }
}

.card {
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
    border: none;
    animation: fadeIn 0.8s ease-out 0.3s both;
    transform: perspective(1000px);
    transition: transform 0.5s;
}

.card:hover {
    transform: perspective(1000px) rotateX(2deg);
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: perspective(1000px) rotateX(5deg) translateY(10px);
    }
    to {
        opacity: 1;
        transform: perspective(1000px) rotateX(0) translateY(0);
    }
}

.login-logo {
    padding-top: 20px;
    margin-bottom: 0px;
    text-align: center;
    background: linear-gradient(to right, #f8f9fa, #e9ecef);
}

.login-logo a {
    color: #2575fc;
    text-decoration: none;
    font-size: 24px;
    font-weight: bold;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

.login-card-body {
    padding: 30px;
    background: white;
}

.login-box-msg {
    color: #6c757d;
    font-size: 16px;
    text-align: center;
    margin-bottom: 20px;
}

.input-group {
    margin-bottom: 20px !important;
    position: relative;
    border-radius: 30px;
    overflow: hidden;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
}

.input-group:focus-within {
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    transform: translateY(-2px);
}

.input-group-text {
    background-color: #f8f9fa;
    border: none;
    border-left: 1px solid #efefef;
    color: #6a11cb;
}

.form-control {
    border: none;
    padding: 12px 15px;
    height: auto;
    font-size: 16px;
    transition: all 0.3s ease;
}

.form-control:focus {
    box-shadow: none;
    background-color: #f9f9f9;
}

.btn-secondary {
    background: linear-gradient(to right, #6a11cb, #2575fc);
    border: none;
    border-radius: 30px;
    padding: 10px 20px;
    font-weight: bold;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
    z-index: 1;
}

.btn-secondary::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 0%;
    height: 100%;
    background: linear-gradient(to right, #2575fc, #6a11cb);
    transition: all 0.5s ease;
    z-index: -1;
}

.btn-secondary:hover {
    transform: translateY(-3px);
    box-shadow: 0 7px 20px rgba(0, 0, 0, 0.3);
    color: white;
}

.btn-secondary:hover::before {
    width: 100%;
}

.btn-secondary:active {
    transform: translateY(-1px);
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
}

.social-auth-links {
    text-align: center;
    padding: 15px 0;
    animation: fadeIn 1s ease-out 0.6s both;
}

.social-auth-links p {
    color: #6c757d;
    font-weight: 500;
    margin-bottom: 10px;
}

.social-auth-links a {
    color: #2575fc;
    text-decoration: none;
    transition: all 0.3s ease;
    padding: 5px 10px;
    border-radius: 20px;
    display: inline-block;
    font-weight: 500;
}

.social-auth-links a:hover {
    color: #6a11cb;
    background-color: rgba(37, 117, 252, 0.1);
    transform: translateY(-2px);
}


.modal {
  z-index: 9999 !important;
}


.modal-open {
    overflow: hidden;
    padding-right: 0 !important;
}

.modal-dialog {
    max-width: 500px;
    margin: 1.75rem auto;
}

.modal-content {
    position: relative;
    display: flex;
    flex-direction: column;
    width: 100%;
    background-color: #fff;
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
    animation: fadeIn 0.5s ease-out;
}

.modal .card {
    margin-bottom: 0;
    box-shadow: none;
    border-radius: 0;
}

.modal-header {
    background: linear-gradient(to right, #f8f9fa, #e9ecef);
    border-bottom: none;
    padding: 20px 30px;
}

.modal-title {
    color: #2575fc;
    font-weight: bold;
}

.modal-body {
    padding: 30px;
}

.modal-footer {
    border-top: none;
    padding: 20px 30px;
}

.btn-danger {
    background: linear-gradient(to right, #ff416c, #ff4b2b);
    border: none;
    border-radius: 30px;
    padding: 10px 20px;
    font-weight: bold;
    transition: all 0.3s ease;
}

.btn-danger:hover {
    transform: translateY(-3px);
    box-shadow: 0 7px 20px rgba(0, 0, 0, 0.3);
}
</style>
