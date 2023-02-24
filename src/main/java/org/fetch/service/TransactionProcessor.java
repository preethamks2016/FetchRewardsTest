package org.fetch.service;

import org.fetch.dtos.entities.Transaction;

import java.util.*;

import static org.fetch.utils.AccountingUtils.*;

public class TransactionProcessor {

    public static List<Transaction> process(List<Transaction> transactionList) {
        transactionList.sort(getTransactionComparator());
        printTransactionList(transactionList);
        // construct a Map of Payer with list of transactions per Payer in ascending order of timestamp
        Map<String, List<Transaction>> payerTransactionMap = buildPayerTransactionMap(transactionList);
        List<Transaction> finalTransactionList = convertToSortedTnxList(payerTransactionMap);
        printTransactionList(finalTransactionList);
        return finalTransactionList;
    }

    private static Map<String, List<Transaction>> buildPayerTransactionMap(List<Transaction> transactionList) {
        // list of transactions with positive points per payer
        Map<String, List<Transaction>> payerPositiveTransactionMap = new HashMap<>();
        // cumulative negative points associated with a payer
        Map<String, Integer> payerNegativeSumMap = new HashMap<>();
        // building the mapping
        for (Transaction transaction : transactionList) {
            if (transaction.getPoints() >= 0) {
                if (payerPositiveTransactionMap.containsKey(transaction.getPayer())) {
                    payerPositiveTransactionMap.get(transaction.getPayer()).add(transaction);
                } else {
                    List<Transaction> list = new LinkedList<>();
                    list.add(transaction);
                    payerPositiveTransactionMap.put(transaction.getPayer(), list);
                }
            } else {
                // if negative then accumulate the negative points for that payer
                if (payerNegativeSumMap.containsKey(transaction.getPayer())) {
                    int negativeSum = payerNegativeSumMap.get(transaction.getPayer());
                    negativeSum -= transaction.getPoints();
                    payerNegativeSumMap.put(transaction.getPayer(), negativeSum);
                } else {
                    payerNegativeSumMap.put(transaction.getPayer(), -1 * transaction.getPoints());
                }
            }

        }
        printPayerWiseTnxs(payerPositiveTransactionMap);
        handleNegativeTransactions(payerPositiveTransactionMap, payerNegativeSumMap);
        printPayerWiseTnxs(payerPositiveTransactionMap);
        return payerPositiveTransactionMap;
    }

    private static void handleNegativeTransactions(Map<String, List<Transaction>> map, Map<String, Integer> payerNegativeSumMap) {
        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            int negativeSum = payerNegativeSumMap.getOrDefault(entry.getKey(), 0);
            // subtract and remove the elements starting from the oldest entry, until negative sum reaches 0
            LinkedList<Transaction> payerTransactions = (LinkedList<Transaction>) entry.getValue();
            while (negativeSum > 0) {
                Transaction oldestTnx = payerTransactions.getFirst();
                if (negativeSum >= oldestTnx.getPoints()) {
                    payerTransactions.removeFirst();
                    negativeSum -= oldestTnx.getPoints();
                } else {
                    oldestTnx.setPoints(oldestTnx.getPoints() - negativeSum);
                    break;
                }
            }
        }
    }

    private static List<Transaction> convertToSortedTnxList(Map<String, List<Transaction>> map) {
        List<Transaction> result = new ArrayList<>();
        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            result.addAll(entry.getValue());
        }
        result.sort(getTransactionComparator());
        return result;
    }
}
