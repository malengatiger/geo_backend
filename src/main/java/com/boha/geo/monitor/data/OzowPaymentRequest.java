package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/*
The bank that the payment should be made to. Merchant
needs to be enabled to send through banking details (18 - 21) in
the post, for most merchants this is setup when creating the
merchant account.
Allowed values are:
• ABSA – 3284A0AD-BA78-4838-8C2B-102981286A2B
• Capitec - 913999FA-3A32-4E3D-82F0-A1DF7E9E4F7B
• FNB - 4816019C-3314-4C80-8B6B-B2CD16DCC4EC
• Nedbank - BF0561FD-4203-4A0C-9174-
CB26FCD87A60
• Standard Bank - AD7D8DA4-1723-4066-94BB6662D845E483
• Investec - 4B45BE85-B616-4BD1-9027-F8FCF8F9AF7B

     🌺  🌺 Token Payment
        If you have previously received a token for the user you only need to pass the fields below along with the post
        variables in Step 1 to initiate a tokenised payment. Only the TokenProfileId will need to appended to the
        concatenated post variables to calculate the post hash check. The token field is not used in the hash check
        calculation.
        These fields are only required if you want the user to use the token you have saved for them to complete the
        payment.
        Property Type Req. Description
        1. TokenProfileId Guid Yes The token profile identifier returned in the token response
        sent to the TokenNotificationUrl.
        2. Token String (Max) Yes The token returned in the token response sent to the
        TokenNotificationUrl.
 */
@Data
@Document(collection = "ozowPaymentRequests")
public class OzowPaymentRequest {
    private String SiteCode, CountryCode, CurrencyCode,
            TransactionReference, BankReference;
    private String Optional1, Optional2, Optional3, Optional4, Optional5, Customer;
    private String CancelUrl, ErrorUrl, SuccessUrl, NotifyUrl,
            TokenNotificationUrl, TokenDeletedNotificationUrl;
    private boolean isTest, RegisterTokenProfile;
    private double Amount;
    private String BankId, BankAccountNumber, BranchCode,
            BankAccountName, PayeeDisplayName, HashCheck;
    private String TokenProfileId, Token;

    public static final String
            ABSA = "3284A0AD-BA78-4838-8C2B-102981286A2B",
            Capitec = "913999FA-3A32-4E3D-82F0-A1DF7E9E4F7B",
            FNB = "4816019C-3314-4C80-8B6B-B2CD16DCC4EC",
            StandardBank = "AD7D8DA4-1723-4066-94BB6662D845E483",
            Investec = "4B45BE85-B616-4BD1-9027-F8FCF8F9AF7B";


}
