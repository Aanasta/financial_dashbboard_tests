package helpers;

import models.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class DatasetComparisonHelper {

    private static final Logger logger = LogManager.getLogger(DatasetComparisonHelper.class);

    public static String areDatasetsDifferent(List<Record> datasetOne, List<Record> datasetTwo) {
        StringBuilder differences = new StringBuilder();

        datasetOne.forEach(recordOne -> {
            String companyName = recordOne.getCompanyName();
            logger.info("Comparing data for company: {}", companyName);
            Optional<Record> matchingRecord = datasetTwo.stream()
                    .filter(record -> record.getCompanyName().equals(companyName))
                    .findFirst();
            if (!matchingRecord.isPresent()) {
                differences.append("Record for company ").append(companyName).append(" is missing in the Table 2.\n");
            } else {
                Record recordTwo = matchingRecord.get();
                if (!recordOne.getTicker().equals(recordTwo.getTicker())) {
                    differences.append("Ticker mismatch for company ").append(recordOne.getCompanyName())
                            .append(". Dataset 1 value: ").append(recordOne.getTicker())
                            .append(", Dataset 2 value: ").append(recordTwo.getTicker()).append("\n");
                }
                if (!recordOne.getCobDate().equals(recordTwo.getCobDate())) {
                    differences.append("COB Date mismatch for company ").append(recordOne.getCompanyName())
                            .append(". Dataset 1 value: ").append(recordOne.getCobDate())
                            .append(", Dataset 2 value: ").append(recordTwo.getCobDate()).append("\n");
                }
                if (!recordOne.getStockPrice().equals(recordTwo.getStockPrice())) {
                    differences.append("Stock Price mismatch for company ").append(recordOne.getCompanyName())
                            .append(". Dataset 1 value: ").append(recordOne.getStockPrice())
                            .append(", Dataset 2 value: ").append(recordTwo.getStockPrice()).append("\n");
                }
                if (!recordOne.getMarketCap().equals(recordTwo.getMarketCap())) {
                    differences.append("Market Cap mismatch for company ").append(recordOne.getCompanyName())
                            .append(". Dataset 1 value: ").append(recordOne.getMarketCap())
                            .append(", Dataset 2 value: ").append(recordTwo.getMarketCap()).append("\n");
                }
            }

        });
        datasetTwo.stream()
                .filter(recordTwo -> datasetOne.stream().noneMatch(recordOne -> recordOne.getCompanyName().equals(recordTwo.getCompanyName())))
                .forEach(recordTwo -> differences.append("Record for company ").append(recordTwo.getCompanyName())
                        .append(" is missing in the first table.\n"));

        return differences.toString();
    }
}
