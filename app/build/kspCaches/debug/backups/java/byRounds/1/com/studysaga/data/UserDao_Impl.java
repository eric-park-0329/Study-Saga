package com.studysaga.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final Converters __converters = new Converters();

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`id`,`username`,`totalMinutes`,`level`,`exp`,`crystals`,`streak`,`lastStudyDate`,`expBoost`,`crystalBoost`,`equippedSkinId`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUsername());
        statement.bindLong(3, entity.getTotalMinutes());
        statement.bindLong(4, entity.getLevel());
        statement.bindLong(5, entity.getExp());
        statement.bindLong(6, entity.getCrystals());
        statement.bindLong(7, entity.getStreak());
        final String _tmp = __converters.localDateToString(entity.getLastStudyDate());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        statement.bindLong(9, entity.getExpBoost());
        statement.bindLong(10, entity.getCrystalBoost());
        if (entity.getEquippedSkinId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getEquippedSkinId());
        }
      }
    };
  }

  @Override
  public Object upsert(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserEntity> observeUser() {
    final String _sql = "SELECT * FROM users WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfTotalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalMinutes");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfExp = CursorUtil.getColumnIndexOrThrow(_cursor, "exp");
          final int _cursorIndexOfCrystals = CursorUtil.getColumnIndexOrThrow(_cursor, "crystals");
          final int _cursorIndexOfStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "streak");
          final int _cursorIndexOfLastStudyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStudyDate");
          final int _cursorIndexOfExpBoost = CursorUtil.getColumnIndexOrThrow(_cursor, "expBoost");
          final int _cursorIndexOfCrystalBoost = CursorUtil.getColumnIndexOrThrow(_cursor, "crystalBoost");
          final int _cursorIndexOfEquippedSkinId = CursorUtil.getColumnIndexOrThrow(_cursor, "equippedSkinId");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final int _tmpTotalMinutes;
            _tmpTotalMinutes = _cursor.getInt(_cursorIndexOfTotalMinutes);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpExp;
            _tmpExp = _cursor.getInt(_cursorIndexOfExp);
            final int _tmpCrystals;
            _tmpCrystals = _cursor.getInt(_cursorIndexOfCrystals);
            final int _tmpStreak;
            _tmpStreak = _cursor.getInt(_cursorIndexOfStreak);
            final LocalDate _tmpLastStudyDate;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfLastStudyDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfLastStudyDate);
            }
            _tmpLastStudyDate = __converters.fromString(_tmp);
            final int _tmpExpBoost;
            _tmpExpBoost = _cursor.getInt(_cursorIndexOfExpBoost);
            final int _tmpCrystalBoost;
            _tmpCrystalBoost = _cursor.getInt(_cursorIndexOfCrystalBoost);
            final Long _tmpEquippedSkinId;
            if (_cursor.isNull(_cursorIndexOfEquippedSkinId)) {
              _tmpEquippedSkinId = null;
            } else {
              _tmpEquippedSkinId = _cursor.getLong(_cursorIndexOfEquippedSkinId);
            }
            _result = new UserEntity(_tmpId,_tmpUsername,_tmpTotalMinutes,_tmpLevel,_tmpExp,_tmpCrystals,_tmpStreak,_tmpLastStudyDate,_tmpExpBoost,_tmpCrystalBoost,_tmpEquippedSkinId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUser(final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM users WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfTotalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalMinutes");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfExp = CursorUtil.getColumnIndexOrThrow(_cursor, "exp");
          final int _cursorIndexOfCrystals = CursorUtil.getColumnIndexOrThrow(_cursor, "crystals");
          final int _cursorIndexOfStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "streak");
          final int _cursorIndexOfLastStudyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStudyDate");
          final int _cursorIndexOfExpBoost = CursorUtil.getColumnIndexOrThrow(_cursor, "expBoost");
          final int _cursorIndexOfCrystalBoost = CursorUtil.getColumnIndexOrThrow(_cursor, "crystalBoost");
          final int _cursorIndexOfEquippedSkinId = CursorUtil.getColumnIndexOrThrow(_cursor, "equippedSkinId");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final int _tmpTotalMinutes;
            _tmpTotalMinutes = _cursor.getInt(_cursorIndexOfTotalMinutes);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpExp;
            _tmpExp = _cursor.getInt(_cursorIndexOfExp);
            final int _tmpCrystals;
            _tmpCrystals = _cursor.getInt(_cursorIndexOfCrystals);
            final int _tmpStreak;
            _tmpStreak = _cursor.getInt(_cursorIndexOfStreak);
            final LocalDate _tmpLastStudyDate;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfLastStudyDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfLastStudyDate);
            }
            _tmpLastStudyDate = __converters.fromString(_tmp);
            final int _tmpExpBoost;
            _tmpExpBoost = _cursor.getInt(_cursorIndexOfExpBoost);
            final int _tmpCrystalBoost;
            _tmpCrystalBoost = _cursor.getInt(_cursorIndexOfCrystalBoost);
            final Long _tmpEquippedSkinId;
            if (_cursor.isNull(_cursorIndexOfEquippedSkinId)) {
              _tmpEquippedSkinId = null;
            } else {
              _tmpEquippedSkinId = _cursor.getLong(_cursorIndexOfEquippedSkinId);
            }
            _result = new UserEntity(_tmpId,_tmpUsername,_tmpTotalMinutes,_tmpLevel,_tmpExp,_tmpCrystals,_tmpStreak,_tmpLastStudyDate,_tmpExpBoost,_tmpCrystalBoost,_tmpEquippedSkinId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
