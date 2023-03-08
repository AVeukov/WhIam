package ru.veyukov.arseniy.whiam.scheme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import ru.veyukov.arseniy.whiam.MainContext
import java.util.*

class SchemeViewPaints(schemeData: SchemeData) {
    private val schemeData = schemeData
    val backPaint: Paint = Paint()
        get() = field

    init {
        backPaint.isAntiAlias = true
        backPaint.isDither = true
        backPaint.color = Color.WHITE
        backPaint.style = Paint.Style.FILL_AND_STROKE
        backPaint.strokeJoin = Paint.Join.ROUND
        backPaint.strokeCap = Paint.Cap.SQUARE
        backPaint.strokeWidth = schemeData.lineWidth
    }

    val wallPaint: Paint = Paint()
        get() = field
    init {
        wallPaint.isAntiAlias = true
        wallPaint.isDither = true
        wallPaint.color = Color.BLACK
        wallPaint.style = Paint.Style.STROKE
        wallPaint.strokeJoin = Paint.Join.ROUND
        wallPaint.strokeCap = Paint.Cap.ROUND
        wallPaint.strokeWidth = schemeData.lineWidth
    }

    val roomPaint: Paint
        get() = field
    init {
        roomPaint = Paint()
        roomPaint.isAntiAlias = true
        roomPaint.isDither = true
        roomPaint.color = Color.WHITE
        roomPaint.style = Paint.Style.FILL_AND_STROKE
        roomPaint.strokeJoin = Paint.Join.ROUND
        roomPaint.strokeCap = Paint.Cap.ROUND
        roomPaint.strokeWidth = schemeData.lineWidth
    }

    val selectedPaint: Paint = Paint()
        get() = field
    init {
        selectedPaint.isAntiAlias = true
        selectedPaint.isDither = true
        selectedPaint.color = Color.GREEN
        selectedPaint.style = Paint.Style.FILL_AND_STROKE
        selectedPaint.strokeJoin = Paint.Join.ROUND
        selectedPaint.strokeCap = Paint.Cap.ROUND
        selectedPaint.strokeWidth = schemeData.lineWidth
    }
    val pathPaint: Paint = Paint()
        get() = field
    init {
        pathPaint.isAntiAlias = true
        pathPaint.isDither = true
        pathPaint.color = Color.GREEN
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeJoin = Paint.Join.ROUND
        pathPaint.strokeCap = Paint.Cap.ROUND
        pathPaint.strokeWidth = schemeData.lineWidth * 10
        pathPaint.alpha = 70
    }
    val textPaint: Paint = Paint()
        get() = field
    init {
        textPaint.isAntiAlias = true
        textPaint.isDither = true
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = schemeData.textSize
    }

    val tempPaint: Paint = Paint()
        get() = field
    init {
        tempPaint.isAntiAlias = true
        tempPaint.isDither = true
        tempPaint.color = Color.RED
        tempPaint.style = Paint.Style.FILL
        tempPaint.textSize = 50F
    }
}