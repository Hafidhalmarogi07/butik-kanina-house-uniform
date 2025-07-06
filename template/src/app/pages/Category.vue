<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Daftar Kategori</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Tambah Kategori
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Cari kategori..." 
                                           v-model="searchQuery" 
                                           @keyup.enter="handleSearch">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button" @click="handleSearch">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nama</th>
                                        <th>Tindakan</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="category in filteredCategories" :key="category.id">
                                        <td>{{ category.id }}</td>
                                        <td>{{ category.name }}</td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="editCategory(category)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="deleteCategory(category)">
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
                            <p>Tidak ada kategori ditemukan</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit Category Modal -->
        <div class="modal fade" id="categoryModal" tabindex="-1" role="dialog" aria-labelledby="categoryModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="categoryModalLabel">{{ isEditing ? 'Edit Kategori' : 'Tambah Kategori Baru' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveCategory">
                            <div class="form-group">
                                <label for="categoryName">Nama Kategori</label>
                                <input type="text" class="form-control" id="categoryName" v-model="currentCategory.name" required>
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
                        Apakah Anda yakin ingin menghapus kategori ini? Tindakan ini tidak dapat dibatalkan.
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
            isEditing: false,
            searchTimeout: null,
            currentCategory: {
                id: null,
                name: ''
            },
            categoryToDelete: null,
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
        filteredCategories() {
            // Since filtering is now done on the server side, 
            // we simply return the categories array
            return this.categories;
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
        this.fetchCategories(0); // Start with page 0 for backend (equivalent to UI page 1)
    },
    watch: {
        searchQuery: function(newVal) {
            // Debounce search to avoid too many requests
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            this.searchTimeout = setTimeout(() => {
                // Reset to first page and fetch categories with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        }
    },
    methods: {
        fetchCategories(page = 0) {
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
            this.Api.get(`/category?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.categories = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.categories = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.categories.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Kesalahan mengambil kategori:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Kesalahan ${error.response.status}: ${error.response.data.message || 'Gagal memuat kategori'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'Tidak ada respons dari server. Silakan periksa koneksi Anda.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Gagal memuat kategori: ' + error.message;
                    }

                    this.loading = false;
                });
        },

        changePage(page) {
            // page parameter is 1-based from UI, convert to 0-based for backend
            const zeroBasedPage = page - 1;
            if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
            this.pagination.pageNumber = zeroBasedPage;
            this.fetchCategories(zeroBasedPage);
        },
        showAddModal() {
            this.isEditing = false;
            this.currentCategory = {
                id: null,
                name: ''
            };
            $('#categoryModal').modal('show');
        },
        editCategory(category) {
            this.isEditing = true;
            this.currentCategory = { ...category };
            $('#categoryModal').modal('show');
        },
        deleteCategory(category) {
            this.categoryToDelete = category;
            $('#deleteModal').modal('show');
        },
        confirmDelete() {
            if (this.categoryToDelete) {
                this.loading = true;
                this.Api.delete(`/category/${this.categoryToDelete.id}`)
                    .then(() => {
                        const index = this.categories.findIndex(c => c.id === this.categoryToDelete.id);
                        if (index !== -1) {
                            this.categories.splice(index, 1);
                        }
                        this.categoryToDelete = null;
                        $('#deleteModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan menghapus kategori:', error);
                        this.error = 'Gagal menghapus kategori';
                        this.loading = false;
                    });
            }
        },
        saveCategory() {
            this.loading = true;

            if (this.isEditing) {
                // Update existing category
                this.Api.put(`/category/${this.currentCategory.id}`, { name: this.currentCategory.name })
                    .then(response => {
                        const index = this.categories.findIndex(c => c.id === this.currentCategory.id);
                        if (index !== -1) {
                            this.categories.splice(index, 1, response.data);
                        }
                        $('#categoryModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan memperbarui kategori:', error);
                        this.error = 'Gagal memperbarui kategori';
                        this.loading = false;
                    });
            } else {
                // Add new category
                this.Api.post('/category', { name: this.currentCategory.name })
                    .then(response => {
                        this.categories.push(response.data);
                        $('#categoryModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Kesalahan menambahkan kategori:', error);
                        this.error = 'Gagal menambahkan kategori';
                        this.loading = false;
                    });
            }
        },

        handleSearch() {
            // Clear any existing timeout
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            // Reset to first page and fetch categories with current search query
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
