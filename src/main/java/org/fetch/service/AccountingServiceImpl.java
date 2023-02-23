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
        // process the transactions - handle negative points by deducting them from other positive points (per payer)
        List<Transaction> processedTransactions = TransactionProcessor.process(inputTransactions);
        Map<String, Integer> resultMap = spendPoints(processedTransactions, request.getPointsToSpend());
        return new AccountingResponse(new JSONObject(resultMap));
    }

    private Map<String, Integer> spendPoints(List<Transaction> transactions, int pointsToSpend) {
        // populate initial balances
        Map<String, Integer> payerBalanceMap = populateTotalBalances(transactions);

        // deduct amount starting from oldest transaction first
        for (Transaction transaction : transactions) {
            String payer = transaction.getPayer();
            int tnxPoints = transaction.getPoints();
            int payerBalance = payerBalanceMap.get(payer);
            // spend all the points
            if (pointsToSpend >= tnxPoints) {
                // spend the amount and deduct from payer's total balance
                pointsToSpend -= tnxPoints;
                payerBalanceMap.put(transaction.getPayer(), payerBalance - tnxPoints);
            } else {
                payerBalanceMap.put(transaction.getPayer(), payerBalance - pointsToSpend);
                break;
            }
        }
        return payerBalanceMap;
    }

    private Map<String, Integer> populateTotalBalances(List<Transaction> transactions) {
        Map<String, Integer> payerBalanceMap = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (payerBalanceMap.containsKey(transaction.getPayer())) {
                int currentBalance = payerBalanceMap.get(transaction.getPayer());
                int newBalance = currentBalance + transaction.getPoints();
                payerBalanceMap.put(transaction.getPayer(), newBalance);
            } else {
                payerBalanceMap.put(transaction.getPayer(), transaction.getPoints());
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
            }
        } catch (Exception e) {
            System.out.println("Error while reading csv");
            e.printStackTrace();
        }
        return transactionList;
    }
}
