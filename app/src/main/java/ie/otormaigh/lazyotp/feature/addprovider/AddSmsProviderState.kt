package ie.otormaigh.lazyotp.feature.addprovider

sealed class AddSmsProviderState {
  object Default : AddSmsProviderState()
  object Loading : AddSmsProviderState()
  object Success : AddSmsProviderState()

  sealed class Fail : AddSmsProviderState() {
    class Sender(val reason: String) : Fail()
    class DigitCount(val reason: String) : Fail()
  }
}