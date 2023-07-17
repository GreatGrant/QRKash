const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const db = admin.firestore();


// Initialize Firebase Admin SDK
admin.initializeApp();

// Define your webhook endpoint
exports.flutterwaveWebhook = functions.https.onRequest(async (req, res) => {
//  try {
//    // Validate the request
//    const isValidRequest = validateRequest(req);
//    if (!isValidRequest) {
//      console.error('Invalid webhook request');
//      return res.status(400).end();
//    }

    // Parse the webhook payload
    const webhookEvent = req.body;
    const eventData = webhookEvent.data;

    // Extract relevant information from the payload
    const txRef = eventData.tx_ref;
    const chargedAmount = eventData.charged_amount;
    const accountId = eventData.account_id;

    // Get the user's wallet document
    const walletDoc = await db.collection('wallets').doc(accountId).get();
    if (!walletDoc.exists) {
      console.error('Wallet document not found');
      return res.status(404).end();
    }

    // Update the wallet balance
    const walletData = walletDoc.data();
    const currentBalance = walletData.balance;
    const updatedBalance = currentBalance + chargedAmount;

    // Update the wallet document in the database
    await walletDoc.ref.update({ balance: updatedBalance });

    // Log the transaction in the wallet's transaction history
    const transactionHistoryRef = walletDoc.ref.collection('transactionHistory').doc();
    await transactionHistoryRef.set({
      txRef,
      amount: chargedAmount,
      timestamp: admin.firestore.FieldValue.serverTimestamp()
    });

    // Return a success response
    return res.status(200).end();
  } catch (error) {
    console.error('Webhook processing error:', error);
    return res.status(500).end();
  }
});

// Validate the webhook request (example implementation, adjust to your needs)
function validateRequest(req) {
  const signature = req.headers['x-flutterwave-signature'];
  const payload = req.rawBody;
  // Verify the signature using your preferred method
  // Return true if the signature is valid, false otherwise
  return verifySignature(signature, payload);
}

// Example signature verification function (adjust to your needs)
function verifySignature(signature, payload) {
  // Implement the signature verification logic
  // Return true if the signature is valid, false otherwise
  return true;
}
