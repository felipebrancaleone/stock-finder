# 📈 Stock Finder API

A **Spring Boot** application that fetches stock price data from **Polygon.io API** and stores it in **MySQL**.

---

## 🚀 Setup Instructions

### 1️⃣ Prerequisites
Ensure you have the following installed:
- **Java 17+**
- **Maven or Gradle**
- **Docker** (for MySQL)
- **Postman or cURL** (for API testing)

---

### 2️⃣ Configure & Start MySQL (Using Docker)
Run the following command to start a **MySQL** container:
```sh
docker compose up -d
```
This will:
- Start a MySQL 8.0 instance.
- Create a database named `testdb`.

#### **Alternative: Manual MySQL Setup**
1. Install **MySQL 8.0**.
2. Create the database:
   ```sql
   CREATE DATABASE testdb;
   ```
3. Update **`src/main/resources/application.properties`** with:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/testdb
   spring.datasource.username=user
   spring.datasource.password=password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

---

### 3️⃣ Set Up Polygon.io API Key
The application requires an **API key** from **Polygon.io**.

1. Sign up at [Polygon.io](https://polygon.io/).
2. Get your **API Key**.
3. Set the environment variable:

#### **For macOS/Linux (Terminal)**
```sh
export POLYGON_API_KEY=your_actual_api_key
```

#### **For Windows (PowerShell)**
```powershell
$env:POLYGON_API_KEY="your_actual_api_key"
```

#### **Alternative: Set in IntelliJ**
1. **Go to** `Run > Edit Configurations...`
2. **Add environment variable**:
   ```
   POLYGON_API_KEY=your_actual_api_key
   ```

---

### 4️⃣ Run the Application
Start the Spring Boot app using:
```sh
./mvnw spring-boot:run  # Maven
# OR
./gradlew bootRun        # Gradle
```
If everything is set up correctly, you should see:
```
Tomcat started on port 8080
```

---

## 📌 API Endpoints

### **1️⃣ Fetch and Store Stock Prices**
- **URL:** `POST /stocks/fetch`
- **Description:** Fetches stock prices from **Polygon.io API** and stores them in **MySQL**.
- **Query Parameters:**
  | Parameter      | Type   | Required | Description                      |
  |---------------|--------|----------|----------------------------------|
  | companySymbol | String | ✅ Yes   | Stock ticker symbol (e.g., `AAPL`) |
  | fromDate      | String | ✅ Yes   | Start date (format: `YYYY-MM-DD`) |
  | toDate        | String | ✅ Yes   | End date (format: `YYYY-MM-DD`) |

#### **Example Request (Postman / cURL)**
```sh
curl -X POST "http://localhost:8080/stocks/fetch?companySymbol=AAPL&fromDate=2025-03-01&toDate=2025-03-10"
```

#### **Example Response**
```json
{
  "message": "Stock data fetched and stored successfully."
}
```

---

### **2️⃣ Get Stock Prices by Company Symbol and Date**
- **URL:** `GET /stocks/{companySymbol}?date=YYYY-MM-DD`
- **Description:** Fetches stock prices **from MySQL** for a given company **and date**.

#### **Example Request**
```sh
curl -X GET "http://localhost:8080/stocks/AAPL?date=2025-03-01"
```

#### **Example Response**
```json
{
  "companySymbol": "AAPL",
  "date": "2025-03-01",
  "openPrice": 174.25,
  "closePrice": 176.10,
  "highPrice": 178.00,
  "lowPrice": 172.80,
  "volume": 58900000
}
```

---

## 🛠️ Running Tests
To run **unit tests** and **integration tests**, use:
```sh
./mvnw test  # Maven
# OR
./gradlew test  # Gradle
```

---

## 🎯 Handling Custom Date Arguments
When calling the `fetch` endpoint, ensure:
- The `fromDate` and `toDate` parameters follow the **`YYYY-MM-DD` format**.
- The date range does not exceed **1 year** (Polygon.io API limitation).

#### **Example Fetch Call**
```sh
curl -X POST "http://localhost:8080/stocks/fetch?companySymbol=GOOGL&fromDate=2024-01-01&toDate=2024-12-31"
```

---

## 🔥 Troubleshooting

### ❌ API Key Not Found Error
```sh
org.springframework.util.PlaceholderResolutionException: Could not resolve placeholder 'POLYGON_API_KEY'
```
✅ **Fix:** Ensure the environment variable is set correctly:
```sh
echo $POLYGON_API_KEY  # macOS/Linux
$env:POLYGON_API_KEY  # Windows
```
If it returns an empty string, set it again.

---

### ❌ MySQL Connection Issues
If you get:
```
Error: Can't connect to MySQL
```
✅ **Fix:**
- Ensure **MySQL is running** (`docker ps`).
- Check **database credentials** in `application.properties`.

---

## 🚀 Contributors
- **Felipe Brancaleone**
- **Company: LeadIQ**
- **Project: Stock Finder**

---

