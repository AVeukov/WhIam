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
        //константа - несуществующий ID
        private const val INVALID_ID = -1
    }

    private val scheme = MainContext.INSTANCE.scheme // объект Scheme из основого контекста
    private val schemeData = scheme.schemeData!! // объект SchemeData - дата класс схемы (данные JSON файла)
    private val paints = SchemeViewPaints(schemeData) // объект параметров отрисовки из отдельного класса
    private val settings = MainContext.INSTANCE.settings // настройки программы  из основного контекста

    private var mDeltaX = 0F // сдвиг схемы по оси Х - горизонталь
    private var mDeltaY = 0F // сдвиг схемы по оси Y - вертикаль
    private var mLastTouchX = 0F // координаты последнего касания экрана Х
    private var mLastTouchY = 0F // координаты последнего касания экрана Y
    private var mFocusX = 0f  // центр масштабирования картинки X, при раздвигании пальцами
    private var mFocusY = 0f  // центр масштабирования картинки Y, при раздвигании пальцами
    private var mScaleFactor = 1f //коэффициент масштабирования схемы, при раздвигании пальцами
    private var mSchemeScale = schemeData.scale // коэффициент увеличения схемы из данных JSON
    private var mActivePointerId = Companion.INVALID_ID // идентификатор точки касания на экране (активного пальца)

    init {
        setBackgroundColor(Color.GRAY) // инициализация - цвет фона Серый
    }

    //обработчик двойного и длительного  касания экрана
    val doubleTapListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean { // срабатывает при двойном касании
            // сбрасываем схему в начальные условия (масштаб, сдвиги по вертикали/горизонтали)
            mDeltaX = 0F
            mDeltaY = 0F
            mScaleFactor = 1f
            mFocusX = 0F
            mFocusY = 0F
            invalidate() // перерисовка схемы
            return true
        }

        override fun onLongPress(e: MotionEvent) { // срабатывает при длительном удержании
            var text = ""
            if (!settings.wifiDataCollection()) { // режим сбора данных Wi-Fi выключен (!)
                text = this@SchemeView.getSelectedInfo(e.x, e.y) // вывод информации о точке нажатия (название и описание комнаты, если в нее попали)
            } else {
                text = "x = " + reverseCalcX(e.x).toString() + "\n" + "y = " + reverseCalcY(e.y).toString() // пересчитываем координаты из экранных в системы координат схемы из JSON
                scheme.addNodeToList( // добавляем координаты и информацию WI-FI в массив контрольных точек с целью последующего экспорта
                    NodeData(
                        scheme.nodeList.size + 1,
                        arrayListOf(reverseCalcX(e.x), reverseCalcY(e.y)),
                        scheme.getWiFiPoints()
                    )
                )
            }
            if (text != "") Toast.makeText(context, text, Toast.LENGTH_LONG).show() // вывод всплывающего сообщения с текстом

        }
    }
    // регистрация детектора нажатий
    val detector = GestureDetectorCompat(context, doubleTapListener)

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) { // основной метод отрисовки данных на холсте Canvas
        super.onDraw(canvas) //вызов метода родителя
        val wiFiPoints = scheme.getWiFiPoints() // Wi-Fi данные в текущей точке
        var text = SimpleDateFormat("HH:mm:ss").format(Date()) // преобразование  текущего времени (Date()) в текст форматированием
        canvas.drawText(text, 10F, 50F, paints.tempPaint) // вывод времени на экран, для контроля перерисовки Canvas
        if (settings.wifiDataCollection()) { // режим сбора данных Wi-Fi
            if (wiFiPoints.isEmpty()) {
                text = R.string.no_data.toString() // Wi-FI сигнал в текущей точке отсутсвует
            } else {
                text = wiFiPoints[0].sid + "(" + wiFiPoints[0].mac + ")" // подготовка информации о имени точки и MAC адресу
            }
            canvas.drawText(text, 10F, 100F, paints.tempPaint) //отрисовка текста на экран
            text = wiFiPoints[0].dBm.toString() + "дБ" // уровень сигнала
            canvas.drawText(text, 10F, 150F, paints.tempPaint) //отрисовка
        }
        if (mDeltaY == 0F) { // если сдвиг схемы нулевой, то расчитываем его
            if (schemeData.height * mSchemeScale < canvas.height) {
                mDeltaY = (canvas.height - schemeData.height * mSchemeScale) / 2 // считаем, чтобы схема была посредине
            }
            if (schemeData.width * mSchemeScale < canvas.width) { // по горизонтали также считаем
                mDeltaX = (canvas.width - schemeData.width * mSchemeScale) / 2
            }
        }
        // масштабирование схемы
        canvas?.apply {
            save() // сохранение всей информации с холста Canvas
            scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY) // масштабирование с фактором mScaleFactor и центром в (mFocusX, mFocusY)
            drawHalls(canvas) //отрисовка коридоров
            drawRooms(canvas) // отрисовка комнат
            drawPartitions(canvas) //отрисовка кабинетов
            drawStairs(canvas) // отрисовка лестниц
            drawDoors(canvas) // отрисовка дверей
            if (settings.wifiDataCollection()) { // режим сбора данных Wi-Fi
                drawNodes(canvas) // отрисовка контрольных точек
            } else {
                // обычный режим работы
                drawPath(calcPath(), paints.pathPaint) // отрисовка пути к целевой точке
                drawCurrentPosition(canvas) // отрисовка текущей позиции
            }
            restore() // восстановление информации
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun drawCurrentPosition(canvas: Canvas) { // отрисовка текущей позиции
        if (scheme.currentPosition != INVALID_ID) { // если текущая позиция определена
            val node = // ищем в массиве контрольных точек, точку соответсвующую ID из scheme.currentPosition
                schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].nodes.stream()
                    .filter { n -> n.id == scheme.currentPosition }
                    .findFirst().orElse(null);
            val x = calcX(node.xy[0]) // пересчитываем координаты из системы координат схемы JSON в систему координат экрана
            val y = calcY(node.xy[1]) // пересчитываем координаты из системы координат схемы JSON в систему координат экрана
            canvas.drawCircle(x, y, mSchemeScale * 3F, paints.selectedPaint) // рисуем маленький кружок
            canvas.drawCircle(x, y, mSchemeScale * 10F, paints.pathPaint) // рисуем большой кружок полу прозрачным цветом pathPaint
        }
    }

    private fun drawNodes(canvas: Canvas) { // отрисовка контрольных точек
        if (scheme.nodeList.isEmpty()) {
            return
        }
        scheme.nodeList.forEach {// в уикле по контрольным точкам
            val x = calcX(it.xy[0]) // пересчитываем координаты из системы координат схемы JSON в систему координат экрана
            val y = calcY(it.xy[1])
            canvas.drawCircle(x, y, mSchemeScale * 1F, paints.tempPaint) // рисуем кружок
            canvas.drawText(it.id.toString(), x, y, paints.tempPaint) // подписываем номер контрольной точки
        }
    }

    private fun drawPartitions(canvas: Canvas) { //  отрисовка перегородок
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].partitions.forEach {// в цикле
            canvas.drawLine( // рисуем линию
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.wallPaint
            )
        }
    }

    private fun drawHalls(canvas: Canvas) { // отрисовка коридоров
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].halls.forEach {
            canvas.drawRect( // рисуем прямоугольник
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.roomPaint
            )
        }
    }

    private fun drawRooms(canvas: Canvas) { // отрисовка комнат
        val bounds = Rect()
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].rooms.forEachIndexed { index, it ->
            if (scheme.targetRoom == index) // рисуем прямоцгольник без границ, целевую комнату рисуем другим цветом
                canvas.drawRect(
                    calcX(it.walls[0]),
                    calcY(it.walls[1]),
                    calcX(it.walls[2]),
                    calcY(it.walls[3]),
                    paints.selectedPaint
                )
            else
                canvas.drawRect( // рисуем прямоцгольник без границ, это определено в paints.roomPaint
                    calcX(it.walls[0]),
                    calcY(it.walls[1]),
                    calcX(it.walls[2]),
                    calcY(it.walls[3]),
                    paints.roomPaint
                )
            canvas.drawRect( // рисуем стены, пустой прямоугольник с границами
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.wallPaint
            )
            paints.textPaint.getTextBounds(it.nameRoom, 0, it.nameRoom.length, bounds); // определяем размер подписи (имени комнаты
            // расчитываем координаты,чтобы текст был посреди комнаты
            val xText = (calcX(it.walls[0]) + ((calcX(it.walls[2])) - calcX(it.walls[0])) / 2) - bounds.width() / 2
            val yText = (calcY(it.walls[1]) + ((calcY(it.walls[3])) - calcY(it.walls[1])) / 2) + bounds.height() / 2
            canvas.drawText( // выводим текст
                it.nameRoom,
                xText,
                yText,
                paints.textPaint
            )

        }
    }

    private fun drawStairs(canvas: Canvas) { // отрисовка лестниц
        val bounds = Rect()
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].stairs.forEach {
            // в цикле по лестницам рисуем прямоугольник лестницы
            canvas.drawRect(
                calcX(it.walls[0]),
                calcY(it.walls[1]),
                calcX(it.walls[2]),
                calcY(it.walls[3]),
                paints.wallPaint
            )
            // рисуем полоски
            if (it.vertical) { // лестница вертикальная
                val dY = paints.wallPaint.strokeWidth // толщина полоски, равна толщине линии
                var y = calcY(it.walls[1]) - 2 * dY // меняем координаты, повертикали в обратную сторону, т.к. мы обращаем координаты
                val yMin = calcY(it.walls[3])
                while (y >= yMin) {
                    canvas.drawLine(calcX(it.walls[0]), y, calcX(it.walls[2]), y, paints.wallPaint) // рисуем линию
                    y = y - 2 * dY
                }
                val x = calcX(it.walls[0] + (it.walls[2] - it.walls[0]) / 2)
                canvas.drawLine(x, calcY(it.walls[1]), x, calcY(it.walls[3]), paints.wallPaint)
            } else { // лестница горизонтальная
                val dX = paints.wallPaint.strokeWidth // толщина полоски, равна толщине линии
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

    private fun drawDoors(canvas: Canvas) { // отрисовка дверей
        // рисуем двери в комнатах
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].rooms.forEach {
            if (!it.doors.isEmpty()) {
                canvas.drawLine(
                    calcX(it.doors[0]), calcY(it.doors[1]), calcX(it.doors[2]), calcY(it.doors[3]), paints.backPaint
                )
            }
        }
        // рисуем двери в перегородках
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].partitions.forEach {
            if (!it.doors.isEmpty()) {
                canvas.drawLine(
                    calcX(it.doors[0]), calcY(it.doors[1]), calcX(it.doors[2]), calcY(it.doors[3]), paints.backPaint
                )
            }
        }
    }

    private fun calcX(x: Int): Float { // пересчет координаты Х из системы координат схемы JSON в экранную систему координат
        // увеличиваем в соответствии с масштабом схемы (из JSON) и сдвигаем
        return x * mSchemeScale + mDeltaX
    }

    private fun calcY(y: Int): Float { // пересчет координаты Y из системы координат схемы JSON в экранную систему координат
        // увеличиваем в соответствии с масштабом схемы (из JSON) и сдвигаем
        // и инвертируем, т.к. на схеме JSON 0 внизу, а на экране 0 вверху
        return height - (y * mSchemeScale + mDeltaY)
    }

    private fun reverseCalcX(x: Float): Int { // обратный пересчет координаты X из экранной системы координат в систему координат схемы JSON
        // учитываем масштабирование пальцами mScaleFactor и смещение относительно центра масштабирования mFocusX
        return ((((x - (1 - mScaleFactor) * mFocusX) / mScaleFactor) - mDeltaX) / mSchemeScale).toInt()
    }

    private fun reverseCalcY(y: Float): Int { // обратный пересчет координаты Y из экранной системы координат в систему координат схемы JSON
        // учитываем масштабирование пальцами mScaleFactor и смещение относительно центра масштабирования mFocusY
        return ((height - ((y - (1 - mScaleFactor) * mFocusY) / mScaleFactor) - mDeltaY) / mSchemeScale).toInt()
    }

    // обработчик (слушатель) событий масштабирования
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        // масштабирование
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // определяем коэффициент амсштабирования
            mScaleFactor *= detector.scaleFactor
            // запрещаем слишком маленький и слишком большой масштаб
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))
            // перерисовываем всё
            invalidate()
            return true
        }
        // старт масштабироование
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            // фиксируем центр масштабирования
            mFocusX = detector.focusX
            mFocusY = detector.focusY
            return true
        }
    }
    // детектор жестов масштабирования, привязан к обработчику scaleListener
    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun onTouchEvent(event: MotionEvent): Boolean { // обработчик действий пальцами
        detector.onTouchEvent(event) // детектор нажатий (двойного и длительного)
        mScaleDetector.onTouchEvent(event) // детектор масштабирования
        when (event.action) { // действия на экране
            MotionEvent.ACTION_DOWN -> { // палец опущен
                // фиксируем координаты последнего нажатия
                mLastTouchX = event.x
                mLastTouchY = event.y
                // фиксируем ID пальца
                mActivePointerId = event.getPointerId(0)
            }

            MotionEvent.ACTION_MOVE -> { // движение пальцем
                if (!mScaleDetector.isInProgress) { // только если не происходит масштабирования
                    mDeltaX += event.getX(mActivePointerId) - mLastTouchX // расчет сдвига
                    mDeltaY -= event.getY(mActivePointerId) - mLastTouchY // по вертикали инвертируем (т.к. у схемы 0 внизу, а у экрана вверху)
                    invalidate() // перерисовываем экран
                }
                // фиксируем координаты последнего нажатия для нашего пальца
                mLastTouchX = event.getX(mActivePointerId)
                mLastTouchY = event.getY(mActivePointerId)
//                animate()
//                    .x(event.rawX + mLastTouchX)
//                    .y(event.rawY + mLastTouchY)
//                    .setDuration(0)
//                    .start()
            }

            MotionEvent.ACTION_UP -> { // палец вверх
                // обнуляем ID пальца
                mActivePointerId = Companion.INVALID_ID
            }

            MotionEvent.ACTION_CANCEL -> { // конец действий
                mActivePointerId = Companion.INVALID_ID
            }
        }

        return true
    }

    private fun getSelectedInfo(x: Float, y: Float): String { // определение текущей комнаты по координатам пальца
        // сброс результатов
        var result = ""
        scheme.targetRoom = Companion.INVALID_ID
        // в цикле по комнатам
        schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].rooms.forEachIndexed { index, r ->
            if ((calcX(r.walls[0]) <= x) and
                (calcX(r.walls[2]) >= x) and
                (calcY(r.walls[1]) >= y) and
                (calcY(r.walls[3]) <= y)
            ) { // если координаты пальца внутри прямоугольника комнаты
                // присваиваем индекс комнате
                scheme.targetRoom = index
                // формируем текст из названия комнаты и описания
                result = r.nameRoom + " - " + r.descriptionRoom
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun calcPath(): Path { // расчет пути на экране  для отрисовки
        var path = Path()
        if (scheme.currentPosition == INVALID_ID) return path // если текущая позиция не определена, то пусто
        val pathPoints = scheme.getPathPoints() // получаем путь, расчитаный алгоритмом Дейкстры
        if (pathPoints.size <= 1) { // если путь из одной точки, то выходим
            //Toast.makeText(context, "Вы на месте", Toast.LENGTH_LONG).show()
            return path
        }
        path.reset();
        val startNode = // определяем стартовую позицию пути, как координаты контрольной точки текущей позиции
            schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].nodes.stream()
                .filter { n -> n.id == scheme.currentPosition }
                .findFirst().orElse(null);
        path.moveTo( // перемещаем курсор пути в стартовую точкуточку
            calcX(startNode.xy[0]),
            calcY(startNode.xy[1])
        )
        var addPath = false // флаг отрисовки пути
        pathPoints.forEach {// в цикле по путевым точкам, расчитаным алгоритмом Дейкстры
            if (it == scheme.currentPosition) addPath = true // в путь добавляем все точки после точки текущей позиции
            if (!addPath) return@forEach
            if (pathPoints[pathPoints.lastIndex] == scheme.currentPosition) return@forEach // если достигли конечной точки, также выходим из цикла
            val node = // определяем узел по ID путевой точки
                schemeData.buildings[scheme.currentBuilding].floors[scheme.currentFloor].nodes.stream().filter { n -> n.id == it }.findFirst()
                    .orElse(null);
            path.lineTo( // рисуем линюю
                calcX(node.xy[0]),
                calcY(node.xy[1])
            )
        }
        path.moveTo( // перемещаемся в начальную точку, иначе нарисует линию из конца в начало
            calcX(startNode.xy[0]),
            calcY(startNode.xy[1])
        )
        path.close()
        return path
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun redraw() { // обновление(перерисовка) для использования при периодическом обновлении
        scheme.recalcCurrentPosition() // определение текущей позиции
        invalidate() //перерисовка
    }
}