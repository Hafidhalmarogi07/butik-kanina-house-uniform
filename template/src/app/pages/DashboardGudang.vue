<template>
    <div class="container-fluid">
        <!-- Stock Alert Modal -->
        <div v-if="showStockAlertModal && stockAlerts.length > 0" class="stock-alert-modal-overlay">
            <div class="stock-alert-modal">
                <div class="stock-alert-body">
                    <div v-for="(alert, index) in stockAlerts" :key="index">
                        <div v-if="alert.reason === 'ORDER_DEFICIT'" class="alert alert-danger">
                            <button @click="removeAlert(index)" class="close-alert-btn">
                                <i class="fas fa-times"></i>
                            </button>
                            <h5><i class="fas fa-exclamation-circle"></i> Kekurangan stok produk!</h5>
                            <p>Produk <strong>{{ alert.product.name }}</strong> membutuhkan tambahan <strong>{{ alert.needed_quantity }}</strong> pcs. Stok saat ini hanya <strong>{{ alert.current_stock }}</strong> pcs.</p>
                            <p><i class="fas fa-hand-point-right"></i> Segera buat produksi agar order bisa diproses.</p>
                            <button @click="goToProduction(alert.product)" class="btn btn-primary mt-2">
                                <i class="fas fa-plus"></i> Buat Produksi
                            </button>
                        </div>
                        <div v-else-if="alert.reason === 'LOW_STOCK'" class="alert alert-warning">
                            <button @click="removeAlert(index)" class="close-alert-btn">
                                <i class="fas fa-times"></i>
                            </button>
                            <h5><i class="fas fa-exclamation-triangle"></i> Stok hampir habis!</h5>
                            <p>Produk <strong>{{ alert.product.name }}</strong> hanya tersisa <strong>{{ alert.current_stock }}</strong> pcs.</p>
                            <p><i class="fas fa-search"></i> Periksa apakah perlu produksi ulang.</p>
                            <button @click="goToProduction(alert.product)" class="btn btn-primary mt-2">
                                <i class="fas fa-plus"></i> Buat Produksi
                            </button>
                        </div>
                    </div>
                    <div class="text-right mt-3">
                        <button @click="closeStockAlertModal" class="btn btn-secondary mr-2">
                            <i class="fas fa-times"></i> Tutup
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Stat Cards -->
        <div class="row">
            <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                    <div class="inner">
                        <h3>{{ warehouseSummary.total_inventory }}</h3>
                        <p>Total Inventaris</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-boxes"></i>
                    </div>
<!--                    <router-link to="/inventory" class="small-box-footer">-->
<!--                        More info <i class="fas fa-arrow-circle-right"></i>-->
<!--                    </router-link>-->
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                    <div class="inner">
                        <h3>{{ warehouseSummary.incoming_stock }}</h3>
                        <p>Stok Masuk</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-truck-loading"></i>
                    </div>
<!--                    <router-link to="/incoming-stock" class="small-box-footer">-->
<!--                        More info <i class="fas fa-arrow-circle-right"></i>-->
<!--                    </router-link>-->
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                    <div class="inner">
                        <h3>{{ warehouseSummary.outgoing_stock }}</h3>
                        <p>Stok Keluar</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shipping-fast"></i>
                    </div>
<!--                    <router-link to="/outgoing-stock" class="small-box-footer">-->
<!--                        More info <i class="fas fa-arrow-circle-right"></i>-->
<!--                    </router-link>-->
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                    <div class="inner">
                        <h3>{{ warehouseSummary.low_stock_items }}</h3>
                        <p>Barang Stok Rendah</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
