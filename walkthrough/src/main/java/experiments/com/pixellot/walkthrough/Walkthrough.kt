package experiments.com.pixellot.walkthrough

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * Created on 15.05.18.
 */
class Walkthrough(var arrow: BezierArrow?, val counter: WalkthroughCounter,
                  var hintLayout: HintLayout?, val uniqueId: String, val maxCountOfImpressions: Int,
                  val animated: Boolean) {

    private var animator: AnimatorSet? = null

    fun destroy() {
        cancelAnimation()
        // remove all views and listeners
        arrow = null
        hintLayout = null
        dismissListener = null
    }

    /**
     * Function which is invoked after dismiss animation is finished.
     */
    var dismissListener: ((id: String) -> Unit)? = null

    fun dismiss() {
        counter.walkthroughShowed()
        if (animated) {
            cancelAnimation()

            val animator = AnimatorSet()
            val arrowAnimator = ObjectAnimator.ofFloat(arrow, View.ALPHA, 0f)
            val hintAnimator = ObjectAnimator.ofFloat(hintLayout, View.ALPHA, 0f)
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    arrow?.visibility = GONE
                    hintLayout?.visibility = GONE
                    dismissListener?.invoke(uniqueId)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            animator.playTogether(arrowAnimator, hintAnimator)
            animator.duration = DURATION
            animator.start()
        } else {
            arrow?.visibility = GONE
            hintLayout?.visibility = GONE
        }
    }

    fun show() {
        if (counter.getCountOfImpressions() >= maxCountOfImpressions) {
            Log.i(TAG, "This walkthrough has already been displayed.")
            return
        }
        arrow?.visibility = VISIBLE
        hintLayout?.visibility = VISIBLE
        if (animated) {
            cancelAnimation()
            // make it visible but
            arrow?.alpha = 0f
            hintLayout?.alpha = 0f

            val animator = AnimatorSet()
            val arrowAnimator = ObjectAnimator.ofFloat(arrow, View.ALPHA, 1.0f)
            val hintAnimator = ObjectAnimator.ofFloat(hintLayout, View.ALPHA, 1.0f)
            animator.playTogether(arrowAnimator, hintAnimator)
            animator.duration = DURATION
            animator.start()
        } else {
            arrow?.alpha = 1f
            hintLayout?.alpha = 1f
        }
    }

    private fun cancelAnimation() {
        animator?.cancel()
        animator?.removeAllListeners()
    }

    companion object {
        val TAG = Walkthrough::class.java.simpleName
        const val DURATION = 300L
    }
}