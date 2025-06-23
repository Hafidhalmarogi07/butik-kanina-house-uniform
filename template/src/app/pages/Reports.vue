<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Reports</h3>
                    </div>
                    <div class="card-body">
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label>Report Type</label>
                                    <select class="form-control" v-model="selectedReportType">
                                        <option value="sales">Sales Report</option>
                                        <option value="inventory">Inventory Report</option>
                                        <option value="production">Production Report</option>
                                        <option value="expenses">Expenses Report</option>
                                        <option value="customers">Customer Report</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label>Period</label>
                                    <select class="form-control" v-model="selectedPeriod">
                                        <option value="daily">Daily</option>
                                        <option value="weekly">Weekly</option>
                                        <option value="monthly">Monthly</option>
                                        <option value="quarterly">Quarterly</option>
                                        <option value="yearly">Yearly</option>
                                        <option value="custom">Custom Range</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-4" v-if="selectedPeriod === 'custom'">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Start Date</label>
                                            <input type="date" class="form-control" v-model="startDate">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>End Date</label>
                                            <input type="date" class="form-control" v-model="endDate">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2 mb-3 d-flex align-items-end">
                                <button type="button" class="btn btn-primary btn-block" @click="generateReport">
                                    <i class="fas fa-chart-bar mr-1"></i> Generate Report
                                </button>
                            </div>
                        </div>

                        <!-- Sales Report -->
                        <div v-if="selectedReportType === 'sales' && showReport" class="report-container">
                          <h4>Sales Report - {{ formatPeriodTitle() }}</h4>
                          <div class="card mt-4">
                                <div class="card-header">
                                    <h5 class="card-title">Recent Sales</h5>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>Date</th>
                                                    <th>Customer</th>
                                                    <th>Items</th>
                                                    <th>Total</th>
                                                    <th>Status</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="sale in recentSales" :key="sale.id">
                                                    <td>{{ sale.id }}</td>
                                                    <td>{{ sale.date }}</td>
                                                    <td>{{ sale.customer }}</td>
                                                    <td>{{ sale.items }}</td>
                                                    <td>Rp {{ sale.total.toLocaleString() }}</td>
                                                    <td><span :class="getStatusClass(sale.status)">{{ sale.status }}</span></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Inventory Report -->
                        <div v-if="selectedReportType === 'inventory' && showReport" class="report-container">
                            <h4>Inventory Report - {{ formatPeriodTitle() }}</h4>
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title">Inventory Status</h5>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Product ID</th>
                                                    <th>Name</th>
                                                    <th>Category</th>
                                                    <th>Size</th>
                                                    <th>Stock</th>
                                                    <th>Status</th>
                                                    <th>Value</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="product in inventoryItems" :key="product.id">
                                                    <td>{{ product.id }}</td>
                                                    <td>{{ product.name }}</td>
                                                    <td>{{ product.category }}</td>
                                                    <td>{{ product.size }}</td>
                                                    <td>{{ product.stock }}</td>
                                                    <td>
                                                        <span :class="getStockStatusClass(product.stock)">
                                                            {{ getStockStatus(product.stock) }}
                                                        </span>
                                                    </td>
                                                    <td>Rp {{ (product.price * product.stock).toLocaleString() }}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Other report types would be implemented similarly -->
                        <div v-if="(selectedReportType === 'production' || selectedReportType === 'expenses' || selectedReportType === 'customers') && showReport" class="report-container">
                            <h4>{{ getReportTitle() }} - {{ formatPeriodTitle() }}</h4>
                            <div class="text-center py-5">
                                <i class="fas fa-chart-bar fa-4x text-muted"></i>
                                <p class="mt-3">{{ getReportTitle() }} would be displayed here</p>
                                <p class="text-muted">This report type is not fully implemented in this demo</p>
                            </div>
                        </div>

                        <!-- No report selected or generated yet -->
                        <div v-if="!showReport" class="text-center py-5">
                            <i class="fas fa-chart-bar fa-4x text-muted"></i>
                            <p class="mt-3">Select a report type and period, then click "Generate Report" to view data</p>
                        </div>
                    </div>
                    <div class="card-footer" v-if="showReport">
                        <div class="btn-group">
                            <button type="button" class="btn btn-default">
                                <i class="fas fa-print"></i> Print
                            </button>
                            <button type="button" class="btn btn-default">
                                <i class="fas fa-file-pdf"></i> Export PDF
                            </button>
                            <button type="button" class="btn btn-default">
                                <i class="fas fa-file-excel"></i> Export Excel
                            </button>
                        </div>
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
            selectedReportType: 'sales',
            selectedPeriod: 'monthly',
            startDate: new Date().toISOString().split('T')[0],
            endDate: new Date().toISOString().split('T')[0],
            showReport: false,
            
            // Sample data for sales report
            totalSales: 22500000,
            totalOrders: 45,
            averageOrderValue: 500000,
            topSellingProduct: 'School Uniform Set',
            recentSales: [
                {
                    id: 'ORD-001',
                    date: '2023-05-15',
                    customer: 'John Doe',
                    items: 3,
                    total: 550000,
                    status: 'Delivered'
                },
                {
                    id: 'ORD-002',
                    date: '2023-05-18',
                    customer: 'Jane Smith',
                    items: 1,
                    total: 150000,
                    status: 'Processing'
                },
                {
                    id: 'ORD-003',
                    date: '2023-05-20',
                    customer: 'SMA Negeri 1 Jakarta',
                    items: 5,
                    total: 850000,
                    status: 'Pending'
                },
                {
                    id: 'ORD-004',
                    date: '2023-05-22',
                    customer: 'SD Islam Al-Azhar',
                    items: 2,
                    total: 350000,
                    status: 'Shipped'
                },
                {
                    id: 'ORD-005',
                    date: '2023-05-25',
                    customer: 'PT Maju Bersama',
                    items: 4,
                    total: 650000,
                    status: 'Cancelled'
                }
            ],
            
            // Sample data for inventory report
            totalProducts: 15,
            totalStock: 230,
            lowStockItems: 3,
            inventoryValue: 34500000,
            inventoryItems: [
                {
                    id: 1,
                    name: 'School Uniform Set',
                    category: 'Sets',
                    size: 'M',
                    stock: 25,
                    price: 350000
                },
                {
                    id: 2,
                    name: 'School Shirt',
                    category: 'Shirts',
                    size: 'L',
                    stock: 50,
                    price: 150000
                },
                {
                    id: 3,
                    name: 'School Pants',
                    category: 'Pants',
                    size: 'M',
                    stock: 30,
                    price: 200000
                },
                {
                    id: 4,
                    name: 'School Skirt',
                    category: 'Skirts',
                    size: 'S',
                    stock: 20,
                    price: 180000
                },
                {
                    id: 5,
                    name: 'School Tie',
                    category: 'Ties',
                    size: 'One Size',
                    stock: 5,
                    price: 50000
                }
            ]
        }
    },
    methods: {
        generateReport() {
            // In a real application, this would fetch data from the server
            // For this demo, we'll just show the static sample data
            this.showReport = true;
        },
        formatPeriodTitle() {
            switch(this.selectedPeriod) {
                case 'daily': return 'Daily (Today)';
                case 'weekly': return 'Weekly (Last 7 Days)';
                case 'monthly': return 'Monthly (May 2023)';
                case 'quarterly': return 'Quarterly (Q2 2023)';
                case 'yearly': return 'Yearly (2023)';
                case 'custom': return `${this.startDate} to ${this.endDate}`;
                default: return '';
            }
        },
        getReportTitle() {
            switch(this.selectedReportType) {
                case 'sales': return 'Sales Report';
                case 'inventory': return 'Inventory Report';
                case 'production': return 'Production Report';
                case 'expenses': return 'Expenses Report';
                case 'customers': return 'Customer Report';
                default: return 'Report';
            }
        },
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
        getStockStatus(stock) {
            if (stock <= 5) return 'Low Stock';
            if (stock <= 20) return 'Medium Stock';
            return 'In Stock';
        },
        getStockStatusClass(stock) {
            if (stock <= 5) return 'badge badge-danger';
            if (stock <= 20) return 'badge badge-warning';
            return 'badge badge-success';
        }
    }
}
</script>

<style scoped>
.report-container {
    margin-top: 20px;
}
.small-box {
    border-radius: 0.25rem;
    box-shadow: 0 0 1px rgba(0,0,0,.125), 0 1px 3px rgba(0,0,0,.2);
    display: block;
    margin-bottom: 20px;
    position: relative;
}
.small-box > .inner {
    padding: 10px;
}
.small-box h3 {
    font-size: 2.2rem;
    font-weight: 700;
    margin: 0 0 10px;
    padding: 0;
    white-space: nowrap;
}
.small-box p {
    font-size: 1rem;
}
.small-box .icon {
    color: rgba(0,0,0,.15);
    z-index: 0;
    font-size: 70px;
    position: absolute;
    right: 15px;
    top: 15px;
    transition: transform .3s linear;
}
.bg-info {
    background-color: #17a2b8!important;
    color: #fff!important;
}
.bg-success {
    background-color: #28a745!important;
    color: #fff!important;
}
.bg-warning {
    background-color: #ffc107!important;
    color: #1f2d3d!important;
}
.bg-danger {
    background-color: #dc3545!important;
    color: #fff!important;
}
.table th, .table td {
    vertical-align: middle;
}
</style>