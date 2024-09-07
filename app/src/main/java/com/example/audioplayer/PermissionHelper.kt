package com.example.audioplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class PermissionHelper(private val activity: AppCompatActivity) {

    private val permissionName = Manifest.permission.READ_MEDIA_AUDIO
    private val permissionRequestId = 1

    fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity, permissionName) == PackageManager.PERMISSION_GRANTED
    }

    fun showPermissionRationaleDialog(onPermissionGranted: () -> Unit) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Permission Required")
            .setMessage("This app needs access to your media files to play audio. Please grant the permission.")
            .setPositiveButton("OK") { _, _ -> requestPermission() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(activity, arrayOf(permissionName), permissionRequestId)
    }

    fun handlePermissionResult(
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        if (requestCode == permissionRequestId) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName)) {
                    showPermissionRationaleDialog(onPermissionGranted)
                } else {
                    showAppSettingsDialog()
                }
            }
        }
    }

    private fun showAppSettingsDialog() {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Permission Required")
            .setMessage("You have permanently denied this permission. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ -> openAppSettings() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${activity.packageName}")
        activity.startActivity(intent)
    }
}
