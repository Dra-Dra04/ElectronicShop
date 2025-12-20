# PowerShell script to start all services
# Chạy: powershell -ExecutionPolicy Bypass -File run_services.ps1

Write-Host "Cleaning up old java processes..." -ForegroundColor Yellow
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force

Start-Sleep -Seconds 2

Write-Host "Starting services..." -ForegroundColor Green
Write-Host ""

# Define services
$services = @(
    @{Name="ProductService"; Port=8081; Path="product-service"; Wait=30},
    @{Name="CustomerService"; Port=8082; Path="customer-service"; Wait=25},
    @{Name="OrderService"; Port=8083; Path="order-service"; Wait=30},
    @{Name="FrontendService"; Port=8080; Path="frontend-service"; Wait=25},
    @{Name="PaymentService"; Port=8086; Path="payment-service"; Wait=25}
)

foreach ($service in $services) {
    Write-Host "Starting $($service.Name) on port $($service.Port)..." -ForegroundColor Cyan
    
    $scriptBlock = {
        param($servicePath, $serviceName)
        Set-Location "D:\ElectronicShop\$servicePath"
        mvn spring-boot:run
    }
    
    Start-Job -ScriptBlock $scriptBlock -ArgumentList $service.Path, $service.Name -Name $service.Name
    
    Write-Host "$($service.Name) started (waiting $($service.Wait)s for startup...)" -ForegroundColor Green
    Start-Sleep -Seconds $service.Wait
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "All services started!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor Yellow
Write-Host "Frontend:  http://localhost:8080"
Write-Host "Product:   http://localhost:8081"
Write-Host "Customer:  http://localhost:8082"
Write-Host "Order:     http://localhost:8083"
Write-Host "Payment:   http://localhost:8086"
Write-Host ""
Write-Host "Running jobs:" -ForegroundColor Yellow
Get-Job | Format-Table -Property Name, State, PSJobTypeName

Write-Host ""
Write-Host "To stop all services:" -ForegroundColor Yellow
Write-Host "Get-Job | Stop-Job"
Write-Host "Get-Job | Remove-Job"
