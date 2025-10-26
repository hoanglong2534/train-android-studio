# Bài Tập Android - Sensors & Network

## Tổng quan
Ứng dụng Android này bao gồm 3 bài tập về cảm biến và mạng:

### Màn hình chính (MainActivity)
- Hiển thị 3 nút cho 3 bài tập khác nhau
- Mỗi nút dẫn đến một activity riêng biệt

## Bài 1: Phát hiện lắc điện thoại (ShakeDetectionActivity)

### Tính năng:
- Sử dụng cảm biến gia tốc (Accelerometer) để phát hiện chuyển động lắc
- Hiển thị giá trị thời gian thực của trục X, Y, Z
- Thông báo khi phát hiện lắc điện thoại
- Ngưỡng phát hiện: 12.0f
- Thời gian chờ giữa các lần phát hiện: 250ms

### Cách hoạt động:
- Đăng ký `SensorManager` với `Sensor.TYPE_ACCELEROMETER`
- Xử lý dữ liệu trong `onSensorChanged()`
- Tính toán tổng dao động từ các trục X, Y, Z
- So sánh với ngưỡng để phát hiện lắc

### File liên quan:
- `ShakeDetectionActivity.java`
- `activity_shake_detection.xml`

## Bài 2: Hiển thị thông tin pin (BatteryStatusActivity)

### Tính năng:
- Hiển thị phần trăm pin còn lại
- Trạng thái sạc (đang sạc/không sạc/pin đầy)
- Loại nguồn sạc (USB/AC/Wireless/Không sạc)
- Nhiệt độ pin
- Điện áp pin
- Progress bar với màu sắc thay đổi theo mức pin

### Cách hoạt động:
- Sử dụng `BroadcastReceiver` để nhận `Intent.ACTION_BATTERY_CHANGED`
- Trích xuất thông tin từ `Intent` với các extra keys của `BatteryManager`
- Cập nhật UI theo thời gian thực
- Đăng ký receiver trong `onResume()` và hủy đăng ký trong `onPause()`

### File liên quan:
- `BatteryStatusActivity.java`
- `activity_battery_status.xml`

## Bài 3: Đọc JSON từ Internet (JsonListViewActivity)

### Tính năng:
- Tải dữ liệu JSON từ JSONPlaceholder API (https://jsonplaceholder.typicode.com/users)
- Phân tích JSON với `JSONObject` và `JSONArray`
- Hiển thị danh sách người dùng trong `ListView`
- Chi tiết người dùng khi nhấn vào item
- Progress bar khi tải dữ liệu

### Cách hoạt động:
- Sử dụng `AsyncTask` để tải dữ liệu từ internet
- `HttpURLConnection` để thực hiện HTTP request
- Phân tích JSON thủ công (không dùng Gson)
- `ArrayAdapter` để hiển thị dữ liệu trong ListView
- Dialog hiển thị chi tiết người dùng

### File liên quan:
- `JsonListViewActivity.java`
- `User.java` (model class)
- `activity_json_list_view.xml`

## Quyền được yêu cầu:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Cấu hình manifest:
- Tất cả activities đã được đăng ký trong `AndroidManifest.xml`
- `android:usesCleartextTraffic="true"` để cho phép HTTP requests

## Cách sử dụng:
1. Mở ứng dụng
2. Chọn bài tập muốn thực hiện
3. Làm theo hướng dẫn trên từng màn hình

## Công nghệ sử dụng:
- Android Java
- SensorManager & Sensor API
- BroadcastReceiver & BatteryManager
- AsyncTask & HttpURLConnection
- JSON parsing
- ListView & ArrayAdapter
- Material Design components

## Ghi chú:
- Ứng dụng được build thành công với Gradle
- Hỗ trợ từ API 24 trở lên
- Sử dụng Edge-to-Edge display
- Responsive design cho các thiết bị khác nhau
