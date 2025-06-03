<template>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Expenses List</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-primary" @click="showAddModal">
                                <i class="fas fa-plus"></i> Add Expense
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-3">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Search expenses..." v-model="searchQuery">
                                    <div class="input-group-append">
                                        <button class="btn btn-default" type="button">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="categoryFilter">
                                    <option value="">All Categories</option>
                                    <option value="Materials">Materials</option>
                                    <option value="Salaries">Salaries</option>
                                    <option value="Rent">Rent</option>
                                    <option value="Utilities">Utilities</option>
                                    <option value="Equipment">Equipment</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <select class="form-control" v-model="monthFilter">
                                    <option value="">All Months</option>
                                    <option value="01">January</option>
                                    <option value="02">February</option>
                                    <option value="03">March</option>
                                    <option value="04">April</option>
                                    <option value="05">May</option>
                                    <option value="06">June</option>
                                    <option value="07">July</option>
                                    <option value="08">August</option>
                                    <option value="09">September</option>
                                    <option value="10">October</option>
                                    <option value="11">November</option>
                                    <option value="12">December</option>
                                </select>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Date</th>
                                        <th>Category</th>
                                        <th>Description</th>
                                        <th>Amount</th>
                                        <th>Payment Method</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="expense in filteredExpenses" :key="expense.id">
                                        <td>{{ expense.id }}</td>
                                        <td>{{ expense.date }}</td>
                                        <td>
                                            <span :class="getCategoryClass(expense.category)">{{ expense.category }}</span>
                                        </td>
                                        <td>{{ expense.description }}</td>
                                        <td>Rp {{ expense.amount.toLocaleString() }}</td>
                                        <td>{{ expense.paymentMethod }}</td>
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
                        <ul class="pagination pagination-sm m-0 float-right">
                            <li class="page-item"><a class="page-link" href="#">&laquo;</a></li>
                            <li class="page-item"><a class="page-link" href="#">1</a></li>
                            <li class="page-item"><a class="page-link" href="#">2</a></li>
                            <li class="page-item"><a class="page-link" href="#">3</a></li>
                            <li class="page-item"><a class="page-link" href="#">&raquo;</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add/Edit Expense Modal -->
        <div class="modal fade" id="expenseModal" tabindex="-1" role="dialog" aria-labelledby="expenseModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="expenseModalLabel">{{ isEditing ? 'Edit Expense' : 'Add New Expense' }}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveExpense">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="expenseDate">Date</label>
                                        <input type="date" class="form-control" id="expenseDate" v-model="currentExpense.date" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="expenseCategory">Category</label>
                                        <select class="form-control" id="expenseCategory" v-model="currentExpense.category" required>
                                            <option value="Materials">Materials</option>
                                            <option value="Salaries">Salaries</option>
                                            <option value="Rent">Rent</option>
                                            <option value="Utilities">Utilities</option>
                                            <option value="Equipment">Equipment</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="expenseAmount">Amount</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">Rp</span>
                                            </div>
                                            <input type="number" class="form-control" id="expenseAmount" v-model="currentExpense.amount" min="0" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="expensePaymentMethod">Payment Method</label>
                                        <select class="form-control" id="expensePaymentMethod" v-model="currentExpense.paymentMethod" required>
                                            <option value="Cash">Cash</option>
                                            <option value="Bank Transfer">Bank Transfer</option>
                                            <option value="Credit Card">Credit Card</option>
                                            <option value="Debit Card">Debit Card</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="expenseDescription">Description</label>
                                <textarea class="form-control" id="expenseDescription" rows="3" v-model="currentExpense.description" required></textarea>
                            </div>
                            <div class="form-group">
                                <label for="expenseNotes">Additional Notes</label>
                                <textarea class="form-control" id="expenseNotes" rows="2" v-model="currentExpense.notes"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Save</button>
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
                        <h5 class="modal-title" id="viewExpenseModalLabel">Expense Details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" v-if="currentExpense">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>ID:</strong> {{ currentExpense.id }}</p>
                                <p><strong>Date:</strong> {{ currentExpense.date }}</p>
                                <p><strong>Category:</strong> <span :class="getCategoryClass(currentExpense.category)">{{ currentExpense.category }}</span></p>
                                <p><strong>Description:</strong> {{ currentExpense.description }}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Amount:</strong> Rp {{ currentExpense.amount ? currentExpense.amount.toLocaleString() : 0 }}</p>
                                <p><strong>Payment Method:</strong> {{ currentExpense.paymentMethod }}</p>
                                <p><strong>Additional Notes:</strong> {{ currentExpense.notes || 'No additional notes' }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this expense record? This action cannot be undone.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" @click="confirmDelete">Delete</button>
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
            currentExpense: {
                id: null,
                date: '',
                category: '',
                description: '',
                amount: 0,
                paymentMethod: 'Cash',
                notes: ''
            },
            expenseToDelete: null,
            expenses: [
                {
                    id: 'EXP-001',
                    date: '2023-05-05',
                    category: 'Materials',
                    description: 'Fabric purchase for school uniforms',
                    amount: 5000000,
                    paymentMethod: 'Bank Transfer',
                    notes: 'Bulk purchase for upcoming production'
                },
                {
                    id: 'EXP-002',
                    date: '2023-05-10',
                    category: 'Salaries',
                    description: 'Staff salaries for May',
                    amount: 8000000,
                    paymentMethod: 'Bank Transfer',
                    notes: 'Monthly payroll'
                },
                {
                    id: 'EXP-003',
                    date: '2023-05-15',
                    category: 'Rent',
                    description: 'Shop rent for May',
                    amount: 3500000,
                    paymentMethod: 'Bank Transfer',
                    notes: 'Monthly rent payment'
                },
                {
                    id: 'EXP-004',
                    date: '2023-05-18',
                    category: 'Utilities',
                    description: 'Electricity and water bills',
                    amount: 1200000,
                    paymentMethod: 'Cash',
                    notes: 'Monthly utilities'
                },
                {
                    id: 'EXP-005',
                    date: '2023-05-22',
                    category: 'Equipment',
                    description: 'New sewing machine',
                    amount: 4500000,
                    paymentMethod: 'Credit Card',
                    notes: 'Replacement for broken machine'
                }
            ]
        }
    },
    computed: {
        filteredExpenses() {
            let filtered = this.expenses;
            
            if (this.searchQuery) {
                const query = this.searchQuery.toLowerCase();
                filtered = filtered.filter(expense => 
                    expense.id.toLowerCase().includes(query) || 
                    expense.description.toLowerCase().includes(query) ||
                    expense.category.toLowerCase().includes(query)
                );
            }
            
            if (this.categoryFilter) {
                filtered = filtered.filter(expense => expense.category === this.categoryFilter);
            }
            
            if (this.monthFilter) {
                filtered = filtered.filter(expense => {
                    const expenseMonth = expense.date.split('-')[1];
                    return expenseMonth === this.monthFilter;
                });
            }
            
            return filtered;
        },
        totalAmount() {
            return this.filteredExpenses.reduce((total, expense) => total + expense.amount, 0);
        }
    },
    methods: {
        getCategoryClass(category) {
            switch(category) {
                case 'Materials': return 'badge badge-primary';
                case 'Salaries': return 'badge badge-info';
                case 'Rent': return 'badge badge-warning';
                case 'Utilities': return 'badge badge-secondary';
                case 'Equipment': return 'badge badge-success';
                case 'Other': return 'badge badge-dark';
                default: return 'badge badge-light';
            }
        },
        showAddModal() {
            this.isEditing = false;
            this.currentExpense = {
                id: null,
                date: new Date().toISOString().split('T')[0],
                category: '',
                description: '',
                amount: 0,
                paymentMethod: 'Cash',
                notes: ''
            };
            $('#expenseModal').modal('show');
        },
        viewExpense(expense) {
            this.currentExpense = { ...expense };
            $('#viewExpenseModal').modal('show');
        },
        editExpense(expense) {
            this.isEditing = true;
            this.currentExpense = { ...expense };
            $('#expenseModal').modal('show');
        },
        deleteExpense(expense) {
            this.expenseToDelete = expense;
            $('#deleteModal').modal('show');
        },
        confirmDelete() {
            if (this.expenseToDelete) {
                const index = this.expenses.findIndex(e => e.id === this.expenseToDelete.id);
                if (index !== -1) {
                    this.expenses.splice(index, 1);
                }
                this.expenseToDelete = null;
                $('#deleteModal').modal('hide');
            }
        },
        saveExpense() {
            if (this.isEditing) {
                // Update existing expense
                const index = this.expenses.findIndex(e => e.id === this.currentExpense.id);
                if (index !== -1) {
                    this.expenses.splice(index, 1, { ...this.currentExpense });
                }
            } else {
                // Add new expense
                const newId = `EXP-${String(this.expenses.length + 1).padStart(3, '0')}`;
                this.expenses.push({
                    ...this.currentExpense,
                    id: newId
                });
            }
            $('#expenseModal').modal('hide');
        }
    }
}
</script>

<style scoped>
.table th, .table td {
    vertical-align: middle;
}
</style>