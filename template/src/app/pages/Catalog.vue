<template>
  <div class="catalog-container">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>Welcome to Kanina House Uniform</h1>
        <p>Discover our high-quality products</p>
      </div>
    </section>

    <!-- Main Content -->
    <div class="container mt-5">
      <div class="row">
        <!-- Sidebar with Categories -->
        <div class="col-md-3">
          <div class="card category-card">
            <div class="card-header bg-primary text-white">
              <h4 class="mb-0">Categories</h4>
            </div>
            <div class="card-body">
              <div class="list-group">
                <a href="#" class="list-group-item list-group-item-action" 
                   :class="{ active: selectedCategory === null }"
                   @click.prevent="selectCategory(null)">
                  All Categories
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
              <h4 class="mb-0">Contact Us</h4>
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
          <!-- Search Bar -->
          <div class="input-group mb-4">
            <input type="text" class="form-control" placeholder="Search products..." 
                   v-model="searchQuery" 
                   @keyup.enter="loadProducts">
            <div class="input-group-append">
              <button class="btn btn-primary" type="button" @click="loadProducts">
                <i class="fas fa-search"></i> Search
              </button>
            </div>
          </div>

          <!-- Loading Indicator -->
          <div v-if="loading" class="text-center my-5">
            <div class="spinner-border text-primary" role="status">
              <span class="sr-only">Loading...</span>
            </div>
          </div>

          <!-- Error Message -->
          <div v-else-if="error" class="alert alert-danger">
            {{ error }}
          </div>

          <!-- No Products Message -->
          <div v-else-if="products.length === 0" class="alert alert-info">
            No products found. Try a different search or category.
          </div>

          <!-- Products Grid -->
          <div v-else class="row">
            <div class="col-md-4 mb-4" v-for="product in products" :key="product.id">
              <div class="card product-card h-100">
                <img :src="product.image_url || require('@/assets/no-image.png')" class="card-img-top product-image" alt="Product Image">
                <div class="card-body">
                  <h5 class="card-title">{{ product.name }}</h5>
                  <p class="card-text description">{{ product.description }}</p>
                  <p class="card-text"><small class="text-muted">Size: {{ product.size }}</small></p>
                  <p class="card-text price">Rp {{ formatPrice(product.selling_price) }}</p>
                </div>
                <div class="card-footer bg-white">
                  <button class="btn btn-success btn-block" @click="contactAboutProduct(product)">
                    <i class="fab fa-whatsapp"></i> Contact for Order
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Pagination -->
          <nav v-if="pagination.totalPages > 1" aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: pagination.pageNumber <= 0 }">
                <a class="page-link" href="#" @click.prevent="changePage(pagination.pageNumber)">Previous</a>
              </li>
              <li v-for="page in pagesArray" :key="page" class="page-item" :class="{ active: page === pagination.pageNumber + 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
              </li>
              <li class="page-item" :class="{ disabled: pagination.pageNumber >= pagination.totalPages - 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(pagination.pageNumber + 2)">Next</a>
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
        pageSize: 9,
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

        if (this.searchQuery) {
          url += `&name=${encodeURIComponent(this.searchQuery)}`;
        }

        if (this.selectedCategory) {
          url += `&category.id=${this.selectedCategory}`;
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
        this.error = 'Failed to load products. Please try again later.';
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

    contactAboutProduct(product) {
      const message = `Hello, I'm interested in the product: ${product.name} (ID: ${product.id}). Can you provide more information?`;
      const whatsappUrl = `https://wa.me/${this.contact.whatsapp.replace(/\+/g, '')}?text=${encodeURIComponent(message)}`;
      window.open(whatsappUrl, '_blank');
    }
  }
};
</script>

<style scoped>
.catalog-container {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.hero-section {
  background: linear-gradient(135deg, #007bff, #28a745);
  color: white;
  text-align: center;
  padding: 100px 0;
  margin-bottom: 30px;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-content h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.hero-content p {
  font-size: 1.5rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
}

.category-card, .contact-card {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: none;
  border-radius: 8px;
  overflow: hidden;
}

.product-card {
  transition: transform 0.3s, box-shadow 0.3s;
  border: none;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  object-fit: cover;
}

.description {
  height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.price {
  font-weight: bold;
  color: #28a745;
  font-size: 1.2rem;
}

.list-group-item.active {
  background-color: #007bff;
  border-color: #007bff;
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2rem;
  }

  .hero-content p {
    font-size: 1.2rem;
  }
}
</style>
