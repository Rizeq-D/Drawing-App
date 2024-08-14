package com.example.drawingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var brushButton: Button
    private var isBrushMode = true
    private var currentShape: Shape = Shape.FREE_DRAW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        drawingView = findViewById(R.id.drawingView)
        brushButton = findViewById(R.id.button_brush)

        drawingView.setShape(currentShape)

        brushButton.setOnClickListener {
            if (isBrushMode) {

                currentShape = Shape.CIRCLE
                drawingView.setShape(currentShape)
                brushButton.text = getString(R.string.brush)
            } else {

                currentShape = Shape.FREE_DRAW
                drawingView.setShape(currentShape)
                brushButton.text = getString(R.string.brush)
            }
            isBrushMode = !isBrushMode
        }

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            drawingView.saveDrawing()
        }

        val clearButton: Button = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            drawingView.clearDrawing()
        }

        val fillButton: Button = findViewById(R.id.fillButton)
        fillButton.setOnClickListener {
            drawingView.setShape(Shape.FILL)
        }

        val fillScreenButton: Button = findViewById(R.id.fillScreenButton)
        fillScreenButton.setOnClickListener {
            drawingView.fillScreen()

            if (!isBrushMode) {
                currentShape = Shape.FREE_DRAW
                drawingView.setShape(currentShape)
                brushButton.text = getString(R.string.brush)
                isBrushMode = true
            }
        }

        val brushSizeSeekBar: SeekBar = findViewById(R.id.brushSizeSeekBar)
        brushSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val newThickness = progress.toFloat().coerceAtLeast(1f)
                drawingView.setStrokeWidth(newThickness)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val colorButtons = mapOf(
            R.id.colorWhite to ContextCompat.getColor(this, android.R.color.white),
            R.id.colorYellow to ContextCompat.getColor(this, android.R.color.holo_orange_light),
            R.id.colorLight_khaki to ContextCompat.getColor(this, R.color.Light_khaki),
            R.id.colorLight_brown to ContextCompat.getColor(this, R.color.Light_brown),
            R.id.colorLight_blue to ContextCompat.getColor(this, R.color.Light_blue),
            R.id.colorLightGrey to ContextCompat.getColor(this, R.color.LightGrey),
            R.id.colorDarkRed to ContextCompat.getColor(this, R.color.DarkRed),
            R.id.colorLighter_blue to ContextCompat.getColor(this, R.color.Lighter_blue),
            R.id.colorLight_green to ContextCompat.getColor(this, R.color.Light_green),
            R.id.colorOrange to ContextCompat.getColor(this, R.color.Orange),
            R.id.colorPurple_900 to ContextCompat.getColor(this, R.color.Purple_900),
            R.id.colorPurple_100 to ContextCompat.getColor(this, R.color.Purple_100),
            R.id.colorTeal_700 to ContextCompat.getColor(this, R.color.Teal_700),
            R.id.colorTeal_200 to ContextCompat.getColor(this, R.color.Teal_200),
            R.id.colorPurple_700 to ContextCompat.getColor(this, R.color.Purple_700),
            R.id.colorPurple_500 to ContextCompat.getColor(this, R.color.Purple_500),
            R.id.colorPurple_200 to ContextCompat.getColor(this, R.color.Purple_200),
            R.id.colorBlack to ContextCompat.getColor(this, android.R.color.black),
            R.id.colorRed to ContextCompat.getColor(this, android.R.color.holo_red_light),
            R.id.colorGreen to ContextCompat.getColor(this, android.R.color.holo_green_light),
            R.id.colorBlue to ContextCompat.getColor(this, android.R.color.holo_blue_light)
        )
        colorButtons.forEach { (buttonId, color) ->
            findViewById<Button>(buttonId).setOnClickListener {
                drawingView.setColor(color)
            }
        }
        
        val drawCircle: Button = findViewById(R.id.circle)
        val drawRectangle: Button = findViewById(R.id.rectangle)
        val drawTriangle: Button = findViewById(R.id.triangle)

        drawCircle.setOnClickListener {
            currentShape = Shape.CIRCLE
            drawingView.setShape(currentShape)
            brushButton.text = getString(R.string.brush)
            isBrushMode = false
        }
        drawRectangle.setOnClickListener {
            currentShape = Shape.RECTANGLE
            drawingView.setShape(currentShape)
            brushButton.text = getString(R.string.brush)
            isBrushMode = false
        }
        drawTriangle.setOnClickListener {
            currentShape = Shape.TRIANGLE
            drawingView.setShape(currentShape)
            brushButton.text = getString(R.string.brush)
            isBrushMode = false
        }
    }
}
