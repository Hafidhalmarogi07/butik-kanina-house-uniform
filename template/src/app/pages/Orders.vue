<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Daftar Pesanan</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showNewOrderModal">
                                <i class="fas fa-plus"></i> Buat Pesanan
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Cari pesanan..." v-model="searchQuery">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
<!--                            <div class="col-md-3">-->
<!--                                <div class="input-group">-->
<!--                                    <div class="input-group-prepend">-->
<!--                                        <span class="input-group-text">-->
<!--                                            <i class="far fa-calendar-alt"></i>-->
<!--                                        </span>-->
<!--                                    </div>-->
<!--                                    <input type="text" class="form-control" id="dateRange" placeholder="Date range">-->
<!--                                </div>-->
<!--                            </div>-->
                            <div class="col-md-3">
                                <select class="form-control" v-model="statusFilter">
                                    <option value="">Semua Status</option>
                                    <option value="PENDING">Tertunda</option>
                                    <option value="CONFIRMED">Dikonfirmasi</option>
                                    <option value="COMPLETED">Selesai</option>
                                    <option value="CANCELLED">Dibatalkan</option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>Pesanan #</th>
                                        <th>Tanggal</th>
                                        <th>Pelanggan</th>
                                        <th>Item</th>
                                        <th>Total</th>
                                        <th>Dibayar</th>
                                        <th>Status</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="order in filteredOrders" :key="order.id">
                                        <td>{{ order.order_number }}</td>
                                        <td>{{ formatDate(order.order_date) }}</td>
                                        <td>{{ order.customer ? order.customer.nama : '' }}</td>
                                        <td>{{ order.details ? order.details.length : 0 }}</td>
                                        <td>{{ formatCurrency(order.total_amount) }}</td>
                                        <td>{{ formatCurrency(order.amount_paid) }}</td>
                                        <td>
                                            <span :class="getStatusClass(order.order_status)">{{ order.order_status }}</span>
                                        </td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="viewOrderDetails(order)">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" @click="addPayment(order)">
                                                    <i class="fas fa-money-bill"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-warning" @click="showUpdateStatusModal(order)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-success" @click="printOrder(order)">
                                                    <i class="fas fa-print"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" @click="showEditOrderModal(order)">
                                                    <i class="fas fa-pencil-alt"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="showDeleteOrderModal(order)">
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
                            <p>Tidak ada pesanan ditemukan</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- New Order Modal -->
        <div class="modal fade" id="newOrderModal" tabindex="-1" role="dialog" aria-labelledby="newOrderModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="newOrderModalLabel">Pesanan Baru</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveOrder">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="customer">Pelanggan</label>
                                        <select class="form-control" id="customer" v-model="currentOrder.customer_id" required>
                                            <option value="">Pilih Pelanggan</option>
                                            <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                                                {{ customer.nama }}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="orderDate">Tanggal Pesanan</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="far fa-calendar-alt"></i>
                                                </span>
                                            </div>
                                            <input type="datetime-local" class="form-control" id="orderDate" v-model="currentOrder.order_date" required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="dueDate">Tanggal Jatuh Tempo</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="far fa-calendar-alt"></i>
                                                </span>
                                            </div>
                                            <input type="date" class="form-control" id="dueDate" v-model="currentOrder.due_date" required>
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
                                                  <th>Produk</th>
                                                  <th>Jumlah</th>
                                                  <th>Subtotal</th>
                                                  <th>Aksi</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(item, index) in currentOrder.details" :key="index">
                                                    <td>
                                                        <select class="form-control" v-model="item.product_id" @change="updateItemPrice(index)">
                                                            <option value="">Pilih Produk</option>
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
                                                            <i class="fas fa-plus"></i> Tambah Barang
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-12">
                                    <h5>Pembayaran</h5>
                                    <div class="table-responsive">
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                  <th>Tanggal Pembayaran</th>
                                                  <th>Jumlah</th>
                                                  <th>Metode Pembayaran</th>
                                                  <th>Catatan</th>
                                                  <th>Aksi</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(payment, index) in currentOrder.payments" :key="index">
                                                    <td>
                                                        <input type="datetime-local" class="form-control" v-model="payment.payment_date">
                                                    </td>
                                                    <td>
                                                        <input type="number" class="form-control" v-model="payment.amount" min="1">
                                                    </td>
                                                    <td>
                                                        <select class="form-control" v-model="payment.payment_method">
                                                            <option value="CASH">Cash</option>
                                                            <option value="TRANSFER">Transfer</option>
                                                            <option value="CREDIT_CARD">Credit Card</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <input type="text" class="form-control" v-model="payment.note">
                                                    </td>
                                                    <td>
                                                        <button type="button" class="btn btn-danger btn-sm" @click="removePayment(index)">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tbody>
