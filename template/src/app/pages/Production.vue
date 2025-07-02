<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Production List</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Add Production
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Search production..." 
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
                                <select class="form-control" v-model="statusFilter" @change="handleSearch">
                                    <option value="">All Statuses</option>
                                    <option value="WAITING">Waiting</option>
                                    <option value="IN_PROGRESS">In Progress</option>
                                    <option value="FINISHED">Finished</option>
                                    <option value="CANCELED">Canceled</option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Product</th>
                                        <th>Quantity</th>
                                        <th>Start Date</th>
                                        <th>End Date</th>
                                        <th>Status</th>
                                        <th>Progress</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="item in productions" :key="item.id">
                                        <td>{{ item.id }}</td>
                                        <td>{{ item.product ? item.product.name : '' }}</td>
                                        <td>{{ item.quantity }}</td>
                                        <td>{{ formatDate(item.start_date) }}</td>
                                        <td>{{ formatDate(item.end_date) }}</td>
                                        <td>
                                            <span :class="getStatusClass(item.status)">{{ formatStatus(item.status) }}</span>
                                        </td>
                                        <td>
                                            <div class="progress">
                                                <div class="progress-bar" :class="getProgressBarClass(item.status)" role="progressbar" 
                                                    :style="{width: item.progress + '%'}" 
                                                    :aria-valuenow="item.progress" aria-valuemin="0" aria-valuemax="100">
                                                    {{ item.progress }}%
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="viewProduction(item)">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" @click="editProduction(item)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="deleteProduction(item)">
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
                            <p>No productions found</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit Production Modal -->
        <div class="modal fade" id="productionModal" tabindex="-1" role="dialog" aria-labelledby="productionModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="productionModalLabel">{{ isEditing ? 'Edit Production' : 'Add New Production' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveProduction">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="productionProduct">Product</label>
                                        <div class="position-relative">
                                            <select class="form-control" id="productionProduct" v-model="currentProduction.product.id" 
                                                    required @scroll="handleProductScroll" ref="productSelect">
                                                <option v-for="product in products" :key="product.id" :value="product.id">
                                                    {{ product.name }} ({{ product.size }})
                                                </option>
                                                <option v-if="productPagination.loading" value="" disabled>Loading more products...</option>
                                                <option v-if="!productPagination.hasMore && products.length > 0" value="" disabled>No more products</option>
                                            </select>
                                            <div v-if="productPagination.loading" class="position-absolute" style="right: 10px; top: 10px;">
                                                <div class="spinner-border spinner-border-sm text-primary" role="status">
                                                    <span class="sr-only">Loading...</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="productionQuantity">Quantity</label>
                                        <input type="number" class="form-control" id="productionQuantity" v-model="currentProduction.quantity" min="1" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="productionStartDate">Start Date</label>
                                        <input type="date" class="form-control" id="productionStartDate" v-model="currentProduction.start_date" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="productionEndDate">End Date</label>
                                        <input type="date" class="form-control" id="productionEndDate" v-model="currentProduction.end_date" required>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="productionStatus">Status</label>
                                        <select class="form-control" id="productionStatus" v-model="currentProduction.status" required>
                                            <option value="WAITING">Waiting</option>
                                            <option value="IN_PROGRESS">In Progress</option>
                                            <option value="FINISHED">Finished</option>
                                            <option value="CANCELED">Canceled</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="productionProgress">Progress (%)</label>
                                        <input type="number" class="form-control" id="productionProgress" v-model="currentProduction.progress" min="0" max="100" required>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="productionDescription">Description</label>
                                <textarea class="form-control" id="productionDescription" rows="3" v-model="currentProduction.description"></textarea>
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

        <!-- View Production Modal -->
        <div class="modal fade" id="viewProductionModal" tabindex="-1" role="dialog" aria-labelledby="viewProductionModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewProductionModalLabel">Production Details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="currentProduction">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>ID:</strong> {{ currentProduction.id }}</p>
                                <p><strong>Product:</strong> {{ currentProduction.product ? currentProduction.product.name : '' }}</p>
                                <p><strong>Category:</strong> {{ currentProduction.product && currentProduction.product.category ? currentProduction.product.category.name : '' }}</p>
                                <p><strong>Quantity:</strong> {{ currentProduction.quantity }}</p>
                                <p><strong>Start Date:</strong> {{ formatDate(currentProduction.start_date) }}</p>
                                <p><strong>End Date:</strong> {{ formatDate(currentProduction.end_date) }}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Status:</strong> <span :class="getStatusClass(currentProduction.status)">{{ formatStatus(currentProduction.status) }}</span></p>
                                <p><strong>Progress:</strong></p>
                                <div class="progress">
                                    <div class="progress-bar" :class="getProgressBarClass(currentProduction.status)" role="progressbar" 
                                        :style="{width: currentProduction.progress + '%'}" 
                                        :aria-valuenow="currentProduction.progress" aria-valuemin="0" aria-valuemax="100">
                                        {{ currentProduction.progress }}%
                                    </div>
                                </div>
                                <p class="mt-3"><strong>Description:</strong> {{ currentProduction.description || 'No description' }}</p>
                                <p><strong>Created:</strong> {{ formatDateTime(currentProduction.created) }}</p>
                                <p><strong>Updated:</strong> {{ formatDateTime(currentProduction.updated) }}</p>
                            </div>
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
                        Are you sure you want to delete this production record? This action cannot be undone.
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
            statusFilter: '',
            searchTimeout: null,
            isEditing: false,
            currentProduction: {
                id: null,
                product: {
                    id: null,
                    name: '',
                    category: {
                        id: null,
                        name: ''
                    }
                },
                quantity: 0,
                start_date: '',
                end_date: '',
                status: 'WAITING',
                progress: 0,
                description: ''
            },
            productionToDelete: null,
            productions: [],
            products: [],
            productPagination: {
                totalPages: 0,
                totalElements: 0,
                pageNumber: 0,
                pageSize: 1000,
                loading: false,
                hasMore: true
            },
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
        this.fetchProductions(0); // Start with page 0 for backend (equivalent to UI page 1)
        this.fetchProducts();

        // Add event listener for modal shown to reset product pagination
        $(document).on('shown.bs.modal', '#productionModal', () => {
            // Reset product pagination if needed
            if (this.products.length === 0 || this.productPagination.pageNumber > 0) {
                this.productPagination.pageNumber = 0;
                this.productPagination.hasMore = true;
                this.fetchProducts(0, false);
            }
        });
    },
    watch: {
        searchQuery: function(newVal) {
            // Debounce search to avoid too many requests
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            this.searchTimeout = setTimeout(() => {
                // Reset to first page and fetch productions with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        }
    },
    methods: {
        fetchProductions(page = 0) {
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
                // Add filter for description containing search query
                filters.push(["description", "like", this.searchQuery]);

                // If we want to search in product name too, add OR operator and product.name filter
                if (this.searchQuery.trim() !== "") {
                    filters.push(["or"]);
                    filters.push(["product.name", "like", this.searchQuery]);
                }
            }

            // Add status filter if present
            if (this.statusFilter) {
                // If we already have filters, add AND operator
                if (filters.length > 0) {
                    filters.push(["and"]);
                }

                // Add filter for status equals statusFilter
                filters.push(["status", "=", this.statusFilter]);
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
            this.Api.get(`/productions?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.productions = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.productions = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.productions.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Error fetching productions:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to load productions'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Failed to load productions: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        fetchProducts(page = 0, append = false) {
            if (this.productPagination.loading || (!append && !this.productPagination.hasMore)) return;

            this.productPagination.loading = true;

            // Prepare query parameters
            const queryParams = new URLSearchParams();
            queryParams.append('page', page + 1); // Convert 0-based to 1-based
            queryParams.append('size', this.productPagination.pageSize);

            this.Api.get(`/product?${queryParams.toString()}`)
                .then(response => {
                    const newProducts = response.data.content || response.data;

                    // If append is true, add to existing products, otherwise replace
                    if (append) {
                        this.products = [...this.products, ...newProducts];
                    } else {
                        this.products = newProducts;
                    }

                    // Update product pagination data
                    this.productPagination = {
                        ...this.productPagination,
                        totalPages: response.data.total_pages || 0,
                        totalElements: response.data.total_elements || 0,
                        pageNumber: page,
                        hasMore: page < (response.data.total_pages - 1),
                        loading: false
                    };
                })
                .catch(error => {
                    console.error('Error fetching products:', error);
                    this.productPagination.loading = false;
                });
        },

        loadMoreProducts() {
            if (this.productPagination.hasMore && !this.productPagination.loading) {
                const nextPage = this.productPagination.pageNumber + 1;
                this.fetchProducts(nextPage, true);
            }
        },

        changePage(page) {
            // page parameter is 1-based from UI, convert to 0-based for backend
            const zeroBasedPage = page - 1;
            if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
            this.pagination.pageNumber = zeroBasedPage;
            this.fetchProductions(zeroBasedPage);
        },

        formatDate(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            return date.toLocaleDateString();
        },

        formatDateTime(dateTimeString) {
            if (!dateTimeString) return '';
            const date = new Date(dateTimeString);
            return date.toLocaleString();
        },

        formatStatus(status) {
            switch(status) {
                case 'WAITING': return 'Waiting';
                case 'IN_PROGRESS': return 'In Progress';
                case 'FINISHED': return 'Finished';
                case 'CANCELED': return 'Canceled';
                default: return status;
            }
        },

        getStatusClass(status) {
            switch(status) {
                case 'WAITING': return 'badge badge-secondary';
                case 'IN_PROGRESS': return 'badge badge-primary';
                case 'FINISHED': return 'badge badge-success';
                case 'CANCELED': return 'badge badge-danger';
                default: return 'badge badge-info';
            }
        },

        getProgressBarClass(status) {
            switch(status) {
                case 'WAITING': return 'bg-secondary';
                case 'IN_PROGRESS': return 'bg-primary';
                case 'FINISHED': return 'bg-success';
                case 'CANCELED': return 'bg-danger';
                default: return 'bg-info';
            }
        },

        handleSearch() {
            // Clear any existing timeout
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            // Reset to first page and fetch productions with current search query
            this.changePage(1);
        },

        showAddModal() {
            this.isEditing = false;
            const today = new Date().toISOString().split('T')[0];
            this.currentProduction = {
                id: null,
                product: {
                    id: null,
                    name: '',
                    category: {
                        id: null,
                        name: ''
                    }
                },
                quantity: 0,
                start_date: today,
                end_date: '',
                status: 'WAITING',
                progress: 0,
                description: ''
            };
            $('#productionModal').modal('show');
        },

        viewProduction(item) {
            this.currentProduction = JSON.parse(JSON.stringify(item));
            $('#viewProductionModal').modal('show');
        },

        editProduction(item) {
            this.isEditing = true;
            // Create a deep copy to avoid modifying the original object
            this.currentProduction = JSON.parse(JSON.stringify(item));

            // Ensure product is properly set
            if (!this.currentProduction.product) {
                this.currentProduction.product = { 
                    id: null,
                    name: '',
                    category: {
                        id: null,
                        name: ''
                    }
                };
            }

            $('#productionModal').modal('show');
        },

        deleteProduction(item) {
            this.productionToDelete = item;
            $('#deleteModal').modal('show');
        },

        confirmDelete() {
            if (this.productionToDelete) {
                this.loading = true;
                this.Api.delete(`/productions/${this.productionToDelete.id}`)
                    .then(() => {
                        // Refresh the productions list
                        this.fetchProductions(this.pagination.pageNumber);
                        this.productionToDelete = null;
                        $('#deleteModal').modal('hide');
                    })
                    .catch(error => {
                        console.error('Error deleting production:', error);
                        this.error = 'Failed to delete production';
                        this.loading = false;
                    });
            }
        },

        handleProductScroll(event) {
            // Check if user has scrolled to the bottom
            const select = event.target;
            const scrollPosition = select.scrollTop + select.clientHeight;
            const scrollHeight = select.scrollHeight;

            // If scrolled to near bottom (within 20px), load more products
            if (scrollHeight - scrollPosition <= 20) {
                this.loadMoreProducts();
            }
        },

        saveProduction() {
            this.loading = true;

            // Prepare production data for API
            const productionData = {
                ...this.currentProduction
            };

            // If product is just an ID, convert it to the expected format
            if (typeof productionData.product === 'object' && productionData.product !== null) {
                // If product.id is set but we're selecting from dropdown, get the full product
                if (productionData.product.id) {
                    const selectedProduct = this.products.find(p => p.id === productionData.product.id);
                    if (selectedProduct) {
                        productionData.product = {
                            id: selectedProduct.id
                        };
                    }
                }
            }

            if (this.isEditing) {
                // Update existing production
                this.Api.put(`/productions/${this.currentProduction.id}`, productionData)
                    .then(response => {
                        // Refresh the productions list
                        this.fetchProductions(this.pagination.pageNumber);
                        $('#productionModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error updating production:', error);
                        this.error = 'Failed to update production';
                        this.loading = false;
                    });
            } else {
                // Add new production
                this.Api.post('/productions', productionData)
                    .then(response => {
                        // Refresh the productions list
                        this.fetchProductions(this.pagination.pageNumber);
                        $('#productionModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error adding production:', error);
                        this.error = 'Failed to add production';
                        this.loading = false;
                    });
            }
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
.progress {
    height: 20px;
}
.progress-bar {
    min-width: 2em;
}
/* Style for product dropdown to enable scrolling */
#productionProduct {
    max-height: 200px;
    overflow-y: auto;
}
</style>
