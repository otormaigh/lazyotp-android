package ie.otormaigh.lazyotp.feature.addprovider

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ie.otormaigh.lazyotp.arch.BaseViewModel
import ie.otormaigh.lazyotp.data.LazySmsDatabase
import ie.otormaigh.lazyotp.data.SmsCodeProvider
import kotlinx.coroutines.launch

class AddSmsProviderViewModel(private val database: LazySmsDatabase) : BaseViewModel() {
  private val _stateMachine = MutableLiveData<AddSmsProviderState>()
  val stateMachine: LiveData<AddSmsProviderState>
    get() = _stateMachine

  fun addProvider(sender: Editable?, digitCount: Editable?) {
    if (validateInput(sender, digitCount)) {
      launch {
        database.smsCodeProviderDao()
          .insert(SmsCodeProvider(sender.toString(), digitCount.toString()))
        _stateMachine.postValue(AddSmsProviderState.Success)
      }
    }
  }

  fun updateProvider(providerId: String, sender: Editable?, digitCount: Editable?) {
    if (validateInput(sender, digitCount)) {
      launch {
        database.smsCodeProviderDao()
          .upsert(providerId, SmsCodeProvider(sender.toString(), digitCount.toString()))
        _stateMachine.postValue(AddSmsProviderState.Success)
      }
    }
  }

  private fun validateInput(sender: Editable?, digitCount: Editable?): Boolean {
    when {
      sender.isNullOrEmpty() -> _stateMachine.postValue(AddSmsProviderState.Fail.Sender("Empty"))
      digitCount.isNullOrEmpty() -> _stateMachine.postValue(AddSmsProviderState.Fail.DigitCount("Empty"))
      else -> return true
    }
    return false
  }
}