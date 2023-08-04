package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shepherdmoney.interviewproject.model.*;
import com.shepherdmoney.interviewproject.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)
    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;

    public CreditCardController(CreditCardRepository creditCardRepository, UserRepository userRepository) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length
        // Create a credit card entity
        CreditCard creditCard = new CreditCard();
        creditCard.setIssuanceBank(payload.getCardIssuanceBank());
        creditCard.setNumber(payload.getCardNumber());

        // Find the user from the database using the payload.getUserId() method
        User user = userRepository.findById(payload.getUserId()).orElse(null);

        if (user != null) {
            // Associate the credit card with the user
            creditCard.setUser(user);

            // Save the credit card to the database
            creditCardRepository.save(creditCard);

            // Return 200 OK with the credit card id
            return ResponseEntity.ok(creditCard.getId());
        } else {
            // Return 404 Not Found if the user does not exist
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return null
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Get the list of credit cards associated with the user
            List<CreditCard> creditCards = user.getCreditCards();

            // Convert the list of CreditCard entities to a list of CreditCardView DTOs
            List<CreditCardView> creditCardViews = creditCards.stream()
                    .map(CreditCardView::fromCreditCard)
                    .collect(Collectors.toList());

            // Return 200 OK with the list of credit card views
            return ResponseEntity.ok(creditCardViews);
        } else {
            // Return empty list if the user does not exist
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        // Find the user from the database using the userId
        CreditCard creditCard = creditCardRepository.findByNumber(creditCardNumber);

        if (creditCard != null) {
            // Return 200 OK with the user id associated with the credit card
            return ResponseEntity.ok(creditCard.getId());
        } else {
            // Return 400 Bad Request if the credit card does not exist
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<Void> updateBalanceHistory(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.
        // Iterate through the payload and update the credit card balance history for each transaction
        for (UpdateBalancePayload transaction : payload) {
            CreditCard creditCard = creditCardRepository.findByNumber(transaction.getCreditCardNumber());
            if (creditCard == null) {
                // Return 400 Bad Request if the credit card does not exist
                return ResponseEntity.badRequest().build();
            }

            // Update the credit card balance history with the new transaction
            creditCard.updateBalanceHistory(transaction.getTransactionTime(), transaction.getTransactionAmount());

            // Save the updated credit card to the database
            creditCardRepository.save(creditCard);
        }

        // Return 200 OK if update is done and successful
        return ResponseEntity.ok().build();
    }
}
