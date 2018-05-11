package experiments.com.pixellot.walkthrough

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View


/**
 * Created on 10.05.18.
 */


class BezieArrow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val start = PointF()
    private val end = PointF()
    private val firstMultiplier = PointF(0.1f, 0f)
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

    lateinit var first: PointF
    lateinit var second: PointF

    fun evaluateMultipliers() {
        first = PointF(start.x + firstMultiplier.x * start.x, start.y + firstMultiplier.y * start.y)
        second = PointF(end.x + end.x * secondMultiplier.x, end.y + end.y * secondMultiplier.y)
        Log.d("BezierArrow", "First: $first. Second: $second")


    }

    fun setEnd(x: Float, y: Float) {
        end.x = x
        end.y = y
        evaluateMultipliers()
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.moveTo(start.x, start.y)
        path.cubicTo(first.x, first.y, second.x, second.y, end.x, end.y)
        canvas.drawPath(path, line)
        arrowPath.reset()
        arrowPath.moveTo(end.x, end.y)
        super.onDraw(canvas)
    }
}