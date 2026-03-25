package org.aisee.app.core.common

import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.play.core.review.ReviewManagerFactory
import org.aisee.app.core.data.UserPreferences

/**
 * Manages Google Play In-App Review prompts.
 *
 * Shows the review dialog on the 3rd app open after signup.
 * Only shows once (tracked via UserPreferences).
 */
class InAppReviewManager(
    private val activity: ComponentActivity,
    private val userPreferences: UserPreferences
) {

    fun tryRequestReview() {
        if (!userPreferences.isLoggedIn) return
        if (userPreferences.hasShownReview) return

        userPreferences.incrementAppOpenCount()
        val count = userPreferences.appOpenCount
        Log.d(TAG, "App open count: $count")

        if (count < REQUIRED_OPENS) return

        Log.d(TAG, "Requesting in-app review")
        val reviewManager = ReviewManagerFactory.create(activity)
        reviewManager.requestReviewFlow().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(activity, reviewInfo).addOnCompleteListener {
                    Log.d(TAG, "Review flow completed")
                    userPreferences.markReviewShown()
                }
            } else {
                Log.e(TAG, "Review flow request failed", task.exception)
            }
        }
    }

    companion object {
        private const val TAG = "InAppReview"
        private const val REQUIRED_OPENS = 3
    }
}
