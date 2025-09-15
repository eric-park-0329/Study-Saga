package com.studysaga.data;

import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GachaDao_Impl implements GachaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GachaResultEntity> __insertionAdapterOfGachaResultEntity;

  public GachaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGachaResultEntity = new EntityInsertionAdapter<GachaResultEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `gacha_results` (`id`,`itemId`,`crystalsSpent`,`pulledIso`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GachaResultEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getItemId());
        statement.bindLong(3, entity.getCrystalsSpent());
        statement.bindString(4, entity.getPulledIso());
      }
    };
  }

  @Override
  public Object insertResult(final GachaResultEntity res,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGachaResultEntity.insertAndReturnId(res);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
