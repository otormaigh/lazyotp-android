package ie.otormaigh.lazyotp.toolbox.extension

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean =
  ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED