<template>
    <div class="container-fluid">
        <!-- Stat Cards -->
        <div class="row">
            <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                    <div class="inner">
                        <h3>120</h3>
                        <p>Total Inventory</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-boxes"></i>
                    </div>
                    <router-link to="/inventory" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                    <div class="inner">
                        <h3>42</h3>
                        <p>Incoming Stock</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-truck-loading"></i>
                    </div>
                    <router-link to="/incoming-stock" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                    <div class="inner">
                        <h3>35</h3>
                        <p>Outgoing Stock</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shipping-fast"></i>
                    </div>
                    <router-link to="/outgoing-stock" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
                </div>
            </div>
            <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                    <div class="inner">
                        <h3>15</h3>
                        <p>Low Stock Items</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <router-link to="/low-stock" class="small-box-footer">
                        More info <i class="fas fa-arrow-circle-right"></i>
                    </router-link>
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
                            Inventory Movement
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
                            Warehouse Capacity
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
                        <h3 class="card-title">Recent Stock Movements</h3>
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
                                        <th>Item</th>
                                        <th>Type</th>
                                        <th>Quantity</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><a href="#">SM9842</a></td>
                                        <td>School Uniform Set</td>
                                        <td><span class="badge badge-success">In</span></td>
                                        <td>50</td>
                                        <td>2023-06-15</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">SM1848</a></td>
                                        <td>School Shirt</td>
                                        <td><span class="badge badge-warning">Out</span></td>
                                        <td>20</td>
                                        <td>2023-06-14</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">SM7429</a></td>
                                        <td>School Pants</td>
                                        <td><span class="badge badge-success">In</span></td>
                                        <td>30</td>
                                        <td>2023-06-13</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">SM7430</a></td>
                                        <td>School Skirt</td>
                                        <td><span class="badge badge-warning">Out</span></td>
                                        <td>15</td>
                                        <td>2023-06-12</td>
                                    </tr>
                                    <tr>
                                        <td><a href="#">SM1849</a></td>
                                        <td>School Tie</td>
                                        <td><span class="badge badge-success">In</span></td>
                                        <td>100</td>
                                        <td>2023-06-11</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer clearfix">
                        <router-link to="/stock-movement/create" class="btn btn-sm btn-info float-left">Add Stock Movement</router-link>
                        <router-link to="/stock-movements" class="btn btn-sm btn-secondary float-right">View All Movements</router-link>
                    </div>
                </div>
            </div>

            <!-- Low Stock Items Table -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Low Stock Items</h3>
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
                                        <span class="badge badge-danger float-right">5 left</span></a>
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
                                        <span class="badge badge-warning float-right">10 left</span></a>
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
                                    <a href="javascript:void(0)" class="product-title">School Tie
                                        <span class="badge badge-danger float-right">3 left</span></a>
                                    <span class="product-description">
                                        School tie with logo
                                    </span>
                                </div>
                            </li>
                            <li class="item">
                                <div class="product-img">
                                    <img src="@assets/no-image.png" alt="Product Image" class="img-size-50">
                                </div>
                                <div class="product-info">
                                    <a href="javascript:void(0)" class="product-title">School Skirt
                                        <span class="badge badge-warning float-right">8 left</span></a>
                                    <span class="product-description">
                                        Pleated grey school skirt
                                    </span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="card-footer text-center">
                        <router-link to="/low-stock" class="uppercase">View All Low Stock Items</router-link>
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
            // Inventory Movement Chart
            if (document.getElementById('inventoryChart')) {
                const inventoryChartCanvas = document.getElementById('inventoryChart').getContext('2d');

                const inventoryChartData = {
                    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
                    datasets: [
                        {
                            label: 'Incoming Stock',
                            backgroundColor: 'rgba(40,167,69,0.9)',
                            borderColor: 'rgba(40,167,69,0.8)',
                            pointRadius: false,
                            pointColor: '#28a745',
                            pointStrokeColor: 'rgba(40,167,69,1)',
                            pointHighlightFill: '#fff',
                            pointHighlightStroke: 'rgba(40,167,69,1)',
                            data: [65, 59, 80, 81, 56, 55, 40]
                        },
                        {
                            label: 'Outgoing Stock',
                            backgroundColor: 'rgba(255,193,7,0.9)',
                            borderColor: 'rgba(255,193,7,0.8)',
                            pointRadius: false,
                            pointColor: '#ffc107',
                            pointStrokeColor: 'rgba(255,193,7,1)',
                            pointHighlightFill: '#fff',
                            pointHighlightStroke: 'rgba(255,193,7,1)',
                            data: [28, 48, 40, 19, 86, 27, 90]
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

            // Warehouse Capacity Chart
            if (document.getElementById('capacityChart')) {
                const capacityChartCanvas = document.getElementById('capacityChart').getContext('2d');
                const capacityData = {
                    labels: [
                        'Used Space',
                        'Available Space'
                    ],
                    datasets: [
                        {
                            data: [70, 30],
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