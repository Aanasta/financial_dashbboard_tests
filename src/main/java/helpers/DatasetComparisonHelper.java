package helpers;

import models.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static helpers.DataConstants.*;

public class DatasetComparisonHelper {

    private static final Logger logger = LogManager.getLogger(DatasetComparisonHelper.class);

    private static final String MISSING_RECORD_MESSAGE = "Record for company %s is missing in %s.\n";
    private static final String MISMATCH_MESSAGE = "%s mismatch for company %s. Dataset 1 value: %s, Dataset 2 value: %s\n";

    private static final String DATASET_ONE = "Table 1";
    private static final String DATASET_TWO = "Table 2";

    public static String areDatasetsDifferent(List<Record> datasetOne, List<Record> datasetTwo) {
        StringBuilder differences = new StringBuilder();
        datasetOne.forEach(recordOne -> {
            String companyName = recordOne.getCompanyName();
            logger.info("Comparing data for company: {}", companyName);
            Optional<Record> matchingRecord = datasetTwo.stream()
                    .filter(record -> record.getCompanyName().equals(companyName))
                    .findFirst();
            if (matchingRecord.isEmpty()) {
                differences.append(String.format(MISSING_RECORD_MESSAGE, companyName, DATASET_TWO));
            } else {
                Record recordTwo = matchingRecord.get();
                compareStringValue(differences, TICKER_FIELD, recordOne.getTicker(), recordTwo.getTicker(), companyName);
                compareStringValue(differences, COB_DATE_FIELD, recordOne.getCobDate(), recordTwo.getCobDate(), companyName);
                compareStockPrice(differences, recordOne.getStockPrice(), recordTwo.getStockPrice(), companyName);
                compareMarketCap(differences, recordOne.getMarketCap(), recordTwo.getMarketCap(), companyName);
            }
        });
        datasetTwo.stream()
                .filter(recordTwo -> datasetOne.stream().noneMatch(recordOne -> recordOne.getCompanyName().equals(recordTwo.getCompanyName())))
                .forEach(recordTwo -> differences.append(String.format(MISSING_RECORD_MESSAGE, recordTwo.getCompanyName(), DATASET_ONE)));
        return differences.toString();
    }

    private static void compareStringValue(StringBuilder differences, String fieldName, String valueOne, String valueTwo, String companyName) {
        if (!valueOne.equals(valueTwo)) {
            differences.append(String.format(MISMATCH_MESSAGE, fieldName, companyName, valueOne, valueTwo));
        }
    }

    private static void compareStockPrice(StringBuilder differences, double valueOne, double valueTwo, String companyName) {
        if (Double.compare(valueOne, valueTwo) != 0) {
            differences.append(String.format(MISMATCH_MESSAGE, STOCK_PRICE_FIELD, companyName, valueOne, valueTwo));
        }
    }

    private static void compareMarketCap(StringBuilder differences, int valueOne, int valueTwo, String companyName) {
        if (valueOne != valueTwo) {
            differences.append(String.format(MISMATCH_MESSAGE, MARKET_CAP_FIELD, companyName, valueOne, valueTwo));
        }
    }
}
