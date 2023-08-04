package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @ManyToOne
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date DESC")
    private List<BalanceHistory> balanceHistory = new ArrayList<>();

    // Method to update the credit card's balance history with a new transaction
    // Method to update the credit card's balance history with a new transaction
    public void updateBalanceHistory(Instant transactionDate, double transactionAmount) {
        double currentBalance = getBalance();
        double newBalance = currentBalance + transactionAmount;

        BalanceHistory newBalanceHistory = new BalanceHistory();
        newBalanceHistory.setDate(transactionDate);
        newBalanceHistory.setBalance(newBalance);

        balanceHistory.add(0, newBalanceHistory);
    }

    public double getBalance() {
        if (!balanceHistory.isEmpty()) {
            return balanceHistory.get(0).getBalance();
        } else {
            return 0.0;
        }
    }
}
