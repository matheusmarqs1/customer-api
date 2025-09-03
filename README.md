# 👥 Customer Management API

📄 **Description**  
REST API developed to manage individual customers. Through this API, it is possible to create, update, retrieve, and delete customers, as well as search by specific attributes.

---

## ⚙️ Features

- **Customer creation:** Allows registering new customers in the system (public).  
- **Customer login:** Authenticate and receive JWT (public).  
- **Customer management:** Users can update, delete, and retrieve **only their own data**.  
- **Admin access:** Admin users can access and manage all customer data.  
- **Security:** Endpoints are protected with **Spring Security + JWT**. Only `POST /customers` and `POST /customers/login` are public.

---

## 🛠️ Technologies

- **Language:** Java 21  
- **Framework:** Spring Boot (Web, JPA, Security)  
- **Dependency Manager:** Maven  
- **Database:** PostgreSQL  
- **Security:** JWT (JSON Web Tokens)  
- **Testing:** JUnit 5, Mockito, Integration Tests with MockMvc and Spring Boot Test  
- **API Documentation:** SpringDoc (Swagger/OpenAPI)  

---

## 📝 Endpoints

- **Local documentation:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

| Endpoint | Method | Description | Access |
|----------|--------|-------------|--------|
| `/customers` | POST | Register new customer | Public |
| `/customers/login` | POST | Authenticate and return JWT | Public |
| `/customers/{id}` | GET | Retrieve customer data | Admin or self |
| `/customers/{id}` | PUT | Update customer data | Admin or self |
| `/customers/{id}` | DELETE | Delete customer | Admin or self |
| `/customers` | GET | List all customers (with pagination and filters) | Admin only |

---

## 🖼️ Project Images

<details>
  <summary>Swagger / API Documentation</summary>
  <img src="images/swagger.png" alt="Swagger UI" width="600">
</details>

<details>
  <summary>Login / JWT Request</summary>
  <img src="images/login.png" alt="Login Request" width="600">
</details>

<details>
  <summary>GET Request with Bearer Token</summary>
  <img src="images/get_request.png" alt="GET Request" width="600">
</details>

---

## ➡️ Application Flow

1. A customer is created via the public endpoint `POST /customers`.  
2. The customer logs in with credentials (`POST /customers/login`) and receives a JWT.  
3. Using the JWT, the customer can access only their own data for retrieval, update, or deletion.  
4. Admin users can manage all customer data and access all endpoints.

---

## ⚙️ Setup & Run

**Prerequisites:**
- Java 21  
- Maven  
- PostgreSQL (for local development)

### 🔧 Local Development Setup

**1. Clone the repository:**
```
git clone https://github.com/matheusmarqs1/customer-api.git
cd customer-api
```

**2. Configure PostgreSQL:**
- Create a database named `customer_api_database`
- Database connection settings can be found in `application-dev.properties`
- Adjust credentials if needed (default: postgres/1234567)
- **Important:** Change the profile in `application.properties` from `prod` to `dev`:
  ```
  spring.profiles.active=dev
  ```

**3. Generate JWT Keys (for local development):**
```
# Generate private key
openssl genrsa -out src/main/resources/app.key 2048

# Generate public key
openssl rsa -in src/main/resources/app.key -pubout -out src/main/resources/app.pub
```

**4. Run the application:**
```
mvn spring-boot:run
```

**5. Access the application:**
- API: `http://localhost:8080`
- Swagger Documentation: `http://localhost:8080/swagger-ui/index.html`
- Use **Postman** or **Insomnia** to test the API endpoints

---

## 🙋 Author

Project developed by [Matheus T.](https://www.linkedin.com/in/matheusmarqs/) 
