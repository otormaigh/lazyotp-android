package ie.elliot.lazysms.feature

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.elliot.lazysms.R
import ie.elliot.lazysms.app
import ie.elliot.lazysms.feature.addprovider.AddSmsProviderBottomSheetFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Job()

  private val recyclerAdapter by lazy { SmsProviderRecyclerAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    rvSmsProvider.apply {
      adapter = recyclerAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    fabAdd.setOnClickListener { AddSmsProviderBottomSheetFragment.show(supportFragmentManager) }

    launch {
      val data = app.database.smsCodeProviderDao().fetchAll()

      withContext(Dispatchers.Main) {
        recyclerAdapter.submitList(data)
      }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS), 13)
  }

  override fun onPause() {
    super.onPause()
    coroutineContext.cancel()
  }
}