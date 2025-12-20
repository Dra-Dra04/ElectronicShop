@echo off
REM Test script để chạy từng service

echo Starting services...
echo.

REM Kill old processes
taskkill /F /IM java.exe > nul 2>&1

timeout /t 2 > nul

REM Payment Service (Port 8086)
echo Starting Payment Service on port 8086...
start "PaymentService" cmd /c "cd d:\ElectronicShop\payment-service && mvn spring-boot:run"

REM Order Service (Port 8083)
echo Starting Order Service on port 8083...
start "OrderService" cmd /c "cd d:\ElectronicShop\order-service && mvn spring-boot:run"

REM Product Service (Port 8081)
echo Starting Product Service on port 8081...
start "ProductService" cmd /c "cd d:\ElectronicShop\product-service && mvn spring-boot:run"

REM Customer Service (Port 8082)
echo Starting Customer Service on port 8082...
start "CustomerService" cmd /c "cd d:\ElectronicShop\customer-service && mvn spring-boot:run"

REM Frontend Service (Port 8080)
echo Starting Frontend Service on port 8080...
start "FrontendService" cmd /c "cd d:\ElectronicShop\frontend-service && mvn spring-boot:run"

echo.
echo All services started in separate windows
echo.
echo Frontend: http://localhost:8080
echo Customer: http://localhost:8082
echo Product: http://localhost:8081
echo Order: http://localhost:8083
echo Payment: http://localhost:8086
echo.
pause
