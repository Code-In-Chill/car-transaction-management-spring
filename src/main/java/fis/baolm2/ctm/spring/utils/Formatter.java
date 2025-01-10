package fis.baolm2.ctm.spring.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {

    public static String formatMoney(Float money) {
        if (money == null) {
            return "0 đ";
        }

        // Tạo NumberFormat cho tiền tệ Việt Nam
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(localeVN);

        // Cấu hình định dạng số
        if (currencyFormat instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) currencyFormat;
            decimalFormat.applyPattern("###,###.##");
        }

        // Định dạng số và thêm ký hiệu tiền tệ
        return currencyFormat.format(money) + " đ";
    }
}