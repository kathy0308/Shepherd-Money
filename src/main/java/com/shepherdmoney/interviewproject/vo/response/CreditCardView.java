package com.shepherdmoney.interviewproject.vo.response;

import com.shepherdmoney.interviewproject.model.CreditCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor // Add this annotation
public class CreditCardView {

    private String issuanceBank;
    private String number;

    // No need to define a separate no-argument constructor manually
    public static CreditCardView fromCreditCard(CreditCard creditCard) {
        CreditCardView creditCardView = new CreditCardView();
        creditCardView.setIssuanceBank(creditCard.getIssuanceBank());
        creditCardView.setNumber(creditCard.getNumber());
        // Set other fields as needed
        return creditCardView;
    }


}