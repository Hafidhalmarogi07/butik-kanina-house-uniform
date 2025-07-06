<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Daftar Produk</h3>
                        <div class="card-tools" v-if="!isAdminToko">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Tambah Produk
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Cari produk..." 
                                           v-model="searchQuery" 
                                           @keyup.enter="handleSearch">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button" @click="handleSearch">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="categoryFilter">
                                    <option value="">Semua Kategori</option>
                                    <option v-for="category in categories" :key="category.id" :value="category.id.toString()">
                                        {{ category.name }}
                                    </option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Gambar</th>
                                        <th>Nama</th>
                                        <th>Kategori</th>
                                        <th>Ukuran</th>
                                        <th>Stok</th>
                                        <th>Harga</th>
                                        <th v-if="!isAdminToko">Aksi</th>
                                        <th v-else></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="product in filteredProducts" :key="product.id">
                                        <td>{{ product.id }}</td>
                                        <td>
                                            <img :src="product.image_url || '@assets/no-image.png'" alt="Product Image" class="img-thumbnail" style="width: 50px; height: 50px;">
                                        </td>
                                        <td>{{ product.name }}</td>
                                        <td>{{ product.category ? product.category.name : '' }}</td>
                                        <td>{{ product.size }}</td>
                                        <td>
                                            <span :class="getStockClass(product.stock)">{{ product.stock }}</span>
                                        </td>
                                        <td>{{ product.selling_price }}</td>
                                        <td>
                                            <div class="btn-group" v-if="!isAdminToko">
                                                <button type="button" class="btn btn-sm btn-info" @click="editProduct(product)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="deleteProduct(product)">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                            <span v-else>-</span>
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
                            <p>Tidak ada produk ditemukan</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit Product Modal -->
        <div class="modal fade" id="productModal" tabindex="-1" role="dialog" aria-labelledby="productModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="productModalLabel">{{ isEditing ? 'Edit Produk' : 'Tambah Produk Baru' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveProduct">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="productName">Nama Produk</label>
                                        <input type="text" class="form-control" id="productName" v-model="currentProduct.name" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="productCategory">Kategori</label>
                                        <select class="form-control" id="productCategory" v-model="currentProduct.category.id" required>
                                            <option v-for="category in categories" :key="category.id" :value="category.id">
                                                {{ category.name }}
                                            </option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="productSize">Ukuran</label>
                                        <select class="form-control" id="productSize" v-model="currentProduct.size" required>
                                            <option value="S">S</option>
                                            <option value="M">M</option>
                                            <option value="L">L</option>
                                            <option value="XL">XL</option>
                                            <option value="XXL">XXL</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="productStock">Stok</label>
                                        <input type="number" class="form-control" id="productStock" v-model="currentProduct.stock" min="0" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="productPrice">Harga</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">Rp</span>
                                            </div>
                                            <input type="number" class="form-control" id="productPrice" v-model="currentProduct.selling_price" min="0" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="productImage">Gambar Produk</label>
                                        <div class="input-group">
                                            <div class="custom-file">
                                                <input type="file" class="custom-file-input" id="productImage" @change="handleImageUpload">
                                                <label class="custom-file-label" for="productImage">
                                                    {{ currentProduct.image_url ? 'Gambar dipilih' : 'Pilih file' }}
                                                </label>
                                            </div>
                                        </div>
                                        <small class="form-text text-muted" v-if="isEditing && currentProduct.image_url">
                                            Gambar saat ini akan dipertahankan jika tidak ada gambar baru yang dipilih
                                        </small>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="productDescription">Deskripsi</label>
                                <textarea class="form-control" id="productDescription" rows="3" v-model="currentProduct.description"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                                <button type="submit" class="btn btn-primary">Simpan</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteModalLabel">Konfirmasi Hapus</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Apakah Anda yakin ingin menghapus produk ini? Tindakan ini tidak dapat dibatalkan.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                        <button type="button" class="btn btn-danger" @click="confirmDelete">Hapus</button>
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
            categoryFilter: '',
            isEditing: false,
            searchTimeout: null,
            currentProduct: {
                id: null,
                name: '',
                category: {
                    id: null,
                    name: ''
                },
                size: '',
                stock: 0,
                selling_price: 0,
                description: '',
                image_url: null
            },
            productToDelete: null,
            products: [],
            categories: [],
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
        isAdminToko() {
            // Check if user has ROLE_ADMIN_TOKO role
            if (this.$store && this.$store.state && this.$store.state.user && this.$store.state.user.roles) {
                return this.$store.state.user.roles.some(role => role.name === 'ROLE_ADMIN_TOKO');
            }
            return false;
        },
        filteredProducts() {
            // Since filtering is now done on the server side, 
            // we simply return the products array
            return this.products;
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
        this.fetchProducts(0); // Start with page 0 for backend (equivalent to UI page 1)
        this.fetchCategories();
    },
    watch: {
        'currentProduct.category.id': function(newVal) {
            if (newVal && this.categories.length > 0) {
                const category = this.categories.find(c => c.id === newVal);
                if (category) {
                    this.currentProduct.category.name = category.name;
                }
            }
        },
        searchQuery: function(newVal) {
            // Debounce search to avoid too many requests
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            this.searchTimeout = setTimeout(() => {
                // Reset to first page and fetch products with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        },
        categoryFilter: function() {
            // Reset to first page and fetch products with new category filter
            this.changePage(1);
        }
    },
    methods: {
        fetchProducts(page = 0) {
            this.loading = true;

            // Prepare params object with pagination
            const params = { 
                page: page, 
                size: this.pagination.pageSize 
            };

            // Prepare filters array for backend
            const filters = [];

            // Add search query if present
            if (this.searchQuery) {
                // Add filter for name containing search query
                filters.push(["name", "like", this.searchQuery]);

                // If we want to search in description too, add OR operator and description filter
                if (this.searchQuery.trim() !== "") {
                    filters.push(["or"]);
                    filters.push(["description", "like", this.searchQuery]);
                }
            }

            // Add category filter if present
            if (this.categoryFilter) {
                // If we already have filters, add AND operator
                if (filters.length > 0) {
                    filters.push(["and"]);
                }

                // Add filter for category.id equals categoryFilter
                filters.push(["category.id", "=", this.categoryFilter]);
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
            this.Api.get(`/product?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.products = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.products = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.products.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Kesalahan mengambil produk:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Gagal memuat produk'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'Tidak ada respons dari server. Silakan periksa koneksi Anda.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Gagal memuat produk: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        changePage(page) {
            // page parameter is 1-based from UI, convert to 0-based for backend
            const zeroBasedPage = page - 1;
            if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
            this.pagination.pageNumber = zeroBasedPage;
            this.fetchProducts(zeroBasedPage);
        },

        fetchCategories() {
            this.Api.get('/category')
                .then(response => {
                    this.categories = response.data.content || response.data;
                })
                .catch(error => {
                    console.error('Kesalahan mengambil kategori:', error);
                });
        },

        getStockClass(stock) {
            if (stock <= 5) return 'badge badge-danger';
            if (stock <= 20) return 'badge badge-warning';
            return 'badge badge-success';
        },

        showAddModal() {
            this.isEditing = false;
            this.currentProduct = {
                id: null,
                name: '',
                category: {
                    id: null,
                    name: ''
                },
                size: '',
                stock: 0,
                selling_price: 0,
                description: '',
                image_url: null
            };
            $('#productModal').modal('show');
        },

        editProduct(product) {
            this.isEditing = true;
            // Create a deep copy to avoid modifying the original object
            this.currentProduct = JSON.parse(JSON.stringify(product));

            // Ensure category is properly set
            if (!this.currentProduct.category) {
                this.currentProduct.category = { id: null, name: '' };
            }

            $('#productModal').modal('show');
        },

        deleteProduct(product) {
            this.productToDelete = product;
            $('#deleteModal').modal('show');
        },

        confirmDelete() {
            if (this.productToDelete) {
                this.loading = true;
                this.Api.delete(`/product/${this.productToDelete.id}`)
                    .then(() => {
                        const index = this.products.findIndex(p => p.id === this.productToDelete.id);
                        if (index !== -1) {
                            this.products.splice(index, 1);
                        }
                        this.productToDelete = null;
                        $('#deleteModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan menghapus produk:', error);
                        this.error = 'Gagal menghapus produk';
                        this.loading = false;
                    });
            }
        },

        saveProduct() {
            this.loading = true;

            if (this.isEditing) {
                // Update existing product
                this.Api.put(`/product/${this.currentProduct.id}`, this.currentProduct)
                    .then(response => {
                        const index = this.products.findIndex(p => p.id === this.currentProduct.id);
                        if (index !== -1) {
                            this.products.splice(index, 1, response.data);
                        }
                        $('#productModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan memperbarui produk:', error);
                        this.error = 'Gagal memperbarui produk';
                        this.loading = false;
                    });
            } else {
                // Add new product
                this.Api.post('/product', this.currentProduct)
                    .then(response => {
                        this.products.push(response.data);
                        $('#productModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan menambahkan produk:', error);
                        this.error = 'Gagal menambahkan produk';
                        this.loading = false;
                    });
            }
        },

        handleImageUpload(event) {
            const file = event.target.files[0];
            if (file) {
                // Convert file to base64 for API
                const reader = new FileReader();
                reader.onload = (e) => {
                    this.currentProduct.image_url = e.target.result;

                    // Update the file input label
                    const fileName = file.name;
                    $(event.target).next('.custom-file-label').html(fileName);
                };
                reader.readAsDataURL(file);
            }
        },

        handleSearch() {
            // Clear any existing timeout
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            // Reset to first page and fetch products with current search query
            this.changePage(1);
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
</style>
