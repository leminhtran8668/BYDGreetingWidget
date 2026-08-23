# BYD Greeting Widget

Widget Android đơn giản dành cho xe BYD (DiLink) và mọi thiết bị Android.

**Tác giả: Lê Minh**

## Tính năng

- **Widget** có 2 nút:
  - 🟢 **Bắt đầu** → phát audio chào khi khởi động xe
  - 🔴 **Kết thúc** → phát audio khi kết thúc hành trình
- Màn hình **Cài đặt** cho phép chọn file audio riêng cho từng nút (hỗ trợ MP3, OGG, WAV, M4A...)
- Lưu quyền truy cập file vĩnh viễn (Persistable URI)
- Giao diện sáng (light theme), tiếng Việt, tối giản, dễ dùng trên màn hình xe

## Cách sử dụng

1. Cài APK lên điện thoại hoặc trực tiếp lên DiLink (qua USB / ADB).
2. Mở app → chọn file audio cho **Khởi động** và **Kết thúc**.
3. Nhấn giữ màn hình chính → **Widget** → tìm **BYD Greeting Widget** → kéo ra.
4. Bấm nút trên widget để phát audio tương ứng.
5. Bấm vào tiêu đề widget để mở lại màn hình cài đặt.

## Build từ source

Yêu cầu:
- Android Studio Hedgehog (2023.1.1) trở lên
- JDK 17

```bash
git clone https://github.com/leminhtran8668/BYDGreetingWidget.git
cd BYDGreetingWidget
# Mở bằng Android Studio (khuyến nghị) hoặc:
gradle assembleDebug
```

File APK sẽ nằm ở:
`app/build/outputs/apk/debug/app-debug.apk`

### GitHub Actions (tự động build APK)

Mỗi lần push lên `main`/`master`, workflow sẽ tự động build APK.  
Vào tab **Actions** → chọn workflow **Build APK** → download artifact **BYD-Greeting-APK**.

Bạn cũng có thể chạy thủ công bằng nút **Run workflow**.

## Cài lên DiLink (BYD)

### Cách 1: USB (dễ nhất)
1. Tạo thư mục `Third Party Apps 55` (hoặc mã nước tương ứng) trên USB FAT32.
2. Copy file APK vào thư mục đó.
3. Cắm USB vào xe → nhập mật khẩu nếu được hỏi (thường là `BYD6125F`).
4. Cài đặt APK.

### Cách 2: ADB
```bash
adb connect <ip-xe>:5555
adb install app-debug.apk
```

Sau khi cài, nhớ **whitelist app** trong phần Quản lý tự khởi động / Pin tối ưu hóa của DiLink nếu cần.

## Cấu trúc project

```
BYDGreetingWidget/
├── app/
│   ├── src/main/
│   │   ├── java/com/byd/greeting/
│   │   │   ├── MainActivity.kt          # Màn hình cài đặt
│   │   │   ├── GreetingWidgetProvider.kt # Widget
│   │   │   ├── AudioPlayer.kt           # Phát audio
│   │   │   └── Prefs.kt                 # Lưu URI audio
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── .github/workflows/build-apk.yml      # GitHub Actions
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Ghi chú

- Widget hoạt động trên mọi thiết bị Android 7.0+ (API 24).
- Không cần quyền đặc biệt ngoài quyền đọc file audio (dùng Storage Access Framework).
- Có thể dùng kèm app tự khởi động (BYDautostart) nếu muốn tự động mở widget hoặc app khi boot.

## License

MIT – tự do sử dụng, chỉnh sửa, chia sẻ.
