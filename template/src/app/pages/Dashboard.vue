<template>
    <div class="container-fluid">
        <!-- Stat Cards -->
        <div class="row">
            <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                    <div class="inner">
                        <h3>{{ dashboardData.sales }}</h3>
                        <p>Total Sales</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <router-link to="/sales" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                    <div class="inner">
                        <h3>{{ dashboardData.orders }}</h3>
                        <p>Orders</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <router-link to="/orders" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                    <div class="inner">
                        <h3>Rp {{ formatCurrency(dashboardData.revenue) }}</h3>
                        <p>Revenue</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-money-bill-alt"></i>
                    </div>
                    <router-link to="/reports" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                    <div class="inner">
                        <h3>Rp {{ formatCurrency(dashboardData.expenses) }}</h3>
                        <p>Expenses</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-chart-pie"></i>
                    </div>
                    <router-link to="/expenses" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Monthly Sales Chart -->
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <i class="fas fa-chart-bar mr-1"></i>
                            Monthly Sales
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
                            <canvas id="salesChart" style="min-height: 250px; height: 250px; max-height: 250px; max-width: 100%;"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Product Category Distribution -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">
                            <i class="fas fa-chart-pie mr-1"></i>
                            Product Categories
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
                        <canvas id="pieChart" style="min-height: 250px; height: 250px; max-height: 250px; max-width: 100%;"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Recent Sales Table -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header border-transparent">
                        <h3 class="card-title">Recent Sales</h3>
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
                                        <th>Order ID</th>
                                        <th>Item</th>
                                        <th>Status</th>
                                        <th>Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><a href="#">OR9842</a></td>
                                        <td>School Uniform Set</td>
                                        <td><span class="badge badge-success">Shipped</span></td>
                                        <td>Rp 350,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR1848</a></td>
                                        <td>School Shirt</td>
                                        <td><span class="badge badge-warning">Pending</span></td>
                                        <td>Rp 150,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR7429</a></td>
                                        <td>School Pants</td>
                                        <td><span class="badge badge-info">Processing</span></td>
                                        <td>Rp 200,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR7429</a></td>
                                        <td>School Skirt</td>
                                        <td><span class="badge badge-primary">Processing</span></td>
                                        <td>Rp 180,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR1848</a></td>
                                        <td>School Tie</td>
                                        <td><span class="badge badge-warning">Pending</span></td>
                                        <td>Rp 50,000</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer clearfix">
                        <router-link to="/sales" class="btn btn-sm btn-info float-left">Place New Order</router-link>
                        <router-link to="/sales" class="btn btn-sm btn-secondary float-right">View All Sales</router-link>
                    </div>
                </div>
            </div>

            <!-- Top Products Table -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Top Products</h3>
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
                            <li class="item">
                                <div class="product-img">
                                    <img src="@assets/no-image.png" alt="Product Image" class="img-size-50">
                                </div>
                                <div class="product-info">
                                    <a href="javascript:void(0)" class="product-title">School Uniform Set
                                        <span class="badge badge-warning float-right">Rp 350,000</span></a>
                                    <span class="product-description">
                                        Complete school uniform set with shirt, pants/skirt, and tie
                                    </span>
                                </div>
                            </li>
                            <li class="item">
                                <div class="product-img">
                                    <img src="@assets/no-image.png" alt="Product Image" class="img-size-50">
                                </div>
                                <div class="product-info">
                                    <a href="javascript:void(0)" class="product-title">School Shirt
                                        <span class="badge badge-info float-right">Rp 150,000</span></a>
                                    <span class="product-description">
                                        White school shirt with embroidered logo
                                    </span>
                                </div>
                            </li>
                            <li class="item">
                                <div class="product-img">
                                    <img src="@assets/no-image.png" alt="Product Image" class="img-size-50">
                                </div>
                                <div class="product-info">
                                    <a href="javascript:void(0)" class="product-title">School Pants
                                        <span class="badge badge-danger float-right">Rp 200,000</span></a>
                                    <span class="product-description">
                                        Grey school pants with adjustable waist
                                    </span>
                                </div>
                            </li>
                            <li class="item">
                                <div class="product-img">
                                    <img src="@assets/no-image.png" alt="Product Image" class="img-size-50">
                                </div>
                                <div class="product-info">
                                    <a href="javascript:void(0)" class="product-title">School Skirt
                                        <span class="badge badge-success float-right">Rp 180,000</span></a>
                                    <span class="product-description">
                                        Pleated grey school skirt
                                    </span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="card-footer text-center">
                        <router-link to="/products" class="uppercase">View All Products</router-link>
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
                expenses: 0
            },
            productCategories: []
        };
    },
    mounted() {
        this.fetchDashboardData();
        this.fetchProductCategories();
        this.initCharts();
    },
    methods: {
        fetchDashboardData() {
            // Get the base URL from environment or use a default

            // Fetch dashboard data from API
          this.Api.get(`/dashboard/inner`)
                .then(response => {
                  if (response.data){
                    this.dashboardData = response.data;
                  }
                })

                .catch(error => {
                    console.error('Error fetching dashboard data:', error);
                });
        },
        fetchProductCategories() {
            // Fetch product categories data from API
            this.Api.get(`/dashboard/product-category`)
                .then(response => {
                    if (response.data) {
                        this.productCategories = response.data;
                        // Update the pie chart with the new data
                        this.updatePieChart();
                    }
                })
                .catch(error => {
                    console.error('Error fetching product categories data:', error);
                });
        },
        formatCurrency(value) {
            // Format number to Indonesian currency format
            if (!value) return '0';

            return new Intl.NumberFormat('id-ID', {
                minimumFractionDigits: 0,
                maximumFractionDigits: 0
            }).format(value);
        },
        initCharts() {
            // Sales Chart
            if (document.getElementById('salesChart')) {
                const salesChartCanvas = document.getElementById('salesChart').getContext('2d');

                const salesChartData = {
                    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
                    datasets: [
                        {
                            label: 'Digital Goods',
                            backgroundColor: 'rgba(60,141,188,0.9)',
                            borderColor: 'rgba(60,141,188,0.8)',
                            pointRadius: false,
                            pointColor: '#3b8bba',
                            pointStrokeColor: 'rgba(60,141,188,1)',
                            pointHighlightFill: '#fff',
                            pointHighlightStroke: 'rgba(60,141,188,1)',
                            data: [28, 48, 40, 19, 86, 27, 90]
                        }
                    ]
                };

                const salesChartOptions = {
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

                // This will get the first returned node in the jQuery collection.
                new Chart(salesChartCanvas, {
                    type: 'bar',
                    data: salesChartData,
                    options: salesChartOptions
                });
            }

            // Initialize Pie Chart with empty data
            // The actual data will be loaded by updatePieChart method
            this.initPieChart();
        },

        initPieChart() {
            if (document.getElementById('pieChart')) {
                const pieChartCanvas = document.getElementById('pieChart').getContext('2d');

                // Default colors for pie chart segments
                const backgroundColors = ['#f56954', '#00a65a', '#f39c12', '#00c0ef', '#3c8dbc', '#d2d6de', '#6c757d', '#007bff', '#28a745', '#17a2b8'];

                // Initial empty data
                const pieData = {
                    labels: [],
                    datasets: [
                        {
                            data: [],
                            backgroundColor: backgroundColors,
                        }
                    ]
                };

                const pieOptions = {
                    maintainAspectRatio: false,
                    responsive: true,
                };

                // Store chart instance to update it later
                this.pieChart = new Chart(pieChartCanvas, {
                    type: 'pie',
                    data: pieData,
                    options: pieOptions
                });
            }
        },

        updatePieChart() {
            if (this.pieChart && this.productCategories.length > 0) {
                // Extract category names and product counts from the API response
                const labels = this.productCategories.map(category => category.category_name);
                const data = this.productCategories.map(category => category.product_count);

                // Update chart data
                this.pieChart.data.labels = labels;
                this.pieChart.data.datasets[0].data = data;

                // Ensure we have enough background colors
                const backgroundColors = ['#f56954', '#00a65a', '#f39c12', '#00c0ef', '#3c8dbc', '#d2d6de', '#6c757d', '#007bff', '#28a745', '#17a2b8'];
                if (labels.length > backgroundColors.length) {
                    // Generate additional colors if needed
                    for (let i = backgroundColors.length; i < labels.length; i++) {
                        const randomColor = '#' + Math.floor(Math.random()*16777215).toString(16);
                        backgroundColors.push(randomColor);
                    }
                }

                this.pieChart.data.datasets[0].backgroundColor = backgroundColors.slice(0, labels.length);

                // Update the chart
                this.pieChart.update();
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
