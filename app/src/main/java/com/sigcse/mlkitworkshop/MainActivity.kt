package com.sigcse.mlkitworkshop

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.sigcse.mlkitworkshop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var imageIndex: Int = 1
    private val maxIndex = 5
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val REQUEST_CAMERA_PERMISSIONS = 1
        const val IMAGE_CAPTURE_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.textOutput.showSoftInputOnFocus = false
        binding.textOutput.isFocusable = false
    }

    fun onText(view: View) {

    }
    
    fun onLabel(view: View) {

    }

    fun onFace(view: View) {

    }

    private fun toTextBox(label: String, value: Any?) {
        binding.textOutput.append("$label: $value\n")
    }

    private fun drawBox(bounds: Rect, label: String, boxColor: Int, textColor: Int) {
        val drawingView = DrawingView(applicationContext, bounds, label, boxColor, textColor)
        val bitmap = binding.imageHolder.drawToBitmap()
        drawingView.draw(Canvas(bitmap))
        runOnUiThread { binding.imageHolder.setImageBitmap(bitmap) }
    }

    private fun drawLine(points: List<PointF>, lineColor: Int) {
        val drawingView = DrawingLineView(applicationContext, points, lineColor)
        val bitmap = binding.imageHolder.drawToBitmap()
        drawingView.draw(Canvas(bitmap))
        runOnUiThread { binding.imageHolder.setImageBitmap(bitmap) }
    }

    private fun addImage(x: Float, y: Float, bounds: Rect, angle: Float, fileName: String) {
        val img = ImageView(this)
        val resID = resources.getIdentifier(fileName, "drawable", packageName)
        img.setImageResource(resID)
        binding.frame.addView(img)
        val params = img.layoutParams
        params.height = bounds.height()
        params.width = bounds.width()
        img.layoutParams = params
        img.visibility = View.VISIBLE
        img.x = x - (bounds.width()/2)
        img.y = y - (bounds.height()/2)
        img.rotation = angle
        img.bringToFront()
    }

    fun launchCamera(view: View) {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA),REQUEST_CAMERA_PERMISSIONS)
        } else {
            val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)
                }
                return
            }
            else -> {
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_CAPTURE_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                val bitmap = data!!.extras!!["data"] as Bitmap
                binding.imageHolder.setImageBitmap(bitmap)
            }
        }
    }

    fun onNext(view: View) {
        imageIndex++
        if(imageIndex > maxIndex) {
            imageIndex = 1
        }
        val resID = resources.getIdentifier("pic$imageIndex", "drawable", packageName)
        binding.imageHolder.setImageResource(resID)
        binding.textOutput.setText("")
        binding.frame.removeAllViews()
        binding.frame.addView(binding.imageHolder)
    }

    fun onPrev(view: View) {
        imageIndex--
        if(imageIndex <= 0) {
            imageIndex = maxIndex
        }
        val resID = resources.getIdentifier("pic$imageIndex", "drawable", packageName)
        binding.imageHolder.setImageResource(resID)
        binding.textOutput.setText("")
        binding.frame.removeAllViews()
        binding.frame.addView(binding.imageHolder)
    }
}
