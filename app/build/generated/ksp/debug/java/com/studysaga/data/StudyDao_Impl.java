package com.studysaga.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StudyDao_Impl implements StudyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StudySessionEntity> __insertionAdapterOfStudySessionEntity;

  private final EntityDeletionOrUpdateAdapter<StudySessionEntity> __updateAdapterOfStudySessionEntity;

  public StudyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudySessionEntity = new EntityInsertionAdapter<StudySessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `study_sessions` (`id`,`startIso`,`endIso`,`minutes`,`completed`,`expGained`,`crystalsGained`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StudySessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getStartIso());
        if (entity.getEndIso() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEndIso());
        }
        statement.bindLong(4, entity.getMinutes());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getExpGained());
        statement.bindLong(7, entity.getCrystalsGained());
      }
    };
    this.__updateAdapterOfStudySessionEntity = new EntityDeletionOrUpdateAdapter<StudySessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `study_sessions` SET `id` = ?,`startIso` = ?,`endIso` = ?,`minutes` = ?,`completed` = ?,`expGained` = ?,`crystalsGained` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StudySessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getStartIso());
        if (entity.getEndIso() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEndIso());
        }
        statement.bindLong(4, entity.getMinutes());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getExpGained());
        statement.bindLong(7, entity.getCrystalsGained());
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final StudySessionEntity session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfStudySessionEntity.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final StudySessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStudySessionEntity.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<StudySessionEntity>> latest() {
    final String _sql = "SELECT * FROM study_sessions ORDER BY id DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"study_sessions"}, new Callable<List<StudySessionEntity>>() {
      @Override
      @NonNull
      public List<StudySessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartIso = CursorUtil.getColumnIndexOrThrow(_cursor, "startIso");
          final int _cursorIndexOfEndIso = CursorUtil.getColumnIndexOrThrow(_cursor, "endIso");
          final int _cursorIndexOfMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "minutes");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfExpGained = CursorUtil.getColumnIndexOrThrow(_cursor, "expGained");
          final int _cursorIndexOfCrystalsGained = CursorUtil.getColumnIndexOrThrow(_cursor, "crystalsGained");
          final List<StudySessionEntity> _result = new ArrayList<StudySessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudySessionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStartIso;
            _tmpStartIso = _cursor.getString(_cursorIndexOfStartIso);
            final String _tmpEndIso;
            if (_cursor.isNull(_cursorIndexOfEndIso)) {
              _tmpEndIso = null;
            } else {
              _tmpEndIso = _cursor.getString(_cursorIndexOfEndIso);
            }
            final int _tmpMinutes;
            _tmpMinutes = _cursor.getInt(_cursorIndexOfMinutes);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final int _tmpExpGained;
            _tmpExpGained = _cursor.getInt(_cursorIndexOfExpGained);
            final int _tmpCrystalsGained;
            _tmpCrystalsGained = _cursor.getInt(_cursorIndexOfCrystalsGained);
            _item = new StudySessionEntity(_tmpId,_tmpStartIso,_tmpEndIso,_tmpMinutes,_tmpCompleted,_tmpExpGained,_tmpCrystalsGained);
            _result.add(_item);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
