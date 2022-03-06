package ie.otormaigh.lazyotp.feature.addprovider

sealed class AddSmsProviderState {
  object Default : AddSmsProviderState()
  object Loading : AddSmsProviderState()
  object Success : AddSmsProviderState()
  object AddItem : AddSmsProviderState()
  data class EditItem(val sender: String?, val codeLength: Int?) : AddSmsProviderState()

  sealed class Fail : AddSmsProviderState() {
    class Sender(val reason: String) : Fail()
    class DigitCount(val reason: String) : Fail()
  }
}