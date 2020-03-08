package com.example.cse438.sigcseml

import android.content.Context
import android.graphics.*
import android.view.View
import com.google.firebase.ml.vision.common.FirebaseVisionPoint

class DrawingLineView(context: Context, var points: List<FirebaseVisionPoint>, var lineColor: Int) : View(context) {


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pen = Paint()

        pen.color = lineColor
        pen.strokeWidth = 8F
        pen.style = Paint.Style.STROKE

        val path = Path()
        path.moveTo(points[0].x, points[0].y)
        points.forEach {
            path.lineTo(it.x, it.y)
        }
        path.close()
        canvas.drawPath(path, pen)
    }
}

