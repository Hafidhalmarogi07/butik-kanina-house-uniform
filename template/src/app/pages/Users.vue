<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">User Management</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Add User
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Search users..." v-model="searchQuery">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="roleFilter">
                                    <option value="">All Roles</option>
                                    <option value="ROLE_SUPERADMIN">Super Admin</option>
                                    <option value="ROLE_ADMIN_TOKO">Store Admin</option>
                                    <option value="ROLE_ADMIN_GUDANG">Warehouse Admin</option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Username</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Status</th>
                                        <th>Last Login</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="user in filteredUsers" :key="user.id">
                                        <td>{{ user.id }}</td>
                                        <td>{{ user.name }}</td>
                                        <td>{{ user.username }}</td>
                                        <td>{{ user.email }}</td>
                                        <td>
                                            <span :class="getRoleClass(user.role)">{{ formatRole(user.role) }}</span>
                                        </td>
                                        <td>
                                            <span :class="getStatusClass(user.active)">
                                                {{ user.active ? 'Active' : 'Inactive' }}
                                            </span>
                                        </td>
                                        <td>{{ user.lastLogin }}</td>
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
                        <ul class="pagination pagination-sm m-0 float-right">
                            <li class="page-item"><a class="page-link" href="#">&laquo;</a></li>
                            <li class="page-item"><a class="page-link" href="#">1</a></li>
                            <li class="page-item"><a class="page-link" href="#">2</a></li>
                            <li class="page-item"><a class="page-link" href="#">3</a></li>
                            <li class="page-item"><a class="page-link" href="#">&raquo;</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit User Modal -->
        <div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="userModalLabel">{{ isEditing ? 'Edit User' : 'Add New User' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveUser">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="userName">Full Name</label>
                                        <input type="text" class="form-control" id="userName" v-model="currentUser.name" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="userUsername">Username</label>
                                        <input type="text" class="form-control" id="userUsername" v-model="currentUser.username" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="userEmail">Email</label>
                                        <input type="email" class="form-control" id="userEmail" v-model="currentUser.email" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="userRole">Role</label>
                                        <select class="form-control" id="userRole" v-model="currentUser.role" required>
                                            <option value="ROLE_SUPERADMIN">Super Admin</option>
                                            <option value="ROLE_ADMIN_TOKO">Store Admin</option>
                                            <option value="ROLE_ADMIN_GUDANG">Warehouse Admin</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="userPassword">Password</label>
                                        <input type="password" class="form-control" id="userPassword" v-model="currentUser.password" :required="!isEditing">
                                        <small class="form-text text-muted" v-if="isEditing">Leave blank to keep current password</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="userConfirmPassword">Confirm Password</label>
                                        <input type="password" class="form-control" id="userConfirmPassword" v-model="confirmPassword" :required="!isEditing">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="custom-control custom-switch">
                                    <input type="checkbox" class="custom-control-input" id="userActive" v-model="currentUser.active">
                                    <label class="custom-control-label" for="userActive">Active</label>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary" :disabled="!isFormValid">Save</button>
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
                        <h5 class="modal-title" id="viewUserModalLabel">User Details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="currentUser">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>ID:</strong> {{ currentUser.id }}</p>
                                <p><strong>Name:</strong> {{ currentUser.name }}</p>
                                <p><strong>Username:</strong> {{ currentUser.username }}</p>
                                <p><strong>Email:</strong> {{ currentUser.email }}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Role:</strong> <span :class="getRoleClass(currentUser.role)">{{ formatRole(currentUser.role) }}</span></p>
                                <p><strong>Status:</strong> <span :class="getStatusClass(currentUser.active)">{{ currentUser.active ? 'Active' : 'Inactive' }}</span></p>
                                <p><strong>Last Login:</strong> {{ currentUser.lastLogin || 'Never' }}</p>
                                <p><strong>Created:</strong> {{ currentUser.created }}</p>
                            </div>
                        </div>
                        
                        <div class="mt-4">
                            <h6>Recent Activity</h6>
                            <table class="table table-sm table-bordered">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Activity</th>
                                        <th>IP Address</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(activity, index) in currentUser.recentActivity" :key="index">
                                        <td>{{ activity.date }}</td>
                                        <td>{{ activity.action }}</td>
                                        <td>{{ activity.ipAddress }}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this user? This action cannot be undone.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" @click="confirmDelete">Delete</button>
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
            currentUser: {
                id: null,
                name: '',
                username: '',
                email: '',
                role: 'ROLE_ADMIN_TOKO',
                active: true,
                password: '',
                lastLogin: '',
                created: '',
                recentActivity: []
            },
            confirmPassword: '',
            userToDelete: null,
            users: [
                {
                    id: 1,
                    name: 'Admin Super',
                    username: 'admin',
                    email: 'admin@example.com',
                    role: 'ROLE_SUPERADMIN',
                    active: true,
                    lastLogin: '2023-05-25 08:30',
                    created: '2023-01-01',
                    recentActivity: [
                        { date: '2023-05-25 08:30', action: 'Login', ipAddress: '192.168.1.1' },
                        { date: '2023-05-24 17:45', action: 'Updated product inventory', ipAddress: '192.168.1.1' },
                        { date: '2023-05-24 09:15', action: 'Login', ipAddress: '192.168.1.1' }
                    ]
                },
                {
                    id: 2,
                    name: 'Store Manager',
                    username: 'store',
                    email: 'store@example.com',
                    role: 'ROLE_ADMIN_TOKO',
                    active: true,
                    lastLogin: '2023-05-25 09:15',
                    created: '2023-01-15',
                    recentActivity: [
                        { date: '2023-05-25 09:15', action: 'Login', ipAddress: '192.168.1.2' },
                        { date: '2023-05-24 16:30', action: 'Processed order #ORD-005', ipAddress: '192.168.1.2' },
                        { date: '2023-05-24 10:00', action: 'Login', ipAddress: '192.168.1.2' }
                    ]
                },
                {
                    id: 3,
                    name: 'Warehouse Manager',
                    username: 'warehouse',
                    email: 'warehouse@example.com',
                    role: 'ROLE_ADMIN_GUDANG',
                    active: true,
                    lastLogin: '2023-05-24 08:45',
                    created: '2023-02-01',
                    recentActivity: [
                        { date: '2023-05-24 08:45', action: 'Login', ipAddress: '192.168.1.3' },
                        { date: '2023-05-23 15:20', action: 'Updated production schedule', ipAddress: '192.168.1.3' },
                        { date: '2023-05-23 09:30', action: 'Login', ipAddress: '192.168.1.3' }
                    ]
                },
                {
                    id: 4,
                    name: 'Test User',
                    username: 'test',
                    email: 'test@example.com',
                    role: 'ROLE_ADMIN_TOKO',
                    active: false,
                    lastLogin: null,
                    created: '2023-05-01',
                    recentActivity: []
                }
            ]
        }
    },
    computed: {
        filteredUsers() {
            let filtered = this.users;
            
            if (this.searchQuery) {
                const query = this.searchQuery.toLowerCase();
                filtered = filtered.filter(user => 
                    user.name.toLowerCase().includes(query) || 
                    user.username.toLowerCase().includes(query) ||
                    user.email.toLowerCase().includes(query)
                );
            }
            
            if (this.roleFilter) {
                filtered = filtered.filter(user => user.role === this.roleFilter);
            }
            
            return filtered;
        },
        isFormValid() {
            if (!this.currentUser.name || !this.currentUser.username || !this.currentUser.email || !this.currentUser.role) {
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
    methods: {
        formatRole(role) {
            switch(role) {
                case 'ROLE_SUPERADMIN': return 'Super Admin';
                case 'ROLE_ADMIN_TOKO': return 'Store Admin';
                case 'ROLE_ADMIN_GUDANG': return 'Warehouse Admin';
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
        getStatusClass(active) {
            return active ? 'badge badge-success' : 'badge badge-secondary';
        },
        showAddModal() {
            this.isEditing = false;
            this.currentUser = {
                id: null,
                name: '',
                username: '',
                email: '',
                role: 'ROLE_ADMIN_TOKO',
                active: true,
                password: '',
                lastLogin: '',
                created: '',
                recentActivity: []
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
                this.users[index].active = !this.users[index].active;
            }
        },
        deleteUser(user) {
            this.userToDelete = user;
            $('#deleteModal').modal('show');
        },
        confirmDelete() {
            if (this.userToDelete) {
                const index = this.users.findIndex(u => u.id === this.userToDelete.id);
                if (index !== -1) {
                    this.users.splice(index, 1);
                }
                this.userToDelete = null;
                $('#deleteModal').modal('hide');
            }
        },
        saveUser() {
            if (this.isEditing) {
                // Update existing user
                const index = this.users.findIndex(u => u.id === this.currentUser.id);
                if (index !== -1) {
                    // Preserve fields that shouldn't be changed in the form
                    const lastLogin = this.users[index].lastLogin;
                    const created = this.users[index].created;
                    const recentActivity = this.users[index].recentActivity;
                    
                    // If password is empty, keep the old one (we don't actually store passwords in this demo)
                    const updatedUser = {
                        ...this.currentUser,
                        lastLogin,
                        created,
                        recentActivity
                    };
                    
                    this.users.splice(index, 1, updatedUser);
                }
            } else {
                // Add new user
                const newId = Math.max(...this.users.map(u => u.id), 0) + 1;
                const now = new Date().toISOString().split('T')[0];
                
                this.users.push({
                    ...this.currentUser,
                    id: newId,
                    created: now,
                    lastLogin: null,
                    recentActivity: []
                });
            }
            $('#userModal').modal('hide');
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
</style>