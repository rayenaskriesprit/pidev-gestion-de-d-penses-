package org.example.dao;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    static {
        Stripe.apiKey = "sk_test_51Qwp5IPD3ejOGFRSMUWzyjh911w6CB7qtmLGTMbp97A1O1ig81rivtFvsvB7oCgDyg8KUVWCoepUqNz2H5wHHTp300oj5Gc2gY";
    }

    public static String processPayment(double amount, String name, String email, String token) throws StripeException {
        // Charge the card with the provided token
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (amount * 100)); // Convert dollars to cents
        chargeParams.put("currency", "usd");
        chargeParams.put("source", token); // Use the provided token (tok_visa)
        chargeParams.put("description", "Payment from " + name + " - " + email);
        chargeParams.put("receipt_email", email); // Send receipt to user's email

        Charge charge = Charge.create(chargeParams);
        return charge.getId();
    }
}
