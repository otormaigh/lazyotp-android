package ie.elliot.lazysms.feature.addprovider

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ie.elliot.lazysms.arch.BaseViewModel
import ie.elliot.lazysms.data.LazySmsDatabase
import ie.elliot.lazysms.data.SmsCodeProvider
import kotlinx.coroutines.launch

class AddSmsProviderViewModel(private val database: LazySmsDatabase) : BaseViewModel() {
  private val _stateMachine = MutableLiveData<AddSmsProviderState>()
  val stateMachine: LiveData<AddSmsProviderState>
    get() = _stateMachine

  fun addProvider(sender: Editable?, digitCount: Editable?): Unit = when {
    sender.isNullOrEmpty() -> _stateMachine.postValue(AddSmsProviderState.Fail.Sender("Empty"))
    digitCount.isNullOrEmpty() -> _stateMachine.postValue(AddSmsProviderState.Fail.DigitCount("Empty"))
    else -> {
      launch {
        database.smsCodeProviderDao().insert(SmsCodeProvider(sender.toString(), digitCount.toString().toInt()))
        _stateMachine.postValue(AddSmsProviderState.Success)
      }
      Unit
    }
  }
}