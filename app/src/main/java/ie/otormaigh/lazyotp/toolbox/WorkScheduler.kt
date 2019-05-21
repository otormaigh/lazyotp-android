package ie.otormaigh.lazyotp.toolbox

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*

object WorkScheduler {
  inline fun <reified W : CoroutineWorker> oneTimeRequest(context: Context, inputData: Data): LiveData<WorkInfo> {
    val workRequest = OneTimeWorkRequestBuilder<W>()
      .setInputData(inputData)
      .build()

    WorkManager.getInstance(context)
      .enqueue(workRequest)

    return WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.id)
  }
}