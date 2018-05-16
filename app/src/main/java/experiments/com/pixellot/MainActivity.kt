package experiments.com.pixellot

import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import experiments.com.pixellot.walkthrough.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var walkthrough: Walkthrough? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout.setOnTouchListener { v, event -> buttonTouched(v, event) }

        hint_layout.post(::createWalkthrough)
    }

    private fun createWalkthrough() {
        val builder = WalkthroughBuilder(this)
        with(builder) {
            hintLayout = hint_layout
            text = "Set from Builder..."
            description = "Set description from Builder.."

            from(hint_layout.hint, HorizontalAlignment.END, VerticalAlignment.CENTER, R.id.layout)
            to(button, HorizontalAlignment.START, VerticalAlignment.CENTER , R.id.layout)
            counter = TempWalkthroughCounter()
            commonLayout = layout
            startAnchor = PointF(0.3f, 0f)
            endAnchor = PointF(-0.2f, 0.1f)
            maxCountOfImpressions = 5

            with(builder.build())
            {
                this.dismissListener = { Toast.makeText(this@MainActivity, "Invoked From listener", Toast.LENGTH_SHORT).show() }
                this.show()
                walkthrough = this
            }
        }
        hint_layout.setOnClickListener { walkthrough!!.dismiss() }
        button.setOnClickListener { walkthrough!!.show() }
    }

    override fun onDestroy() {
        super.onDestroy()
        walkthrough?.destroy()
    }

    private var oldX: Float = 0f
    private var oldY: Float = 0f

    private fun buttonTouched(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            oldX = event.x
            oldY = event.y
            return true
        }
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_POINTER_UP) {
            moveArrow(event.x, event.y)
        }
        return false
    }

    private fun moveArrow(newX: Float, newY: Float) {
        button.x = newX
        button.y = newY
        walkthrough!!.arrow?.setEnd(button.x, button.y + button.height / 2)
        walkthrough!!.arrow?.invalidate()
    }

    inner class TempWalkthroughCounter : WalkthroughCounter {
        private var counter = 0
        override fun walkthroughShowed() {
            counter++
        }

        override fun getCountOfImpressions(): Int {
            return counter
        }

        override fun reset() {
            counter = 0
        }

    }
}
