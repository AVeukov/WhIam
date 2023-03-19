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
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.scheme.data.NodeData
import ru.veyukov.arseniy.whiam.scheme.data.WifiPointData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.predicate.makeAccessPointsPredicate
import java.text.SimpleDateFormat
import java.util.*

class SchemeView(context: Context) : View(context) {
    companion object {
        private const val INVALID_ID = -1
    }

    private val scheme = MainContext.INSTANCE.scheme
    private val schemeData = scheme.schemeData!!
    private val paints = SchemeViewPaints(schemeData)

    private val settings = MainContext.INSTANCE.settings

    private var mDeltaX = 0F
    private var mDeltaY = 0F
    private var mLastTouchX = 0F
    private var mLastTouchY = 0F
    private var mFocusX = 0f
    private var mFocusY = 0f
    private var mScaleFactor = 1f
    private var mSchemeScale = schemeData.scale
    private var mActivePointerId = Companion.INVALID_ID

    init {
        setBackgroundColor(Color.GRAY)
    }

    val doubleTapListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            mDeltaX = 0F
            mDeltaY = 0F
            mScaleFactor = 1f
            mFocusX = 0F
            mFocusY = 0F
            invalidate()
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            var text = ""
            if (!settings.wifiDataCollection()) {
                text = this@SchemeView.getSelectedInfo(e.x, e.y)
            } else {
                text = "x = " + reverseCalcX(e.x).toString() + "\n" + "y = " + reverseCalcY(e.y).toString()
                scheme.addNodeToList(
                    NodeData(
                        scheme.nodeList.size + 1,
                        arrayListOf(reverseCalcX(e.x), reverseCalcY(e.y)),
                        scheme.getWiFiPoints()
                    )
                )
            }
            if (text != "") Toast.makeText(context, text, Toast.LENGTH_LONG).show()

        }
    }
    val detector = GestureDetectorCompat(context, doubleTapListener)

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val wiFiPoints = scheme.getWiFiPoints()
        var text = SimpleDateFormat("HH:mm:ss").format(Date())
        canvas.drawText(text, 10F, 50F, paints.tempPaint)
        if (settings.wifiDataCollection()) {
            if (wiFiPoints.isEmpty()) {
                text = R.string.no_data.toString()
            } else {
                text = wiFiPoints[0].sid + "(" + wiFiPoints[0].mac + ")"
            }
            canvas.drawText(text, 10F, 100F, paints.tempPaint)
            text = wiFiPoints[0].dBm.toString() + "дБ"
            canvas.drawText(text, 10F, 150F, paints.tempPaint)
        }
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
            scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY)
            drawHalls(canvas)
            drawRooms(canvas)
            drawPartitions(canvas)
            drawStairs(canvas)
            drawDoors(canvas)
            if (settings.wifiDataCollection()) {
                drawNodes(canvas)
            } else {
                drawPath(calcPath(), paints.pathPaint)
                drawCurrentPosition(canvas)
            }
            restore()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun drawCurrentPosition(canvas: Canvas) {
        if (scheme.currentPosition != INVALID_ID) {
            val node =
                schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].nodes.stream()
                    .filter { n -> n.id == scheme.currentPosition }
                    .findFirst().orElse(null);
            val x = calcX(node.xy[0])
            val y = calcY(node.xy[1])
            canvas.drawCircle(x, y, mSchemeScale * 3F, paints.selectedPaint)
            canvas.drawCircle(x, y, mSchemeScale * 10F, paints.pathPaint)
        }
    }

    private fun drawNodes(canvas: Canvas) {
        if (scheme.nodeList.isEmpty()) {
            return
        }
        scheme.nodeList.forEach {
            val x = calcX(it.xy[0])
            val y = calcY(it.xy[1])
            canvas.drawCircle(x, y, mSchemeScale * 1F, paints.tempPaint)
            canvas.drawText(it.id.toString(), x, y, paints.tempPaint)
        }
    }

    private fun drawPartitions(canvas: Canvas) {
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].partitions.forEach {
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
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].halls.forEach {
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
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].rooms.forEachIndexed { index, it ->
            if (scheme.targetRoom == index)
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
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].stairs.forEach {
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
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].rooms.forEach {
            if (!it.doors.isEmpty()) {
                canvas.drawLine(
                    calcX(it.doors[0]), calcY(it.doors[1]), calcX(it.doors[2]), calcY(it.doors[3]), paints.backPaint
                )
            }
        }
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].partitions.forEach {
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

    private fun reverseCalcX(x: Float): Int {
        return ((((x - (1 - mScaleFactor) * mFocusX) / mScaleFactor) - mDeltaX) / mSchemeScale).toInt()
    }

    private fun reverseCalcY(y: Float): Int {
        return ((height - ((y - (1 - mScaleFactor) * mFocusY) / mScaleFactor) - mDeltaY) / mSchemeScale).toInt()
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))
            invalidate()
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mFocusX = detector.focusX
            mFocusY = detector.focusY
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
        scheme.targetRoom = Companion.INVALID_ID
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].rooms.forEachIndexed { index, r ->
            if ((calcX(r.walls[0]) <= x) and
                (calcX(r.walls[2]) >= x) and
                (calcY(r.walls[1]) >= y) and
                (calcY(r.walls[3]) <= y)
            ) {
                scheme.targetRoom = index
                result = r.nameRoom + " - " + r.descriptionRoom
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun calcPath(): Path {
        var path = Path()
        if (scheme.currentPosition == INVALID_ID) return path
        val pathPoints = scheme.getPathPoints()
        if (pathPoints.size <= 1) {
            //Toast.makeText(context, "Вы на месте", Toast.LENGTH_LONG).show()
            return path
        }
        path.reset();
        val startNode =
            schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].nodes.stream()
                .filter { n -> n.id == scheme.currentPosition }
                .findFirst().orElse(null);
        path.moveTo(
            calcX(startNode.xy[0]),
            calcY(startNode.xy[1])
        )
        var addPath = false
        pathPoints.forEach {
            if (it == scheme.currentPosition) addPath = true
            if (!addPath) return@forEach
            if (pathPoints[pathPoints.lastIndex] == scheme.currentPosition) return@forEach
            val node =
                schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].nodes.stream().filter { n -> n.id == it }.findFirst()
                    .orElse(null);
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun redraw() {
        scheme.recalcCurrentPosition()
        invalidate()

    }
}