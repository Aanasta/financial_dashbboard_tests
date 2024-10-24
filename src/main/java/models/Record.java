package models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Record {

    private String companyName;
    private String ticker;
    private String cobDate;
    private double stockPrice;
    private int marketCap;
}
