package experiments.com.pixellot.walkthrough

import android.content.Context
import android.graphics.PointF
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created on 11.05.18.
 */
class WalkthroughBuilder(val context: Context) {
    // mandatory
    var text: String? = null
    var description: String? = null
    var counter: WalkthroughCounter? = null
    var hintLayout2: HintLayout? = null
    var commonLayout: ViewGroup? = null

    // optional
    var maxCountOfImpressions = 1
    var animated = true
    var startAnchor: PointF = PointF(0f, 0f)
    var endAnchor: PointF = PointF(0f, 0f)
    private var start = PointF()
    private var end = PointF()
    private var fromBinding: Binding? = null
    private var toBinding: Binding? = null

    fun from(view: View, hor: HorizontalAlignment, ver: VerticalAlignment, parentId: Int): WalkthroughBuilder {
        fromBinding = Binding(view, hor, ver, parentId)
        return this
    }

    fun to(view: View, hor: HorizontalAlignment, ver: VerticalAlignment, parentId: Int): WalkthroughBuilder {
        toBinding = Binding(view, hor, ver, parentId)
        return this
    }

    fun build(): Walkthrough {
        val layout = hintLayout2!!
        Objects.requireNonNull(layout, "Set layout to proceed...")
        Objects.requireNonNull(text, "Set hint text to proceed...")
        Objects.requireNonNull(description, "Set additional hint description to proceed...")
        Objects.requireNonNull(commonLayout, "Set layout to proceed...")
        Objects.requireNonNull(counter, "...And don't forget to pass WalkthroughCounter.")
        Objects.requireNonNull(fromBinding, "Oops, you forgot to add view to start from")
        Objects.requireNonNull(toBinding, "Oops, you forgot to add view to end with")

        if (layout.hint.text != text) {
            layout.hint.text = text
        }
        if (layout.description.text != description) {
            layout.description.text = description
        }
        val arrow = BezierArrow(context)
        layout.description.post {
            start = fromBinding!!.evaluate()
            arrow.setStart(start.x, start.y)
            end = toBinding!!.evaluate()
            arrow.setEnd(end.x, end.y)
            arrow.setFirstAnchorDelta(startAnchor.x * start.x, startAnchor.y * start.y)
            arrow.setSecondAnchorDelta(endAnchor.x * end.x, endAnchor.y * end.y)
            commonLayout!!.addView(arrow)
        }
        return Walkthrough(arrow, counter!!, layout, "AddTagWalkthrough", maxCountOfImpressions, animated)
    }
}

class Binding(val view: View, val horizontal: HorizontalAlignment, val vertical: VerticalAlignment, val parentId: Int) {
    fun evaluate(): PointF {
        val left = getRelativeLeft(view, parentId)
        val top = getRelativeTop(view, parentId)
        val x = left + horizontal.multiplier * view.measuredWidth
        val y = top + vertical.multiplier * view.measuredHeight
        return PointF(x, y)
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
}

enum class HorizontalAlignment(val multiplier: Float) {
    START(0.0f), END(1.0f), CENTER(0.5f)
}

enum class VerticalAlignment(val multiplier: Float) {
    TOP(0f), BOTTOM(1.0f), CENTER(0.5f)
}

