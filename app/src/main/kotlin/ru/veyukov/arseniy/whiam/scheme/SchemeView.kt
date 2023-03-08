package ru.veyukov.arseniy.whiam.scheme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GestureDetectorCompat
import ru.veyukov.arseniy.whiam.MainContext
import java.util.*
import kotlin.collections.ArrayList

class SchemeView(context: Context) : View(context) {
    companion object {
        private const val INVALID_ID = -1
    }

    private var buildingID: Int
    private var floorID: Int

    private val scheme = MainContext.INSTANCE.scheme
    private val schemeData = scheme.schemeData!!
    private val paints = SchemeViewPaints(schemeData)

    private var mDeltaX = 0F
    private var mDeltaY = 0F
    private var mLastTouchX = 0F
    private var mLastTouchY = 0F
    private var mScaleFactor = 1f
    private var mSchemeScale = schemeData.scale
    private var mActivePointerId = Companion.INVALID_ID
    private var mSelectedRoomID: Int = Companion.INVALID_ID
    private var mSelectedPoint: Int = 4005
    private var mPathPoint = ArrayList<Int>()

    init {
        setBackgroundColor(Color.GRAY)
        buildingID = scheme.currentBuilding
        floorID = scheme.currentFloor
    }

    val doubleTapListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            mDeltaX = 0F
            mDeltaY = 0F
            mScaleFactor = 1f
            invalidate()
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val text = this@SchemeView.getSelectedInfo(e.x, e.y)
            if (text != "") Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }
    val detector = GestureDetectorCompat(context, doubleTapListener)

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        buildingID = scheme.currentBuilding
        floorID = scheme.currentFloor

        //canvas.drawText(Date().toString(), 10F, 100F, paints.tempPaint)
        if (mDeltaY == 0F) {
            if (schemeData.height * mSchemeScale < canvas.height) {
                mDeltaY = (canvas.height - schemeData.height * mSchemeScale) / 2
            }
            if (schemeData.width * mSchemeScale < canvas.width) {
                mDeltaX = (canvas.width - schemeData.width * mSchemeScale) / 2
            }
        }
        canvas?.apply {
            save()
            scale(mScaleFactor, mScaleFactor, canvas.width.toFloat() / 2, canvas.height.toFloat() / 2)
            drawHalls(canvas)
            drawRooms(canvas)
            drawPartitions(canvas)
            drawStairs(canvas)
            drawDoors(canvas)
            drawCurrentPosition(canvas)
            //drawPath(calcPath(),paints.pathPaint)

            restore()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun drawCurrentPosition(canvas: Canvas) {
        if (mSelectedPoint != INVALID_ID) {
            val node = schemeData.buildings[buildingID].floors[floorID].nodes.stream().filter{ n-> n.id == mSelectedPoint }.findFirst().orElse(null);
            val x = calcX(node.xy[0])
            val y = calcY(node.xy[1])
            canvas.drawCircle(x, y, mScaleFactor * 10F, paints.selectedPaint)
            canvas.drawCircle(x, y, mScaleFactor * 30F, paints.pathPaint)
        }
    }

    private fun drawPartitions(canvas: Canvas) {
        schemeData.buildings[buildingID].floors[floorID].partitions.forEach {
            canvas.drawLine(
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.wallPaint
            )
        }
    }

    private fun drawHalls(canvas: Canvas) {
        schemeData.buildings[buildingID].floors[floorID].halls.forEach {
            canvas.drawRect(
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.roomPaint
            )
        }
    }

    private fun drawRooms(canvas: Canvas) {
        val bounds = Rect()
        schemeData.buildings[buildingID].floors[floorID].rooms.forEachIndexed { index, it ->
            if (mSelectedRoomID == index)
                canvas.drawRect(
                    calcX(it.walls[0]),
                    calcY(it.walls[1]),
                    calcX(it.walls[2]),
                    calcY(it.walls[3]),
                    paints.selectedPaint
                )
            else
                canvas.drawRect(
                    calcX(it.walls[0]),
                    calcY(it.walls[1]),
                    calcX(it.walls[2]),
                    calcY(it.walls[3]),
                    paints.roomPaint
                )
            canvas.drawRect(
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.wallPaint
            )
            paints.textPaint.getTextBounds(it.nameRoom, 0, it.nameRoom.length, bounds);
            val xText = (calcX(it.walls[0]) + ((calcX(it.walls[2])) - calcX(it.walls[0])) / 2) - bounds.width() / 2
            val yText = (calcY(it.walls[1]) + ((calcY(it.walls[3])) - calcY(it.walls[1])) / 2) + bounds.height() / 2
            canvas.drawText(
                it.nameRoom,
                xText,
                yText,
                paints.textPaint
            )

        }
    }

    private fun drawStairs(canvas: Canvas) {
        val bounds = Rect()
        schemeData.buildings[buildingID].floors[floorID].stairs.forEach {
            canvas.drawRect(
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.wallPaint
            )
            if (it.vertical) {
                val dY = paints.wallPaint.strokeWidth
                var y = calcY(it.walls[1]) - 2 * dY
                val yMin = calcY(it.walls[3])
                while (y >= yMin) {
                    canvas.drawLine(calcX(it.walls[0]), y, calcX(it.walls[2]), y, paints.wallPaint)
                    y = y - 2 * dY
                }
                val x = calcX(it.walls[0] + (it.walls[2] - it.walls[0]) / 2)
                canvas.drawLine(x, calcY(it.walls[1]), x, calcY(it.walls[3]), paints.wallPaint)
            } else {
                val dX = paints.wallPaint.strokeWidth
                var x = calcX(it.walls[0]) + 2 * dX
                val xMax = calcX(it.walls[2])
                while (x <= xMax) {
                    canvas.drawLine(x, calcY(it.walls[1]), x, calcY(it.walls[3]), paints.wallPaint)
                    x = x + 2 * dX
                }
                val y = calcY(it.walls[1] + (it.walls[3] - it.walls[1]) / 2)
                canvas.drawLine(calcX(it.walls[0]), y, calcX(it.walls[2]), y, paints.wallPaint)
            }
        }
    }

    private fun drawDoors(canvas: Canvas) {
        schemeData.buildings[buildingID].floors[floorID].rooms.forEach {
            if (!it.doors.isEmpty()) {
                canvas.drawLine(
                    calcX(it.doors[0]), calcY(it.doors[1]), calcX(it.doors[2]), calcY(it.doors[3]), paints.backPaint
                )
            }
        }
        schemeData.buildings[buildingID].floors[floorID].partitions.forEach {
            if (!it.doors.isEmpty()) {
                canvas.drawLine(
                    calcX(it.doors[0]), calcY(it.doors[1]), calcX(it.doors[2]), calcY(it.doors[3]), paints.backPaint
                )
            }
        }
    }

    private fun calcX(x: Int): Float {
        return x * mSchemeScale + mDeltaX
    }

    private fun calcY(y: Int): Float {
        return height - (y * mSchemeScale + mDeltaY)
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))
            invalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        mScaleDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchX = event.x
                mLastTouchY = event.y
                mActivePointerId = event.getPointerId(0)
            }

            MotionEvent.ACTION_MOVE -> {
                if (!mScaleDetector.isInProgress) {
                    mDeltaX += event.getX(mActivePointerId) - mLastTouchX
                    mDeltaY -= event.getY(mActivePointerId) - mLastTouchY
                    invalidate()
                }
                mLastTouchX = event.getX(mActivePointerId)
                mLastTouchY = event.getY(mActivePointerId)
//                animate()
//                    .x(event.rawX + mLastTouchX)
//                    .y(event.rawY + mLastTouchY)
//                    .setDuration(0)
//                    .start()
            }

            MotionEvent.ACTION_UP -> {
                mActivePointerId = Companion.INVALID_ID
            }

            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = Companion.INVALID_ID
            }
        }

        return true
    }

    private fun getSelectedInfo(x: Float, y: Float): String {
        var result = ""
        mSelectedRoomID = Companion.INVALID_ID
        schemeData.buildings[buildingID].floors[floorID].rooms.forEachIndexed { index, r ->
            if ((calcX(r.walls[0]) <= x) and
                (calcX(r.walls[2]) >= x) and
                (calcY(r.walls[1]) >= y) and
                (calcY(r.walls[3]) <= y)
            ) {
                mSelectedRoomID = index
                result = r.nameRoom + " - " + r.descriptionRoom
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun calcPath(): Path {
        var path = Path()
        val pathPoints = arrayListOf<Int>( 4004,4006,4008,4012,4013,4014,4015, 4017)
        path.reset();
        val startNode = schemeData.buildings[buildingID].floors[floorID].nodes.stream().filter{ n-> n.id == mSelectedPoint }.findFirst().orElse(null);
        path.moveTo(
            calcX(startNode.xy[0]),
            calcY(startNode.xy[1])
        )
        pathPoints.forEach {
            val node = schemeData.buildings[buildingID].floors[floorID].nodes.stream().filter{ n-> n.id == it }.findFirst().orElse(null);
            path.lineTo(
                    calcX(node.xy[0]),
                    calcY(node.xy[1])
                )
        }
        path.moveTo(
            calcX(startNode.xy[0]),
            calcY(startNode.xy[1])
        )
        path.close()
        return path
    }

    fun redraw() {
        invalidate()

    }
}