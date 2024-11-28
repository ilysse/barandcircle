package com.example.barandcircle

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.barandcircle.CircleAndBarView

class MainActivity : AppCompatActivity() {

    private lateinit var circleAndBarView: CircleAndBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        circleAndBarView = findViewById(R.id.circleAndBarView)
        val buttonLeft = findViewById<Button>(R.id.buttonLeft)
        val buttonRight = findViewById<Button>(R.id.buttonRight)

        // Move the bar to the left when the left button is pressed
        buttonLeft.setOnClickListener {
            circleAndBarView.moveBarLeft()
        }

        // Move the bar to the right when the right button is pressed
        buttonRight.setOnClickListener {
            circleAndBarView.moveBarRight()
        }
    }
}
