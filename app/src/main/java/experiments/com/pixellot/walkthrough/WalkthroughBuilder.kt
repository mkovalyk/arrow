package experiments.com.pixellot.walkthrough

import android.graphics.PointF
import android.view.View
import kotlin.math.PI

/**
 * Created on 11.05.18.
 */
class WalkthroughBuilder {
    private lateinit var text: String
    private lateinit var additionalText: String
    private var startAnchor: PointF = PointF(0f, 0f)
    private var endAnchor: PointF = PointF(0f, 0f)
    private val start = PointF()
    private val end = PointF()

    private var arrowRadius: Float = 50f
    private var arrowAngle: Float = PI.toFloat() / 12 // 15 degree each size

    fun text(text: String) = apply { this.text = text }

    fun additionalText(text: String) = apply { this.additionalText = text }

    fun startAnchor(startAnchor: PointF) = apply { this.startAnchor = startAnchor }

    fun endAnchor(endAnchor: PointF) = apply { this.endAnchor = endAnchor }

    fun arrowRadius(radius: Float) = apply { this.arrowRadius = radius }

    fun arrowAngle(angle: Float) = apply { this.arrowAngle = angle }

    fun from(view: View, hor: HorizontalAllignment, ver: VerticalAllignment, parentId: Int) {
        val left = getRelativeLeft(view, parentId)
        val top = getRelativeTop(view, parentId)
        start.x = left + hor.multiplier() * view.width
        start.y = top + ver.multiplier() * view.height
    }

    fun to(view: View, hor: HorizontalAllignment, ver: VerticalAllignment, parentId: Int) {
        val left = getRelativeLeft(view, parentId)
        val top = getRelativeTop(view, parentId)
        end.x = left + hor.multiplier() * view.width
        end.y = top + ver.multiplier() * view.height
    }

    private fun getRelativeLeft(myView: View, parentId: Int): Int {
        return if (myView.id == parentId)
            myView.left
        else
            myView.left + getRelativeLeft(myView.parent as View, parentId)
    }

    private fun getRelativeTop(myView: View, parentId: Int): Int {
        return if (myView.id == parentId)
            myView.top
        else
            myView.top + getRelativeTop(myView.parent as View, parentId)
    }

    enum class HorizontalAllignment {
        START {
            override fun multiplier() = 0.0f
        },
        END {
            override fun multiplier() = 1.0f
        },
        CENTER {
            override fun multiplier() = 0.5f
        };

        abstract fun multiplier(): Float
    }

    enum class VerticalAllignment {
        TOP {
            override fun multiplier() = 0f
        },
        BOTTOM {
            override fun multiplier() = 1.0f
        },
        CENTER {
            override fun multiplier() = 0.5f
        };

        abstract fun multiplier(): Float
    }
}

