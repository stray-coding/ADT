package com.coding.compose.base

import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import com.coding.dec.utils.Paths
import org.jetbrains.skiko.toBitmap
import java.io.File
import javax.imageio.ImageIO

object MyIcons {
    fun getAppIconPainter(): Painter {
        val buffer = ImageIO.read(File(Paths.getIcon()))
        return BitmapPainter(buffer.toBitmap().asComposeImageBitmap())
    }

    fun getOnTopNormalPainter(): Painter {
        val buffer = ImageIO.read(File(Paths.getOnTopNormal()))
        return BitmapPainter(buffer.toBitmap().asComposeImageBitmap())
    }

    fun getOnTopSelectedPainter(): Painter {
        val buffer = ImageIO.read(File(Paths.getOnTopSelected()))
        return BitmapPainter(buffer.toBitmap().asComposeImageBitmap())
    }
}