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
                                    <option value="Completed">Completed</option>
                                    <option value="Pending">Pending</option>
                                    <option value="Cancelled">Cancelled</option>
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
                                        <td>{{ sale.invoice }}</td>
                                        <td>{{ formatDate(sale.date) }}</td>
                                        <td>{{ sale.customer }}</td>
                                        <td>{{ sale.items.length }}</td>
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
                                        <select class="form-control" id="customer" v-model="currentSale.customer" required>
                                            <option value="">Select Customer</option>
                                            <option v-for="customer in customers" :key="customer.id" :value="customer.name">
                                                {{ customer.name }}
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
                                                    <th>Size</th>
                                                    <th>Price</th>
                                                    <th>Quantity</th>
                                                    <th>Subtotal</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(item, index) in currentSale.items" :key="index">
                                                    <td>
                                                        <select class="form-control" v-model="item.product" @change="updateItemPrice(index)">
                                                            <option value="">Select Product</option>
                                                            <option v-for="product in products" :key="product.id" :value="product.name">
                                                                {{ product.name }}
                                                            </option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <select class="form-control" v-model="item.size">
                                                            <option value="">Select Size</option>
                                                            <option value="XS">XS</option>
                                                            <option value="S">S</option>
                                                            <option value="M">M</option>
                                                            <option value="L">L</option>
                                                            <option value="XL">XL</option>
                                                            <option value="XXL">XXL</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <input type="number" class="form-control" v-model="item.price" @change="calculateSubtotal(index)" min="0">
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
                                                    <td colspan="6">
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
                                            <div class="d-flex justify-content-between mb-2">
                                                <h6>Subtotal:</h6>
                                                <span>{{ formatCurrency(calculateTotal()) }}</span>
                                            </div>
                                            <div class="d-flex justify-content-between mb-2">
                                                <h6>Tax (10%):</h6>
                                                <span>{{ formatCurrency(calculateTax()) }}</span>
                                            </div>
                                            <div class="d-flex justify-content-between">
                                                <h5>Total:</h5>
                                                <h5>{{ formatCurrency(calculateGrandTotal()) }}</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-success">Complete Sale</button>
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
                                <p><strong>Invoice #:</strong> {{ selectedSale.invoice }}</p>
                                <p><strong>Date:</strong> {{ formatDate(selectedSale.date) }}</p>
                                <p><strong>Status:</strong> <span :class="getStatusClass(selectedSale.status)">{{ selectedSale.status }}</span></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Customer:</strong> {{ selectedSale.customer }}</p>
                                <p><strong>Total:</strong> {{ formatCurrency(selectedSale.total) }}</p>
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
                                    <tr v-for="(item, index) in selectedSale.items" :key="index">
                                        <td>{{ item.product }}</td>
                                        <td>{{ item.size }}</td>
                                        <td>{{ formatCurrency(item.price) }}</td>
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
                invoice: '',
                date: new Date().toISOString().substr(0, 10),
                customer: '',
                items: [],
                total: 0,
                status: 'Completed',
                notes: ''
            },
            sales: [
                {
                    id: 1,
                    invoice: 'INV-2023-001',
                    date: '2023-06-15',
                    customer: 'John Doe',
                    items: [
                        { product: 'School Uniform Set', size: 'M', price: 350000, quantity: 1, subtotal: 350000 }
                    ],
                    total: 350000,
                    status: 'Completed',
                    notes: 'Customer paid in cash'
                },
                {
                    id: 2,
                    invoice: 'INV-2023-002',
                    date: '2023-06-16',
                    customer: 'Jane Smith',
                    items: [
                        { product: 'School Shirt', size: 'L', price: 150000, quantity: 2, subtotal: 300000 },
                        { product: 'School Pants', size: 'L', price: 200000, quantity: 1, subtotal: 200000 }
                    ],
                    total: 500000,
                    status: 'Completed',
                    notes: ''
                },
                {
                    id: 3,
                    invoice: 'INV-2023-003',
                    date: '2023-06-17',
                    customer: 'Robert Johnson',
                    items: [
                        { product: 'School Skirt', size: 'S', price: 180000, quantity: 1, subtotal: 180000 },
                        { product: 'School Tie', size: 'One Size', price: 50000, quantity: 1, subtotal: 50000 }
                    ],
                    total: 230000,
                    status: 'Pending',
                    notes: 'Customer will pick up tomorrow'
                },
                {
                    id: 4,
                    invoice: 'INV-2023-004',
                    date: '2023-06-18',
                    customer: 'Emily Davis',
                    items: [
                        { product: 'School Uniform Set', size: 'S', price: 350000, quantity: 1, subtotal: 350000 }
                    ],
                    total: 350000,
                    status: 'Cancelled',
                    notes: 'Customer changed their mind'
                },
                {
                    id: 5,
                    invoice: 'INV-2023-005',
                    date: '2023-06-19',
                    customer: 'Michael Wilson',
                    items: [
                        { product: 'School Shirt', size: 'XL', price: 150000, quantity: 3, subtotal: 450000 }
                    ],
                    total: 450000,
                    status: 'Completed',
                    notes: ''
                }
            ],
            customers: [
                { id: 1, name: 'John Doe' },
                { id: 2, name: 'Jane Smith' },
                { id: 3, name: 'Robert Johnson' },
                { id: 4, name: 'Emily Davis' },
                { id: 5, name: 'Michael Wilson' }
            ],
            products: [
                {
                    id: 1,
                    name: 'School Uniform Set',
                    price: 350000
                },
                {
                    id: 2,
                    name: 'School Shirt',
                    price: 150000
                },
                {
                    id: 3,
                    name: 'School Pants',
                    price: 200000
                },
                {
                    id: 4,
                    name: 'School Skirt',
                    price: 180000
                },
                {
                    id: 5,
                    name: 'School Tie',
                    price: 50000
                }
            ]
        }
    },
    computed: {
        filteredSales() {
            let filtered = this.sales;
            
            if (this.searchQuery) {
                const query = this.searchQuery.toLowerCase();
                filtered = filtered.filter(sale => 
                    sale.invoice.toLowerCase().includes(query) || 
                    sale.customer.toLowerCase().includes(query)
                );
            }
            
            if (this.statusFilter) {
                filtered = filtered.filter(sale => sale.status === this.statusFilter);
            }
            
            return filtered;
        }
    },
    mounted() {
        // Initialize date range picker
        $('#dateRange').daterangepicker({
            locale: {
                format: 'MM/DD/YYYY'
            }
        });
    },
    methods: {
        formatDate(dateString) {
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            return new Date(dateString).toLocaleDateString(undefined, options);
        },
        formatCurrency(value) {
            return 'Rp ' + value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
        getStatusClass(status) {
            switch(status) {
                case 'Completed':
                    return 'badge badge-success';
                case 'Pending':
                    return 'badge badge-warning';
                case 'Cancelled':
                    return 'badge badge-danger';
                default:
                    return 'badge badge-secondary';
            }
        },
        showNewSaleModal() {
            this.currentSale = {
                id: null,
                invoice: 'INV-' + new Date().getFullYear() + '-' + (this.sales.length + 1).toString().padStart(3, '0'),
                date: new Date().toISOString().substr(0, 10),
                customer: '',
                items: [{ product: '', size: '', price: 0, quantity: 1, subtotal: 0 }],
                total: 0,
                status: 'Completed',
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
            alert('Printing invoice: ' + sale.invoice);
        },
        addItem() {
            this.currentSale.items.push({ product: '', size: '', price: 0, quantity: 1, subtotal: 0 });
        },
        removeItem(index) {
            this.currentSale.items.splice(index, 1);
            if (this.currentSale.items.length === 0) {
                this.addItem();
            }
            this.calculateTotal();
        },
        updateItemPrice(index) {
            const item = this.currentSale.items[index];
            const product = this.products.find(p => p.name === item.product);
            if (product) {
                item.price = product.price;
                this.calculateSubtotal(index);
            }
        },
        calculateSubtotal(index) {
            const item = this.currentSale.items[index];
            item.subtotal = item.price * item.quantity;
            this.calculateTotal();
        },
        calculateTotal() {
            let total = 0;
            this.currentSale.items.forEach(item => {
                total += item.subtotal;
            });
            this.currentSale.total = total;
            return total;
        },
        calculateTax() {
            return this.calculateTotal() * 0.1;
        },
        calculateGrandTotal() {
            return this.calculateTotal() + this.calculateTax();
        },
        saveSale() {
            // Validate form
            if (!this.currentSale.customer || this.currentSale.items.some(item => !item.product)) {
                alert('Please fill in all required fields');
                return;
            }

            // Calculate final total
            this.currentSale.total = this.calculateGrandTotal();

            if (this.currentSale.id) {
                // Update existing sale
                const index = this.sales.findIndex(s => s.id === this.currentSale.id);
                if (index !== -1) {
                    this.sales.splice(index, 1, { ...this.currentSale });
                }
            } else {
                // Add new sale
                const newId = Math.max(...this.sales.map(s => s.id), 0) + 1;
                this.sales.push({
                    ...this.currentSale,
                    id: newId
                });
            }
            $('#newSaleModal').modal('hide');
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
</style>