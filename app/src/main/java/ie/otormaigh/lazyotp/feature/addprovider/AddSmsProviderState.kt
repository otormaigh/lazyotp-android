package ie.otormaigh.lazyotp.feature.addprovider

import androidx.annotation.StringRes

sealed class AddSmsProviderState {
  object Success : AddSmsProviderState()

  sealed class Fail : AddSmsProviderState() {
    class Sender(@StringRes val reason: Int) : Fail()
    class DigitCount(@StringRes val reason: Int) : Fail()
  }
}