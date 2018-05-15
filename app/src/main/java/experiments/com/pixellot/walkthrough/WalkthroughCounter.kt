package experiments.com.pixellot.walkthrough

/**
 * Created on 15.05.18.
 */
interface WalkthroughCounter {
    /**
     * This should be invoked when walk-through is dismissed by the user.
     */
    fun walkthroughShowed()

    /**
     * Return count of times walk-through is showed to used
     */
    fun getCountOfImpressions(): Int

    fun reset()
}