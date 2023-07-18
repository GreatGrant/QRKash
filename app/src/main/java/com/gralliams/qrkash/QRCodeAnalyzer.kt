package com.gralliams.qrkash

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class QRCodeAnalyzer(private val qrCodeListener: (ImageProxy) -> Unit) : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        qrCodeListener(imageProxy)
    }
}