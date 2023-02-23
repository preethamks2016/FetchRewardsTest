package org.fetch.service;

import com.opencsv.CSVReader;
import org.fetch.dtos.entities.Transaction;
import org.fetch.dtos.requests.AccountingRequest;
import org.fetch.dtos.responses.AccountingResponse;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountingServiceImpl implements AccountingService {
    @Override
    public AccountingResponse calculatePoints(AccountingRequest request) {
        List<Transaction> inputTransactions = constructInputTransactions(request.getCsvReader());
        List<Transaction> processedTransactions = TransactionProcessor.process(inputTransactions);
        Map<String, Integer> resultMap = spendAndDeductAmount(processedTransactions, request.getAmountToSpend());
        return new AccountingResponse(new JSONObject(resultMap));
    }

    private Map<String, Integer> spendAndDeductAmount(List<Transaction> transactions, int amountToSpend) {
        Map<String, Integer> payerBalanceMap = new HashMap<>();
        // populate initial balances
        for (Transaction transaction : transactions) {
            if (payerBalanceMap.containsKey(transaction.getPayer())) {
                int currentBalance = payerBalanceMap.get(transaction.getPayer());
                int newBalance = currentBalance + transaction.getPoints();
                payerBalanceMap.put(transaction.getPayer(), newBalance);
            } else {
                payerBalanceMap.put(transaction.getPayer(), transaction.getPoints());
            }
        }

        // deduct amount starting from oldest first
        for (Transaction transaction : transactions) {
            String payer = transaction.getPayer();
            int tnxPoints = transaction.getPoints();
            if (amountToSpend >= tnxPoints) {
                // spend the amount and deduct from payer's total balance
                amountToSpend -= tnxPoints;
                int payerBalance = payerBalanceMap.get(payer);
                int newBalance = payerBalance - tnxPoints;
                payerBalanceMap.put(transaction.getPayer(), newBalance);
            } else {
                int payerBalance = payerBalanceMap.get(payer);
                int newBalance = payerBalance - amountToSpend;
                payerBalanceMap.put(transaction.getPayer(), newBalance);
                break;
            }
        }
        return payerBalanceMap;
    }

    private List<Transaction> constructInputTransactions(CSVReader csvReader) {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            // skip the first row which contains column names of the transaction table
            csvReader.readNext();
            // read one line at a time and add to transaction list
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                String payer = nextLine[0];
                int points = Integer.parseInt(nextLine[1]);
                long timestamp = Instant.parse((nextLine[2])).toEpochMilli();
                transactionList.add(new Transaction(payer, points, timestamp));
//                System.out.print(payer);
//                System.out.print(", ");
//                System.out.print(amount);
//                System.out.print(", ");
//                System.out.print(ts);
//                System.out.println(", ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionList;
    }
}
