# Library  API

---
### Application is currently in beta version.

### Characteristics

- Authentication
  - User registration 
  - User login
  - Get logged user
  - Implemented access and refresh tokens
  - Confirm email
  - Resend confirm email
  - Forgot password
  - Change password
  - Logout
- Authorization
  - Implemented roles
- Admin
  - Create user
  - Delete user
  - Update user
  - Updating user roles
  - Get user roles
  - Get user
  - Get users
- Book
  - Create book
  - Get book
  - Get books
  - Delete book
  - Update book
- Loans
  - Create loan
  - Get loan
  - Get loans
  - Get user loans
  - Delete loan
  - Update loan

---
- Pagination,filtration and sorting
  - Users
    - Users can be sorted by : first_name or created_at
    - Sorting direction can be: ASC or DESC
  - Books
    - Books can be filtered by: title and/or author and/or book type and/or status
    - Books can be sorted by: title or release yer
    - Sorting direction can be: ASC or DESC
  - Loans
    - Loans can be filtered by: book title and/or member and/or range(fromDate-toDate) and/or status
    - Loans can be sorted by: source_title, borrow_date, created_at
    - Sorting direction can be: ASC or DESC