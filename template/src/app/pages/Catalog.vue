<template>
  <div class="catalog-container">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>Selamat Datang di Kanina House Uniform</h1>
        <p>Temukan produk berkualitas tinggi kami</p>
      </div>
    </section>

    <!-- Main Content -->
    <div class="container mt-5">
      <div class="row">
        <!-- Sidebar with Categories -->
        <div class="col-md-3">
          <div class="card category-card">
            <div class="card-header bg-primary text-white">
              <h4 class="mb-0">Kategori</h4>
            </div>
            <div class="card-body">
              <div class="list-group">
                <a href="#" class="list-group-item list-group-item-action" 
                   :class="{ active: selectedCategory === null }"
                   @click.prevent="selectCategory(null)">
                  Semua Kategori
                </a>
                <a href="#" class="list-group-item list-group-item-action" 
                   v-for="category in categories" 
                   :key="category.id"
                   :class="{ active: selectedCategory === category.id }"
                   @click.prevent="selectCategory(category.id)">
                  {{ category.name }}
                </a>
              </div>
            </div>
          </div>

          <!-- Contact Card -->
          <div class="card mt-4 contact-card">
            <div class="card-header bg-success text-white">
              <h4 class="mb-0">Hubungi Kami</h4>
            </div>
            <div class="card-body">
              <p><i class="fas fa-envelope"></i> <a :href="'mailto:' + contact.email">{{ contact.email }}</a></p>
              <p><i class="fas fa-phone"></i> <a :href="'tel:' + contact.phone">{{ contact.phone }}</a></p>
              <p><i class="fab fa-whatsapp"></i> <a :href="'https://wa.me/' + contact.whatsapp.replace(/\+/g, '')" target="_blank">WhatsApp</a></p>
              <p><i class="fas fa-map-marker-alt"></i> {{ contact.address }}</p>
            </div>
          </div>
        </div>

        <!-- Products Grid -->
        <div class="col-md-9">
          <!-- Search and Filter Section -->
          <div class="search-filter-container mb-4">
            <div class="search-box">
              <div class="input-group">
                <div class="input-group-prepend">
                  <span class="input-group-text">
                    <i class="fas fa-search"></i>
                  </span>
                </div>
                <input 
                  type="text" 
                  class="form-control search-input" 
                  placeholder="Cari produk..." 
                  v-model="searchQuery" 
                  @keyup.enter="loadProducts"
                >
                <div class="input-group-append" v-if="searchQuery">
                  <button class="btn btn-outline-secondary clear-search" type="button" @click="clearSearch">
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="filter-buttons">
              <button 
                class="btn filter-btn" 
                :class="{'btn-primary': selectedCategory === null, 'btn-outline-primary': selectedCategory !== null}"
                @click="selectCategory(null)"
              >
                Semua
              </button>
              <button 
                v-for="category in categories.slice(0, 3)" 
                :key="category.id"
                class="btn filter-btn" 
                :class="{'btn-primary': selectedCategory === category.id, 'btn-outline-primary': selectedCategory !== category.id}"
                @click="selectCategory(category.id)"
              >
                {{ category.name }}
              </button>
              <div class="dropdown d-inline-block" v-if="categories.length > 3">
                <button class="btn btn-outline-primary filter-btn dropdown-toggle" type="button" id="moreCategories" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  Lainnya
                </button>
                <div class="dropdown-menu" aria-labelledby="moreCategories">
                  <a 
                    v-for="category in categories.slice(3)" 
                    :key="category.id"
                    class="dropdown-item" 
                    href="#"
                    :class="{'active': selectedCategory === category.id}"
                    @click.prevent="selectCategory(category.id)"
                  >
                    {{ category.name }}
                  </a>
                </div>
              </div>
            </div>
          </div>

          <!-- Loading Indicator -->
          <div v-if="loading" class="text-center my-5">
            <div class="spinner-border text-primary" role="status">
              <span class="sr-only">Memuat...</span>
            </div>
          </div>

          <!-- Error Message -->
          <div v-else-if="error" class="alert alert-danger">
            {{ error }}
          </div>

          <!-- No Products Message -->
          <div v-else-if="products.length === 0" class="no-products-container">
            <div class="no-products-message">
              <i class="fas fa-box-open fa-3x mb-3"></i>
              <h3>Belum ada produk di katalog</h3>
              <p>Coba gunakan kata kunci pencarian lain atau pilih kategori yang berbeda.</p>
            </div>
          </div>

          <!-- Products Grid -->
          <div v-else class="product-grid">
            <div class="product-card-wrapper" v-for="product in products" :key="product.id">
              <div class="card product-card h-100">
                <div class="product-image-container">
                  <img :src="product.image_url || require('@/assets/no-image.png')" class="product-image" alt="Product Image">
                </div>
                <div class="card-body">
                  <h4 class="product-name">{{ product.name }}</h4>
                  <p class="product-description">{{ product.description }}</p>
                  <p class="product-size"><small>Size: {{ product.size }}</small></p>
                  <p class="product-price">Rp {{ formatPrice(product.selling_price) }}</p>
                  <p class="product-stock" v-if="product.stock !== undefined">
                    <small>Stok: {{ product.stock > 0 ? product.stock : 'Habis' }}</small>
                  </p>
                </div>
                <div class="card-footer bg-white">
                  <button class="btn btn-success btn-block" @click="contactAboutProduct(product)">
                    <i class="fab fa-whatsapp"></i> Hubungi untuk Pemesanan
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Pagination -->
          <nav v-if="pagination.totalPages > 1" aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: pagination.pageNumber <= 0 }">
                <a class="page-link" href="#" @click.prevent="changePage(pagination.pageNumber)">Sebelumnya</a>
              </li>
              <li v-for="page in pagesArray" :key="page" class="page-item" :class="{ active: page === pagination.pageNumber + 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
              </li>
              <li class="page-item" :class="{ disabled: pagination.pageNumber >= pagination.totalPages - 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(pagination.pageNumber + 2)">Selanjutnya</a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Catalog',
  data() {
    return {
      products: [],
      categories: [],
      contact: {
        email: '',
        phone: '',
        whatsapp: '',
        address: ''
      },
      selectedCategory: null,
      searchQuery: '',
      loading: false,
      error: null,
      pagination: {
        pageNumber: 0,
        pageSize: 12,
        totalPages: 0,
        totalElements: 0
      }
    };
  },
  computed: {
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
  created() {
    this.loadCategories();
    this.loadProducts();
    this.loadContactInfo();
  },
  methods: {
    async loadProducts() {
      this.loading = true;
      this.error = null;

      try {
        // Convert 0-based page to 1-based page for API
        let url = `/api/v1/public/products?page=${this.pagination.pageNumber + 1}&size=${this.pagination.pageSize}`;

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
        if (this.selectedCategory) {
          // If we already have filters, add AND operator
          if (filters.length > 0) {
            filters.push(["and"]);
          }

          // Add filter for category.id equals selectedCategory
          filters.push(["category.id", "=", this.selectedCategory]);
        }

        // Add filters to URL if any
        if (filters.length > 0) {
          url += `&filters=${encodeURIComponent(JSON.stringify(filters))}`;
        }

        const response = await axios.get(url);
        this.products = response.data.content;
        this.pagination = {
          pageNumber: response.data.pageable.page_number,
          pageSize: response.data.pageable.page_size,
          totalPages: response.data.total_pages,
          totalElements: response.data.total_elements
        };
      } catch (err) {
        console.error('Error loading products:', err);
        this.error = 'Gagal memuat produk. Silakan coba lagi nanti.';
      } finally {
        this.loading = false;
      }
    },

    async loadCategories() {
      try {
        const response = await axios.get('/api/v1/public/categories');
        this.categories = response.data.content;
      } catch (err) {
        console.error('Error loading categories:', err);
      }
    },

    async loadContactInfo() {
      try {
        const response = await axios.get('/api/v1/public/contact');
        this.contact = response.data;
      } catch (err) {
        console.error('Error loading contact info:', err);
      }
    },

    selectCategory(categoryId) {
      this.selectedCategory = categoryId;
      this.pagination.pageNumber = 0;
      this.loadProducts();
    },

    changePage(page) {
      this.pagination.pageNumber = page - 1;
      this.loadProducts();
    },

    formatPrice(price) {
      return price.toLocaleString('id-ID');
    },

    clearSearch() {
      this.searchQuery = '';
      this.pagination.pageNumber = 0;
      this.loadProducts();
    },

    contactAboutProduct(product) {
      const message = `Halo, saya tertarik dengan produk: ${product.name} (ID: ${product.id}). Bisakah Anda memberikan informasi lebih lanjut?`;
      const whatsappUrl = `https://wa.me/${this.contact.whatsapp.replace(/\+/g, '')}?text=${encodeURIComponent(message)}`;
      window.open(whatsappUrl, '_blank');
    }
  }
};
</script>

