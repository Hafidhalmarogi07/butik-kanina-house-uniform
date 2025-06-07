<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Sales List</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-success" @click="showNewSaleModal">
                                <i class="fas fa-plus"></i> New Sale
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Search sales..." v-model="searchQuery">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">
                                            <i class="far fa-calendar-alt"></i>
                                        </span>
                                    </div>
                                    <input type="text" class="form-control" id="dateRange" placeholder="Date range">
                                </div>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="statusFilter">
                                    <option value="">All Statuses</option>
                                    <option value="COMPLETED">Completed</option>
                                    <option value="PENDING">Pending</option>
                                    <option value="CANCELLED">Cancelled</option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>Invoice #</th>
                                        <th>Date</th>
                                        <th>Customer</th>
                                        <th>Items</th>
                                        <th>Total</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="sale in filteredSales" :key="sale.id">
                                        <td>{{ sale.invoice_number }}</td>
                                        <td>{{ formatDate(sale.date) }}</td>
                                        <td>{{ sale.customer ? sale.customer.nama : '' }}</td>
                                        <td>{{ sale.details ? sale.details.length : 0 }}</td>
                                        <td>{{ formatCurrency(sale.total) }}</td>
                                        <td>
                                            <span :class="getStatusClass(sale.status)">{{ sale.status }}</span>
                                        </td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="viewSaleDetails(sale)">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" @click="printInvoice(sale)">
                                                    <i class="fas fa-print"></i>
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
                            <p>No sales found</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- New Sale Modal -->
        <div class="modal fade" id="newSaleModal" tabindex="-1" role="dialog" aria-labelledby="newSaleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="newSaleModalLabel">New Sale</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveSale">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="customer">Customer</label>
                                        <select class="form-control" id="customer" v-model="currentSale.customer.id" required>
                                            <option value="">Select Customer</option>
                                            <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                                                {{ customer.nama }}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="saleDate">Date</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="far fa-calendar-alt"></i>
                                                </span>
                                            </div>
                                            <input type="date" class="form-control" id="saleDate" v-model="currentSale.date" required>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-12">
                                    <h5>Items</h5>
                                    <div class="table-responsive">
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>Product</th>
                                                    <th>Quantity</th>
                                                    <th>Subtotal</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(item, index) in currentSale.details" :key="index">
                                                    <td>
                                                        <select class="form-control" v-model="item.product.id" @change="updateItemPrice(index)">
                                                            <option value="">Select Product</option>
                                                            <option v-for="product in products" :key="product.id" :value="product.id">
                                                                {{ product.name }} ({{ product.size }}) - {{ formatCurrency(product.selling_price) }}
                                                            </option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <input type="number" class="form-control" v-model="item.quantity" @change="calculateSubtotal(index)" min="1">
                                                    </td>
                                                    <td>{{ formatCurrency(item.subtotal) }}</td>
                                                    <td>
                                                        <button type="button" class="btn btn-danger btn-sm" @click="removeItem(index)">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="4">
                                                        <button type="button" class="btn btn-primary btn-sm" @click="addItem">
                                                            <i class="fas fa-plus"></i> Add Item
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="notes">Notes</label>
                                        <textarea class="form-control" id="notes" rows="3" v-model="currentSale.notes"></textarea>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between">
                                                <h5>Total:</h5>
                                                <h5>{{ formatCurrency(calculateTotal()) }}</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-success" :disabled="loading">
                                    <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    Complete Sale
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Sale Details Modal -->
        <div class="modal fade" id="saleDetailsModal" tabindex="-1" role="dialog" aria-labelledby="saleDetailsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="saleDetailsModalLabel">Sale Details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="selectedSale">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <p><strong>Invoice #:</strong> {{ selectedSale.invoice_number }}</p>
                                <p><strong>Date:</strong> {{ formatDate(selectedSale.date) }}</p>
                                <p><strong>Status:</strong> <span :class="getStatusClass(selectedSale.status)">{{ selectedSale.status }}</span></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Customer:</strong> {{ selectedSale.customer ? selectedSale.customer.nama : '' }}</p>
                                <p><strong>Total:</strong> {{ formatCurrency(selectedSale.total) }}</p>
                                <p><strong>Admin:</strong> {{ selectedSale.admin ? selectedSale.admin.full_name : '' }}</p>
                            </div>
                        </div>

                        <h6>Items</h6>
                        <div class="table-responsive">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>Size</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(item, index) in selectedSale.details" :key="index">
                                        <td>{{ item.product ? item.product.name : '' }}</td>
                                        <td>{{ item.product ? item.product.size : '' }}</td>
                                        <td>{{ item.product ? formatCurrency(item.product.selling_price) : '' }}</td>
                                        <td>{{ item.quantity }}</td>
                                        <td>{{ formatCurrency(item.subtotal) }}</td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="4" class="text-right"><strong>Total:</strong></td>
                                        <td>{{ formatCurrency(selectedSale.total) }}</td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>

                        <div class="row mt-3" v-if="selectedSale.notes">
                            <div class="col-12">
                                <p><strong>Notes:</strong></p>
                                <p>{{ selectedSale.notes }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" @click="printInvoice(selectedSale)">
                            <i class="fas fa-print"></i> Print Invoice
                        </button>
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
            selectedSale: null,
            currentSale: {
                id: null,
                invoice_number: '',
                date: new Date().toISOString().substr(0, 10),
                customer: {
                    id: null
                },
                details: [],
                total: 0,
                status: 'COMPLETED',
                notes: ''
            },
            sales: [],
            customers: [],
            products: [],
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
        filteredSales() {
            // Since filtering is now done on the server side, 
            // we simply return the sales array
            return this.sales;
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
        // Initialize date range picker
        $('#dateRange').daterangepicker({
            locale: {
                format: 'MM/DD/YYYY'
            }
        });

        // Fetch initial data
        this.fetchSales(0);
        this.fetchCustomers();
        this.fetchProducts();
    },
    watch: {
        searchQuery: function(newVal) {
            // Debounce search to avoid too many requests
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            this.searchTimeout = setTimeout(() => {
                // Reset to first page and fetch sales with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        },
        statusFilter: function() {
            // Reset to first page and fetch sales with new status filter
            this.changePage(1);
        }
    },
    methods: {
        fetchSales(page = 0) {
            this.loading = true;

            // Prepare params object with pagination
            const params = {
                sort: '-id',
                page: page, 
                size: this.pagination.pageSize 
            };

            // Prepare filters array for backend
            const filters = [];

            // Add search query if present
            if (this.searchQuery) {
                // Add filter for invoice_number containing search query
                filters.push(["invoice_number", "like", this.searchQuery]);

                // If we want to search in customer name too, add OR operator and customer filter
                if (this.searchQuery.trim() !== "") {
                    filters.push(["or"]);
                    filters.push(["customer.nama", "like", this.searchQuery]);
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
            this.Api.get(`/sale?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.sales = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.sales = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.sales.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Error fetching sales:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to load sales'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Failed to load sales: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        fetchCustomers() {
            this.Api.get('/customers')
                .then(response => {
                    this.customers = response.data.content || response.data;
                })
                .catch(error => {
                    console.error('Error fetching customers:', error);
                });
        },

        fetchProducts() {
            this.Api.get('/product')
                .then(response => {
                    this.products = response.data.content || response.data;
                })
                .catch(error => {
                    console.error('Error fetching products:', error);
                });
        },

        changePage(page) {
            // page parameter is 1-based from UI, convert to 0-based for backend
            const zeroBasedPage = page - 1;
            if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
            this.pagination.pageNumber = zeroBasedPage;
            this.fetchSales(zeroBasedPage);
        },

        formatDate(dateString) {
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            return new Date(dateString).toLocaleDateString(undefined, options);
        },

        formatCurrency(value) {
            return 'Rp ' + value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },

        getStatusClass(status) {
            switch(status) {
                case 'COMPLETED':
                    return 'badge badge-success';
                case 'PENDING':
                    return 'badge badge-warning';
                case 'CANCELLED':
                    return 'badge badge-danger';
                default:
                    return 'badge badge-secondary';
            }
        },

        showNewSaleModal() {
            this.currentSale = {
                id: null,
                invoice_number: '',
                date: new Date().toISOString().substr(0, 10),
                customer: {
                    id: null
                },
                details: [{ 
                    product: {
                        id: null
                    }, 
                    quantity: 1, 
                    subtotal: 0 
                }],
                total: 0,
                status: 'COMPLETED',
                notes: ''
            };
            $('#newSaleModal').modal('show');
        },

        viewSaleDetails(sale) {
            this.selectedSale = sale;
            $('#saleDetailsModal').modal('show');
        },

        printInvoice(sale) {
            // In a real application, this would open a print view or generate a PDF
            alert('Printing invoice: ' + sale.invoice_number);
        },

        addItem() {
            this.currentSale.details.push({ 
                product: {
                    id: null
                }, 
                quantity: 1, 
                subtotal: 0 
            });
        },

        removeItem(index) {
            this.currentSale.details.splice(index, 1);
            if (this.currentSale.details.length === 0) {
                this.addItem();
            }
            this.calculateTotal();
        },

        updateItemPrice(index) {
            const item = this.currentSale.details[index];
            const product = this.products.find(p => p.id === item.product.id);
            if (product) {
                this.calculateSubtotal(index);
            }
        },

        calculateSubtotal(index) {
            const item = this.currentSale.details[index];
            const product = this.products.find(p => p.id === item.product.id);
            if (product) {
                item.subtotal = product.selling_price * item.quantity;
            }
            this.calculateTotal();
        },

        calculateTotal() {
            let total = 0;
            this.currentSale.details.forEach(item => {
                total += item.subtotal || 0;
            });
            this.currentSale.total = total;
            return total;
        },

        saveSale() {
            // Validate form
            if (!this.currentSale.customer.id || this.currentSale.details.some(item => !item.product.id)) {
                alert('Please fill in all required fields');
                return;
            }

            this.loading = true;

            // Prepare the request payload
            const payload = {
                customer: {
                    id: this.currentSale.customer.id
                },
                details: this.currentSale.details.map(item => ({
                    quantity: item.quantity,
                    product: {
                        id: item.product.id
                    }
                }))
            };

            // Make the API call to create a new sale
            this.Api.post('/sale', payload)
                .then(response => {
                    // Add the new sale to the list
                    this.sales.unshift(response.data);
                    $('#newSaleModal').modal('hide');
                    this.loading = false;

                    // Refresh the sales list
                    this.fetchSales(this.pagination.pageNumber);
                })
                .catch(error => {
                    console.error('Error creating sale:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to create sale'}`;
                    } else if (error.request) {
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        this.error = 'Failed to create sale: ' + error.message;
                    }

                    this.loading = false;
                });
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
</style>
