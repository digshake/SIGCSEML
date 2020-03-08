package com.example.cse438.sigcseml

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class DrawingView(context: Context, var boundingBox: Rect, var label: String, var boxColor: Int, var textColor: Int) : View(context) {

    val MAX_FONT_SIZE = 96F

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT

        pen.color = boxColor
        pen.strokeWidth = 8F
        pen.style = Paint.Style.STROKE
        canvas.drawRect(boundingBox, pen)

        // Draw label
        var tagSize = Rect(0, 0, 0, 0)
        var length = label.length

        // calculate the right font size
        pen.style = Paint.Style.FILL_AND_STROKE
        pen.color = textColor
        pen.strokeWidth = 2F

        pen.textSize = MAX_FONT_SIZE
        pen.getTextBounds(label, 0, label.length, tagSize)
        val fontSize: Float = pen.textSize * boundingBox.width() / tagSize.width()

        // adjust the font size so texts are inside the bounding box
        if (fontSize < pen.textSize) pen.textSize = fontSize

        var margin = (boundingBox.width() - tagSize.width()) / 2.0F
        if (margin < 0F)margin = 0F

        // draw tags onto bitmap (bmp is in upside down format)
        canvas.drawText(
            label, boundingBox.left + margin,
            boundingBox.top + tagSize.height().toFloat(), pen
        )

    }
}

