const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
const app = express();

// Initialize the Firebase Admin SDK
admin.initializeApp();

app.use(express.json());

// Define your webhook endpoint
app.post('/webhook', (req, res) => {
  const authToken = req.headers.authorization;

  admin
    .auth()
    .verifyIdToken(authToken)
    .then((decodedToken) => {
      // Request is legitimate
      const event = req.body;
      handleWebhookEvent(event);
      res.status(200).end();
    })
    .catch((error) => {
      // Request is not legitimate
      res.status(401).end();
    });
});

// Handle the webhook event
function handleWebhookEvent(event) {
  // Process the webhook event here
  console.log('Received webhook event:', event);
  // Add your custom logic to handle the event
   const { event: eventType, data } = event;

    switch (eventType) {
      case 'charge.completed':
        handleChargeCompletedEvent(data);
        break;
      case 'payment.failed':
        handlePaymentFailedEvent(data);
        break;
      // Add more cases for other event types you want to handle
      default:
        console.log('Unsupported event type:', eventType);
        break;
    }
}

async function handleChargeCompletedEvent(data) {
  // Extract relevant data from the event payload
  const { tx_ref, amount, currency, customer } = data;

  try {
    // Get a reference to the customer's document in your Firestore database
    const customerRef = admin.firestore().collection('customers').doc(customer.id);

    // Update the customer's wallet balance or payment status
    await customerRef.update({
      walletBalance: admin.firestore.FieldValue.increment(amount),
      paymentStatus: 'completed',
      lastPayment: {
        amount,
        currency,
        timestamp: admin.firestore.FieldValue.serverTimestamp(),
      },
    });

    console.log('Charge completed event:', data);
    console.log('Crediting customer wallet:', customer.email);
    console.log('Amount:', amount, currency);
  } catch (error) {
    console.error('Error updating customer data:', error);
  }
}



// Export the Express app as a Cloud Function
exports.webhookEndpoint = functions.https.onRequest(app);

