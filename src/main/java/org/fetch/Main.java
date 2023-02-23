package org.fetch;

import com.opencsv.CSVReader;
import org.fetch.dtos.requests.AccountingRequest;
import org.fetch.dtos.responses.AccountingResponse;
import org.fetch.service.AccountingService;
import org.fetch.service.AccountingServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        int amountToSpend = Integer.parseInt(args[0]);
        String filePath = args[1];
        System.out.println(amountToSpend);
        System.out.println(filePath);

        // parsing a CSV file into CSVReader class constructor
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(filePath));
            AccountingRequest request = new AccountingRequest(csvReader, amountToSpend);
            //todo: Guice injection
            AccountingService service = new AccountingServiceImpl();
            AccountingResponse response = service.calculatePoints(request);
            System.out.println(response.getJson().toString(4));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + filePath);
            e.printStackTrace();
        }

    }
}
