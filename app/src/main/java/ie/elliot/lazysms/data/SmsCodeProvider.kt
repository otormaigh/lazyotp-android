package ie.elliot.lazysms.data

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*

@Entity(tableName = "sms_code_provider")
data class SmsCodeProvider(
  @PrimaryKey
  val sender: String,
  val codeLength: Int
) {

  companion object {
    val diffUtil = object : DiffUtil.ItemCallback<SmsCodeProvider>() {
      override fun areItemsTheSame(oldItem: SmsCodeProvider, newItem: SmsCodeProvider): Boolean =
        oldItem.sender == newItem.sender

      override fun areContentsTheSame(oldItem: SmsCodeProvider, newItem: SmsCodeProvider): Boolean =
        oldItem.sender == newItem.sender &&
          oldItem.codeLength == newItem.codeLength
    }
  }
}

@Dao
interface SmsCodeproviderDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(data: SmsCodeProvider)

  @Query(
    """
    SELECT * FROM sms_code_provider
    ORDER BY sender ASC
  """
  )
  fun fetchAllLive(): LiveData<List<SmsCodeProvider>>

  @Query(
    """
    SELECT * FROM sms_code_provider
  """
  )
  suspend fun fetchAllAsync(): List<SmsCodeProvider>
}