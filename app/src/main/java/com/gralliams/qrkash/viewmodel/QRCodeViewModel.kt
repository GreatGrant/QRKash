package com.gralliams.qrkash.viewmodel

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.nio.ByteBuffer

class QRCodeViewModel : ViewModel() {
    private val _qrCodeImage = MutableLiveData<Bitmap>()
    private val _scannedText = MutableLiveData<String>()

    private val TAG = "ViewModel"
    // Expose the LiveData for observing the QR code image updates
    val qrCodeImage: LiveData<Bitmap> get() = _qrCodeImage
    val scannedText: LiveData<String> get() = _scannedText


    // Method to generate the QR code based on the input amount
    fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            _qrCodeImage.value = bitmap
        } catch (e: WriterException) {
            Log.d(TAG, "generateQRCode: ${e.message}")
        }
        return bitmap
    }

    // Method to decode the QR code and extract the text
    fun decodeQRCode(imageBuffer: ByteBuffer, width: Int, height: Int) {
        val inputImage = InputImage.fromByteBuffer(imageBuffer, width, height, 0, InputImage.IMAGE_FORMAT_NV21)

        // Set up barcode scanner options
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        // Create the barcode scanner
        val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

        // Process the input image and get the QR code result
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                // Check if there are any barcodes detected
                if (barcodes.isNotEmpty()) {
                    // Get the first barcode (QR code) from the result
                    val barcode = barcodes[0]
                    // Extract the text from the QR code
                    val extractedText = barcode.rawValue ?: ""
                    // Update the LiveData with the extracted text
                    _scannedText.value = extractedText
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error decoding QR code: ${e.message}")
                // Handle the decoding error if needed
            }
    }
}
