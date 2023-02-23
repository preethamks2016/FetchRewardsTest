package org.fetch.utils;

import org.fetch.dtos.entities.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AccountingUtils {

    private static Comparator<Transaction> comparator = null;

    // Singleton Design Pattern
    public static Comparator<Transaction> getTransactionComparator() {
        if (comparator == null) {
            comparator = Comparator.comparing(Transaction::getTimestamp);
        }
        return comparator;
    }

    public static void printTransactionList(List<Transaction> transactionList) {
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getPayer() + ", " + transaction.getPoints());
        }
        System.out.println();
    }

    public static void printPayerWiseTnxs(Map<String, List<Transaction>> map) {
        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (Transaction tnx : entry.getValue()) {
                System.out.print(tnx.getPoints() + ", ");
            }
        }
        System.out.println("\n");
    }
}
