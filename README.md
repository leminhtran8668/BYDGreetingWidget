# BYD Greeting

Panel nổi nhỏ cho xe BYD (DiLink) — kéo được, nổi trên mọi app (bản đồ, launcher).

**Tác giả: Lê Minh**

## Tính năng (v1.2)

- **Panel nổi rất nhỏ** với 2 nút: 🟢 Bắt đầu / 🔴 Kết thúc
- **Kéo tự do** bằng tay nắm ⋮⋮
- **Nổi trên mọi app** (bản đồ Google Maps, Waze, launcher…)
- **Tự mở khi boot** xe (cần whitelist trong DiLink)
- Chọn file audio riêng cho từng nút
- Không phụ thuộc widget (launcher BYD thường chặn widget bên thứ 3)

## Cách dùng trên xe

1. Cài APK → mở app.
2. Chọn 2 file audio.
3. Bấm **Bật panel nổi** → cấp quyền **Hiển thị trên các ứng dụng khác**.
4. Panel nhỏ xuất hiện → kéo ⋮⋮ đặt góc màn hình (không che bản đồ).
5. Bật **Tự mở panel khi boot xe**.
6. Vào **Cài đặt DiLink → Quản lý tự khởi động / Pin** → whitelist **BYD Greeting**.

## Build

```bash
git clone https://github.com/leminhtran8668/BYDGreetingWidget.git
cd BYDGreetingWidget
gradle assembleDebug
```

GitHub Actions tự build APK mỗi lần push → tab **Actions** → artifact **BYD-Greeting-APK**.

## License

MIT
