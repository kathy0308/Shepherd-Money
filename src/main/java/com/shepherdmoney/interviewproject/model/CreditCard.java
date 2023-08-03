package com.shepherdmoney.interviewproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    // TODO: Credit card's owner. For detailed hint, please see User class
    private User user;

    // TODO: Credit card's balance history. It is a requirement that the dates in the balanceHistory 
    //       list must be in chronological order, with the most recent date appearing first in the list. 
    //       Additionally, the first object in the list must have a date value that matches today's date, 
    //       since it represents the current balance of the credit card. For example:
    //       [
    //         {date: '2023-04-13', balance: 1500},
    //         {date: '2023-04-12', balance: 1200},
    //         {date: '2023-04-11', balance: 1000},
    //         {date: '2023-04-10', balance: 800}
    //       ]
    private List<BalanceHistory> balanceHistory;

    // Method to update the credit card's balance history with a new transaction
    // Method to update the credit card's balance history with a new transaction
    public void updateBalanceHistory(Instant transactionDate, double transactionAmount) {
        // Get the current balance
        double currentBalance = getBalance();

        // Create a new balance history entry for the transaction
        BalanceHistory newBalanceHistory = new BalanceHistory();
        newBalanceHistory.setDate(transactionDate);
        newBalanceHistory.setBalance(currentBalance + transactionAmount);

        // Add the new balance history entry to the beginning of the list (to maintain chronological order)
        balanceHistory.add(0, newBalanceHistory);
    }

    // Other methods

    // Helper method to get the current balance of the credit card
    public double getBalance() {
        if (!balanceHistory.isEmpty()) {
            return balanceHistory.get(0).getBalance();
        } else {
            return 0.0;
        }
    }


}
