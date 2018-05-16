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

    val radius = 35
    val angleDegree = PI.toFloat() / 12

    private val start: PointF
    private val end: PointF
    private val firstAnchorDeltas: PointF
    private val secondAnchorDeltas: PointF

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
        val firstDeltaX = styleAttrs.getFloat(R.styleable.BezieArrow_firstDeltaX, 0f)
        val firstDeltaY = styleAttrs.getFloat(R.styleable.BezieArrow_firstDeltaY, 0f)
        val secondDeltaX = styleAttrs.getFloat(R.styleable.BezieArrow_secondDeltaX, 0f)
        val secondDeltaY = styleAttrs.getFloat(R.styleable.BezieArrow_secondDeltaY, 0f)
        val width = styleAttrs.getDimension(R.styleable.BezieArrow_line_width, 6f)
        val color = styleAttrs.getColor(R.styleable.BezieArrow_line_color, Color.parseColor("#f5a623"))
        styleAttrs.recycle()

        start = PointF(fromX, fromY)
        end = PointF(toX, toY)
        firstAnchorDeltas = PointF(firstDeltaX, firstDeltaY)
        secondAnchorDeltas = PointF(secondDeltaX, secondDeltaY)

        line.style = Paint.Style.STROKE
        line.color = color
        line.strokeWidth = width
        line.strokeCap = Paint.Cap.ROUND

        arrow.style = Paint.Style.FILL
        arrow.color = color
        evaluateArrowPoints()
    }

    fun setFirstAnchorDelta(x: Float, y: Float) {
        firstAnchorDeltas.x = x
        firstAnchorDeltas.y = y
        evaluateArrowPoints()
    }

    fun setSecondAnchorDelta(x: Float, y: Float) {
        secondAnchorDeltas.x = x
        secondAnchorDeltas.y = y
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
        firstAnchor.x = start.x + firstAnchorDeltas.x
        firstAnchor.y = start.y + firstAnchorDeltas.y

        secondAnchor.x = end.x + secondAnchorDeltas.x
        secondAnchor.y = end.y + secondAnchorDeltas.y
        Log.d(TAG, "First: $firstAnchor. Second: $secondAnchor")

        val before = Bezier(start, firstAnchor, secondAnchor, end).findFor(0.99f)
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
//        canvas.drawCircle(firstAnchor.x, firstAnchor.y, 20f, line)
//        canvas.drawCircle(secondAnchor.x, secondAnchor.y, 20f, line)
        super.onDraw(canvas)
    }
}