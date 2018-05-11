package experiments.com.pixellot.walkthrough

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created on 10.05.18.
 */
class BezieArrow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val start = PointF()
    private val end = PointF()
    private val firstMultiplier = PointF(0.2f, 0f)
    private val secondMultiplier = PointF(-0.1f, 0f)
    private val line = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val arrowPath = Path()
    private val arrow = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        line.style = Paint.Style.STROKE
        line.color = Color.parseColor("#f5a623")
        line.strokeWidth = 6f
        line.strokeCap = Paint.Cap.ROUND

        arrow.style = Paint.Style.FILL
        arrow.color = Color.parseColor("#f5a623")
        arrow.strokeWidth = 3f
    }

    fun setStart(x: Float, y: Float) {
        start.x = x
        start.y = y
        evaluateMultipliers()
    }

    val first = PointF()
    val second = PointF()
    val result = PointF()
    private val TAG = "BezierArrow"

    fun evaluateMultipliers() {
        first.x = start.x + firstMultiplier.x * start.x
        first.y = start.y + firstMultiplier.y * start.y

        second.x = end.x + end.x * secondMultiplier.x
        second.y = end.y + end.y * secondMultiplier.y
        Log.d(TAG, "First: $first. Second: $second")


        val before = Bezier(start, first, second, end).findFor(0.94f)
        result.x = before.x
        result.y = before.y
        val angle = getAngle(end, result)

        firstArrowVertex.x = end.x + cos(angle - angleDegree) * radius
        firstArrowVertex.y = end.y + sin(angle - angleDegree) * radius

        secondArrowVertex.x = end.x + cos(angle + angleDegree) * radius
        secondArrowVertex.y = end.y + sin(angle + angleDegree) * radius

//        thirdArrowVertex.x = end.x + cos(angle) * radius * 0.5f
//        thirdArrowVertex.y = end.y + sin(angle) * radius * 0.5f
        thirdArrowVertex.x = end.x
        thirdArrowVertex.y = end.y


        Log.d(TAG, "evaluateMultipliers: first: $firstArrowVertex second: $secondArrowVertex")
    }

    private val radius = 35
    private val angleDegree = PI.toFloat() / 12
    private val firstArrowVertex = PointF()
    private val secondArrowVertex = PointF()
    private val thirdArrowVertex = PointF()


    fun getAngle(first: PointF, second: PointF): Float {
        val angle = atan2((second.y - first.y), (second.x - first.x))
        Log.d(TAG, "getAngle: ${Math.toDegrees(angle.toDouble())}")
        return angle
    }

    fun setEnd(x: Float, y: Float) {
        end.x = x
        end.y = y
        evaluateMultipliers()
    }

    override fun onDraw(canvas: Canvas) {
        // draw line itself
        path.reset()

        path.moveTo(start.x, start.y)
        path.cubicTo(first.x, first.y, second.x, second.y, end.x, end.y)
        canvas.drawPath(path, line)

        // draw arrow with correct angle
        arrowPath.reset()
//        arrowPath.moveTo(end.x, end.y)
        arrowPath.moveTo(firstArrowVertex.x, firstArrowVertex.y)
        arrowPath.lineTo(secondArrowVertex.x, secondArrowVertex.y)
        arrowPath.lineTo(thirdArrowVertex.x, thirdArrowVertex.y)
        arrowPath.close()
        canvas.drawPath(arrowPath, arrow)
        canvas.drawCircle(first.x, first.y, 20f, line)
        canvas.drawCircle(second.x, second.y, 20f, line)
//        canvas.drawCircle(result.x, result.y, 10f, arrow)
        super.onDraw(canvas)
    }
}