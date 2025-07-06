<template>
    <div class="container-fluid">
        <!-- Stat Cards -->
        <div class="row">
            <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                    <div class="inner">
                        <h3>{{ dashboardData.sales }}</h3>
                        <p>Penjualan Hari Ini</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <router-link to="/daily-sales" class="small-box-footer">
                        Info Lebih Lanjut <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                    <div class="inner">
                        <h3>{{ dashboardData.orders }}</h3>
                        <p>Pesanan Baru</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <router-link to="/new-orders" class="small-box-footer">
                        Info Lebih Lanjut <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                    <div class="inner">
                        <h3>{{ dashboardData.orders_pending }}</h3>
                        <p>Pesanan Tertunda</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <router-link to="/pending-orders" class="small-box-footer">
                        Info Lebih Lanjut <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                    <div class="inner">
                        <h3>Rp {{ formatCurrency(dashboardData.revenue) }}</h3>
                        <p>Pendapatan Hari Ini</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-money-bill-alt"></i>
                    </div>
                    <router-link to="/revenue" class="small-box-footer">
                        Info Lebih Lanjut <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Daily Sales Chart -->
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <i class="fas fa-chart-line mr-1"></i>
                            Penjualan Harian
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
                            <canvas id="dailySalesChart" style="min-height: 250px; height: 250px; max-height: 250px; max-width: 100%;"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Payment Methods -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <i class="fas fa-credit-card mr-1"></i>
                            Metode Pembayaran
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
                        <canvas id="paymentChart" style="min-height: 250px; height: 250px; max-height: 250px; max-width: 100%;"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Recent Orders Table -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header border-transparent">
                        <h3 class="card-title">Pesanan Terbaru</h3>
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
                                        <th>ID Pesanan</th>
                                        <th>Pelanggan</th>
                                        <th>Status</th>
                                        <th>Jumlah</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <tr v-for="order in recentOrders" :key="order.id">
                                  <td><a href="#">{{ order.order_number }}</a></td>
                                  <td>{{ order.customer_name }}</td>
                                  <td><span class="badge" :class="getStatusBadgeClass(order.status)">{{ order.status }}</span></td>
                                  <td>Rp {{ formatCurrency(order.amount) }}</td>
                                </tr>
                                <!-- Fallback if no sales are available -->
                                <tr v-if="recentOrders.length === 0">
                                  <td colspan="4" class="text-center">Tidak ada penjualan terbaru</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer clearfix">
                        <router-link to="/orders" class="btn btn-sm btn-info float-left">Buat Pesanan Baru</router-link>
                        <router-link to="/orders" class="btn btn-sm btn-secondary float-right">Lihat Semua Pesanan</router-link>
                    </div>
                </div>
            </div>

            <!-- Best Selling Products -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Produk Terlaris</h3>
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
                        <li v-for="(product, index) in topProducts" :key="product.product_id" class="item">
                          <div class="product-img">
                            <img :src="product.image_url || '@assets/no-image.png'" alt="Product Image" class="img-size-50">
                          </div>
                          <div class="product-info">
                            <a href="javascript:void(0)" class="product-title">{{ product.product_name }}
                              <span class="badge float-right" :class="getBadgeClass(index)">Rp {{ formatCurrency(product.price) }}</span></a>
                            <span class="product-description"> {{ product.product_description }} </span>
                          </div>
                        </li>
                        <!-- Fallback if no products are available -->
                        <li v-if="topProducts.length === 0" class="item">
                          <div class="product-info">
                            <span class="product-title">Tidak ada produk tersedia</span>
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
      dashboardData: {
        sales: 0,
        orders: 0,
        revenue: 0,
        expenses: 0,
        orders_pending:0
      },
      productCategories: [],
      topProducts: [],
      recentSales: [],
      recentOrders: [],
      monthlySales: [],
      dailySales: []
    };
  },
    mounted() {
        this.fetchDashboardData();
        this.fetchTopProducts();
        this.fetchRecentOrders();
        this.fetchDailySales();
    },
    methods: {
      fetchDashboardData() {
        // Get the base URL from environment or use a default

        // Fetch dashboard data from API
        this.Api.get(`/dashboard/today`)
            .then(response => {
              if (response.data){
                this.dashboardData = response.data;
              }
            })

            .catch(error => {
              console.error('Kesalahan mengambil data dashboard:', error);
            });
      },
      fetchTopProducts() {
        // Fetch top products data from API (limit to 4 products)
        this.Api.get(`/dashboard/top-products?limit=4`)
            .then(response => {
              if (response.data) {
                this.topProducts = response.data;
              }
            })
            .catch(error => {
              console.error('Kesalahan mengambil data produk terlaris:', error);
            });
      },
      fetchRecentOrders() {
        // Fetch recent sales data from API (limit to 5 sales)
        this.Api.get(`/dashboard/recent-orders?limit=5`)
            .then(response => {
              if (response.data) {
                this.recentOrders = response.data;
              }
            })
            .catch(error => {
              console.error('Kesalahan mengambil data penjualan terbaru:', error);
            });
      },
      getBadgeClass(index) {
        // Return different badge classes based on index
        const badgeClasses = ['badge-warning', 'badge-info', 'badge-danger', 'badge-success', 'badge-primary'];
        return badgeClasses[index % badgeClasses.length];
      },
      getStatusBadgeClass(status) {
        // Return badge class based on sale status
        if (!status) return 'badge-secondary';

        switch(status) {
          case 'COMPLETED':
          case 'SHIPPED':
            return 'badge-success';
          case 'PENDING':
            return 'badge-warning';
          case 'PROCESSING':
            return 'badge-info';
          case 'CANCELLED':
            return 'badge-danger';
          default:
            return 'badge-primary';
        }
      },
      formatCurrency(value) {
        // Format number to Indonesian currency format
        if (!value) return '0';

        return new Intl.NumberFormat('id-ID', {
          minimumFractionDigits: 0,
          maximumFractionDigits: 0
        }).format(value);
      },

      fetchDailySales() {
        // Fetch daily sales data from API
        this.Api.get(`/dashboard/daily-sales`)
            .then(response => {
              if (response.data) {
                this.dailySales = response.data;
                this.initCharts();
              }
            })
            .catch(error => {
              console.error('Kesalahan mengambil data penjualan harian:', error);
              this.initCharts(); // Initialize charts even if there's an error
            });
      },
        initCharts() {
            // Daily Sales Chart
            if (document.getElementById('dailySalesChart')) {
                const dailySalesChartCanvas = document.getElementById('dailySalesChart').getContext('2d');

                const dailySalesChartData = {
                    labels: ['Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu', 'Minggu'],
                    datasets: [
                        {
                            label: 'Penjualan',
                            backgroundColor: 'rgba(60,141,188,0.9)',
                            borderColor: 'rgba(60,141,188,0.8)',
                            pointRadius: true,
                            pointColor: '#3b8bba',
                            pointStrokeColor: 'rgba(60,141,188,1)',
                            pointHighlightFill: '#fff',
                            pointHighlightStroke: 'rgba(60,141,188,1)',
                            data: [28, 48, 40, 19, 86, 27, 90]
                        }
                    ]
                };

                const dailySalesChartOptions = {
                    maintainAspectRatio: false,
                    responsive: true,
                    legend: {
                        display: false
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

                new Chart(dailySalesChartCanvas, {
                    type: 'line',
                    data: dailySalesChartData,
                    options: dailySalesChartOptions
                });
            }

            // Payment Methods Chart
            if (document.getElementById('paymentChart')) {
                const paymentChartCanvas = document.getElementById('paymentChart').getContext('2d');
                const paymentData = {
                    labels: [
                        'Tunai',
                        'Kartu Kredit',
                        'Kartu Debit',
                        'Transfer Bank',
                        'E-Wallet'
                    ],
                    datasets: [
                        {
                            data: [30, 25, 15, 10, 20],
                            backgroundColor: ['#28a745', '#dc3545', '#ffc107', '#17a2b8', '#6c757d'],
                        }
                    ]
                };
                const paymentOptions = {
                    maintainAspectRatio: false,
                    responsive: true,
                };

                new Chart(paymentChartCanvas, {
                    type: 'pie',
                    data: paymentData,
                    options: paymentOptions
                });
            }
        }
    }
}
</script>

<style scoped>
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