<!--                                            <tfoot>-->
<!--                                                <tr>-->
<!--                                                    <td colspan="5">-->
<!--                                                        <button type="button" class="btn btn-primary btn-sm" @click="addPayment">-->
<!--                                                            <i class="fas fa-plus"></i> Add Payment-->
<!--                                                        </button>-->
<!--                                                    </td>-->
<!--                                                </tr>-->
<!--                                            </tfoot>-->
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="notes">Catatan</label>
                                        <textarea class="form-control" id="notes" rows="3" v-model="currentOrder.notes"></textarea>
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
                                <button type="submit" class="btn btn-primary" :disabled="loading">
                                    <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    Create Order
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Order Details Modal -->
        <div class="modal fade" id="orderDetailsModal" tabindex="-1" role="dialog" aria-labelledby="orderDetailsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderDetailsModalLabel">Order Details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="selectedOrder">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <p><strong>Order #:</strong> {{ selectedOrder.order_number }}</p>
                                <p><strong>Date:</strong> {{ formatDate(selectedOrder.order_date) }}</p>
                                <p><strong>Due Date:</strong> {{ formatDate(selectedOrder.due_date) }}</p>
                                <p><strong>Status:</strong> <span :class="getStatusClass(selectedOrder.order_status)">{{ selectedOrder.order_status }}</span></p>
                                <p><strong>Payment Status:</strong> <span :class="getPaymentStatusClass(selectedOrder.payment_status)">{{ selectedOrder.payment_status }}</span></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Customer:</strong> {{ selectedOrder.customer ? selectedOrder.customer.nama : '' }}</p>
                                <p><strong>Total:</strong> {{ formatCurrency(selectedOrder.total_amount) }}</p>
                                <p><strong>Paid:</strong> {{ formatCurrency(selectedOrder.amount_paid) }}</p>
                                <p><strong>Admin:</strong> {{ selectedOrder.admin ? selectedOrder.admin.full_name : '' }}</p>
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
                                    <tr v-for="(item, index) in selectedOrder.details" :key="index">
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
                                        <td>{{ formatCurrency(selectedOrder.total_amount) }}</td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>

                        <h6 class="mt-4">Payments</h6>
                        <div class="table-responsive">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Amount</th>
                                        <th>Method</th>
                                        <th>Notes</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(payment, index) in selectedOrder.payments" :key="index">
                                        <td>{{ formatDate(payment.payment_date) }}</td>
                                        <td>{{ formatCurrency(payment.amount) }}</td>
                                        <td>{{ payment.payment_method }}</td>
                                        <td>{{ payment.notes }}</td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3" class="text-right"><strong>Total Paid:</strong></td>
                                        <td>{{ formatCurrency(selectedOrder.amount_paid) }}</td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" @click="printOrder(selectedOrder)">
                            <i class="fas fa-print"></i> Print Order
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Payment Modal -->
        <div class="modal fade" id="addPaymentModal" tabindex="-1" role="dialog" aria-labelledby="addPaymentModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addPaymentModalLabel">Add Payment</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="savePayment">
                            <div class="form-group">
                                <label for="paymentDate">Payment Date</label>
                                <input type="datetime-local" class="form-control" id="paymentDate" v-model="newPayment.payment_date" required>
                            </div>
                            <div class="form-group">
                                <label for="paymentAmount">Amount</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">Rp</span>
                                    </div>
                                    <input type="number" class="form-control" id="paymentAmount" v-model="newPayment.amount" min="1" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="paymentMethod">Payment Method</label>
                                <select class="form-control" id="paymentMethod" v-model="newPayment.payment_method" required>
                                    <option value="CASH">Cash</option>
                                    <option value="TRANSFER">Transfer</option>
                                    <option value="CREDIT_CARD">Credit Card</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="paymentNotes">Notes</label>
                                <textarea class="form-control" id="paymentNotes" rows="3" v-model="newPayment.note"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary" :disabled="loading">
                                    <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    Save Payment
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Update Status Modal -->
        <div class="modal fade" id="updateStatusModal" tabindex="-1" role="dialog" aria-labelledby="updateStatusModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="updateStatusModalLabel">Update Order Status</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateOrderStatus">
                            <div class="form-group">
                                <label for="orderStatus">Status</label>
                                <select class="form-control" id="orderStatus" v-model="newStatus" required>
                                    <option value="PENDING">Pending</option>
                                    <option value="CONFIRMED">Confirmed</option>
                                    <option value="COMPLETED">Completed</option>
                                    <option value="CANCELLED">Cancelled</option>
                                </select>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary" :disabled="loading">
                                    <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    Update Status
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Order Modal -->
        <div class="modal fade" id="editOrderModal" tabindex="-1" role="dialog" aria-labelledby="editOrderModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editOrderModalLabel">Edit Order</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateOrder">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="edit-customer">Customer</label>
                                        <select class="form-control" id="edit-customer" v-model="editingOrder.customer_id" required>
                                            <option value="">Select Customer</option>
                                            <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                                                {{ customer.nama }}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="edit-orderDate">Order Date</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="far fa-calendar-alt"></i>
                                                </span>
                                            </div>
                                            <input type="datetime-local" class="form-control" id="edit-orderDate" v-model="editingOrder.order_date" required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="edit-dueDate">Due Date</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="far fa-calendar-alt"></i>
                                                </span>
                                            </div>
                                            <input type="date" class="form-control" id="edit-dueDate" v-model="editingOrder.due_date" required>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="edit-orderStatus">Order Status</label>
                                        <select class="form-control" id="edit-orderStatus" v-model="editingOrder.order_status" required>
                                            <option value="PENDING">Pending</option>
                                            <option value="CONFIRMED">Confirmed</option>
                                            <option value="COMPLETED">Completed</option>
                                            <option value="CANCELLED">Cancelled</option>
                                        </select>
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
                                                <tr v-for="(item, index) in editingOrder.details" :key="index">
                                                    <td>
                                                        <select class="form-control" v-model="item.product_id" @change="updateEditItemPrice(index)">
                                                            <option value="">Select Product</option>
                                                            <option v-for="product in products" :key="product.id" :value="product.id">
                                                                {{ product.name }} ({{ product.size }}) - {{ formatCurrency(product.selling_price) }}
                                                            </option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <input type="number" class="form-control" v-model="item.quantity" @change="calculateEditSubtotal(index)" min="1">
                                                    </td>
                                                    <td>{{ formatCurrency(item.subtotal) }}</td>
                                                    <td>
                                                        <button type="button" class="btn btn-danger btn-sm" @click="removeEditItem(index)">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="4">
                                                        <button type="button" class="btn btn-primary btn-sm" @click="addEditItem">
                                                            <i class="fas fa-plus"></i> Add Item
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-12">
                                    <h5>Payments</h5>
                                    <div class="table-responsive">
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>Payment Date</th>
                                                    <th>Amount</th>
                                                    <th>Payment Method</th>
                                                    <th>Notes</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="(payment, index) in editingOrder.payments" :key="index">
                                                    <td>
                                                        <input type="datetime-local" class="form-control" v-model="payment.payment_date">
                                                    </td>
                                                    <td>
                                                        <input type="number" class="form-control" v-model="payment.amount" min="1">
                                                    </td>
                                                    <td>
                                                        <select class="form-control" v-model="payment.payment_method">
                                                            <option value="CASH">Cash</option>
                                                            <option value="TRANSFER">Transfer</option>
                                                            <option value="CREDIT_CARD">Credit Card</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <input type="text" class="form-control" v-model="payment.note">
                                                    </td>
                                                    <td>
                                                        <button type="button" class="btn btn-danger btn-sm" @click="removeEditPayment(index)">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="5">
                                                        <button type="button" class="btn btn-primary btn-sm" @click="addEditPayment">
                                                            <i class="fas fa-plus"></i> Add Payment
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
                                        <label for="edit-notes">Notes</label>
                                        <textarea class="form-control" id="edit-notes" rows="3" v-model="editingOrder.notes"></textarea>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between">
                                                <h5>Total:</h5>
                                                <h5>{{ formatCurrency(calculateEditTotal()) }}</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary" :disabled="loading">
                                    <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    Update Order
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Order Confirmation Modal -->
        <div class="modal fade" id="deleteOrderModal" tabindex="-1" role="dialog" aria-labelledby="deleteOrderModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteOrderModalLabel">Confirm Delete</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to delete order #{{ orderToDelete ? orderToDelete.order_number : '' }}?</p>
                        <p class="text-danger">This action cannot be undone.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" @click="deleteOrder" :disabled="loading">
                            <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                            Delete
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
            selectedOrder: null,
            currentOrder: {
                customer_id: null,
                order_date: new Date().toISOString().slice(0, 16),
                due_date: new Date(new Date().setDate(new Date().getDate() + 7)).toISOString().split('T')[0],
                details: [],
                payments: [],
                notes: ''
            },
            newPayment: {
                payment_date: new Date().toISOString().slice(0, 16),
                amount: 0,
                payment_method: 'CASH',
                note: ''
            },
            orderToAddPayment: null,
            orderToUpdateStatus: null,
            newStatus: '',
            // New properties for edit and delete functionality
            editingOrder: {
                id: null,
                customer_id: null,
                order_date: '',
                due_date: '',
                order_status: '',
                details: [],
                payments: [],
                notes: ''
            },
            orderToDelete: null,
            orders: [],
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
        filteredOrders() {
            // Since filtering is now done on the server side, 
            // we simply return the orders array
            return this.orders;
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
        this.fetchOrders(0);
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
                // Reset to first page and fetch orders with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        },
        statusFilter: function() {
            // Reset to first page and fetch orders with new status filter
            this.changePage(1);
        }
    },
    methods: {
        fetchOrders(page = 0) {
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
                // Add filter for order_number containing search query
                filters.push(["order_number", "like", this.searchQuery]);

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
                filters.push(["order_status", "=", this.statusFilter]);
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
            this.Api.get(`/orders?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.orders = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.orders = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.orders.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Error fetching orders:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to load orders'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Failed to load orders: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        fetchCustomers() {
            this.Api.get('/customers?size=1000')
                .then(response => {
                    this.customers = response.data.content || response.data;
                })
                .catch(error => {
                    console.error('Error fetching customers:', error);
                });
        },

        fetchProducts() {
            this.Api.get('/product?size=1000')
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
            this.fetchOrders(zeroBasedPage);
        },

        formatDate(dateString) {
            if (!dateString) return '';
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            return new Date(dateString).toLocaleDateString(undefined, options);
        },

        formatCurrency(value) {
            if (value === null || value === undefined) return 'Rp 0';
            return 'Rp ' + value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },

        getStatusClass(status) {
            switch(status) {
                case 'PENDING':
                    return 'badge badge-warning';
                case 'CONFIRMED':
                    return 'badge badge-info';
                case 'COMPLETED':
                    return 'badge badge-success';
                case 'CANCELLED':
                    return 'badge badge-danger';
                default:
                    return 'badge badge-secondary';
            }
        },

        getPaymentStatusClass(status) {
            switch(status) {
                case 'PAID':
                    return 'badge badge-success';
                case 'PARTIALLY_PAID':
                    return 'badge badge-warning';
                case 'UNPAID':
                    return 'badge badge-danger';
                default:
                    return 'badge badge-secondary';
            }
        },

        showNewOrderModal() {
            this.currentOrder = {
                customer_id: null,
                order_date: new Date().toISOString().slice(0, 16),
                due_date: new Date(new Date().setDate(new Date().getDate() + 7)).toISOString().split('T')[0],
                details: [{ 
                    product_id: null, 
                    quantity: 1, 
                    subtotal: 0 
                }],
                payments: [{ 
                    payment_date: new Date().toISOString().slice(0, 16), 
                    amount: 0, 
                    payment_method: 'CASH', 
                    note: 'Initial payment' 
                }],
                notes: ''
            };
            $('#newOrderModal').modal('show');
        },

        viewOrderDetails(order) {
            this.selectedOrder = order;
            $('#orderDetailsModal').modal('show');
        },

        addPayment(order) {
            this.orderToAddPayment = order;
            this.newPayment = {
                payment_date: new Date().toISOString().slice(0, 16),
                amount: 0,
                payment_method: 'CASH',
                note: ''
            };
            $('#addPaymentModal').modal('show');
        },

        addItem() {
            this.currentOrder.details.push({ 
                product_id: null, 
                quantity: 1, 
                subtotal: 0 
            });
        },

        removeItem(index) {
            this.currentOrder.details.splice(index, 1);
            if (this.currentOrder.details.length === 0) {
                this.addItem();
            }
            this.calculateTotal();
        },

        addPaymentItem() {
            this.currentOrder.payments.push({ 
                payment_date: new Date().toISOString().slice(0, 16), 
                amount: 0, 
                payment_method: 'CASH', 
                note: '' 
            });
        },

        removePayment(index) {
            this.currentOrder.payments.splice(index, 1);
            if (this.currentOrder.payments.length === 0) {
                this.addPaymentItem();
            }
        },

        updateItemPrice(index) {
            const item = this.currentOrder.details[index];
            const product = this.products.find(p => p.id === item.product_id);
            if (product) {
                this.calculateSubtotal(index);
            }
        },

        calculateSubtotal(index) {
            const item = this.currentOrder.details[index];
            const product = this.products.find(p => p.id === item.product_id);
            if (product) {
                item.subtotal = product.selling_price * item.quantity;
            }
            this.calculateTotal();
        },

        calculateTotal() {
            let total = 0;
            this.currentOrder.details.forEach(item => {
                total += item.subtotal || 0;
            });
            return total;
        },

        saveOrder() {
            // Validate form
            if (!this.currentOrder.customer_id || this.currentOrder.details.some(item => !item.product_id)) {
                alert('Please fill in all required fields');
                return;
            }

            this.loading = true;

            // Prepare the request payload
            const payload = {
                customer_id: this.currentOrder.customer_id,
                order_date: this.currentOrder.order_date,
                due_date: this.currentOrder.due_date,
                details: this.currentOrder.details.map(item => ({
                    product_id: item.product_id,
                    quantity: item.quantity
                })),
                payments: this.currentOrder.payments.map(payment => ({
                    payment_date: payment.payment_date,
                    amount: payment.amount,
                    payment_method: payment.payment_method,
                    note: payment.note
                }))
            };

            // Make the API call to create a new order
            this.Api.post('/orders', payload)
                .then(response => {
                    // Add the new order to the list
                    this.orders.unshift(response.data);
                    $('#newOrderModal').modal('hide');
                    this.loading = false;

                    // Refresh the orders list
                    this.fetchOrders(this.pagination.pageNumber);
                })
                .catch(error => {
                    console.error('Error creating order:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to create order'}`;
                    } else if (error.request) {
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        this.error = 'Failed to create order: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        savePayment() {
            if (!this.orderToAddPayment) return;

            this.loading = true;

            // Prepare the payment request
            const paymentRequest = {
                payment_date: this.newPayment.payment_date,
                amount: this.newPayment.amount,
                payment_method: this.newPayment.payment_method,
                note: this.newPayment.note
            };

            // Make the API call to add a payment
            this.Api.post(`/orders/${this.orderToAddPayment.id}/payment`, paymentRequest)
                .then(response => {
                    // Update the order in the list
                    const index = this.orders.findIndex(o => o.id === this.orderToAddPayment.id);
                    if (index !== -1) {
                        this.orders.splice(index, 1, response.data);
                    }
                    $('#addPaymentModal').modal('hide');
                    this.loading = false;

                    // Refresh the orders list
                    this.fetchOrders(this.pagination.pageNumber);
                })
                .catch(error => {
                    console.error('Error adding payment:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to add payment'}`;
                    } else if (error.request) {
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        this.error = 'Failed to add payment: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        showUpdateStatusModal(order) {
            this.orderToUpdateStatus = order;
            this.newStatus = order.order_status;
            $('#updateStatusModal').modal('show');
        },

        updateOrderStatus() {
            if (!this.orderToUpdateStatus) return;

            this.loading = true;

            // Prepare the status update request
            const statusRequest = {
                status: this.newStatus
            };

            // Make the API call to update the order status
            this.Api.post(`/orders/${this.orderToUpdateStatus.id}/status`, statusRequest)
                .then(response => {
                    // Update the order in the list
                    const index = this.orders.findIndex(o => o.id === this.orderToUpdateStatus.id);
                    if (index !== -1) {
                        this.orders.splice(index, 1, response.data);
                    }
                    $('#updateStatusModal').modal('hide');
                    this.loading = false;

                    // Refresh the orders list
                    this.fetchOrders(this.pagination.pageNumber);
                })
                .catch(error => {
                    console.error('Error updating order status:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to update order status'}`;
                    } else if (error.request) {
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        this.error = 'Failed to update order status: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        printOrder(order) {
            // Store the current order for printing
            this.selectedOrder = order;

            // Create a new window for printing
            const printWindow = window.open('', '_blank');

            // Generate the order HTML content
            const orderContent = this.generateOrderHTML(order);

            // Write the content to the new window
            printWindow.document.write(orderContent);

            // Trigger print when content is loaded
            printWindow.onload = function() {
                setTimeout(function() {
                    printWindow.print();
                    // Close the window after printing (or after print dialog is closed)
                    printWindow.onafterprint = function() {
                        printWindow.close();
                    };
                }, 500);
            };

            // Close the document to finish loading
            printWindow.document.close();
        },

        generateOrderHTML(order) {
            // Format the date
            const formattedDate = this.formatDate(order.order_date);
            const formattedDueDate = this.formatDate(order.due_date);

            // Generate HTML for the order
            return `
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Order #${order.order_number}</title>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 20px;
                        }
                        .order-header {
                            text-align: center;
                            margin-bottom: 20px;
                        }
                        .order-title {
                            font-size: 24px;
                            font-weight: bold;
                        }
                        .order-details {
                            margin-bottom: 20px;
                        }
                        .order-details div {
                            margin-bottom: 5px;
                        }
                        table {
                            width: 100%;
                            border-collapse: collapse;
                        }
                        th, td {
                            padding: 8px;
                            text-align: left;
                            border-bottom: 1px solid #ddd;
                        }
                        th {
                            background-color: #f2f2f2;
                        }
                        .total-row {
                            font-weight: bold;
                        }
                        .notes {
                            margin-top: 20px;
                        }
                        .payment-details {
                            margin-top: 30px;
                            margin-bottom: 20px;
                        }
                        .payment-title {
                            font-size: 18px;
                            font-weight: bold;
                            margin-bottom: 10px;
                        }
                        .status-badge {
                            display: inline-block;
                            padding: 3px 8px;
                            border-radius: 4px;
                            font-size: 12px;
                            font-weight: bold;
                            color: white;
                        }
                        .status-pending {
                            background-color: #ffc107;
                        }
                        .status-confirmed {
                            background-color: #17a2b8;
                        }
                        .status-completed {
                            background-color: #28a745;
                        }
                        .status-cancelled {
                            background-color: #dc3545;
                        }
                        .payment-status-paid {
                            background-color: #28a745;
                        }
                        .payment-status-partially-paid {
                            background-color: #ffc107;
                        }
                        .payment-status-unpaid {
                            background-color: #dc3545;
                        }
                        @media print {
                            body {
                                print-color-adjust: exact;
                                -webkit-print-color-adjust: exact;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="order-header">
                        <div class="order-title">ORDER</div>
                        <div>Butik Kanina House Uniform</div>
                    </div>

                    <div class="order-details">
                        <div><strong>Order #:</strong> ${order.order_number}</div>
                        <div><strong>Date:</strong> ${formattedDate}</div>
                        <div><strong>Due Date:</strong> ${formattedDueDate}</div>
                        <div><strong>Customer:</strong> ${order.customer ? order.customer.nama : ''}</div>
                        <div><strong>Status:</strong> <span class="status-badge status-${order.order_status.toLowerCase()}">${order.order_status}</span></div>
                        <div><strong>Payment Status:</strong> <span class="status-badge payment-status-${order.payment_status.toLowerCase().replace('_', '-')}">${order.payment_status}</span></div>
                    </div>

                    <h3>Items</h3>
                    <table>
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
                            ${order.details ? order.details.map(item => `
                                <tr>
                                    <td>${item.product ? item.product.name : ''}</td>
                                    <td>${item.product ? item.product.size : ''}</td>
                                    <td>${item.product ? this.formatCurrency(item.product.selling_price) : ''}</td>
                                    <td>${item.quantity}</td>
                                    <td>${this.formatCurrency(item.subtotal)}</td>
                                </tr>
                            `).join('') : ''}
                            <tr class="total-row">
                                <td colspan="4" style="text-align: right;"><strong>Total:</strong></td>
                                <td>${this.formatCurrency(order.total_amount)}</td>
                            </tr>
                        </tbody>
                    </table>

                    <div class="payment-details">
                        <div class="payment-title">Payment Information</div>
                        <table>
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Amount</th>
                                    <th>Method</th>
                                    <th>Notes</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${order.payments ? order.payments.map(payment => `
                                    <tr>
                                        <td>${this.formatDate(payment.payment_date)}</td>
                                        <td>${this.formatCurrency(payment.amount)}</td>
                                        <td>${payment.payment_method}</td>
                                        <td>${payment.notes || ''}</td>
                                    </tr>
                                `).join('') : ''}
                                <tr class="total-row">
                                    <td colspan="3" style="text-align: right;"><strong>Total Paid:</strong></td>
                                    <td>${this.formatCurrency(order.amount_paid)}</td>
                                </tr>
                                <tr class="total-row">
                                    <td colspan="3" style="text-align: right;"><strong>Balance:</strong></td>
                                    <td>${this.formatCurrency(order.total_amount - order.amount_paid)}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    ${order.notes ? `
                    <div class="notes">
                        <strong>Notes:</strong>
                        <p>${order.notes}</p>
                    </div>
                    ` : ''}

                    <div style="margin-top: 50px; text-align: center;">
                        <p>Thank you for your business!</p>
                    </div>
                </body>
                </html>
            `;
        },

        // Edit Order Methods
        showEditOrderModal(order) {
            // Clone the order to avoid modifying the original
            this.editingOrder = {
                id: order.id,
                customer_id: order.customer ? order.customer.id : null,
                order_date: order.order_date ? new Date(order.order_date).toISOString().slice(0, 16) : '',
                due_date: order.due_date ? new Date(order.due_date).toISOString().split('T')[0] : '',
                order_status: order.order_status,
                details: [],
                payments: [],
                notes: order.notes || ''
            };

            // Clone details to avoid modifying the original
            if (order.details && order.details.length > 0) {
                this.editingOrder.details = order.details.map(item => ({
                    id: item.id,
                    product_id: item.product ? item.product.id : null,
                    quantity: item.quantity,
                    subtotal: item.subtotal
                }));
            } else {
                this.editingOrder.details = [{
                    product_id: null,
                    quantity: 1,
                    subtotal: 0
                }];
            }

            // Clone payments to avoid modifying the original
            if (order.payments && order.payments.length > 0) {
                this.editingOrder.payments = order.payments.map(payment => ({
                    id: payment.id,
                    payment_date: payment.payment_date ? new Date(payment.payment_date).toISOString().slice(0, 16) : '',
                    amount: payment.amount,
                    payment_method: payment.payment_method,
                    note: payment.notes || ''
                }));
            } else {
                this.editingOrder.payments = [];
            }

            $('#editOrderModal').modal('show');
        },

        updateEditItemPrice(index) {
            const item = this.editingOrder.details[index];
            const product = this.products.find(p => p.id === item.product_id);
            if (product) {
                this.calculateEditSubtotal(index);
            }
        },

        calculateEditSubtotal(index) {
            const item = this.editingOrder.details[index];
            const product = this.products.find(p => p.id === item.product_id);
            if (product) {
                item.subtotal = product.selling_price * item.quantity;
            }
            this.calculateEditTotal();
        },

        calculateEditTotal() {
            let total = 0;
            this.editingOrder.details.forEach(item => {
                total += item.subtotal || 0;
            });
            return total;
        },

        addEditItem() {
            this.editingOrder.details.push({
                product_id: null,
                quantity: 1,
                subtotal: 0
            });
        },

        removeEditItem(index) {
            this.editingOrder.details.splice(index, 1);
            if (this.editingOrder.details.length === 0) {
                this.addEditItem();
            }
            this.calculateEditTotal();
        },

        addEditPayment() {
            this.editingOrder.payments.push({
                payment_date: new Date().toISOString().slice(0, 16),
                amount: 0,
                payment_method: 'CASH',
                note: ''
            });
        },

        removeEditPayment(index) {
            this.editingOrder.payments.splice(index, 1);
        },

        updateOrder() {
            // Validate form
            if (!this.editingOrder.customer_id || this.editingOrder.details.some(item => !item.product_id)) {
                alert('Please fill in all required fields');
                return;
            }

            this.loading = true;

            // Prepare the request payload
            const payload = {
                id: this.editingOrder.id,
                customer_id: this.editingOrder.customer_id,
                order_date: this.editingOrder.order_date,
                due_date: this.editingOrder.due_date,
                order_status: this.editingOrder.order_status,
                details: this.editingOrder.details.map(item => ({
                    id: item.id, // Include ID for existing items
                    product_id: item.product_id,
                    quantity: item.quantity
                })),
                payments: this.editingOrder.payments.map(payment => ({
                    id: payment.id, // Include ID for existing payments
                    payment_date: payment.payment_date,
                    amount: payment.amount,
                    payment_method: payment.payment_method,
                    note: payment.note
                })),
                notes: this.editingOrder.notes
            };

            // Make the API call to update the order
            this.Api.put(`/orders/${this.editingOrder.id}`, payload)
                .then(response => {
                    // Update the order in the list
                    const index = this.orders.findIndex(o => o.id === this.editingOrder.id);
                    if (index !== -1) {
                        this.orders.splice(index, 1, response.data);
                    }
                    $('#editOrderModal').modal('hide');
                    this.loading = false;

                    // Refresh the orders list
                    this.fetchOrders(this.pagination.pageNumber);
                })
                .catch(error => {
                    console.error('Error updating order:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to update order'}`;
                    } else if (error.request) {
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        this.error = 'Failed to update order: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        // Delete Order Methods
        showDeleteOrderModal(order) {
            this.orderToDelete = order;
            $('#deleteOrderModal').modal('show');
        },

        deleteOrder() {
            if (!this.orderToDelete) return;

            this.loading = true;

            // Make the API call to delete the order
            this.Api.delete(`/orders/${this.orderToDelete.id}`)
                .then(() => {
                    // Remove the order from the list
                    const index = this.orders.findIndex(o => o.id === this.orderToDelete.id);
                    if (index !== -1) {
                        this.orders.splice(index, 1);
                    }
                    $('#deleteOrderModal').modal('hide');
                    this.loading = false;

                    // Refresh the orders list
                    this.fetchOrders(this.pagination.pageNumber);
                })
                .catch(error => {
                    console.error('Error deleting order:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Failed to delete order'}`;
                    } else if (error.request) {
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        this.error = 'Failed to delete order: ' + error.message;
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