<!--                    <router-link to="/low-stock" class="small-box-footer">-->
<!--                        More info <i class="fas fa-arrow-circle-right"></i>-->
<!--                    </router-link>-->
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Inventory Movement Chart -->
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <i class="fas fa-chart-line mr-1"></i>
                            Pergerakan Stok Barang
                        </h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                            <button type="button" class="btn btn-tool" data-card-widget="remove">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="chart">
                            <canvas id="inventoryChart" style="min-height: 250px; height: 250px; max-height: 250px; max-width: 100%;"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Warehouse Capacity -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <i class="fas fa-warehouse mr-1"></i>
                            Kapasitas Gudang
                        </h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                            <button type="button" class="btn btn-tool" data-card-widget="remove">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <canvas id="capacityChart" style="min-height: 250px; height: 250px; max-height: 250px; max-width: 100%;"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Recent Stock Movements Table -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header border-transparent">
                        <h3 class="card-title">Pergerakan Stok Terbaru</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                            <button type="button" class="btn btn-tool" data-card-widget="remove">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table m-0">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Barang</th>
                                        <th>Tipe</th>
                                        <th>Jumlah</th>
                                        <th>Tanggal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(movement, index) in recentStockMovements" :key="index">
                                        <td><a href="#">{{ movement.id }}</a></td>
                                        <td>{{ movement.item_name }}</td>
                                        <td>
                                            <span :class="['badge', movement.type === 'IN' ? 'badge-success' : 'badge-warning']">
                                                {{ movement.type === 'IN' ? 'In' : 'Out' }}
                                            </span>
                                        </td>
                                        <td>{{ Math.abs(movement.quantity) }}</td>
                                        <td>{{ formatDate(movement.date) }}</td>
                                    </tr>
                                    <tr v-if="recentStockMovements.length === 0">
                                        <td colspan="5" class="text-center">No recent stock movements found</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
<!--                    <div class="card-footer clearfix">-->
<!--                        <router-link to="/stock-movement/create" class="btn btn-sm btn-info float-left">Tambah Pergerakan Stok</router-link>-->
<!--                        <router-link to="/stock-movements" class="btn btn-sm btn-secondary float-right">Lihat Semua Pergerakan</router-link>-->
<!--                    </div>-->
                </div>
            </div>

            <!-- Low Stock Items Table -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Barang Stok Rendah</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                            <button type="button" class="btn btn-tool" data-card-widget="remove">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body p-0">
                        <ul class="products-list product-list-in-card pl-2 pr-2">
                            <li v-for="(item, index) in lowStockItems" :key="index" class="item">
                                <div class="product-img">
                                    <img :src="item.image_url || '@assets/no-image.png'" alt="Product Image" class="img-size-50">
                                </div>
                                <div class="product-info">
                                    <a href="javascript:void(0)" class="product-title">{{ item.product_name }}
                                        <span :class="['badge float-right', item.current_stock <= 5 ? 'badge-danger' : 'badge-warning']">{{ item.current_stock }} left</span></a>
                                    <span class="product-description">
                                        {{ item.product_description || 'No description available' }}
                                    </span>
                                </div>
                            </li>
                            <li v-if="lowStockItems.length === 0" class="item">
                                <div class="product-info text-center">
                                    <span>No low stock items found</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="card-footer text-center">
                        <router-link to="/products" class="uppercase">Lihat Semua Produk</router-link>
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
            warehouseSummary: {
                total_inventory: 0,
                incoming_stock: 0,
                outgoing_stock: 0,
                low_stock_items: 0
            },
            inventoryMovement: [],
            warehouseCapacity: {
                used_space: 0,
                available_space: 0
            },
            recentStockMovements: [],
            lowStockItems: [],
            stockAlerts: [],
            showStockAlertModal: false
        };
    },
    mounted() {
        this.fetchWarehouseSummary();
        this.fetchInventoryMovement();
        this.fetchWarehouseCapacity();
        this.fetchRecentStockMovements();
        this.fetchLowStockItems();
        this.fetchStockAlerts();
    },
    methods: {
        goToProduction(product) {
            // Store the product in localStorage to be used in Production page
            localStorage.setItem('selectedProductForProduction', JSON.stringify(product));

            // Close the modal
            this.closeStockAlertModal();

            // Navigate to Production page
            this.$router.push('/production');
        },
        formatDate(dateString) {
            if (!dateString) return '';
            const date = new Date(dateString);
            return date.toISOString().split('T')[0]; // Format as YYYY-MM-DD
        },
        fetchWarehouseSummary() {
            this.Api.get('/dashboard/warehouse-summary')
                .then(response => {
                    if (response.data) {
                        this.warehouseSummary = response.data;
                    }
                })
                .catch(error => {
                    console.error('Error fetching warehouse summary:', error);
                });
        },
        fetchInventoryMovement() {
            this.Api.get('/dashboard/inventory-movement')
                .then(response => {
                    if (response.data) {
                        this.inventoryMovement = response.data;
                        this.initInventoryChart();
                    }
                })
                .catch(error => {
                    console.error('Error fetching inventory movement:', error);
                    this.initInventoryChart(); // Initialize chart even if there's an error
                });
        },
        fetchWarehouseCapacity() {
            this.Api.get('/dashboard/warehouse-capacity')
                .then(response => {
                    if (response.data) {
                        this.warehouseCapacity = response.data;
                        this.initCapacityChart();
                    }
                })
                .catch(error => {
                    console.error('Error fetching warehouse capacity:', error);
                    this.initCapacityChart(); // Initialize chart even if there's an error
                });
        },
        fetchRecentStockMovements() {
            this.Api.get('/dashboard/recent-stock-movements?limit=6')
                .then(response => {
                    if (response.data) {
                        this.recentStockMovements = response.data;
                    }
                })
                .catch(error => {
                    console.error('Error fetching recent stock movements:', error);
                });
        },
        fetchLowStockItems() {
            this.Api.get('/dashboard/low-stock-items?limit=4')
                .then(response => {
                    if (response.data) {
                        this.lowStockItems = response.data;
                    }
                })
                .catch(error => {
                    console.error('Error fetching low stock items:', error);
                });
        },
        fetchStockAlerts() {
            this.Api.get('/dashboard/stock-alerts')
                .then(response => {
                    if (response.data && response.data.length > 0) {
                        this.stockAlerts = response.data;
                        this.showStockAlertModal = true;
                    }
                })
                .catch(error => {
                    console.error('Error fetching stock alerts:', error);
                });
        },
        closeStockAlertModal() {
            this.showStockAlertModal = false;
        },
        removeAlert(index) {
            // Remove the alert at the specified index
            this.stockAlerts.splice(index, 1);

            // If no more alerts, close the modal
            if (this.stockAlerts.length === 0) {
                this.showStockAlertModal = false;
            }
        },
        markAlertAsResolved(alertId) {
            this.Api.put(`/dashboard/stock-alerts/${alertId}/resolve`)
                .then(() => {
                    // Remove the alert from the list
                    this.stockAlerts = this.stockAlerts.filter(alert => alert.id !== alertId);

                    // If no more alerts, close the modal
                    if (this.stockAlerts.length === 0) {
                        this.showStockAlertModal = false;
                    } else if (this.currentAlertIndex >= this.stockAlerts.length) {
                        // If the current index is now out of bounds, adjust it
                        this.currentAlertIndex = this.stockAlerts.length - 1;
                    }
                })
                .catch(error => {
                    console.error('Error marking alert as resolved:', error);
                });
        },
        initCharts() {
            this.initInventoryChart();
            this.initCapacityChart();
        },
        initInventoryChart() {
            // Inventory Movement Chart
            if (document.getElementById('inventoryChart')) {
                const inventoryChartCanvas = document.getElementById('inventoryChart').getContext('2d');

                // Extract data from the API response
                let labels = [];
                let incomingData = [];
                let outgoingData = [];

                // Check if we have data from the API
                if (this.inventoryMovement && this.inventoryMovement.length > 0) {
                    // Extract labels and data from the inventoryMovement array
                    this.inventoryMovement.forEach(item => {
                        labels.push(item.month || '');
                        incomingData.push(item.incoming_stock || 0);
                        outgoingData.push(item.outgoing_stock || 0);
                    });
                } else {
                    // Fallback to default data if no API data is available
                    labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
                    incomingData = [65, 59, 80, 81, 56, 55, 40];
                    outgoingData = [28, 48, 40, 19, 86, 27, 90];
                }

                const inventoryChartData = {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Stok Masuk',
                            backgroundColor: 'rgba(40,167,69,0.9)',
                            borderColor: 'rgba(40,167,69,0.8)',
                            pointRadius: false,
                            pointColor: '#28a745',
                            pointStrokeColor: 'rgba(40,167,69,1)',
                            pointHighlightFill: '#fff',
                            pointHighlightStroke: 'rgba(40,167,69,1)',
                            data: incomingData
                        },
                        {
                            label: 'Stok Keluar',
                            backgroundColor: 'rgba(255,193,7,0.9)',
                            borderColor: 'rgba(255,193,7,0.8)',
                            pointRadius: false,
                            pointColor: '#ffc107',
                            pointStrokeColor: 'rgba(255,193,7,1)',
                            pointHighlightFill: '#fff',
                            pointHighlightStroke: 'rgba(255,193,7,1)',
                            data: outgoingData
                        }
                    ]
                };

                const inventoryChartOptions = {
                    maintainAspectRatio: false,
                    responsive: true,
                    legend: {
                        display: true
                    },
                    scales: {
                        xAxes: [{
                            gridLines: {
                                display: false
                            }
                        }],
                        yAxes: [{
                            gridLines: {
                                display: false
                            }
                        }]
                    }
                };

                new Chart(inventoryChartCanvas, {
                    type: 'line',
                    data: inventoryChartData,
                    options: inventoryChartOptions
                });
            }
        },
        initCapacityChart() {
            // Warehouse Capacity Chart
            if (document.getElementById('capacityChart')) {
                const capacityChartCanvas = document.getElementById('capacityChart').getContext('2d');
                const capacityData = {
                    labels: [
                        'Ruang Terpakai',
                        'Ruang Tersedia'
                    ],
                    datasets: [
                        {   
                            data: [this.warehouseCapacity.used_space, this.warehouseCapacity.available_space],
                            backgroundColor: ['#17a2b8', '#e9ecef'],
                        }
                    ]
                };
                const capacityOptions = {
                    maintainAspectRatio: false,
                    responsive: true,
                };

                new Chart(capacityChartCanvas, {
                    type: 'doughnut',
                    data: capacityData,
                    options: capacityOptions
                });
            }
        }
    }
}
</script>

