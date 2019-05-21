package ie.otormaigh.lazyotp.arch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Job()
}