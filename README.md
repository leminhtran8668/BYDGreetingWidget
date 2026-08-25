# BYD Greeting

Panel nổi nhỏ cho xe BYD (DiLink) — kéo được, nổi trên mọi app.

**Tác giả: Lê Minh**

## v1.5

- **Đã bỏ** mã kích hoạt / license
- Panel nổi siêu nhỏ (2 chấm xanh/đỏ), kéo ⋮ di chuyển
- Nổi trên bản đồ / launcher
- Tự mở khi boot (cần whitelist DiLink)
- targetSdk 30 + manifest đơn giản hơn để cài được trên head unit

## Cài trên xe

1. Download APK từ **Actions** → artifact **BYD-Greeting-APK** (giải nén file `.apk`).
2. Copy vào USB → cài trên DiLink (hoặc ADB).
3. Mở app → chọn audio → **Bật panel nổi** → cấp quyền hiển thị trên app khác.
4. Whitelist tự khởi động trong cài đặt DiLink.

## Build

```bash
gradle assembleDebug
```

## License

MIT
