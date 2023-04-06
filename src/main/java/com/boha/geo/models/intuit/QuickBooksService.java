package com.boha.geo.models.intuit;

import org.springframework.stereotype.Service;

@Service
public class QuickBooksService {
}

/*
Request - Create a token
Operation: POST (from your browser) https://api.intuit.com/quickbooks/v4/payments/tokens
Content type: application/json
will send object Charge


Create the authorization
Now create a charges authorization using the token created in the last section. Life time of a token is 15 minutes and is for one time use. This is the first half of a credit card transaction, with the charges.capture attribute set to false, and is used to get authorization that the merchandise will be paid by the card issuer.

Request - send CreateAuthorization
Operation: POST (from your server) https://api.intuit.com/quickbooks/v4/payments/charges
Content type: application/json

receive AuthorizationResponse


Capture the charge
At fulfillment time, you capture the charge.

Request - send AmountBag with id from AuthorizationResponse
Operation: POST (from your server) https://api.intuit.com/quickbooks/v4/payments/charges/<id>/capture
where ``<id>`` is the id from the authorization response. For this example, ``<id>`` is ECZG9D8YZ2WY.
Content type: application/json


Best practice: use automatic reconciliation
When creating the sales receipt, set up the sale to be automatically reconciled with the QuickBooks company account register. After the transaction settles from merchant services, QuickBooks Online automatically creates a deposit in the merchant’s company file.

In order to take advantage of automatic reconciliation, set the following parameters in the salesreceipt create request:

CreditCardPayment.CreditChargeResponse.CCTransId to the value returned in the original QuickBooks Payments charge response.
CreditCardPayment.CrecitChargeInfo.ProcessPayment to true. This triggers QuickBooks Online services to store the credit card charge response information, in particular the CCTransId field that is fundamental to credit card transaction reconciliation. This triggers the notation Transaction has been processed for the sales receipt on the QuickBooks user interface.
TxnSourcetoIntuitPayment. This inserts the transaction into a list of pending deposits to be automatically matched and reconciled with the merchant’s checking account.
Note

show production endpoint URLs, but request and response samples use sandbox objects

Request - send SalesReceipt
Operation: POST https://quickbooks.api.intuit.com/v3/company/<realmId>/salesreceipt
Content type: application/json

 */
