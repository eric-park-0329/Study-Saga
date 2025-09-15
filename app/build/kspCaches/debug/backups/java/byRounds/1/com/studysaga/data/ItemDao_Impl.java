package com.studysaga.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
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
public final class ItemDao_Impl implements ItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ItemEntity> __insertionAdapterOfItemEntity;

  private final EntityInsertionAdapter<InventoryItemEntity> __insertionAdapterOfInventoryItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfUnequipAll;

  private final SharedSQLiteStatement __preparedStmtOfEquip;

  public ItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItemEntity = new EntityInsertionAdapter<ItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `items` (`id`,`name`,`rarity`,`type`,`description`,`boostPercent`,`boostTarget`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getRarity());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getDescription());
        statement.bindLong(6, entity.getBoostPercent());
        statement.bindString(7, entity.getBoostTarget());
      }
    };
    this.__insertionAdapterOfInventoryItemEntity = new EntityInsertionAdapter<InventoryItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `inventory` (`id`,`itemId`,`obtainedIso`,`equipped`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InventoryItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getItemId());
        statement.bindString(3, entity.getObtainedIso());
        final int _tmp = entity.getEquipped() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__preparedStmtOfUnequipAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE inventory SET equipped = 0";
        return _query;
      }
    };
    this.__preparedStmtOfEquip = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE inventory SET equipped = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertItem(final ItemEntity item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertInventory(final InventoryItemEntity inv,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfInventoryItemEntity.insertAndReturnId(inv);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object unequipAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUnequipAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUnequipAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object equip(final long invId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfEquip.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, invId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfEquip.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<InventoryRow>> inventoryWithItems() {
    final String _sql = "SELECT i.id, i.name, i.rarity, i.type, i.description, i.boostPercent, i.boostTarget, inv.id as invId, inv.equipped as equipped FROM items i JOIN inventory inv ON i.id=inv.itemId ORDER BY inv.id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"items",
        "inventory"}, new Callable<List<InventoryRow>>() {
      @Override
      @NonNull
      public List<InventoryRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfName = 1;
          final int _cursorIndexOfRarity = 2;
          final int _cursorIndexOfType = 3;
          final int _cursorIndexOfDescription = 4;
          final int _cursorIndexOfBoostPercent = 5;
          final int _cursorIndexOfBoostTarget = 6;
          final int _cursorIndexOfInvId = 7;
          final int _cursorIndexOfEquipped = 8;
          final List<InventoryRow> _result = new ArrayList<InventoryRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final InventoryRow _item;
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpRarity;
            _tmpRarity = _cursor.getString(_cursorIndexOfRarity);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final int _tmpBoostPercent;
            _tmpBoostPercent = _cursor.getInt(_cursorIndexOfBoostPercent);
            final String _tmpBoostTarget;
            _tmpBoostTarget = _cursor.getString(_cursorIndexOfBoostTarget);
            final long _tmpInvId;
            _tmpInvId = _cursor.getLong(_cursorIndexOfInvId);
            final boolean _tmpEquipped;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEquipped);
            _tmpEquipped = _tmp != 0;
            _item = new InventoryRow(_tmpId,_tmpName,_tmpRarity,_tmpType,_tmpDescription,_tmpBoostPercent,_tmpBoostTarget,_tmpInvId,_tmpEquipped);
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
