package experiments.com.pixellot.walkthrough

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created on 10.05.18.
 */
class BezieArrow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val start = PointF()
    private val end = PointF()
    private val firstMultiplier = PointF()
    private val secondMultiplier = PointF()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    init {
        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#f5a623")
        paint.strokeWidth = 10f
        paint.strokeCap = Paint.Cap.ROUND
    }

    fun setStart(x: Float, y: Float) {
        start.x = x
        start.y = y
        evaluateMultipliers()
    }

    fun evaluateMultipliers() {
        val resultPointX = (end.x + start.x) / 2
        val resultPointY = (end.y + start.y) / 2

        if (start.x == 0f) {
            firstMultiplier.x = 1f
        } else {
            firstMultiplier.x = resultPointX / start.x
        }
        if (start.y == 0f) {
            firstMultiplier.y = 1f
        } else {
            firstMultiplier.y = resultPointY / start.y
        }
        if (resultPointX == 0f) {
            secondMultiplier.x = 1f
        } else {
            secondMultiplier.x = end.x / resultPointX
        }
        if (resultPointY == 0f) {
            secondMultiplier.y = 1f
        } else {
            secondMultiplier.y = end.y / resultPointY
        }
    }

    fun setEnd(x: Float, y: Float) {
        end.x = x
        end.y = y
        evaluateMultipliers()
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.moveTo(start.x, start.y)
        path.cubicTo(firstMultiplier.x * start.x, firstMultiplier.y * start.y, end.x / secondMultiplier.x, end.y / secondMultiplier.y, end.x, end.y)
        canvas.drawPath(path, paint)
        super.onDraw(canvas)
    }
}