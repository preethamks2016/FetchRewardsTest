package org.fetch;

import com.opencsv.CSVReader;
import org.fetch.dtos.requests.AccountingRequest;
import org.fetch.dtos.responses.AccountingResponse;
import org.fetch.service.AccountingService;
import org.fetch.service.AccountingServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class FetchTest {
    public static void main(String[] args) {
        int pointsToSpend = Integer.parseInt(args[0]);
        String filePath = args[1];

        try {
            // parsing a CSV file into CSVReader class constructor
            CSVReader csvReader = new CSVReader(new FileReader(filePath));
            // Accounting Service for calculating the points after spending
            AccountingRequest request = new AccountingRequest(csvReader, pointsToSpend);
            AccountingService service = new AccountingServiceImpl();
            AccountingResponse response = service.calculatePoints(request);
            // print JSON output
            System.out.println(response.getJson().toString(4));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + filePath);
            e.printStackTrace();
        }
    }
}