<style scoped>
/* Stock Alert Modal Styles */
.stock-alert-modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1050;
}

.stock-alert-modal {
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
    width: 90%;
    max-width: 600px;
    max-height: 90vh;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
}

/* Removed stock-alert-header styles as it's no longer used */

.stock-alert-body {
    padding: 20px;
    flex-grow: 1;
}

.stock-alert-body .alert {
    margin-bottom: 20px;
    padding: 15px;
    border-radius: 5px;
    position: relative;
}

.stock-alert-body .alert-danger {
    background-color: #f8d7da;
    border-color: #f5c6cb;
    color: #721c24;
}

.stock-alert-body .alert-warning {
    background-color: #fff3cd;
    border-color: #ffeeba;
    color: #856404;
}

.stock-alert-body .alert h5 {
    margin-top: 0;
    margin-bottom: 10px;
    font-weight: 600;
}

.stock-alert-body .alert i {
    margin-right: 5px;
}

.product-details {
    display: flex;
    margin-top: 20px;
    background-color: #f8f9fa;
    border-radius: 5px;
    padding: 15px;
}

.product-image {
    width: 100px;
    height: 100px;
    margin-right: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-image img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}

.product-info {
    flex-grow: 1;
}

.product-info h5 {
    margin-top: 0;
    margin-bottom: 10px;
    font-weight: 600;
}

.product-info p {
    margin: 5px 0;
    color: #6c757d;
}

.close-alert-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    background: transparent;
    border: none;
    color: inherit;
    font-size: 16px;
    cursor: pointer;
    padding: 0;
    margin: 0;
}

/* Existing styles */
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

.small-box > .small-box-footer {
    background-color: rgba(0,0,0,.1);
    color: rgba(255,255,255,.8);
    display: block;
    padding: 3px 0;
    position: relative;
    text-align: center;
    text-decoration: none;
    z-index: 10;
}

.small-box > .small-box-footer:hover {
    background-color: rgba(0,0,0,.15);
    color: #fff;
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
}

.small-box .icon > i {
    font-size: 90px;
    position: absolute;
    right: 15px;
    top: 15px;
    transition: transform .3s linear;
}

.small-box:hover .icon > i {
    transform: scale(1.1);
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

.products-list {
    list-style: none;
    margin: 0;
    padding: 0;
}

.products-list > .item {
    border-radius: 0.25rem;
    padding: 10px 0;
    background-color: #fff;
}

.products-list > .item:after {
    clear: both;
    content: "";
    display: block;
}

.products-list .product-img {
    float: left;
}

.products-list .product-img img {
    height: 50px;
    width: 50px;
}

.products-list .product-info {
    margin-left: 60px;
}

.products-list .product-title {
    font-weight: 600;
}

.products-list .product-description {
    color: #6c757d;
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.img-size-50 {
    width: 50px;
}
</style>
