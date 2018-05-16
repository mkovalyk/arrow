package experiments.com.pixellot.walkthrough

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.*


/**
 * Created on 10.05.18.
 */

fun PointF.distanceTo(another: PointF): Float {
    return sqrt((this.x - another.x).pow(2) + (this.y - another.y).pow(2))
}

class BezieArrow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val radius = 35
    val angleDegree = PI.toFloat() / 12

    private val start: PointF
    private val end: PointF
    private val firstMultiplier: PointF
    //= PointF(0.2f, 0f)
    private val secondMultiplier: PointF
    //= PointF(-0.1f, 0f)

    private val line = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val arrowPath = Path()
    private val arrow = Paint(Paint.ANTI_ALIAS_FLAG)
    private val firstAnchor = PointF()
    private val secondAnchor = PointF()
    private val result = PointF()
    private val firstArrowVertex = PointF()
    private val secondArrowVertex = PointF()
    private val thirdArrowVertex = PointF()
    private val TAG = "BezierArrow"

    init {
        val styleAttrs = context.obtainStyledAttributes(attrs, R.styleable.BezieArrow, defStyleAttr, R.style.WalkthroughStyle)

        val fromX = styleAttrs.getFloat(R.styleable.BezieArrow_fromX, 0f)
        val toX = styleAttrs.getFloat(R.styleable.BezieArrow_toX, 0f)
        val fromY = styleAttrs.getFloat(R.styleable.BezieArrow_fromY, 0f)
        val toY = styleAttrs.getFloat(R.styleable.BezieArrow_toY, 0f)
        val firstMultiplierX = styleAttrs.getFloat(R.styleable.BezieArrow_firstMultiplierX, 0f)
        val firstMultiplierY = styleAttrs.getFloat(R.styleable.BezieArrow_firstMultiplierY, 0f)
        val secondMultiplierX = styleAttrs.getFloat(R.styleable.BezieArrow_secondMultiplierX, 0f)
        val secondMultiplierY = styleAttrs.getFloat(R.styleable.BezieArrow_secondMultiplierY, 0f)
        val width = styleAttrs.getDimension(R.styleable.BezieArrow_line_width, 6f)
        val color = styleAttrs.getColor(R.styleable.BezieArrow_line_color, Color.parseColor("#f5a623"))
        styleAttrs.recycle()

        start = PointF(fromX, fromY)
        end = PointF(toX, toY)
        firstMultiplier = PointF(firstMultiplierX, firstMultiplierY)
        secondMultiplier = PointF(secondMultiplierX, secondMultiplierY)

        line.style = Paint.Style.STROKE
        line.color = color
        line.strokeWidth = width
        line.strokeCap = Paint.Cap.ROUND

        arrow.style = Paint.Style.FILL
        arrow.color = color
        evaluateArrowPoints()
    }

    fun setFirstMultiplier(x: Float, y: Float) {
        firstMultiplier.x = x
        firstMultiplier.y = y
        evaluateArrowPoints()
    }

    fun setSecondMultiplier(x: Float, y: Float) {
        secondMultiplier.x = x
        secondMultiplier.y = y
        evaluateArrowPoints()
    }

    fun setStart(x: Float, y: Float) {
        start.x = x
        start.y = y
        evaluateArrowPoints()
    }

    fun setEnd(x: Float, y: Float) {
        end.x = x
        end.y = y
        evaluateArrowPoints()
    }

    private fun evaluateArrowPoints() {
        firstAnchor.x = start.x + firstMultiplier.x * start.x
        firstAnchor.y = start.y + firstMultiplier.y * start.y

        secondAnchor.x = end.x + end.x * secondMultiplier.x
        secondAnchor.y = end.y + end.y * secondMultiplier.y
        Log.d(TAG, "First: $firstAnchor. Second: $secondAnchor")


        // Point should varies between [0.93, 0.99] depending on distance to second anchor.
        val proportion = 0.07f * end.distanceTo(secondAnchor) / end.distanceTo(start)
        val before = Bezier(start, firstAnchor, secondAnchor, end).findFor(1.0f - min(0.07f, proportion))
        result.x = before.x
        result.y = before.y
        val angle = getAngle(end, result)

        firstArrowVertex.x = end.x + cos(angle - angleDegree) * radius
        firstArrowVertex.y = end.y + sin(angle - angleDegree) * radius

        secondArrowVertex.x = end.x + cos(angle + angleDegree) * radius
        secondArrowVertex.y = end.y + sin(angle + angleDegree) * radius

        thirdArrowVertex.x = end.x
        thirdArrowVertex.y = end.y
        Log.d(TAG, "evaluateArrowPoints: first: $firstArrowVertex second: $secondArrowVertex")
    }

    private fun getAngle(first: PointF, second: PointF): Float {
        val angle = atan2((second.y - first.y), (second.x - first.x))
        Log.d(TAG, "getAngle: ${Math.toDegrees(angle.toDouble())}")
        return angle
    }

    override fun onDraw(canvas: Canvas) {
        // draw line itself
        path.reset()

        path.moveTo(start.x, start.y)
        path.cubicTo(firstAnchor.x, firstAnchor.y, secondAnchor.x, secondAnchor.y, result.x, result.y)
        canvas.drawPath(path, line)

        // draw arrow with correct angle
        arrowPath.reset()
//        arrowPath.moveTo(end.x, end.y)
        arrowPath.moveTo(firstArrowVertex.x, firstArrowVertex.y)
        arrowPath.lineTo(secondArrowVertex.x, secondArrowVertex.y)
        arrowPath.lineTo(thirdArrowVertex.x, thirdArrowVertex.y)
        arrowPath.close()
        canvas.drawPath(arrowPath, arrow)
        canvas.drawCircle(secondAnchor.x, secondAnchor.y, 20f, line)
        super.onDraw(canvas)
    }
}