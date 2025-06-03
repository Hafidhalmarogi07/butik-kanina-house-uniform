<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Order List</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Create Order
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Search orders..." v-model="searchQuery">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="statusFilter">
                                    <option value="">All Statuses</option>
                                    <option value="Pending">Pending</option>
                                    <option value="Processing">Processing</option>
                                    <option value="Shipped">Shipped</option>
                                    <option value="Delivered">Delivered</option>
                                    <option value="Cancelled">Cancelled</option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Customer</th>
                                        <th>Date</th>
                                        <th>Items</th>
                                        <th>Total</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="order in filteredOrders" :key="order.id">
                                        <td>{{ order.id }}</td>
                                        <td>{{ order.customer }}</td>
                                        <td>{{ order.date }}</td>
                                        <td>{{ order.items }}</td>
                                        <td>Rp {{ order.total.toLocaleString() }}</td>
                                        <td>
                                            <span :class="getStatusClass(order.status)">{{ order.status }}</span>
                                        </td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="viewOrder(order)">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" @click="editOrder(order)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="deleteOrder(order)">
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

        <!-- Add/Edit Order Modal -->
        <div class="modal fade" id="orderModal" tabindex="-1" role="dialog" aria-labelledby="orderModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderModalLabel">{{ isEditing ? 'Edit Order' : 'Create New Order' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveOrder">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="orderCustomer">Customer</label>
                                        <select class="form-control" id="orderCustomer" v-model="currentOrder.customer" required>
                                            <option value="John Doe">John Doe</option>
                                            <option value="Jane Smith">Jane Smith</option>
                                            <option value="Robert Johnson">Robert Johnson</option>
                                            <option value="Emily Davis">Emily Davis</option>
                                            <option value="Michael Brown">Michael Brown</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="orderDate">Order Date</label>
                                        <input type="date" class="form-control" id="orderDate" v-model="currentOrder.date" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="orderStatus">Status</label>
                                        <select class="form-control" id="orderStatus" v-model="currentOrder.status" required>
                                            <option value="Pending">Pending</option>
                                            <option value="Processing">Processing</option>
                                            <option value="Shipped">Shipped</option>
                                            <option value="Delivered">Delivered</option>
                                            <option value="Cancelled">Cancelled</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="orderItems">Number of Items</label>
                                        <input type="number" class="form-control" id="orderItems" v-model="currentOrder.items" min="1" required>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="orderTotal">Total Amount</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">Rp</span>
                                    </div>
                                    <input type="number" class="form-control" id="orderTotal" v-model="currentOrder.total" min="0" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="orderNotes">Notes</label>
                                <textarea class="form-control" id="orderNotes" rows="3" v-model="currentOrder.notes"></textarea>
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

        <!-- View Order Modal -->
        <div class="modal fade" id="viewOrderModal" tabindex="-1" role="dialog" aria-labelledby="viewOrderModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewOrderModalLabel">Order Details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="currentOrder">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Order ID:</strong> {{ currentOrder.id }}</p>
                                <p><strong>Customer:</strong> {{ currentOrder.customer }}</p>
                                <p><strong>Date:</strong> {{ currentOrder.date }}</p>
                                <p><strong>Status:</strong> <span :class="getStatusClass(currentOrder.status)">{{ currentOrder.status }}</span></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Items:</strong> {{ currentOrder.items }}</p>
                                <p><strong>Total:</strong> Rp {{ currentOrder.total ? currentOrder.total.toLocaleString() : 0 }}</p>
                                <p><strong>Notes:</strong> {{ currentOrder.notes || 'No notes' }}</p>
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
                        Are you sure you want to delete this order? This action cannot be undone.
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
            isEditing: false,
            currentOrder: {
                id: null,
                customer: '',
                date: '',
                items: 0,
                total: 0,
                status: 'Pending',
                notes: ''
            },
            orderToDelete: null,
            orders: [
                {
                    id: 'ORD-001',
                    customer: 'John Doe',
                    date: '2023-05-15',
                    items: 3,
                    total: 550000,
                    status: 'Delivered',
                    notes: 'School uniform for 3 children'
                },
                {
                    id: 'ORD-002',
                    customer: 'Jane Smith',
                    date: '2023-05-18',
                    items: 1,
                    total: 150000,
                    status: 'Processing',
                    notes: 'Needs to be delivered by next week'
                },
                {
                    id: 'ORD-003',
                    customer: 'Robert Johnson',
                    date: '2023-05-20',
                    items: 5,
                    total: 850000,
                    status: 'Pending',
                    notes: 'Custom size requirements'
                },
                {
                    id: 'ORD-004',
                    customer: 'Emily Davis',
                    date: '2023-05-22',
                    items: 2,
                    total: 350000,
                    status: 'Shipped',
                    notes: ''
                },
                {
                    id: 'ORD-005',
                    customer: 'Michael Brown',
                    date: '2023-05-25',
                    items: 4,
                    total: 650000,
                    status: 'Cancelled',
                    notes: 'Customer changed their mind'
                }
            ]
        }
    },
    computed: {
        filteredOrders() {
            let filtered = this.orders;
            
            if (this.searchQuery) {
                const query = this.searchQuery.toLowerCase();
                filtered = filtered.filter(order => 
                    order.id.toLowerCase().includes(query) || 
                    order.customer.toLowerCase().includes(query)
                );
            }
            
            if (this.statusFilter) {
                filtered = filtered.filter(order => order.status === this.statusFilter);
            }
            
            return filtered;
        }
    },
    methods: {
        getStatusClass(status) {
            switch(status) {
                case 'Pending': return 'badge badge-warning';
                case 'Processing': return 'badge badge-info';
                case 'Shipped': return 'badge badge-primary';
                case 'Delivered': return 'badge badge-success';
                case 'Cancelled': return 'badge badge-danger';
                default: return 'badge badge-secondary';
            }
        },
        showAddModal() {
            this.isEditing = false;
            this.currentOrder = {
                id: null,
                customer: '',
                date: new Date().toISOString().split('T')[0],
                items: 0,
                total: 0,
                status: 'Pending',
                notes: ''
            };
            $('#orderModal').modal('show');
        },
        viewOrder(order) {
            this.currentOrder = { ...order };
            $('#viewOrderModal').modal('show');
        },
        editOrder(order) {
            this.isEditing = true;
            this.currentOrder = { ...order };
            $('#orderModal').modal('show');
        },
        deleteOrder(order) {
            this.orderToDelete = order;
            $('#deleteModal').modal('show');
        },
        confirmDelete() {
            if (this.orderToDelete) {
                const index = this.orders.findIndex(o => o.id === this.orderToDelete.id);
                if (index !== -1) {
                    this.orders.splice(index, 1);
                }
                this.orderToDelete = null;
                $('#deleteModal').modal('hide');
            }
        },
        saveOrder() {
            if (this.isEditing) {
                // Update existing order
                const index = this.orders.findIndex(o => o.id === this.currentOrder.id);
                if (index !== -1) {
                    this.orders.splice(index, 1, { ...this.currentOrder });
                }
            } else {
                // Add new order
                const newId = `ORD-${String(this.orders.length + 1).padStart(3, '0')}`;
                this.orders.push({
                    ...this.currentOrder,
                    id: newId
                });
            }
            $('#orderModal').modal('hide');
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
</style>