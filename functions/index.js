const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const db = admin.firestore();

// Define your webhook endpoint
exports.flutterwaveWebhook = functions.https.onRequest(async (req, res) => {
  try {
    // Parse the webhook payload
    const { event, data } = req.body;
    const { tx_ref, charged_amount, account_id } = data;

    // Get the user's wallet document
    const walletDoc = await db.collection('wallets').doc(account_id).get();
    if (!walletDoc.exists) {
      console.error('Wallet document not found');
      return res.status(404).json({ error: 'Wallet document not found' });
    }

    // Update the wallet balance based on the event type
    const walletData = walletDoc.data();
    const currentBalance = walletData.balance;

    // Handle different events that affect the wallet balance
    switch (event) {
      case 'charge.completed':
        const updatedBalance = currentBalance + charged_amount;
        await walletDoc.ref.update({ balance: updatedBalance });
        break;

      case 'charge.reversed':
        // Parse the refunded amount from the event payload
        const refundedAmount = data.refunded_amount || 0;

        // Ensure the refunded amount does not exceed the balance
        if (refundedAmount > currentBalance) {
          console.error('Refunded amount exceeds wallet balance');
          return res.status(400).json({ error: 'Refunded amount exceeds wallet balance' });
        }

        const newBalance = currentBalance - refundedAmount;
        await walletDoc.ref.update({ balance: newBalance });
        break;

      // Add more cases for other relevant events if needed

      default:
        // For unhandled events, do nothing and log the event
        console.error('Unhandled webhook event:', event);
        return res.status(400).json({ error: 'Unhandled webhook event' });
    }

    // Log the transaction in the wallet's transaction history
    const transactionHistoryRef = walletDoc.ref.collection('transactionHistory').doc();
    await transactionHistoryRef.set({
      tx_ref,
      amount: charged_amount,
      timestamp: admin.firestore.FieldValue.serverTimestamp(),
    });

    // Return a success response
    return res.status(200).json({ message: 'Webhook processed successfully' });
  } catch (error) {
    console.error('Webhook processing error:', error);
    return res.status(500).json({ error: 'Webhook processing error' });
  }
});
