package controller;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper methods to facilitate the use of the MonetaryAmount type.
 * Created by Florian Hug <florian.hug@gmail.com> on 12/30/14.
 */
public class MoneyHelper {
    public static final CurrencyUnit DEFAULT_CURRENCY = MonetaryCurrencies.getCurrency("CHF");
    private static List<String> currencies;

    public static List<String> getCurrencies() {
        if (MoneyHelper.currencies == null) {
            MoneyHelper.currencies = MonetaryCurrencies
                    .getCurrencies()
                    .stream()
                    .map(CurrencyUnit::getCurrencyCode)
                    .sorted()
                    .collect(Collectors.toList());
            MoneyHelper.currencies.remove("CHF");
            MoneyHelper.currencies.remove("EUR");
            MoneyHelper.currencies.remove("USD");
            MoneyHelper.currencies.add(0, "CHF");
            MoneyHelper.currencies.add(1, "EUR");
            MoneyHelper.currencies.add(2, "USD");
        }
        return MoneyHelper.currencies;
    }
}
