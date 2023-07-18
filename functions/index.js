const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const db = admin.firestore();

// Define your webhook endpoint
exports.flutterwaveWebhook = functions.https.onRequest(async (req, res) => {
  try {
//    // Validate the request
//    const isValidRequest = validateRequest(req);
//    if (!isValidRequest) {
//      console.error('Invalid webhook request');
//      return res.status(400).end();
//    }

    // Parse the webhook payload
    const { tx_ref, charged_amount, account_id } = req.body.data;

    // Get the user's wallet document
    const walletDoc = await db.collection('wallets').doc(account_id).get();
    if (!walletDoc.exists) {
      console.error('Wallet document not found');
      return res.status(404).end();
    }

    // Update the wallet balance
    const walletData = walletDoc.data();
    const currentBalance = walletData.balance;
    const updatedBalance = currentBalance + charged_amount;

    // Update the wallet document in the database
    await walletDoc.ref.update({ balance: updatedBalance });

    // Log the transaction in the wallet's transaction history
    const transactionHistoryRef = walletDoc.ref.collection('transactionHistory').doc();
    await transactionHistoryRef.set({
      tx_ref,
      amount: charged_amount,
      timestamp: admin.firestore.FieldValue.serverTimestamp()
    });

    // Return a success response
    return res.status(200).end();
  } catch (error) {
    console.error('Webhook processing error:', error);
    return res.status(500).end();
  }
});