<style scoped>
/* Base Styles */
.catalog-container {
  min-height: 100vh;
  background-color: #f8f9fa;
  font-family: 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* Hero Section */
.hero-section {
  background: linear-gradient(135deg, #007bff, #28a745);
  color: white;
  text-align: center;
  padding: 80px 0;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.hero-content h1 {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.hero-content p {
  font-size: 1.5rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

/* Sidebar Cards */
.category-card, .contact-card {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: none;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 20px;
  transition: box-shadow 0.3s ease;
}

.category-card:hover, .contact-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.12);
}

.list-group-item.active {
  background-color: #007bff;
  border-color: #007bff;
}

.list-group-item {
  border-left: none;
  border-right: none;
  padding: 12px 20px;
  transition: background-color 0.2s ease;
}

.list-group-item:hover:not(.active) {
  background-color: #f0f7ff;
}

/* Product Grid */
.product-grid {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

/* Product Cards */
.product-card-wrapper {
  display: flex;
}

.product-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s, box-shadow 0.3s;
  border: none;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background-color: white;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}

.product-image-container {
  height: 220px;
  overflow: hidden;
  position: relative;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.card-body {
  padding: 20px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 10px;
  color: #333;
  line-height: 1.3;
}

.product-description {
  color: #666;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  font-size: 0.95rem;
  line-height: 1.5;
}

.product-size {
  color: #777;
  margin-bottom: 8px;
  font-size: 0.9rem;
}

.product-price {
  font-weight: 700;
  color: #28a745;
  font-size: 1.4rem;
  margin-top: auto;
  margin-bottom: 8px;
}

.product-stock {
  color: #666;
  font-size: 0.85rem;
  margin-bottom: 0;
}

.card-footer {
  padding: 15px 20px;
  background-color: white;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.btn-success {
  padding: 10px 15px;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.btn-success:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(40, 167, 69, 0.3);
}

/* No Products Message */
.no-products-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  padding: 30px;
}

.no-products-message {
  text-align: center;
  background-color: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  max-width: 500px;
  width: 100%;
}

.no-products-message i {
  color: #6c757d;
  margin-bottom: 20px;
}

.no-products-message h3 {
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}

.no-products-message p {
  color: #666;
}

/* Search and Filter */
.search-filter-container {
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 25px;
}

.search-box {
  margin-bottom: 15px;
}

.search-input {
  border-radius: 0 4px 4px 0;
  height: 45px;
  font-size: 1rem;
}

.input-group-text {
  background-color: #f8f9fa;
  border-right: none;
  color: #6c757d;
}

.clear-search {
  border-color: #ced4da;
  color: #6c757d;
  border-left: none;
}

.clear-search:hover {
  background-color: #f8f9fa;
  color: #dc3545;
}

.filter-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.filter-btn {
  border-radius: 20px;
  padding: 6px 15px;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.filter-btn:hover {
  transform: translateY(-2px);
}

.dropdown-menu {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border: none;
  padding: 8px 0;
}

.dropdown-item {
  padding: 8px 20px;
  transition: background-color 0.2s ease;
}

.dropdown-item.active, 
.dropdown-item:active {
  background-color: #007bff;
  color: white;
}

/* Pagination */
.pagination {
  margin-top: 30px;
}

.page-link {
  border-radius: 6px;
  margin: 0 3px;
  color: #007bff;
  border: 1px solid rgba(0, 123, 255, 0.2);
  transition: all 0.2s ease;
}

.page-link:hover {
  background-color: #f0f7ff;
  border-color: rgba(0, 123, 255, 0.5);
  z-index: 1;
}

.page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
  box-shadow: 0 2px 5px rgba(0, 123, 255, 0.3);
}

/* Responsive Styles */
@media (min-width: 576px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 992px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (min-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2rem;
  }

  .hero-content p {
    font-size: 1.2rem;
  }

  .product-image-container {
    height: 180px;
  }

  .card-body {
    padding: 15px;
  }

  .product-name {
    font-size: 1.1rem;
  }

  .product-price {
    font-size: 1.2rem;
  }

  .search-filter-container {
    padding: 15px;
  }

  .filter-buttons {
    gap: 8px;
  }

  .filter-btn {
    padding: 5px 12px;
    font-size: 0.85rem;
  }

  .search-input {
    height: 40px;
  }

  .no-products-message {
    padding: 25px 15px;
  }

  .no-products-message h3 {
    font-size: 1.3rem;
  }
}
</style>
