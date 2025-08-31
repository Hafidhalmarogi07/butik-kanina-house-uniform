<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-3">
                <div class="card card-primary card-outline">
              <div class="card-body box-profile">
               <div class="text-center" v-if="form.photo_profile != null && form.photo_profile != '' ">
                  <img class="profile-user-img img-fluid img-circle" v-bind="{src:form.photo_profile}" alt="User profile picture">
                </div>
                <div class="text-center" v-else>
                  <img class="profile-user-img img-fluid img-circle" v-bind="{src:'http://localhost:8096/cdn/admin-icon-png-18.jpg'}" alt="User profile picture">
                </div>

                <h3 class="profile-username text-center">{{form.name}}</h3>

                <p class="text-muted text-center">{{form.roles[0].name}}</p>

                <!-- <ul class="list-group list-group-unbordered mb-3">
                  <li class="list-group-item">
                    <b>Followers</b> <a class="float-right">1,322</a>
                  </li>
                  <li class="list-group-item">
                    <b>Following</b> <a class="float-right">543</a>
                  </li>
                  <li class="list-group-item">
                    <b>Friends</b> <a class="float-right">13,287</a>
                  </li>
                </ul>

                <a href="#" class="btn btn-primary btn-block"><b>Follow</b></a> -->
              </div>
              <!-- /.card-body -->
            </div>
            </div>
            <div class="col-md-9">
                <div class="card card-primary card-outline">
                    <div class="card-header">
                      <div class="row">
                        <div class="col-sm-10">
                          <h3 class="card-title">Profil</h3>
                        </div>
                        <div class="col-sm-2">
                          <button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal1">Ubah Kata Sandi</button>
                        </div>
                      </div>
                    </div>
                    <div class="card-body">
                        <form class="form-horizontal">
                        <div class="form-group row">
                          <label for="inputName" class="col-sm-2 col-form-label">Nama Lengkap</label>
                          <div class="col-sm-10">
                            <input type="email" class="form-control" id="inputName" v-model="form.name">
                          </div>
                        </div>
                        <div class="form-group row">
                          <label for="inputEmail" class="col-sm-2 col-form-label">Email</label>
                          <div class="col-sm-10">
                            <input type="email" class="form-control" v-model="form.email" id="inputEmail">
                          </div>
                        </div>
                        <div class="form-group row">
                          <label for="phone_number" class="col-sm-2 col-form-label">Telepon</label>
                          <div class="col-sm-10">
                            <textarea class="form-control" id="phone_number" v-model="form.phone_number"></textarea>
                          </div>
                        </div>

                        <!-- <div class="form-group row">
                          <div class="offset-sm-2 col-sm-10">
                            <div class="checkbox">
                              <label>
                                <input type="checkbox"> I agree to the <a href="#">terms and conditions</a>
                              </label>
                            </div>
                          </div>
                        </div> -->
                        <!-- <div class="form-group row">
                          <div class="offset-sm-2 col-sm-10">
                            <button type="submit" class="btn btn-danger">Submit</button>
                          </div>
                        </div> -->
                      </form>
                      <div class="modal" id="myModal1">
                        <div class="modal-dialog modal-lg-4">
                            <div class="modal-content">
                                <!-- Modal Header -->
                                <div class="modal-header">
                                    <h4 class="modal-title">Form Ubah Kata Sandi</h4>
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                </div>
                                <!-- Modal body -->
                                <div class="modal-body">
                                    <div class="alert alert-danger alert-dismissible" v-if="error">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <h5>
                                            <i class="icon fa fa-ban"></i>
                                            Gagal mengubah kata sandi!
                                        </h5>
                                        <p>{{ this.error }}</p>
                                    </div>
                                    <form @submit.prevent="update" class="card">
                                        <div class="card-body">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label>Kata Sandi Lama</label>
                                                        <input type="password" class="form-control" v-model="formchange.password" :disabled="loading">
                                                    </div>
                                                    <div class="form-group">
                                                        <label>Kata Sandi Baru</label>
                                                        <input type="password" class="form-control" v-model="formchange.new_password" :disabled="loading">
                                                    </div>
                                                    <div class="form-group">
                                                        <label>Masukkan Ulang Kata Sandi</label>
                                                        <input type="password" class="form-control" v-model="formchange.re_new_password" :disabled="loading">
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
                                    <div id="snackbar">{{text_validation}}</div>
                                </div>
                                <!-- Modal footer -->
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Tutup</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
const {console} = window
export default {
    data() {
        return {
            form : {
                name: "",
                email: "",
                phone_number: null,
                photo_profile: null,
                roles: [{
                  name: null
                }]
            },
            formchange : {
                password: null,
                new_password: null,
                re_new_password: null
            },
            error: undefined,
            loading: false,
            text_validation : null
        }
    },
    mounted() {
            this.Api.get('/users/me').then(res => {
                this.form = res.data
                console.log(this.form)
            })
    },
    methods: {
        update() {
            this.loading = true
            if(!this.validationPassword()){
              this.loading = false
              return;
            }
            this.Api.post('/users/update_password', this.formchange).then(res => {
              this.loading = false
              alert("Kata Sandi berhasil diubah");
              $('#myModal1').modal('hide');
            }).catch((e)  => {
              this.loading = false
              this.error = e.response.data.message
              this.text_validation = "Pengiriman Gagal";
              this.message();
            })
        },
        validationPassword(){
            let response = true;
            if(typeof this.formchange.password == 'undefined' || this.formchange.password == null || this.formchange.password == ""){
                this.text_validation = "Kata Sandi Lama Kosong"
                this.message()
                response = false;
                return response;
            }
            if(typeof this.formchange.new_password == 'undefined' || this.formchange.new_password == null || this.formchange.new_password == ""){
                this.text_validation = "Kata Sandi Baru Kosong"
                this.message()
                response = false;
                return response;
            }
            if(typeof this.formchange.re_new_password == 'undefined' || this.formchange.re_new_password == null || this.formchange.re_new_password == ""){
                this.text_validation = "Masukkan Ulang Kata Sandi Kosong"
                this.message()
                response = false
                return response;
            }
            return response;
        },
        message(){
            let x = document.getElementById("snackbar");
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
        },
    }
}
</script>
