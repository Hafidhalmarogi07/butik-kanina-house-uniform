<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Manajemen Pengguna</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Tambah Pengguna
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Cari pengguna..." 
                                           v-model="searchQuery" 
                                           @keyup.enter="handleSearch">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button" @click="handleSearch">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <!-- <div class="col-md-3">
                                <select class="form-control" v-model="roleFilter">
                                    <option value="">Semua Peran</option>
                                    <option value="ROLE_SUPERADMIN">Super Admin</option>
                                    <option value="ROLE_ADMIN_TOKO">Admin Toko</option>
                                    <option value="ROLE_ADMIN_GUDANG">Admin Gudang</option>
                                </select>
                            </div> -->
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nama</th>
                                        <th>Email</th>
                                        <th>Peran</th>
                                        <th>Status</th>
                                        <th>Login Terakhir</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="user in filteredUsers" :key="user.id">
                                        <td>{{ user.id }}</td>
                                        <td>{{ user.full_name }}</td>
                                        <td>{{ user.email }}</td>
                                        <td>
                                            <span :class="getRoleClass(user.role)">{{ formatRole(user.role) }}</span>
                                        </td>
                                        <td>
                                            <span :class="getStatusClass(user.status)">
                                                {{ user.status ? 'Aktif' : 'Tidak Aktif' }}
                                            </span>
                                        </td>
                                        <td>{{ user.updated }}</td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="viewUser(user)">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" @click="editUser(user)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm" 
                                                    :class="user.active ? 'btn-warning' : 'btn-success'"
                                                    @click="toggleUserStatus(user)">
                                                    <i :class="user.active ? 'fas fa-ban' : 'fas fa-check'"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="deleteUser(user)">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer clearfix">
                        <div v-if="loading" class="text-center">
                            <div class="spinner-border text-primary" role="status">
                                <span class="sr-only">Memuat...</span>
                            </div>
                        </div>
                        <div v-else-if="error" class="alert alert-danger">
                            {{ error }}
                        </div>
                        <ul v-else-if="pagination.totalPages > 0" class="pagination pagination-sm m-0 float-right">
                            <li class="page-item" :class="{ disabled: pagination.pageNumber <= 0 }">
                                <a class="page-link" href="#" @click.prevent="changePage(pagination.pageNumber)">&laquo;</a>
                            </li>
                            <li v-for="page in pagesArray" :key="page" class="page-item" :class="{ active: page === pagination.pageNumber + 1 }">
                                <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                            </li>
                            <li class="page-item" :class="{ disabled: pagination.pageNumber >= pagination.totalPages - 1 }">
                                <a class="page-link" href="#" @click.prevent="changePage(pagination.pageNumber + 2)">&raquo;</a>
                            </li>
                        </ul>
                        <div v-else class="text-center">
                            <p>Tidak ada pengguna ditemukan</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit User Modal -->
        <div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="userModalLabel">{{ isEditing ? 'Edit Pengguna' : 'Tambah Pengguna Baru' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveUser">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="userName">Nama Lengkap</label>
                                        <input type="text" class="form-control" id="userName" v-model="currentUser.full_name" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="userEmail">Email</label>
                                        <input type="email" class="form-control" id="userEmail" v-model="currentUser.email" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="userPhone">Nomor Telepon</label>
                                        <input type="tel" class="form-control" id="userPhone" v-model="currentUser.phone_number">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="userRole">Peran</label>
                                        <select class="form-control" id="userRole" v-model="currentUser.role" required>
                                            <option value="ROLE_SUPERADMIN">Super Admin</option>
                                            <option value="ROLE_ADMIN_TOKO">Admin Toko</option>
                                            <option value="ROLE_ADMIN_GUDANG">Admin Gudang</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="userJobTitle">Jabatan</label>
                                        <input type="text" class="form-control" id="userJobTitle" v-model="currentUser.job_tittle">
                                    </div>
                                    <div class="form-group">
                                        <label for="userPassword">Kata Sandi</label>
                                        <input type="password" class="form-control" id="userPassword" v-model="currentUser.password" :required="!isEditing">
                                        <small class="form-text text-muted" v-if="isEditing">Biarkan kosong untuk mempertahankan kata sandi saat ini</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="userConfirmPassword">Konfirmasi Kata Sandi</label>
                                        <input type="password" class="form-control" id="userConfirmPassword" v-model="confirmPassword" :required="!isEditing">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="userAddress">Alamat</label>
                                        <textarea class="form-control" id="userAddress" rows="2" v-model="currentUser.address"></textarea>
                                    </div>
                                    <div class="form-group">
                                        <label for="userDescription">Deskripsi</label>
                                        <textarea class="form-control" id="userDescription" rows="2" v-model="currentUser.description"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="custom-control custom-switch">
                                    <input type="checkbox" class="custom-control-input" id="userActive" v-model="currentUser.status">
                                    <label class="custom-control-label" for="userActive">Aktif</label>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                                <button type="submit" class="btn btn-primary" :disabled="!isFormValid">Simpan</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- View User Modal -->
        <div class="modal fade" id="viewUserModal" tabindex="-1" role="dialog" aria-labelledby="viewUserModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewUserModalLabel">Detail Pengguna</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="currentUser">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>ID:</strong> {{ currentUser.id }}</p>
                                <p><strong>Nama Lengkap:</strong> {{ currentUser.full_name }}</p>
                                <p><strong>Email:</strong> {{ currentUser.email }}</p>
                                <p><strong>Telepon:</strong> {{ currentUser.phone_number || 'Tidak ada' }}</p>
                                <p><strong>Jabatan:</strong> {{ currentUser.job_tittle || 'Tidak ada' }}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Peran:</strong> <span :class="getRoleClass(currentUser.role)">{{ formatRole(currentUser.role) }}</span></p>
                                <p><strong>Status:</strong> <span :class="getStatusClass(currentUser.status)">{{ currentUser.status ? 'Aktif' : 'Tidak Aktif' }}</span></p>
                                <p><strong>Dibuat:</strong> {{ currentUser.created }}</p>
                                <p><strong>Diperbarui:</strong> {{ currentUser.updated }}</p>
                                <p><strong>Alamat:</strong> {{ currentUser.address || 'Tidak ada' }}</p>
                            </div>
                        </div>

                        <div class="mt-4" v-if="currentUser.description">
                            <h6>Deskripsi</h6>
                            <p>{{ currentUser.description }}</p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Tutup</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteModalLabel">Konfirmasi Hapus</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Apakah Anda yakin ingin menghapus pengguna ini? Tindakan ini tidak dapat dibatalkan.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                        <button type="button" class="btn btn-danger" @click="confirmDelete">Hapus</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            searchQuery: '',
            roleFilter: '',
            isEditing: false,
            searchTimeout: null,
            currentUser: {
                id: null,
                full_name: '',
                email: '',
                role: 'ROLE_ADMIN_TOKO',
                status: true,
                password: '',
                created: '',
                updated: '',
                photo_profile: null,
                description: null,
                job_tittle: null,
                phone_number: null,
                address: null
            },
            confirmPassword: '',
            userToDelete: null,
            users: [],
            pagination: {
                totalPages: 0,
                totalElements: 0,
                pageNumber: 0,
                pageSize: 10
            },
            loading: false,
            error: null
        }
    },
    computed: {
        filteredUsers() {
            // Since filtering is now done on the server side, 
            // we simply return the users array
            return this.users;
        },
        pagesArray() {
            if (!this.pagination.totalPages || this.pagination.totalPages <= 0) return [];

            const pages = [];
            const maxVisiblePages = 5;
            const halfVisiblePages = Math.floor(maxVisiblePages / 2);

            // Ensure pageNumber is a number and not negative
            const currentPage = Math.max(0, this.pagination.pageNumber || 0);

            let startPage = Math.max(1, currentPage + 1 - halfVisiblePages);
            let endPage = Math.min(this.pagination.totalPages, startPage + maxVisiblePages - 1);

            if (endPage - startPage + 1 < maxVisiblePages) {
                startPage = Math.max(1, endPage - maxVisiblePages + 1);
            }

            for (let i = startPage; i <= endPage; i++) {
                pages.push(i);
            }

            return pages;
        },
        isFormValid() {
            if (!this.currentUser.full_name || !this.currentUser.email || !this.currentUser.role) {
                return false;
            }

            if (!this.isEditing && (!this.currentUser.password || this.currentUser.password !== this.confirmPassword)) {
                return false;
            }

            if (this.isEditing && this.currentUser.password && this.currentUser.password !== this.confirmPassword) {
                return false;
            }

            return true;
        }
    },
    mounted() {
        this.fetchUsers(0); // Start with page 0 for backend (equivalent to UI page 1)
    },
    watch: {
        searchQuery: function(newVal) {
            // Debounce search to avoid too many requests
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            this.searchTimeout = setTimeout(() => {
                // Reset to first page and fetch users with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        },
        roleFilter: function() {
            // Reset to first page and fetch users with new role filter
            this.changePage(1);
        }
    },
    methods: {
        fetchUsers(page = 0) {
            this.loading = true;

            // Prepare params object with pagination
            const params = { 
                page: page, 
                size: this.pagination.pageSize 
            };

            // Prepare filters array for backend
            const filters = [];

            // Add search query if present
            if (this.searchQuery) {
                // Add filter for full_name containing search query
                filters.push(["full_name", "like", this.searchQuery]);

                // If we want to search in email too, add OR operator and email filter
                if (this.searchQuery.trim() !== "") {
                    filters.push(["or"]);
                    filters.push(["email", "like", this.searchQuery]);
                }
            }

            

            // Add filters to params if any
            if (filters.length > 0) {
                params.filters = JSON.stringify(filters);
            }

            // Convert params object to URL query string
            const queryParams = new URLSearchParams();
            // Add role filter if present
            if (this.roleFilter) {
            
                // Add filter for role equals roleFilter
                queryParams.append("role", this.roleFilter);
            }

            // Add pagination parameters
            // Convert 0-based page to 1-based page for API
            queryParams.append('page', params.page + 1);
            queryParams.append('size', params.size);

            // Add sort parameter if present
            if (params.sort) {
                queryParams.append('sort', params.sort);
            }

            // Add filters parameter if present
            if (params.filters) {
                queryParams.append('filters', params.filters);
            }

            // Make the API call with the constructed URL
            this.Api.get(`/user-profiles?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.users = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.users = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.users.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Kesalahan mengambil pengguna:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Gagal memuat pengguna'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'Tidak ada respons dari server. Silakan periksa koneksi Anda.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Gagal memuat pengguna: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        changePage(page) {
            // page parameter is 1-based from UI, convert to 0-based for backend
            const zeroBasedPage = page - 1;
            if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
            this.pagination.pageNumber = zeroBasedPage;
            this.fetchUsers(zeroBasedPage);
        },

        formatRole(role) {
            switch(role) {
                case 'ROLE_SUPERADMIN': return 'Super Admin';
                case 'ROLE_ADMIN_TOKO': return 'Admin Toko';
                case 'ROLE_ADMIN_GUDANG': return 'Admin Gudang';
                default: return role;
            }
        },
        getRoleClass(role) {
            switch(role) {
                case 'ROLE_SUPERADMIN': return 'badge badge-danger';
                case 'ROLE_ADMIN_TOKO': return 'badge badge-primary';
                case 'ROLE_ADMIN_GUDANG': return 'badge badge-success';
                default: return 'badge badge-secondary';
            }
        },
        getStatusClass(status) {
            return status ? 'badge badge-success' : 'badge badge-secondary';
        },
        showAddModal() {
            this.isEditing = false;
            this.currentUser = {
                id: null,
                full_name: '',
                email: '',
                role: 'ROLE_ADMIN_TOKO',
                status: true,
                password: '',
                created: '',
                updated: '',
                photo_profile: null,
                description: null,
                job_tittle: null,
                phone_number: null,
                address: null
            };
            this.confirmPassword = '';
            $('#userModal').modal('show');
        },
        viewUser(user) {
            this.currentUser = { ...user };
            $('#viewUserModal').modal('show');
        },
        editUser(user) {
            this.isEditing = true;
            this.currentUser = { ...user, password: '' };
            this.confirmPassword = '';
            $('#userModal').modal('show');
        },
        toggleUserStatus(user) {
            const index = this.users.findIndex(u => u.id === user.id);
            if (index !== -1) {
                this.users[index].status = !this.users[index].status;
            }
        },
        deleteUser(user) {
            this.userToDelete = user;
            $('#deleteModal').modal('show');
        },
        confirmDelete() {
            if (this.userToDelete) {
                this.loading = true;
                this.Api.delete(`/user-profiles/${this.userToDelete.id}`)
                    .then(() => {
                        const index = this.users.findIndex(u => u.id === this.userToDelete.id);
                        if (index !== -1) {
                            this.users.splice(index, 1);
                        }
                        this.userToDelete = null;
                        $('#deleteModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan menghapus pengguna:', error);
                        this.error = 'Gagal menghapus pengguna';
                        this.loading = false;
                    });
            }
        },
        saveUser() {
            this.loading = true;

            if (this.isEditing) {
                // Update existing user
                this.Api.put(`/user-profiles/${this.currentUser.id}`, this.currentUser)
                    .then(response => {
                        const index = this.users.findIndex(u => u.id === this.currentUser.id);
                        if (index !== -1) {
                            this.users.splice(index, 1, response.data);
                        }
                        $('#userModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan memperbarui pengguna:', error);
                        this.error = 'Gagal memperbarui pengguna';
                        this.loading = false;
                    });
            } else {
                // Add new user
                this.Api.post('/user-profiles', this.currentUser)
                    .then(response => {
                        this.users.push(response.data);
                        $('#userModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan menambahkan pengguna:', error);
                        this.error = 'Gagal menambahkan pengguna';
                        this.loading = false;
                    });
            }
        },

        handleSearch() {
            // Clear any existing timeout
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            // Reset to first page and fetch users with current search query
            this.changePage(1);
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
</style>
