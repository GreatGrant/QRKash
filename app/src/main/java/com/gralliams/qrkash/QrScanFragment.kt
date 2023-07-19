package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gralliams.qrkash.databinding.FragmentQrScanBinding
import com.gralliams.qrkash.model.DecryptedInfo
import com.gralliams.qrkash.viewmodel.QRCodeViewModel
import com.gralliams.qrkash.viewmodel.ScannedSharedViewModel
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrScanFragment : Fragment() {
    private lateinit var sharedViewModel: ScannedSharedViewModel
    private lateinit var binding: FragmentQrScanBinding
    private lateinit var qrCodeViewModel: QRCodeViewModel
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_scan, container, false)

        return binding.root
    }

    @ExperimentalGetImage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qrCodeViewModel = ViewModelProvider(requireActivity())[QRCodeViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScannedSharedViewModel::class.java]

        qrCodeViewModel.scannedText.observe(viewLifecycleOwner) { scannedText ->
            //Todo() Check if the scanned data has a valid format
            // Check if the scanned data has a valid format
            val decryptedInfo = decryptQR(scannedText)

            if (decryptedInfo.isDataValid()) {
                // If it's valid, set it in the sharedViewModel and navigate to the next fragment
                sharedViewModel.setScannedData(scannedText)
                findNavController().navigate(R.id.action_qrScanFragment2_to_transferFragment2)
            } else {
                // If it's invalid, show a toast message
                Toast.makeText(requireContext(), "$scannedText Invalid scanned data format.\nEnsure the code you are scanning is generated from QRKash.", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize the camera executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Set up the camera preview and start scanning
        startCamera()


    }

    @ExperimentalGetImage
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QRCodeAnalyzer { imageProxy ->
                        processImage(imageProxy)
                    })
                }

            try {
                cameraProvider.unbindAll()

                // Select back camera as the default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)

            } catch (ex: Exception) {
                // Handle camera initialization error
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @ExperimentalGetImage
    private fun processImage(imageProxy: ImageProxy) {
        val image = imageProxy.image ?: return

        val nv21ByteArray = YuvToNv21(image).nv21ByteArray

        // Pass the converted image and dimensions to the ViewModel for decoding
        qrCodeViewModel.decodeQRCode(nv21ByteArray, image.width, image.height)

        imageProxy.close()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    private fun DecryptedInfo.isDataValid(): Boolean {
        // Implement your logic here to check if the decrypted data is valid
        // For example, you can check if recipient, amount, account, email, and bank are not empty
        return recipient.isNotEmpty() && amount.isNotEmpty() && account.isNotEmpty() && email.isNotEmpty() && bank.isNotEmpty()
    }

    private fun decryptQR(stringToDecrypt: String?): DecryptedInfo {
        // Extracting recipient
        val recipientRegex = Regex("Recipient:\\s(.*?),")
        val recipientMatch = stringToDecrypt?.let { recipientRegex.find(it) }
        val recipient = recipientMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting amount
        val amountRegex = Regex("amount:\\s(.*?),")
        val amountMatch = stringToDecrypt?.let { amountRegex.find(it) }
        val amount = amountMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting email
        val emailRegex = Regex("email:\\s(.*?),")
        val emailMatch = stringToDecrypt?.let { emailRegex.find(it) }
        val email = emailMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting account
        val accountRegex = Regex("account:\\s(.*?),")
        val accountMatch = stringToDecrypt?.let { accountRegex.find(it) }
        val account = accountMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting bank
        val bankRegex = Regex("bank:\\s(.*)")
        val bankMatch = stringToDecrypt?.let { bankRegex.find(it) }
        val bank = bankMatch?.groupValues?.get(1)?.trim() ?: ""

        return DecryptedInfo(recipient, amount, email, account, bank)
    }

}

fun ByteBuffer.toByteArray(): ByteArray {
    rewind() // Rewind the buffer to copy the whole content
    val data = ByteArray(remaining())
    get(data)
    return data
}

