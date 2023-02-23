package org.fetch.dtos.requests;

import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountingRequest {
    CSVReader csvReader;
    int amountToSpend;

}
