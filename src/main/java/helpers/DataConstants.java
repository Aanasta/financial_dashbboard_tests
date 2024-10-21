package helpers;

public final class DataConstants {

    public static final String TICKER_FIELD = "Ticker";
    public static final String COB_DATE_FIELD = "COB Date";
    public static final String STOCK_PRICE_FIELD = "Stock Price";
    public static final String MARKET_CAP_FIELD = "Market Cap";

    private DataConstants() {
        throw new UnsupportedOperationException("Cannot instantiate data constants class");
    }
}
