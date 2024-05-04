package net.uqcloud.infs7202.project.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyParser {
    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        if (formatter instanceof DecimalFormat decimalFormat) {
            decimalFormat.setParseBigDecimal(true);
        }

        return (BigDecimal) formatter.parse(amount.replaceAll("[^\\d.,]",""));
    }

    public static String toCurrency(final double amount, final Locale locale) {
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);

        return formatter.format(amount);
    }
}
