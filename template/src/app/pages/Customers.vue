<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Customer List</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Add Customer
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Search customers..." 
                                           v-model="searchQuery" 
                                           @keyup.enter="handleSearch">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button" @click="handleSearch">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="typeFilter">
                                    <option value="">All Types</option>
                                    <option value="INDIVIDU">Individual</option>
                                    <option value="INSTANSI">Institution</option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Type</th>
                                        <th>Phone</th>
                                        <th>Address</th>
                                        <th>Total Orders</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="customer in filteredCustomers" :key="customer.id">
                                        <td>{{ customer.id }}</td>
                                        <td>{{ customer.nama }}</td>
                                        <td>
                                            <span :class="getTypeClass(customer.tipe)">{{ customer.tipe }}</span>
                                        </td>
                                        <td>{{ customer.phone_number }}</td>
                                        <td>{{ truncateText(customer.alamat, 30) }}</td>
                                        <td>{{ customer.totalOrders || 0 }}</td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="editCustomer(customer)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="deleteCustomer(customer)">
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
                                <span class="sr-only">Loading...</span>
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
                            <p>No customers found</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit Customer Modal -->
        <div class="modal fade" id="customerModal" tabindex="-1" role="dialog" aria-labelledby="customerModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="customerModalLabel">{{ isEditing ? 'Edit Customer' : 'Add New Customer' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveCustomer">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="customerName">Name</label>
                                        <input type="text" class="form-control" id="customerName" v-model="currentCustomer.nama" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="customerType">Type</label>
                                        <select class="form-control" id="customerType" v-model="currentCustomer.tipe" required>
                                            <option value="INDIVIDU">Individual</option>
                                            <option value="INSTANSI">Institution</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="customerPhone">Phone</label>
                                        <input type="tel" class="form-control" id="customerPhone" v-model="currentCustomer.phone_number" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="customerAddress">Address</label>
                                        <textarea class="form-control" id="customerAddress" rows="3" v-model="currentCustomer.alamat" required></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="customerNotes">Notes</label>
                                <textarea class="form-control" id="customerNotes" rows="2" v-model="currentCustomer.notes"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Save</button>
                            </div>
                        </form>
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
                        Are you sure you want to delete this customer? This action cannot be undone.
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
            typeFilter: '',
            isEditing: false,
            searchTimeout: null,
            currentCustomer: {
                id: null,
                nama: '',
                tipe: 'INDIVIDU',
                phone_number: '',
                alamat: '',
                totalOrders: 0,
                notes: '',
                recentOrders: []
            },
            customerToDelete: null,
            customers: [],
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
        filteredCustomers() {
            // Since filtering is now done on the server side, 
            // we simply return the customers array
            return this.customers;
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
        }
    },
    mounted() {
        this.fetchCustomers(0); // Start with page 0 for backend (equivalent to UI page 1)
    },
    watch: {
        searchQuery: function(newVal) {
            // Debounce search to avoid too many requests
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            this.searchTimeout = setTimeout(() => {
                // Reset to first page and fetch customers with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        },
        typeFilter: function() {
            // Reset to first page and fetch customers with new type filter
            this.changePage(1);
        }
    },
    methods: {
        fetchCustomers(page = 0) {
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
                // Add filter for nama containing search query
                filters.push(["nama", "like", this.searchQuery]);

                // If we want to search in noHp too, add OR operator and noHp filter
                if (this.searchQuery.trim() !== "") {
                    filters.push(["or"]);
                    filters.push(["phone_number", "like", this.searchQuery]);
                }
            }

            // Add type filter if present
            if (this.typeFilter) {
                // If we already have filters, add AND operator
                if (filters.length > 0) {
                    filters.push(["and"]);
                }

                // Add filter for tipe equals typeFilter
                filters.push(["tipe", "=", this.typeFilter]);
            }

            // Add filters to params if any
            if (filters.length > 0) {
                params.filters = JSON.stringify(filters);
            }

            // Convert params object to URL query string
            const queryParams = new URLSearchParams();

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
            this.Api.get(`/customers?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.customers = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.customers = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.customers.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Error fetching customers:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to load customers'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Failed to load customers: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        changePage(page) {
            // page parameter is 1-based from UI, convert to 0-based for backend
            const zeroBasedPage = page - 1;
            if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
            this.pagination.pageNumber = zeroBasedPage;
            this.fetchCustomers(zeroBasedPage);
        },

        getTypeClass(type) {
            switch(type) {
                case 'INDIVIDU': return 'badge badge-primary';
                case 'INSTANSI': return 'badge badge-info';
                default: return 'badge badge-secondary';
            }
        },

        truncateText(text, maxLength) {
            if (text && text.length > maxLength) {
                return text.substring(0, maxLength) + '...';
            }
            return text;
        },

        showAddModal() {
            this.isEditing = false;
            this.currentCustomer = {
                id: null,
                nama: '',
                tipe: 'INDIVIDU',
                phone_number: '',
                alamat: '',
                totalOrders: 0,
                notes: '',
                recentOrders: []
            };
            $('#customerModal').modal('show');
        },

        editCustomer(customer) {
            this.isEditing = true;
            this.currentCustomer = JSON.parse(JSON.stringify(customer));
            $('#customerModal').modal('show');
        },

        deleteCustomer(customer) {
            this.customerToDelete = customer;
            $('#deleteModal').modal('show');
        },

        confirmDelete() {
            if (this.customerToDelete) {
                this.loading = true;
                this.Api.delete(`/customers/${this.customerToDelete.id}`)
                    .then(() => {
                        const index = this.customers.findIndex(c => c.id === this.customerToDelete.id);
                        if (index !== -1) {
                            this.customers.splice(index, 1);
                        }
                        this.customerToDelete = null;
                        $('#deleteModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error deleting customer:', error);
                        this.error = 'Failed to delete customer';
                        this.loading = false;
                    });
            }
        },

        saveCustomer() {
            this.loading = true;

            if (this.isEditing) {
                // Update existing customer
                this.Api.put(`/customers/${this.currentCustomer.id}`, this.currentCustomer)
                    .then(response => {
                        const index = this.customers.findIndex(c => c.id === this.currentCustomer.id);
                        if (index !== -1) {
                            this.customers.splice(index, 1, response.data);
                        }
                        $('#customerModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error updating customer:', error);
                        this.error = 'Failed to update customer';
                        this.loading = false;
                    });
            } else {
                // Add new customer
                this.Api.post('/customers', this.currentCustomer)
                    .then(response => {
                        this.customers.push(response.data);
                        $('#customerModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error adding customer:', error);
                        this.error = 'Failed to add customer';
                        this.loading = false;
                    });
            }
        },

        handleSearch() {
            // Clear any existing timeout
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            // Reset to first page and fetch customers with current search query
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
