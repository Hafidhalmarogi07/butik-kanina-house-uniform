<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h3 class="card-title">Daftar Pengembalian</h3>
            <div class="card-tools">
              <button type="button" class="btn btn-primary" @click="showNewReturnModal">
                <i class="fas fa-plus"></i> Buat Pengembalian
              </button>
            </div>
          </div>
          <div class="card-body">
            <div class="row mb-3">
              <div class="col-md-3">
                <div class="input-group">
                  <input type="text" class="form-control" placeholder="Cari pengembalian..." v-model="searchQuery">
                  <div class="input-group-append">
                    <button class="btn btn-default" type="button">
                      <i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>
              <div class="col-md-3">
                <select class="form-control" v-model="statusFilter">
                  <option value="">Semua Status</option>
                  <option value="PENDING">Tertunda</option>
                  <option value="APPROVED">Disetujui</option>
                  <option value="REJECTED">Ditolak</option>
                  <option value="COMPLETED">Selesai</option>
                </select>
              </div>
            </div>
            <div class="table-responsive">
              <table class="table table-bordered table-striped">
                <thead>
                <tr>
                  <th>Pesanan #</th>
                  <th>Tanggal Pengembalian</th>
                  <th>Alasan</th>
                  <th>Barang</th>
                  <th>Total Pengembalian Dana</th>
                  <th>Jenis Pengembalian</th>
                  <th>Status</th>
                  <th>Tindakan</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="returnOrder in filteredReturns" :key="returnOrder.id">
                  <td>{{ returnOrder.order ? returnOrder.order.order_number : '' }}</td>
                  <td>{{ formatDate(returnOrder.return_date) }}</td>
                  <td>{{ returnOrder.reason }}</td>
                  <td>{{ returnOrder.details ? returnOrder.details.length : 0 }}</td>
                  <td>{{ formatCurrency(returnOrder.total_refund) }}</td>
                  <td>{{ returnOrder.return_type }}</td>
                  <td>
                    <span :class="getStatusClass(returnOrder.status_return)">{{ returnOrder.status_return }}</span>
                  </td>
                  <td>
                    <div class="btn-group">
                      <button type="button" class="btn btn-sm btn-info" @click="viewReturnDetails(returnOrder)">
                        <i class="fas fa-eye"></i>
                      </button>
                      <button type="button" class="btn btn-sm btn-warning" @click="showUpdateStatusModal(returnOrder)">
                        <i class="fas fa-edit"></i>
                      </button>
                      <button type="button" class="btn btn-sm btn-danger" @click="showDeleteReturnModal(returnOrder)">
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
                <span class="sr-only">Loading...</span>
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
              <p>Tidak ada pengembalian ditemukan</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- New Return Modal -->
    <div class="modal fade" id="newReturnModal" tabindex="-1" role="dialog" aria-labelledby="newReturnModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="newReturnModalLabel">Pengembalian Baru</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveReturn">
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label for="orderNumber">Nomor Pesanan</label>
                    <input type="text" class="form-control" id="orderNumber" v-model="currentReturn.order_number" required>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label for="returnType">Jenis Pengembalian</label>
                    <select class="form-control" id="returnType" v-model="currentReturn.return_type" required>
                      <option value="REFUND">Pengembalian Dana</option>
                      <option value="EXCHANGE">Penukaran</option>
                    </select>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label for="reason">Alasan</label>
                    <textarea class="form-control" id="reason" rows="3" v-model="currentReturn.reason" required></textarea>
                  </div>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-12">
                  <h5>Barang</h5>
                  <div class="table-responsive">
                    <table class="table table-bordered">
                      <thead>
                      <tr>
                        <th>Produk</th>
                        <th>Jumlah</th>
                        <th>Alasan</th>
                        <th>Tindakan</th>
                      </tr>
                      </thead>
                      <tbody>
                      <tr v-for="(item, index) in currentReturn.items" :key="index">
                        <td>
                          <select class="form-control" v-model="item.product_id" required>
                            <option value="">Pilih Produk</option>
                            <option v-for="product in products" :key="product.id" :value="product.id">
                              {{ product.name }} ({{ product.size }}) - {{ formatCurrency(product.selling_price) }}
                            </option>
                          </select>
                        </td>
                        <td>
                          <input type="number" class="form-control" v-model="item.quantity" min="1" required>
                        </td>
                        <td>
                          <input type="text" class="form-control" v-model="item.reason" required>
                        </td>
                        <td>
                          <button type="button" class="btn btn-danger btn-sm" @click="removeItem(index)">
                            <i class="fas fa-trash"></i>
                          </button>
                        </td>
                      </tr>
                      </tbody>
                      <tfoot>
                      <tr>
                        <td colspan="4">
                          <button type="button" class="btn btn-primary btn-sm" @click="addItem">
                            <i class="fas fa-plus"></i> Tambah Barang
                          </button>
                        </td>
                      </tr>
                      </tfoot>
                    </table>
                  </div>
                </div>
              </div>

              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                <button type="submit" class="btn btn-primary" :disabled="loading">
                  <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                  Buat Pengembalian
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- Return Details Modal -->
    <div class="modal fade" id="returnDetailsModal" tabindex="-1" role="dialog" aria-labelledby="returnDetailsModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="returnDetailsModalLabel">Detail Pengembalian</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body" v-if="selectedReturn">
            <div class="row mb-3">
              <div class="col-md-6">
                <p><strong>Pesanan #:</strong> {{ selectedReturn.order ? selectedReturn.order.order_number : '' }}</p>
                <p><strong>Tanggal Pengembalian:</strong> {{ formatDate(selectedReturn.return_date) }}</p>
                <p><strong>Jenis Pengembalian:</strong> {{ selectedReturn.return_type }}</p>
                <p><strong>Status:</strong> <span :class="getStatusClass(selectedReturn.status_return)">{{ selectedReturn.status_return }}</span></p>
              </div>
              <div class="col-md-6">
                <p><strong>Admin:</strong> {{ selectedReturn.admin ? selectedReturn.admin.full_name : '' }}</p>
                <p><strong>Alasan:</strong> {{ selectedReturn.reason }}</p>
                <p><strong>Total Pengembalian Dana:</strong> {{ formatCurrency(selectedReturn.total_refund) }}</p>
              </div>
            </div>

            <h6>Barang</h6>
            <div class="table-responsive">
              <table class="table table-bordered">
                <thead>
                <tr>
                  <th>Produk</th>
                  <th>Ukuran</th>
                  <th>Harga</th>
                  <th>Jumlah</th>
                  <th>Catatan</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(item, index) in selectedReturn.details" :key="index">
                  <td>{{ item.product ? item.product.name : '' }}</td>
                  <td>{{ item.product ? item.product.size : '' }}</td>
                  <td>{{ item.product ? formatCurrency(item.product.selling_price) : '' }}</td>
                  <td>{{ item.quantity }}</td>
                  <td>{{ item.note }}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Tutup</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Update Status Modal -->
    <div class="modal fade" id="updateStatusModal" tabindex="-1" role="dialog" aria-labelledby="updateStatusModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="updateStatusModalLabel">Perbarui Status Pengembalian</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="updateReturnStatus">
              <div class="form-group">
                <label for="returnStatus">Status</label>
                <select class="form-control" id="returnStatus" v-model="newStatus" required>
                  <option value="PENDING">Tertunda</option>
                  <option value="APPROVED">Disetujui</option>
                  <option value="REJECTED">Ditolak</option>
                  <option value="COMPLETED">Selesai</option>
                </select>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                <button type="submit" class="btn btn-primary" :disabled="loading">
                  <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                  Perbarui Status
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- Delete Return Confirmation Modal -->
    <div class="modal fade" id="deleteReturnModal" tabindex="-1" role="dialog" aria-labelledby="deleteReturnModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="deleteReturnModalLabel">Konfirmasi Hapus</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>Apakah Anda yakin ingin menghapus pengembalian untuk pesanan #{{ returnToDelete && returnToDelete.order ? returnToDelete.order.order_number : '' }}?</p>
            <p class="text-danger">Tindakan ini tidak dapat dibatalkan.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
            <button type="button" class="btn btn-danger" @click="deleteReturn" :disabled="loading">
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
      statusFilter: '',
      selectedReturn: null,
      currentReturn: {
        order_number: '',
        reason: '',
        return_type: 'REFUND',
        items: [{
          product_id: null,
          quantity: 1,
          reason: ''
        }]
      },
      returnToUpdateStatus: null,
      newStatus: '',
      returnToDelete: null,
      returns: [],
      products: [],
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
    filteredReturns() {
      // Since filtering is now done on the server side,
      // we simply return the returns array
      return this.returns;
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
    // Fetch initial data
    this.fetchReturns(0);
    this.fetchProducts();
  },
  watch: {
    searchQuery: function(newVal) {
      // Debounce search to avoid too many requests
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(() => {
        // Reset to first page and fetch returns with new search query
        this.changePage(1);
      }, 300); // 300ms debounce
    },
    statusFilter: function() {
      // Reset to first page and fetch returns with new status filter
      this.changePage(1);
    }
  },
  methods: {
    fetchReturns(page = 0) {
      this.loading = true;

      // Prepare params object with pagination
      const params = {
        sort: '-id',
        page: page,
        size: this.pagination.pageSize
      };

      // Prepare filters array for backend
      const filters = [];

      // Add search query if present
      if (this.searchQuery) {
        // Add filter for order.order_number containing search query
        filters.push(["order.order_number", "like", this.searchQuery]);
      }

      // Add status filter if present
      if (this.statusFilter) {
        // If we already have filters, add AND operator
        if (filters.length > 0) {
          filters.push(["and"]);
        }

        // Add filter for status equals statusFilter
        filters.push(["status_return", "=", this.statusFilter]);
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
      this.Api.get(`/order-returns?${queryParams.toString()}`)
          .then(response => {
            // Check if response has content property (Spring Data pagination)
            if (response.data && response.data.content) {
              this.returns = response.data.content;

              // Update pagination data
              this.pagination = {
                totalPages: response.data.total_pages || 0,
                totalElements: response.data.total_elements || 0,
                pageNumber: response.data.number !== undefined ? response.data.number : page,
                pageSize: response.data.size || this.pagination.pageSize
              };
            } else {
              // Handle case where response is not paginated
              this.returns = Array.isArray(response.data) ? response.data : [];
              this.pagination.totalElements = this.returns.length;
              this.pagination.totalPages = 1;
            }

            this.loading = false;
          })
          .catch(error => {
            console.error('Error fetching returns:', error);

            // Provide more specific error message if available
            if (error.response) {
              // The request was made and the server responded with a status code
              // that falls out of the range of 2xx
              this.error = `Error ${error.response.status}: ${error.response.data.message || 'Gagal memuat pengembalian'}`;
            } else if (error.request) {
              // The request was made but no response was received
              this.error = 'Tidak ada respons dari server. Silakan periksa koneksi Anda.';
            } else {
              // Something happened in setting up the request that triggered an Error
              this.error = 'Gagal memuat pengembalian: ' + error.message;
            }

            this.loading = false;
          });
    },

    fetchProducts() {
      this.Api.get('/product?size=1000')
          .then(response => {
            this.products = response.data.content || response.data;
          })
          .catch(error => {
            console.error('Error fetching products:', error);
          });
    },

    changePage(page) {
      // page parameter is 1-based from UI, convert to 0-based for backend
      const zeroBasedPage = page - 1;
      if (zeroBasedPage < 0 || (this.pagination.totalPages > 0 && zeroBasedPage >= this.pagination.totalPages)) return;
      this.pagination.pageNumber = zeroBasedPage;
      this.fetchReturns(zeroBasedPage);
    },

    formatDate(dateString) {
      if (!dateString) return '';
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },

    formatCurrency(value) {
      if (value === null || value === undefined) return 'Rp 0';
      return 'Rp ' + value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

    getStatusClass(status) {
      switch(status) {
        case 'PENDING':
          return 'badge badge-warning';
        case 'APPROVED':
          return 'badge badge-info';
        case 'REJECTED':
          return 'badge badge-danger';
        case 'COMPLETED':
          return 'badge badge-success';
        default:
          return 'badge badge-secondary';
      }
    },

    showNewReturnModal() {
      this.currentReturn = {
        order_number: '',
        reason: '',
        return_type: 'REFUND',
        items: [{
          product_id: null,
          quantity: 1,
          reason: ''
        }]
      };
      $('#newReturnModal').modal('show');
    },

    viewReturnDetails(returnOrder) {
      this.selectedReturn = returnOrder;
      $('#returnDetailsModal').modal('show');
    },

    addItem() {
      this.currentReturn.items.push({
        product_id: null,
        quantity: 1,
        reason: ''
      });
    },

    removeItem(index) {
      this.currentReturn.items.splice(index, 1);
      if (this.currentReturn.items.length === 0) {
        this.addItem();
      }
    },

    saveReturn() {
      // Validate form
      if (!this.currentReturn.order_number || !this.currentReturn.reason ||
          this.currentReturn.items.some(item => !item.product_id || !item.reason)) {
        alert('Silakan isi semua kolom yang diperlukan');
        return;
      }

      this.loading = true;

      // Prepare the request payload
      const payload = {
        order_number: this.currentReturn.order_number,
        reason: this.currentReturn.reason,
        return_type: this.currentReturn.return_type,
        items: this.currentReturn.items.map(item => ({
          product_id: item.product_id,
          quantity: item.quantity,
          reason: item.reason
        }))
      };

      // Make the API call to create a new return
      this.Api.post('/order-returns', payload)
          .then(response => {
            // Add the new return to the list
            this.returns.unshift(response.data);
            $('#newReturnModal').modal('hide');
            this.loading = false;

            // Refresh the returns list
            this.fetchReturns(this.pagination.pageNumber);
          })
          .catch(error => {
            console.error('Error creating return:', error);

            // Provide more specific error message if available
            if (error.response) {
              this.error = `Error ${error.response.status}: ${error.response.data.message || 'Gagal membuat pengembalian'}`;
            } else if (error.request) {
              this.error = 'Tidak ada respons dari server. Silakan periksa koneksi Anda.';
            } else {
              this.error = 'Gagal membuat pengembalian: ' + error.message;
            }

            this.loading = false;
          });
    },

    showUpdateStatusModal(returnOrder) {
      this.returnToUpdateStatus = returnOrder;
      this.newStatus = returnOrder.status_return;
      $('#updateStatusModal').modal('show');
    },

    updateReturnStatus() {
      if (!this.returnToUpdateStatus) return;

      this.loading = true;

      // Prepare the status update request
      const statusRequest = {
        status: this.newStatus
      };

      // Make the API call to update the return status
      this.Api.put(`/order-returns/${this.returnToUpdateStatus.id}/status`, statusRequest)
          .then(response => {
            // Update the return in the list
            const index = this.returns.findIndex(r => r.id === this.returnToUpdateStatus.id);
            if (index !== -1) {
              this.returns.splice(index, 1, response.data);
            }
            $('#updateStatusModal').modal('hide');
            this.loading = false;

            // Refresh the returns list
            this.fetchReturns(this.pagination.pageNumber);
          })
          .catch(error => {
            console.error('Error updating return status:', error);

            // Provide more specific error message if available
            if (error.response) {
              this.error = `Error ${error.response.status}: ${error.response.data.message || 'Gagal memperbarui status'}`;
            } else if (error.request) {
              this.error = 'Tidak ada respons dari server. Silakan periksa koneksi Anda.';
            } else {
              this.error = 'Gagal memperbarui status: ' + error.message;
            }

            this.loading = false;
          });
    },

    showDeleteReturnModal(returnOrder) {
      this.returnToDelete = returnOrder;
      $('#deleteReturnModal').modal('show');
    },

    deleteReturn() {
      if (!this.returnToDelete) return;

      this.loading = true;

      // Make the API call to delete the return
      this.Api.delete(`/order-returns/${this.returnToDelete.id}`)
          .then(() => {
            // Remove the return from the list
            const index = this.returns.findIndex(r => r.id === this.returnToDelete.id);
            if (index !== -1) {
              this.returns.splice(index, 1);
            }
            $('#deleteReturnModal').modal('hide');
            this.loading = false;

            // Refresh the returns list
            this.fetchReturns(this.pagination.pageNumber);
          })
          .catch(error => {
            console.error('Error deleting return:', error);

            // Provide more specific error message if available
            if (error.response) {
              this.error = `Error ${error.response.status}: ${error.response.data.message || 'Gagal menghapus pengembalian'}`;
            } else if (error.request) {
              this.error = 'Tidak ada respons dari server. Silakan periksa koneksi Anda.';
            } else {
              this.error = 'Gagal menghapus pengembalian: ' + error.message;
            }

            this.loading = false;
          });
    }
  }
}
</script>
