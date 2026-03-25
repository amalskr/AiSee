package org.aisee.app.core.common

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

/**
 * Manages in-app updates with automatic update type selection.
 *
 * Version code encoding: major * 10000 + minor * 100 + patch
 * Example: 2.0.0 = 20000, 2.0.1 = 20001, 2.1.0 = 20100, 3.0.0 = 30000
 *
 * Update type logic:
 * - Patch change only (e.g., 20000 → 20001) → FLEXIBLE
 * - Minor or major change (e.g., 20000 → 20100 or 30000) → IMMEDIATE
 */
class InAppUpdateManager(private val activity: ComponentActivity) : DefaultLifecycleObserver {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
    private var updateType: Int = AppUpdateType.FLEXIBLE
    private var onFlexibleUpdateReady: (() -> Unit)? = null

    private val updateResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
        activity.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                Log.d(TAG, "Update flow cancelled or failed: ${result.resultCode}")
            }
        }

    private val installStateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Log.d(TAG, "Flexible update downloaded, ready to install")
            onFlexibleUpdateReady?.invoke()
        }
    }

    init {
        activity.lifecycle.addObserver(this)
    }

    fun checkForUpdate(onFlexibleDownloadReady: () -> Unit = {}) {
        onFlexibleUpdateReady = onFlexibleDownloadReady

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                updateType = determineUpdateType(info)
                Log.d(
                    TAG,
                    "Update available: ${info.availableVersionCode()}. " +
                            "Type: ${if (updateType == AppUpdateType.IMMEDIATE) "IMMEDIATE" else "FLEXIBLE"}"
                )

                if (info.isUpdateTypeAllowed(updateType)) {
                    if (updateType == AppUpdateType.FLEXIBLE) {
                        appUpdateManager.registerListener(installStateListener)
                    }
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateResultLauncher,
                        AppUpdateOptions.newBuilder(updateType).build()
                    )
                }
            } else if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            } else {
                Log.d(TAG, "No update available")
            }
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to check for updates", e)
        }
    }

    fun completeFlexibleUpdate() {
        appUpdateManager.completeUpdate()
    }

    override fun onResume(owner: LifecycleOwner) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (updateType == AppUpdateType.IMMEDIATE &&
                info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
            if (updateType == AppUpdateType.FLEXIBLE &&
                info.installStatus() == InstallStatus.DOWNLOADED
            ) {
                onFlexibleUpdateReady?.invoke()
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        appUpdateManager.unregisterListener(installStateListener)
    }

    /**
     * Determines update type based on version code difference.
     *
     * Version code format: major * 10000 + minor * 100 + patch
     * - Only patch changed → FLEXIBLE
     * - Major or minor changed → IMMEDIATE
     */
    private fun determineUpdateType(info: AppUpdateInfo): Int {
        val currentCode = getCurrentVersionCode()
        val availableCode = info.availableVersionCode()

        val currentMajorMinor = currentCode / 100  // e.g., 20000 / 100 = 200
        val availableMajorMinor = availableCode / 100

        Log.d(TAG, "Current versionCode: $currentCode, Available: $availableCode")
        Log.d(TAG, "Current major.minor: $currentMajorMinor, Available major.minor: $availableMajorMinor")

        return if (availableMajorMinor > currentMajorMinor) {
            // Major or minor version changed → IMMEDIATE
            AppUpdateType.IMMEDIATE
        } else {
            // Only patch changed → FLEXIBLE
            AppUpdateType.FLEXIBLE
        }
    }

    private fun getCurrentVersionCode(): Int {
        return try {
            val pInfo = activity.packageManager.getPackageInfo(activity.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                pInfo.versionCode
            }
        } catch (_: Exception) {
            0
        }
    }

    companion object {
        private const val TAG = "InAppUpdate"
    }
}
