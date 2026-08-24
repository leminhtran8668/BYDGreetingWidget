# BYD Greeting

App Android đơn giản dành cho xe BYD (DiLink) và mọi thiết bị Android.

**Tác giả: Lê Minh**

## Tính năng

- **Popup** hiện khi mở app với 2 nút lớn:
  - 🟢 **Bắt đầu** → phát audio chào khi khởi động xe
  - 🔴 **Kết thúc** → phát audio khi kết thúc hành trình
- **Công tắc** trong cài đặt: bật/tắt tự hiện popup khi mở app
- Chọn file audio riêng cho từng nút (MP3, OGG, WAV, M4A...)
- Lưu quyền truy cập file vĩnh viễn (Persistable URI)
- Giao diện sáng (light theme), tiếng Việt, tối giản

> Launcher BYD thường **không cho thêm widget bên thứ 3**, nên app dùng popup thay vì widget.

## Cách sử dụng trên xe

1. Cài APK lên DiLink (USB / ADB).
2. Mở app → chọn file audio cho **Khởi động** và **Kết thúc**.
3. Bật công tắc **Tự hiện popup khi mở app**.
4. Mỗi lần mở app sẽ hiện popup 2 nút → bấm để phát audio.
5. Có thể tắt popup trong cài đặt nếu không cần.

## Build từ source

```bash
git clone https://github.com/leminhtran8668/BYDGreetingWidget.git
cd BYDGreetingWidget
# Mở bằng Android Studio hoặc:
gradle assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

### GitHub Actions

Push lên `main` → tab **Actions** → download artifact **BYD-Greeting-APK**.

## Cài lên DiLink (BYD)

### USB
1. Thư mục `Third Party Apps 55` (hoặc mã nước) trên USB FAT32.
2. Copy APK vào → cắm xe → mật khẩu thường `BYD6125F`.

### ADB
```bash
adb connect <ip-xe>:5555
adb install app-debug.apk
```

## License

MIT – tự do sử dụng, chỉnh sửa, chia sẻ.
