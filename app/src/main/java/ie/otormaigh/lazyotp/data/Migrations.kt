package ie.otormaigh.lazyotp.data

import androidx.annotation.VisibleForTesting
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MemberVisibilityCanBePrivate")
object Migrations {
  @VisibleForTesting
  val `1_2` = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
      database.execSQL(
        """
        CREATE TABLE sms_code_provider_backup(
          sender TEXT PRIMARY KEY NOT NULL,
          codeLength TEXT NOT NULL
        )
      """
      )
      database.execSQL(
        """
        INSERT INTO sms_code_provider_backup(sender, codeLength)
        SELECT sender, codeLength FROM sms_code_provider
      """
      )
      database.execSQL("DROP TABLE sms_code_provider")
      database.execSQL("ALTER TABLE sms_code_provider_backup RENAME TO sms_code_provider")
    }
  }

  val ALL = arrayOf(`1_2`)
}