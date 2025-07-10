<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Daftar Pengeluaran</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Tambah Pengeluaran
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Cari pengeluaran..." 
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
                                    <option v-for="type in expenseTypes" :key="type" :value="type">
                                        {{ type }}
                                    </option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="monthFilter">
                                    <option value="">Semua Bulan</option>
                                    <option value="01">Januari</option>
                                    <option value="02">Februari</option>
                                    <option value="03">Maret</option>
                                    <option value="04">April</option>
                                    <option value="05">Mei</option>
                                    <option value="06">Juni</option>
                                    <option value="07">Juli</option>
                                    <option value="08">Agustus</option>
                                    <option value="09">September</option>
                                    <option value="10">Oktober</option>
                                    <option value="11">November</option>
                                    <option value="12">Desember</option>
                                </select>
                            </div>
                        </div>
                        <div v-if="loading" class="text-center my-4">
                            <div class="spinner-border text-primary" role="status">
                                <span class="sr-only">Memuat...</span>
                            </div>
                        </div>
                        <div v-else-if="error" class="alert alert-danger">
                            {{ error }}
                        </div>
                        <div v-else class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tanggal</th>
                                        <th>Tipe</th>
                                        <th>Deskripsi</th>
                                        <th>Jumlah</th>
                                        <th>Metode Pembayaran</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="expense in filteredExpenses" :key="expense.id">
                                        <td>{{ expense.id }}</td>
                                        <td>{{ expense.date }}</td>
                                        <td>
                                            <span :class="getCategoryClass(expense.type)">{{ expense.type }}</span>
                                        </td>
                                        <td>{{ expense.description }}</td>
                                        <td>Rp {{ expense.amount.toLocaleString() }}</td>
                                        <td>{{ expense.payment_method }}</td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-sm btn-info" @click="viewExpense(expense)">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" @click="editExpense(expense)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger" @click="deleteExpense(expense)">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <th colspan="4" class="text-right">Total:</th>
                                        <th>Rp {{ totalAmount.toLocaleString() }}</th>
                                        <th colspan="2"></th>
                                    </tr>
                                </tfoot>
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
                            <p>Tidak ada pengeluaran ditemukan</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit Expense Modal -->
        <div class="modal fade" id="expenseModal" tabindex="-1" role="dialog" aria-labelledby="expenseModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="expenseModalLabel">{{ isEditing ? 'Edit Pengeluaran' : 'Tambah Pengeluaran Baru' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveExpense">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="expenseDate">Tanggal</label>
                                        <input type="date" class="form-control" id="expenseDate" v-model="currentExpense.date" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="expenseType">Tipe</label>
                                        <select class="form-control" id="expenseType" v-model="currentExpense.type" required>
                                            <option value="">Pilih Tipe</option>
                                            <option v-for="type in expenseTypes" :key="type" :value="type">
                                                {{ type }}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="expenseAmount">Jumlah</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">Rp</span>
                                            </div>
                                            <input type="number" class="form-control" id="expenseAmount" v-model="currentExpense.amount" min="0" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="expensePaymentMethod">Metode Pembayaran</label>
                                        <select class="form-control" id="expensePaymentMethod" v-model="currentExpense.payment_method" required>
                                            <option value="">Pilih Metode Pembayaran</option>
                                            <option v-for="method in paymentMethods" :key="method" :value="method">
                                                {{ method }}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="expenseDescription">Deskripsi</label>
                                <textarea class="form-control" id="expenseDescription" rows="3" v-model="currentExpense.description" required></textarea>
                            </div>
                            <div class="form-group">
                                <label for="expenseNote">Catatan Tambahan</label>
                                <textarea class="form-control" id="expenseNote" rows="2" v-model="currentExpense.note"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                                <button type="submit" class="btn btn-primary" :disabled="loading">
                                    <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    Simpan
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- View Expense Modal -->
        <div class="modal fade" id="viewExpenseModal" tabindex="-1" role="dialog" aria-labelledby="viewExpenseModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewExpenseModalLabel">Detail Pengeluaran</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="currentExpense">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>ID:</strong> {{ currentExpense.id }}</p>
                                <p><strong>Tanggal:</strong> {{ currentExpense.date }}</p>
                                <p><strong>Tipe:</strong> <span :class="getCategoryClass(currentExpense.type)">{{ currentExpense.type }}</span></p>
                                <p><strong>Deskripsi:</strong> {{ currentExpense.description }}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Jumlah:</strong> Rp {{ currentExpense.amount ? currentExpense.amount.toLocaleString() : 0 }}</p>
                                <p><strong>Metode Pembayaran:</strong> {{ currentExpense.payment_method }}</p>
                                <p><strong>Catatan Tambahan:</strong> {{ currentExpense.note || 'Tidak ada catatan tambahan' }}</p>
                                <p><strong>Dibuat:</strong> {{ currentExpense.created || 'N/A' }}</p>
                                <p><strong>Diperbarui:</strong> {{ currentExpense.updated || 'N/A' }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Tutup</button>
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
                        Apakah Anda yakin ingin menghapus catatan pengeluaran ini? Tindakan ini tidak dapat dibatalkan.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                        <button type="button" class="btn btn-danger" @click="confirmDelete" :disabled="loading">
                            <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                            Hapus
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
            categoryFilter: '',
            monthFilter: '',
            isEditing: false,
            searchTimeout: null,
            currentExpense: {
                id: null,
                date: '',
                description: '',
                amount: 0,
                type: '',
                payment_method: '',
                note: ''
            },
            expenseToDelete: null,
            expenses: [],
            expenseTypes: [
                'MATERIAL',
                'SALARY',
                'OPERATIONAL',
                'REFUND',
                'OTHER'
            ],
            paymentMethods: [
                'CASH',
                'CREDIT_CARD',
                'TRANSFER',
                'BANK_TRANSFER',
                'DEBIT_CARD',
                'OTHERS'
            ],
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
        filteredExpenses() {
            // Since filtering is now done on the server side, 
            // we simply return the expenses array
            return this.expenses;
        },
        totalAmount() {
            return this.filteredExpenses.reduce((total, expense) => total + expense.amount, 0);
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
        this.fetchExpenses(0); // Start with page 0 for backend (equivalent to UI page 1)
    },
    watch: {
        searchQuery: function(newVal) {
            // Debounce search to avoid too many requests
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            this.searchTimeout = setTimeout(() => {
                // Reset to first page and fetch expenses with new search query
                this.changePage(1);
            }, 300); // 300ms debounce
        },
        categoryFilter: function() {
            // Reset to first page and fetch expenses with new category filter
            this.changePage(1);
        },
        monthFilter: function() {
            // Reset to first page and fetch expenses with new month filter
            this.changePage(1);
        }
    },
    methods: {
        getCategoryClass(type) {
            switch(type) {
                case 'MATERIAL': return 'badge badge-primary';
                case 'SALARY': return 'badge badge-info';
                case 'OPERATIONAL': return 'badge badge-warning';
                case 'REFUND': return 'badge badge-secondary';
                case 'OTHER': return 'badge badge-dark';
                default: return 'badge badge-light';
            }
        },
        fetchExpenses(page = 0) {
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
                // Add filter for description containing search query
                filters.push(["description", "like", this.searchQuery]);
            }

            // Add category filter if present
            if (this.categoryFilter) {
                // If we already have filters, add AND operator
                if (filters.length > 0) {
                    filters.push(["and"]);
                }
                // Add filter for type equals categoryFilter
                filters.push(["type", "=", this.categoryFilter]);
            }

            // Add month filter if present
            if (this.monthFilter) {
                // If we already have filters, add AND operator
                if (filters.length > 0) {
                    filters.push(["and"]);
                }
                // Add filter for date containing month
                filters.push(["date", "like", `-${this.monthFilter}-`]);
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
            this.Api.get(`/expense?${queryParams.toString()}`)
                .then(response => {
                    // Check if response has content property (Spring Data pagination)
                    if (response.data && response.data.content) {
                        this.expenses = response.data.content;

                        // Update pagination data
                        this.pagination = {
                            totalPages: response.data.total_pages || 0,
                            totalElements: response.data.total_elements || 0,
                            pageNumber: response.data.number !== undefined ? response.data.number : page,
                            pageSize: response.data.size || this.pagination.pageSize
                        };
                    } else {
                        // Handle case where response is not paginated
                        this.expenses = Array.isArray(response.data) ? response.data : [];
                        this.pagination.totalElements = this.expenses.length;
                        this.pagination.totalPages = 1;
                    }

                    this.loading = false;
                })
                .catch(error => {
                    console.error('Error fetching expenses:', error);

                    // Provide more specific error message if available
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        this.error = `Error ${error.response.status}: ${error.response.data.message || 'Gagal memuat pengeluaran'}`;
                    } else if (error.request) {
                        // The request was made but no response was received
                        this.error = 'No response from server. Please check your connection.';
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        this.error = 'Failed to load expenses: ' + error.message;
                    }

                    this.loading = false;
                });
        },
        changePage(page) {
            // page parameter is 1-based from UI, convert to 0-based for backend
            const zeroBasedPage = page - 1;
            if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
            this.pagination.pageNumber = zeroBasedPage;
            this.fetchExpenses(zeroBasedPage);
        },
        showAddModal() {
            this.isEditing = false;
            this.currentExpense = {
                id: null,
                date: new Date().toISOString().split('T')[0],
                description: '',
                amount: 0,
                type: '',
                payment_method: 'CASH',
                note: ''
            };
            $('#expenseModal').modal('show');
        },
        viewExpense(expense) {
            this.currentExpense = { ...expense };
            $('#viewExpenseModal').modal('show');
        },
        editExpense(expense) {
            this.isEditing = true;
            this.currentExpense = JSON.parse(JSON.stringify(expense));
            $('#expenseModal').modal('show');
        },
        deleteExpense(expense) {
            this.expenseToDelete = expense;
            $('#deleteModal').modal('show');
        },
        confirmDelete() {
            if (this.expenseToDelete) {
                this.loading = true;
                this.Api.delete(`/expense/${this.expenseToDelete.id}`)
                    .then(() => {
                        const index = this.expenses.findIndex(e => e.id === this.expenseToDelete.id);
                        if (index !== -1) {
                            this.expenses.splice(index, 1);
                        }
                        this.expenseToDelete = null;
                        $('#deleteModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error deleting expense:', error);
                        this.error = 'Failed to delete expense';
                        this.loading = false;
                    });
            }
        },
        saveExpense() {
            this.loading = true;

            if (this.isEditing) {
                // Update existing expense
                this.Api.put(`/expense/${this.currentExpense.id}`, this.currentExpense)
                    .then(response => {
                        const index = this.expenses.findIndex(e => e.id === this.currentExpense.id);
                        if (index !== -1) {
                            this.expenses.splice(index, 1, response.data);
                        }
                        $('#expenseModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error updating expense:', error);
                        this.error = 'Failed to update expense';
                        this.loading = false;
                    });
            } else {
                // Add new expense
                this.Api.post('/expense', this.currentExpense)
                    .then(response => {
                        this.expenses.push(response.data);
                        $('#expenseModal').modal('hide');
                        this.loading = false;
                    })
                    .catch(error => {
                        console.error('Error adding expense:', error);
                        this.error = 'Failed to add expense';
                        this.loading = false;
                    });
            }
        },
        handleSearch() {
            // Clear any existing timeout
            if (this.searchTimeout) {
                clearTimeout(this.searchTimeout);
            }

            // Reset to first page and fetch expenses with current search query
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
