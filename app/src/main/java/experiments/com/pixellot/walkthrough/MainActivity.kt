package experiments.com.pixellot.walkthrough

import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private var walkthrough: Walkthrough? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout.setOnTouchListener { v, event -> buttonTouched(v, event) }

        hintLayout.post(::createWalkthrough)
    }

    private fun createWalkthrough() {
        val builder = WalkthroughBuilder(this)
        with(builder) {
            hintLayout2 = hintLayout
            text = "Set from Builder..."
            description = "Set description from Builder.."

            // width of the view after changing text doesn't apply immediately.
            hintLayout.post({
                from(hintLayout.hint, WalkthroughBuilder.HorizontalAlignment.END, WalkthroughBuilder.VerticalAlignment.CENTER, R.id.layout)
                to(button, WalkthroughBuilder.HorizontalAlignment.START, WalkthroughBuilder.VerticalAlignment.CENTER, R.id.layout)
                counter = TempWalkthroughCounter()
                commonLayout = layout
                startAnchor = PointF(start.x * 0.3f, 0f)
                endAnchor = PointF(end.x * -0.2f, end.y * 0.1f)
                maxCountOfImpressions = 5

                walkthrough = builder.build()
                walkthrough!!.dismissListener = { Toast.makeText(this@MainActivity, "Invoked From listener", Toast.LENGTH_SHORT).show() }
                walkthrough!!.show()
            })
        }
        hintLayout.setOnClickListener { walkthrough!!.dismiss() }
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
