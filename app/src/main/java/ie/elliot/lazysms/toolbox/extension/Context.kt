package ie.elliot.lazysms.toolbox.extension

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

val Context.phoneNumber: String
  @SuppressLint("MissingPermission", "HardwareIds")
  get() = if (isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
    (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number
  } else {
    "unknown"
  }

fun Context.isPermissionGranted(permission: String): Boolean =
  ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED