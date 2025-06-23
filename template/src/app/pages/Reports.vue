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
                                        <option value="expenses">Expenses Report</option>
                                        <option value="orders">Orders Report</option>
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
                                                    <td>{{ sale.invoice_number }}</td>
                                                    <td>{{ sale.date }}</td>
                                                    <td>{{ sale.customer.nama }}</td>
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
                      <!-- Order Report -->
                      <div v-if="selectedReportType === 'orders' && showReport" class="report-container">
                        <h4>Order Report - {{ formatPeriodTitle() }}</h4>
                        <div class="card mt-4">
                          <div class="card-header">
                            <h5 class="card-title">Orders</h5>
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
                                <tr v-for="order in orderData" :key="order.id">
                                  <td>{{ order.order_number }}</td>
                                  <td>{{ formatDate(order.order_date) }}</td>
                                  <td>{{ order.customer ? order.customer.nama : '' }}</td>
                                  <td>{{ order.details ? order.details.length : 0 }}</td>
                                  <td>Rp {{ formatCurrency(order.total_amount) }}</td>
                                  <td><span :class="getStatusClass(order.order_status)">{{ order.order_status }}</span></td>
                                </tr>
                                </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                      </div>

                      <!-- Expenses Report -->
                      <div v-if="selectedReportType === 'expenses' && showReport" class="report-container">
                        <h4>Expenses Report - {{ formatPeriodTitle() }}</h4>
                        <div class="card mt-4">
                          <div class="card-header">
                            <h5 class="card-title">Expenses</h5>
                          </div>
                          <div class="card-body">
                            <div class="table-responsive">
                              <table class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                  <th>ID</th>
                                  <th>Date</th>
                                  <th>Category</th>
                                  <th>Description</th>
                                  <th>Amount</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="expense in expenseData" :key="expense.id">
                                  <td>{{ expense.id }}</td>
                                  <td>{{ formatDate(expense.date) }}</td>
                                  <td>{{ expense.payment_method }}</td>
                                  <td>{{ expense.description }}</td>
                                  <td>Rp {{ formatCurrency(expense.amount) }}</td>
                                </tr>
                                </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                      </div>

                        <!-- Other report types would be implemented similarly -->
                        <div v-if="(selectedReportType === 'production' ||  selectedReportType === 'customers') && showReport" class="report-container">
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
<!--                            <button type="button" class="btn btn-default" @click="printReport">-->
<!--                                <i class="fas fa-print"></i> Print-->
<!--                            </button>-->
                            <button type="button" class="btn btn-default" @click="exportPDF">
                                <i class="fas fa-file-pdf"></i> Export PDF
                            </button>
                            <button type="button" class="btn btn-default" @click="exportExcel">
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
            recentSales: [],

            // Sample data for inventory report
            totalProducts: 15,
            totalStock: 230,
            lowStockItems: 3,
            inventoryValue: 34500000,
            inventoryItems: [],
            orderData: [],
            expenseData: [],
        }
    },
    methods: {
        generateReport() {
            // In a real application, this would fetch data from the server
            // For this demo, we'll just show the static sample data

          this.showReport = false;

          try {
            let endpoint = '';
            let response = null;

            // Determine which API endpoint to use based on report type
            if (this.selectedReportType === 'sales') {
              endpoint = '/sales-report/get';
            } else if (this.selectedReportType === 'expenses') {
              endpoint = '/expense-report/get';
            } else if (this.selectedReportType === 'orders') {
              endpoint = '/order-report/get';
            } else {
              // For other report types, show static data for now
              this.showReport = true;
              return;
            }

            // Prepare query parameters
            const params = { options: this.selectedPeriod };

            // Add start and end dates if custom period is selected
            if (this.selectedPeriod === 'custom') {
              params.startDate = this.startDate;
              params.endDate = this.endDate;
            }

            // Make API request
            this.Api.get(endpoint,  params )
                .then(response => {
                  // Update data based on report type
                  if (this.selectedReportType === 'sales') {
                    this.recentSales = response.data;
                  } else if (this.selectedReportType === 'expenses') {
                    this.expenseData = response.data;
                  } else if (this.selectedReportType === 'orders') {
                    this.orderData = response.data;
                  }
                  this.showReport = true;
                })
          } catch (error) {
            console.error('Error fetching report data:', error);
            alert('Failed to fetch report data. Please try again.');
          }
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
                case 'expenses': return 'Expenses Report';
                case 'orders': return 'Orders Report';
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
        },
        formatDate(dateString) {
          if (!dateString) return '';
          const date = new Date(dateString);
          return date.toLocaleDateString('id-ID', { day: '2-digit', month: '2-digit', year: 'numeric' });
        },

        formatCurrency(value) {
          if (value === undefined || value === null) return 'Rp 0';
          return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR' }).format(value);
        },
        exportPDF() {
            try {
                let endpoint = '';

                // Determine which API endpoint to use based on report type
                if (this.selectedReportType === 'sales') {
                    endpoint = '/api/v1/sales-report/pdf';
                } else if (this.selectedReportType === 'expenses') {
                    endpoint = '/api/v1/expense-report/pdf';
                } else if (this.selectedReportType === 'orders') {
                    endpoint = '/api/v1/order-report/pdf';
                } else {
                    alert('PDF export is not available for this report type');
                    return;
                }

                // Prepare query parameters
                const params = { options: this.selectedPeriod };

                // Add start and end dates if custom period is selected
                if (this.selectedPeriod === 'custom') {
                    params.startDate = this.startDate;
                    params.endDate = this.endDate;
                }

                // Open the PDF in a new window/tab
                window.open(`${endpoint}?options=${this.selectedPeriod}`, '_blank');

            } catch (error) {
                console.error('Error exporting PDF:', error);
                alert('Failed to export PDF. Please try again.');
            }
        },
        exportExcel() {
            try {
                let endpoint = '';

                // Determine which API endpoint to use based on report type
                if (this.selectedReportType === 'sales') {
                    endpoint = '/api/v1/sales-report/excel';
                } else if (this.selectedReportType === 'expenses') {
                    endpoint = '/api/v1/expense-report/excel';
                } else if (this.selectedReportType === 'orders') {
                    endpoint = '/api/v1/order-report/excel';
                } else {
                    alert('Excel export is not available for this report type');
                    return;
                }

                // Prepare query parameters
                const params = { options: this.selectedPeriod };

                // Add start and end dates if custom period is selected
                if (this.selectedPeriod === 'custom') {
                    params.startDate = this.startDate;
                    params.endDate = this.endDate;
                }

                // Open the Excel download in a new window/tab
                window.open(`${endpoint}?options=${this.selectedPeriod}`, '_blank');

            } catch (error) {
                console.error('Error exporting Excel:', error);
                alert('Failed to export Excel. Please try again.');
            }
        },
        printReport() {
            try {
                // Create a new window for printing
                const printWindow = window.open('', '_blank');

                // Generate the report HTML content
                const reportContent = this.generateReportHTML();

                // Write the content to the new window
                printWindow.document.write(reportContent);

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

            } catch (error) {
                console.error('Error printing report:', error);
                alert('Failed to print report. Please try again.');
            }
        },

        generateReportHTML() {
            // Format the date range
            let dateRange = '';
            if (this.selectedPeriod === 'custom') {
                dateRange = `${this.formatDate(this.startDate)} to ${this.formatDate(this.endDate)}`;
            } else {
                dateRange = this.selectedPeriod.charAt(0).toUpperCase() + this.selectedPeriod.slice(1);
            }

            // Determine report title based on type
            let reportTitle = '';
            let reportData = '';

            if (this.selectedReportType === 'sales') {
                reportTitle = 'Sales Report';
                reportData = this.generateSalesReportHTML();
            } else if (this.selectedReportType === 'expenses') {
                reportTitle = 'Expenses Report';
                reportData = this.generateExpensesReportHTML();
            } else if (this.selectedReportType === 'orders') {
                reportTitle = 'Orders Report';
                reportData = this.generateOrdersReportHTML();
            } else if (this.selectedReportType === 'inventory') {
                reportTitle = 'Inventory Report';
                reportData = this.generateInventoryReportHTML();
            }

            // Generate HTML for the report
            return `
                <!DOCTYPE html>
                <html>
                <head>
                    <title>${reportTitle}</title>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 20px;
                        }
                        .report-header {
                            text-align: center;
                            margin-bottom: 20px;
                        }
                        .report-title {
                            font-size: 24px;
                            font-weight: bold;
                        }
                        .report-subtitle {
                            font-size: 16px;
                            margin-top: 5px;
                        }
                        .report-details {
                            margin-bottom: 20px;
                        }
                        .report-details div {
                            margin-bottom: 5px;
                        }
                        .summary-box {
                            border: 1px solid #ddd;
                            padding: 10px;
                            margin-bottom: 20px;
                            background-color: #f9f9f9;
                        }
                        .summary-item {
                            display: inline-block;
                            width: 24%;
                            text-align: center;
                            padding: 10px 0;
                        }
                        .summary-value {
                            font-size: 20px;
                            font-weight: bold;
                        }
                        .summary-label {
                            font-size: 14px;
                            color: #666;
                        }
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            margin-top: 20px;
                        }
                        th, td {
                            padding: 8px;
                            text-align: left;
                            border-bottom: 1px solid #ddd;
                        }
                        th {
                            background-color: #f2f2f2;
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
                    <div class="report-header">
                        <div class="report-title">${reportTitle}</div>
                        <div class="report-subtitle">Butik Kanina House Uniform</div>
                        <div class="report-subtitle">Period: ${dateRange}</div>
                    </div>

                    ${reportData}
                </body>
                </html>
            `;
        },

        generateSalesReportHTML() {
            return `
                <div class="summary-box">
                    <div class="summary-item">
                        <div class="summary-value">${this.formatCurrency(this.totalSales)}</div>
                        <div class="summary-label">Total Sales</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-value">${this.totalOrders}</div>
                        <div class="summary-label">Total Orders</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-value">${this.formatCurrency(this.averageOrderValue)}</div>
                        <div class="summary-label">Average Order</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-value">${this.topSellingProduct}</div>
                        <div class="summary-label">Top Product</div>
                    </div>
                </div>

                <h3>Recent Sales</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Invoice</th>
                            <th>Customer</th>
                            <th>Amount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${this.recentSales.map(sale => `
                            <tr>
                                <td>${this.formatDate(sale.date)}</td>
                                <td>${sale.invoice}</td>
                                <td>${sale.customer}</td>
                                <td>${this.formatCurrency(sale.amount)}</td>
                                <td>${sale.status}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        },

        generateExpensesReportHTML() {
            return `
                <h3>Expenses Summary</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Category</th>
                            <th>Amount</th>
                            <th>Date</th>
                            <th>Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${this.expenseData.map(expense => `
                            <tr>
                                <td>${expense.category}</td>
                                <td>${this.formatCurrency(expense.amount)}</td>
                                <td>${this.formatDate(expense.date)}</td>
                                <td>${expense.description}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        },

        generateOrdersReportHTML() {
            return `
                <h3>Orders Summary</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Date</th>
                            <th>Customer</th>
                            <th>Status</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${this.orderData.map(order => `
                            <tr>
                                <td>${order.id}</td>
                                <td>${this.formatDate(order.date)}</td>
                                <td>${order.customer}</td>
                                <td>${order.status}</td>
                                <td>${this.formatCurrency(order.total)}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        },

        generateInventoryReportHTML() {
            return `
                <div class="summary-box">
                    <div class="summary-item">
                        <div class="summary-value">${this.totalProducts}</div>
                        <div class="summary-label">Total Products</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-value">${this.totalStock}</div>
                        <div class="summary-label">Total Stock</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-value">${this.lowStockItems}</div>
                        <div class="summary-label">Low Stock Items</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-value">${this.formatCurrency(this.inventoryValue)}</div>
                        <div class="summary-label">Inventory Value</div>
                    </div>
                </div>

                <h3>Inventory Items</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>SKU</th>
                            <th>Category</th>
                            <th>Stock</th>
                            <th>Price</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${this.inventoryItems.map(item => `
                            <tr>
                                <td>${item.name}</td>
                                <td>${item.sku}</td>
                                <td>${item.category}</td>
                                <td>${item.stock}</td>
                                <td>${this.formatCurrency(item.price)}</td>
                                <td>${this.formatCurrency(item.price * item.stock)}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
        },

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
