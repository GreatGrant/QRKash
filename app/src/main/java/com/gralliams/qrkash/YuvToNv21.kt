package com.gralliams.qrkash

import android.media.Image

class YuvToNv21(image: Image) {
    val nv21ByteArray: ByteArray

    init {
        val bufferY = image.planes[0].buffer
        val bufferU = image.planes[1].buffer
        val bufferV = image.planes[2].buffer

        val lengthY = bufferY.remaining()
        val lengthU = bufferU.remaining()
        val lengthV = bufferV.remaining()

        val data = ByteArray(lengthY + lengthU + lengthV)

        bufferY.get(data, 0, lengthY)
        bufferU.get(data, lengthY, lengthU)
        bufferV.get(data, lengthY + lengthU, lengthV)

        nv21ByteArray = data
    }
}
