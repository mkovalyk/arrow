package experiments.com.pixellot.walkthrough

import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * Created on 15.05.18.
 */
class Walkthrough(val arrow: BezieArrow, val counter: WalkthroughCounter,
                  val maxCountOfImpressions: Int = 1, val hintLayout: HintLayout) {
    fun dismiss(dismissedByUser: Boolean) {
        if (dismissedByUser) {
            counter.walkthroughShowed()
        }
        arrow.visibility = GONE
        hintLayout.visibility = GONE
    }

    fun show(animated: Boolean) {
        hintLayout.visibility = VISIBLE
        arrow.invalidate()
    }
}