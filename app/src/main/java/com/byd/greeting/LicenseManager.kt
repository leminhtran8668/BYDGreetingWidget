package com.byd.greeting

import android.content.Context
import android.provider.Settings
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Kích hoạt bản quyền OFFLINE, khóa theo thiết bị:
 * 1. App hiển thị "Mã thiết bị" (device ID) cho khách.
 * 2. Khách gửi mã thiết bị đó cho bạn (qua Zalo/Messenger...) kèm chuyển khoản.
 * 3. Bạn dùng công cụ KeyGen riêng (xem file tools/keygen, KHÔNG nằm trong app)
 *    để tính ra "Mã kích hoạt" tương ứng, gửi lại cho khách.
 * 4. Khách nhập mã đó vào app -> app tự tính lại và so sánh -> khớp thì mở khóa.
 *
 * Vì mã kích hoạt được tính từ (device ID + khóa bí mật), một mã chỉ dùng
 * được đúng thiết bị đã cấp, không thể dùng cho máy khác.
 *
 * LƯU Ý QUAN TRỌNG: khóa bí mật (LICENSE_SECRET) phải KHÔNG được commit lên
 * repo Git công khai — nếu không ai đọc source cũng tự tạo được mã kích hoạt
 * miễn phí. Khóa được truyền vào qua BuildConfig, đọc từ file local.properties
 * (không nằm trong Git, xem hướng dẫn trong build.gradle.kts).
 */
object LicenseManager {

    /** Lấy mã định danh thiết bị để hiển thị cho khách gửi cho bạn. */
    fun getDeviceId(context: Context): String {
        val id = try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            null
        }
        return if (id.isNullOrBlank()) "UNKNOWN-DEVICE" else id
    }

    /** Tính mã kích hoạt hợp lệ cho 1 device ID, dựa trên khóa bí mật nhúng lúc build. */
    private fun computeExpectedCode(deviceId: String): String {
        val secret = BuildConfig.LICENSE_SECRET
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256"))
        val hash = mac.doFinal(deviceId.trim().uppercase().toByteArray(Charsets.UTF_8))
        val hex = hash.joinToString("") { b -> "%02X".format(b) }
        // Lấy 16 ký tự hex đầu (64 bit) cho gọn, chia nhóm 4 ký tự dễ đọc/gõ
        return hex.substring(0, 16)
    }

    /** Định dạng đẹp để hiển thị / dễ gõ: XXXX-XXXX-XXXX-XXXX */
    fun formatCode(rawCode: String): String {
        return rawCode.chunked(4).joinToString("-")
    }

    /** Chuẩn hóa mã người dùng nhập (bỏ dấu gạch, khoảng trắng, hoa/thường) để so sánh. */
    private fun normalize(code: String): String {
        return code.trim().uppercase().replace("-", "").replace(" ", "")
    }

    fun isActivated(context: Context): Boolean = Prefs.isLicenseActivated(context)

    /** Thử kích hoạt bằng mã người dùng nhập. Trả về true nếu đúng. */
    fun tryActivate(context: Context, inputCode: String): Boolean {
        val expected = computeExpectedCode(getDeviceId(context))
        val given = normalize(inputCode)
        val ok = expected == given
        if (ok) {
            Prefs.setLicenseActivated(context, true)
        }
        return ok
    }
}
