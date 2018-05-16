package experiments.com.pixellot.walkthrough

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


/**
 * View that draws Bezier curve and arrow at the end of it.
 *
 * Created on 10.05.18.
 */
class BezierArrow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val radius: Float // radius of the arrow i.e. length of the arrow line
    private val angleDegree: Float // bigger angle - "wider" arrow should be

    private val start: PointF
    private val end: PointF
    private val firstAnchorDeltas: PointF // to be more flexible - just use relative values instead of absolute.
    private val secondAnchorDeltas: PointF


    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePath = Path()
    private val arrowPath = Path()

    private val firstAnchor = PointF()
    private val secondAnchor = PointF()
    private val pointRightBeforeEnd = PointF()
    private val firstArrowVertex = PointF()
    private val secondArrowVertex = PointF()
    private val thirdArrowVertex = PointF()
    private val tag = "BezierArrow"

    init {
        val styleAttrs = context.obtainStyledAttributes(attrs, R.styleable.BezierArrow, defStyleAttr, R.style.WalkthroughStyle)

        val fromX = styleAttrs.getFloat(R.styleable.BezierArrow_fromX, 0f)
        val toX = styleAttrs.getFloat(R.styleable.BezierArrow_toX, 0f)
        val fromY = styleAttrs.getFloat(R.styleable.BezierArrow_fromY, 0f)
        val toY = styleAttrs.getFloat(R.styleable.BezierArrow_toY, 0f)
        val firstDeltaX = styleAttrs.getFloat(R.styleable.BezierArrow_firstDeltaX, 0f)
        val firstDeltaY = styleAttrs.getFloat(R.styleable.BezierArrow_firstDeltaY, 0f)
        val secondDeltaX = styleAttrs.getFloat(R.styleable.BezierArrow_secondDeltaX, 0f)
        val secondDeltaY = styleAttrs.getFloat(R.styleable.BezierArrow_secondDeltaY, 0f)
        val width = styleAttrs.getDimension(R.styleable.BezierArrow_line_width, 6f)
        val color = styleAttrs.getColor(R.styleable.BezierArrow_line_color, Color.parseColor("#f5a623"))
        radius = styleAttrs.getDimension(R.styleable.BezierArrow_arrow_radius, 80f)
        angleDegree = Math.toRadians(styleAttrs.getFloat(R.styleable.BezierArrow_arrow_angle, 15f).toDouble()).toFloat()

        styleAttrs.recycle()

        start = PointF(fromX, fromY)
        end = PointF(toX, toY)
        firstAnchorDeltas = PointF(firstDeltaX, firstDeltaY)
        secondAnchorDeltas = PointF(secondDeltaX, secondDeltaY)

        linePaint.style = Paint.Style.STROKE
        linePaint.color = color
        linePaint.strokeWidth = width
        linePaint.strokeCap = Paint.Cap.ROUND

        arrowPaint.style = Paint.Style.FILL
        arrowPaint.color = color
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

        // evaluate point that is almost at the end. It is used to get angle between it and final point.
        // this angle is used to draw arrow head.
        val result = Bezier(start, firstAnchor, secondAnchor, end).findFor(0.99f)
        pointRightBeforeEnd.x = result.x
        pointRightBeforeEnd.y = result.y
        val angle = getAngle(end, pointRightBeforeEnd)

        firstArrowVertex.x = end.x + cos(angle - angleDegree) * radius
        firstArrowVertex.y = end.y + sin(angle - angleDegree) * radius

        secondArrowVertex.x = end.x + cos(angle + angleDegree) * radius
        secondArrowVertex.y = end.y + sin(angle + angleDegree) * radius

        thirdArrowVertex.x = end.x
        thirdArrowVertex.y = end.y
    }

    private fun getAngle(first: PointF, second: PointF): Float {
        return atan2((second.y - first.y), (second.x - first.x))
    }

    override fun onDraw(canvas: Canvas) {
        // draw line itself
        linePath.reset()

        linePath.moveTo(start.x, start.y)
        linePath.cubicTo(firstAnchor.x, firstAnchor.y, secondAnchor.x, secondAnchor.y, pointRightBeforeEnd.x, pointRightBeforeEnd.y)
        canvas.drawPath(linePath, linePaint)

        // draw arrow with correct angle
        arrowPath.reset()
        arrowPath.moveTo(firstArrowVertex.x, firstArrowVertex.y)
        arrowPath.lineTo(secondArrowVertex.x, secondArrowVertex.y)
        arrowPath.lineTo(thirdArrowVertex.x, thirdArrowVertex.y)
        arrowPath.close()
        canvas.drawPath(arrowPath, arrowPaint)
    }
}