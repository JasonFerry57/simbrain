package org.simbrain.network.gui

import org.piccolo2d.PNode
import org.piccolo2d.nodes.PImage
import org.piccolo2d.nodes.PPath
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Image
import javax.swing.SwingUtilities

class ImageBox(val width: Int, val height: Int, thickness: Float) : PNode() {

    var image: Image? = null
        set(image) {
            field = image
            SwingUtilities.invokeLater {
                pImage.image = image
                setBounds(0.0, 0.0, width.toDouble(), height.toDouble())
            }
        }

    private val box = PPath.createRectangle(0.0, 0.0, width.toDouble(), height.toDouble())!!
            .apply { stroke = BasicStroke(thickness).also { strokePaint = Color.ORANGE } }
            .also { addChild(it) }

    private val pImage = PImage().also { addChild(it) }


}