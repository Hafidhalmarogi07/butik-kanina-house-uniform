<template>
    <div class="container-fluid">
        <!-- Stat Cards -->
        <div class="row">
            <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                    <div class="inner">
                        <h3>45</h3>
                        <p>Today's Sales</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <router-link to="/daily-sales" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                    <div class="inner">
                        <h3>12</h3>
                        <p>New Orders</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <router-link to="/new-orders" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                    <div class="inner">
                        <h3>8</h3>
                        <p>Pending Orders</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <router-link to="/pending-orders" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                    <div class="inner">
                        <h3>Rp 5,250,000</h3>
                        <p>Today's Revenue</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-money-bill-alt"></i>
                    </div>
                    <router-link to="/revenue" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
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
                            Daily Sales
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
                            Payment Methods
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
                        <h3 class="card-title">Recent Orders</h3>
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
                                        <th>Customer</th>
                                        <th>Status</th>
                                        <th>Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><a href="#">OR9842</a></td>
                                        <td>John Smith</td>
                                        <td><span class="badge badge-success">Completed</span></td>
                                        <td>Rp 350,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR1848</a></td>
                                        <td>Susan Johnson</td>
                                        <td><span class="badge badge-warning">Pending</span></td>
                                        <td>Rp 150,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR7429</a></td>
                                        <td>Michael Brown</td>
                                        <td><span class="badge badge-info">Processing</span></td>
                                        <td>Rp 200,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR7430</a></td>
                                        <td>Emily Davis</td>
                                        <td><span class="badge badge-primary">Processing</span></td>
                                        <td>Rp 180,000</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">OR1849</a></td>
                                        <td>Robert Wilson</td>
                                        <td><span class="badge badge-warning">Pending</span></td>
                                        <td>Rp 50,000</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer clearfix">
                        <router-link to="/order/create" class="btn btn-sm btn-info float-left">Create New Order</router-link>
                        <router-link to="/orders" class="btn btn-sm btn-secondary float-right">View All Orders</router-link>
                    </div>
                </div>
            </div>

            <!-- Best Selling Products -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Best Selling Products</h3>
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
                                        <span class="badge badge-success float-right">120 sold</span></a>
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
                                        <span class="badge badge-info float-right">95 sold</span></a>
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
                                        <span class="badge badge-warning float-right">85 sold</span></a>
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
                                        <span class="badge badge-danger float-right">75 sold</span></a>
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
    mounted() {
        this.initCharts();
    },
    methods: {
        initCharts() {
            // Daily Sales Chart
            if (document.getElementById('dailySalesChart')) {
                const dailySalesChartCanvas = document.getElementById('dailySalesChart').getContext('2d');

                const dailySalesChartData = {
                    labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                    datasets: [
                        {
                            label: 'Sales',
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
                        'Cash',
                        'Credit Card',
                        'Debit Card',
                        'Bank Transfer',
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