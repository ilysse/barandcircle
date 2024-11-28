package com.example.barandcircle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import kotlin.random.Random

class CircleAndBarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint: Paint = Paint()
    private val barPaint: Paint = Paint()

    private var circleRadius: Float = 50f
    private var circleX: Float = 0f
    private var circleY: Float = 0f
    private var screenWidth: Float = 0f
    private var screenHeight: Float = 0f

    private var barWidth: Float = 300f
    private var barHeight: Float = 50f
    private var barX: Float = 0f

    private var barSpeed: Float = 40f // Speed to move the bar

    private var angle: Float = 45f // Initial angle of movement (in degrees)
    private var speed: Float = 15f // Ball speed

    private val random = Random(System.currentTimeMillis())

    // Handler for continuous updates
    private val handler = Handler()
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateBallPosition()
            invalidate() // Redraw the view
            handler.postDelayed(this, 16) // ~60 FPS (16ms delay)
        }
    }

    init {
        paint.color = Color.BLUE
        paint.isAntiAlias = true
        barPaint.color = Color.RED
        barPaint.isAntiAlias = true

        // Start the continuous movement of the ball
        handler.post(updateRunnable)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Update screen dimensions
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()

        // Set initial position of the ball in the center
        circleX = screenWidth / 2
        circleY = screenHeight / 2

        // Set the initial position of the bar at the bottom
        barX = (screenWidth - barWidth) / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the ball
        canvas.drawCircle(circleX, circleY, circleRadius, paint)

        // Draw the bar at the bottom
        canvas.drawRect(barX, screenHeight - barHeight, barX + barWidth, screenHeight, barPaint)

        // Check for collision with the bar (paddle)
        if (circleY + circleRadius >= screenHeight - barHeight && circleX in barX..(barX + barWidth)) {
            // Ball touches the top of the bar, bounce it UP
            bounceOffBar()
            circleY = screenHeight - barHeight - circleRadius // Prevent ball from sticking to the bar
        }

        // Keep ball within the screen bounds (left and right walls)
        if (circleX - circleRadius < 0 || circleX + circleRadius > screenWidth) {
            angle = 180 - angle // Reverse direction horizontally
        }

        // Ball hits the top of the screen
        if (circleY - circleRadius < 0) {
            angle = -angle // Reverse direction vertically
        }
    }

    private fun bounceOffBar() {
        // Reflect the ball from the top of the bar (paddle)
        // Reset the ball's vertical speed to move UP after hitting the bar
        angle = -angle // Reverse the vertical direction (always go up)

        // Optionally, adjust the horizontal angle based on where the ball hits the bar
        val barCenter = barX + barWidth / 2
        val ballCenter = circleX

        // Calculate the horizontal offset of the ball from the center of the bar
        val offsetFromCenter = ballCenter - barCenter
        val maxBounceAngle = 45 // The maximum angle deviation from the vertical (in degrees)

        // Adjust the horizontal bounce angle
        val angleAdjustment = (offsetFromCenter / (barWidth / 2)) * maxBounceAngle
        angle += angleAdjustment
    }

    private fun updateBallPosition() {
        // Convert angle to radians
        val angleInRadians = Math.toRadians(angle.toDouble())

        // Calculate the new position based on speed and angle
        circleX += (speed * cos(angleInRadians)).toFloat()
        circleY += (speed * sin(angleInRadians)).toFloat()
    }

    // Function to move the bar left
    fun moveBarLeft() {
        barX -= barSpeed
        if (barX < 0) barX = 0f
        invalidate()
    }

    // Function to move the bar right
    fun moveBarRight() {
        barX += barSpeed
        if (barX + barWidth > screenWidth) barX = screenWidth - barWidth
        invalidate()
    }

    // Handling touch to move the bar
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                // Move the bar with touch movement
                barX = event.x - barWidth / 2
                if (barX < 0) barX = 0f
                if (barX + barWidth > screenWidth) barX = screenWidth - barWidth
                invalidate()
            }
        }
        return true
    }
}