package com.example.drawingapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Stack

enum class Shape {
    FREE_DRAW, CIRCLE, RECTANGLE, TRIANGLE, FILL
}

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var drawPath: Path = Path()
    private var drawPaint: Paint = Paint()
    private var canvasPaint: Paint = Paint(Paint.DITHER_FLAG)
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    private var currentColor: Int = Color.BLACK
    private var strokeWidth: Float = 10f

    private var startX: Float = 0f
    private var startY: Float = 0f
    private var endX: Float = 0f
    private var endY: Float = 0f

    private var currentShape: Shape = Shape.FREE_DRAW

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        drawPaint.color = currentColor
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = strokeWidth
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)

        when (currentShape) {
            Shape.FREE_DRAW -> canvas.drawPath(drawPath, drawPaint)
            Shape.CIRCLE -> {
                val radius = Math.hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat()
                canvas.drawCircle(startX, startY, radius, drawPaint)
            }
            Shape.RECTANGLE -> canvas.drawRect(startX, startY, endX, endY, drawPaint)
            Shape.TRIANGLE -> {
                val path = Path().apply {
                    moveTo((startX + endX) / 2, startY)
                    lineTo(startX, endY)
                    lineTo(endX, endY)
                    close()
                }
                canvas.drawPath(path, drawPaint)
            }
            Shape.FILL -> {

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                if (currentShape == Shape.FILL) {
                    fillArea(startX, startY)
                    invalidate()
                    return true
                } else if (currentShape == Shape.FREE_DRAW) {
                    drawPath.moveTo(startX, startY)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                if (currentShape == Shape.FREE_DRAW) {
                    drawPath.lineTo(endX, endY)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (currentShape == Shape.FREE_DRAW) {
                    drawCanvas?.drawPath(drawPath, drawPaint)
                    drawPath.reset()
                } else {
                    drawShapeOnCanvas()
                }
            }
        }
        return true
    }

    private fun drawShapeOnCanvas() {
        when (currentShape) {
            Shape.CIRCLE -> {
                val radius = Math.hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat()
                drawCanvas?.drawCircle(startX, startY, radius, drawPaint)
            }
            Shape.RECTANGLE -> drawCanvas?.drawRect(startX, startY, endX, endY, drawPaint)
            Shape.TRIANGLE -> {
                val path = Path().apply {
                    moveTo((startX + endX) / 2, startY)
                    lineTo(startX, endY)
                    lineTo(endX, endY)
                    close()
                }
                drawCanvas?.drawPath(path, drawPaint)
            }
            else -> return
        }
    }

    private fun fillArea(x: Float, y: Float) {
        val startX = x.toInt()
        val startY = y.toInt()

        val targetColor = canvasBitmap?.getPixel(startX, startY) ?: return
        if (targetColor != currentColor) {
            floodFill(canvasBitmap, startX, startY, targetColor, currentColor)
            drawCanvas?.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
        }
    }

    private fun floodFill(bitmap: Bitmap?, x: Int, y: Int, targetColor: Int, replacementColor: Int) {
        if (bitmap == null) return

        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val stack = Stack<Point>()
        stack.push(Point(x, y))

        while (stack.isNotEmpty()) {
            val point = stack.pop()
            val px = point.x
            val py = point.y

            if (px < 0 || px >= width || py < 0 || py >= height) continue
            if (pixels[py * width + px] != targetColor) continue

            pixels[py * width + px] = replacementColor

            stack.push(Point(px + 1, py))
            stack.push(Point(px - 1, py))
            stack.push(Point(px, py + 1))
            stack.push(Point(px, py - 1))
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }

    fun setColor(newColor: Int) {
        currentColor = newColor
        drawPaint.color = currentColor
    }

    fun setStrokeWidth(newWidth: Float) {
        strokeWidth = newWidth
        drawPaint.strokeWidth = strokeWidth
    }

    fun setShape(shape: Shape) {
        currentShape = shape
    }

    fun clearDrawing() {
        drawCanvas?.drawColor(Color.WHITE)
        invalidate()
    }

    fun saveDrawing() {
        val filePath = context.getExternalFilesDir(null)?.absolutePath + "/DrawingApp"
        val dir = File(filePath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "drawing_${System.currentTimeMillis()}.png")
        try {
            FileOutputStream(file).use { fos ->
                canvasBitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                Toast.makeText(context, "Drawing saved", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to save drawing", Toast.LENGTH_SHORT).show()
        }
    }

    fun fillScreen() {
        drawCanvas?.drawColor(currentColor)
        invalidate()
    }
}


