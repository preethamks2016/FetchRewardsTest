package org.fetch.service;

import org.fetch.dtos.entities.Transaction;

import java.util.*;

public class TransactionProcessor {

    private static void printTransactionList(List<Transaction> transactionList) {
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getPayer() + ", " + transaction.getPoints());
        }
        System.out.println();
    }

    private static void printPayerWiseTnxs(Map<String, List<Transaction>> map) {
        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            System.out.print(entry.getKey()+ ": ");
            for(Transaction tnx: entry.getValue()) {
                System.out.print(tnx.getPoints()+ ", ");
            }
        }
        System.out.println("\n");
    }

    public static List<Transaction> process(List<Transaction> transactionList) {
        Comparator<Transaction> transactionComparator = Comparator.comparing(Transaction::getTimestamp);
        transactionList.sort(transactionComparator);
        printTransactionList(transactionList);
        Map<String, List<Transaction>> payerTransactionMap = buildPayerTransactionMap(transactionList);
        List<Transaction> finalTransactionList = convertToSortedTnxList(payerTransactionMap, transactionComparator);
        printTransactionList(finalTransactionList);
        return finalTransactionList;
    }

    private static Map<String, List<Transaction>> buildPayerTransactionMap(List<Transaction> transactionList) {
        Map<String, List<Transaction>> payerPositiveTransactionMap = new HashMap<>();
        Map<String, Integer> payerNegativeSumMap = new HashMap<>();
        // building a mapping of per Payer transactions
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
                // if negative accumulate the negative
                if (payerNegativeSumMap.containsKey(transaction.getPayer())) {
                    int negativeSum = payerNegativeSumMap.get(transaction.getPayer());
                    negativeSum -= transaction.getPoints();
                    payerNegativeSumMap.put(transaction.getPayer(), negativeSum);
                } else {
                    payerNegativeSumMap.put(transaction.getPayer(), -1*transaction.getPoints());
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

    private static List<Transaction> convertToSortedTnxList(Map<String, List<Transaction>> map, Comparator<Transaction> transactionComparator) {
        List<Transaction> result = new ArrayList<>();
        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            result.addAll(entry.getValue());
        }
        result.sort(transactionComparator);
        return result;
    }
}
