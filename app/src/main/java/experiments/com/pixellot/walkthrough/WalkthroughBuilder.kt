package experiments.com.pixellot.walkthrough

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created on 11.05.18.
 */
class WalkthroughBuilder(val context: Context) {
    // mandatory
    var text: String? = null
        set(value) {
            field = value
            hintLayout2?.hint?.text = value
        }
    var description: String? = null
        set(value) {
            field = value
            hintLayout2?.description?.text = value
        }
    var counter: WalkthroughCounter? = null
    var hintLayout2: HintLayout? = null
    var commonLayout: ViewGroup? = null

    // optional
    var maxCountOfImpressions = 1
    var startAnchor: PointF = PointF(0f, 0f)
    var endAnchor: PointF = PointF(0f, 0f)
    private val start = PointF()
    private val end = PointF()
    private val style: Int? = null

    fun from(view: View, hor: HorizontalAlignment, ver: VerticalAlignment, parentId: Int): WalkthroughBuilder {
        val left = getRelativeLeft(view, parentId)
        val top = getRelativeTop(view, parentId)
        start.x = left + hor.multiplier() * view.width
        start.y = top + ver.multiplier() * view.height
        Log.d(TAG, "from: $start $view.")
        return this
    }

    fun to(view: View, hor: HorizontalAlignment, ver: VerticalAlignment, parentId: Int): WalkthroughBuilder {
        val left = getRelativeLeft(view, parentId)
        val top = getRelativeTop(view, parentId)
        end.x = left + hor.multiplier() * view.width
        end.y = top + ver.multiplier() * view.height
        return this
    }

    fun build(): Walkthrough {
        val layout = hintLayout2!!
        Objects.requireNonNull(layout, "Set layout to proceed...")
        Objects.requireNonNull(text, "Set hint text to proceed...")
        Objects.requireNonNull(description, "Set additional hint description to proceed...")
        Objects.requireNonNull(commonLayout, "Set layout to proceed...")
        Objects.requireNonNull(counter, "...And don't forget to pass WalkthroughCounter.")

        val arrow = BezieArrow(context, defStyleAttr = R.style.WalkthroughStyle)
        arrow.setStart(start.x, start.y)
        arrow.setEnd(end.x, end.y)
        arrow.setFirstMultiplier(startAnchor.x, startAnchor.y)
        arrow.setSecondMultiplier(endAnchor.x, endAnchor.y)

        commonLayout!!.addView(arrow)
//        with(layout)
//        {
//            hint.text = text
//            this.description.text = this@WalkthroughBuilder.description
//        }
        return Walkthrough(arrow, counter!!, maxCountOfImpressions, layout)
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

    enum class HorizontalAlignment {
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

    enum class VerticalAlignment {
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
    companion object {
        val TAG = "WalkthroughBuilder"
    }
}

