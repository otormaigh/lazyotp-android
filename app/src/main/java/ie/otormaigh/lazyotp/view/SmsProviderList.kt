package ie.otormaigh.lazyotp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import ie.otormaigh.lazyotp.data.SmsCodeProvider

@Composable
fun SmsProviderList(smsProviders: List<SmsCodeProvider>, action: MutableLiveData<SmsProviderAction>) {
  Column(
    Modifier.fillMaxSize()
  ) {
    TopAppBar(
      title = { Text(text = "Lazy OTP") },
      actions = {
        IconButton(onClick = {
          action.postValue(SmsProviderAction.GoToSettings)
        }) {
          Icon(imageVector = Icons.Default.Settings, "")
        }
      }
    )

    SmsProvideInputField()

    LazyColumn {
      items(smsProviders) { smsProvider ->
        SmsProviderListItem(smsProvider) {
          action.postValue(SmsProviderAction.ItemClick)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SmsProviderListItem(smsCodeProvider: SmsCodeProvider, onItemClick: (SmsCodeProvider) -> Unit) {
  Surface(
    color = Color.Transparent,
    onClick = {
      onItemClick(smsCodeProvider)
    }) {
    Row(
      modifier = Modifier.padding(all = 16.dp)
    ) {
      Text(
        modifier = Modifier.fillMaxWidth(0.75f),
        text = smsCodeProvider.sender,
        fontSize = 18.sp,
        color = Color.White
      )

      Text(
        text = smsCodeProvider.codeLength,
        fontSize = 18.sp,
        color = Color.White
      )
    }
  }
}


@Composable
fun SmsProvideInputField() {
  Row(
    modifier = Modifier.padding(all = 16.dp)
  ) {
    OutlinedTextField(
      value = "",
      onValueChange = { },
      label = { Text("Sender name") },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
      modifier = Modifier
        .padding(end = 16.dp)
        .fillMaxWidth(0.75f),
    )

    OutlinedTextField(
      value = "",
      onValueChange = { },
      label = { Text("Digits") },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
  }
}

sealed class SmsProviderAction {
  object GoToSettings : SmsProviderAction()
  object ItemClick : SmsProviderAction()
}