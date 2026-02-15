package com.siteops.data.local;

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
import com.siteops.data.model.SiteVisit;
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
public final class SiteVisitDao_Impl implements SiteVisitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SiteVisit> __insertionAdapterOfSiteVisit;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<SiteVisit> __deletionAdapterOfSiteVisit;

  private final EntityDeletionOrUpdateAdapter<SiteVisit> __updateAdapterOfSiteVisit;

  public SiteVisitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSiteVisit = new EntityInsertionAdapter<SiteVisit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `site_visits` (`id`,`projectId`,`title`,`date`,`notes`,`photoUris`,`pdfUri`,`markupsJson`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SiteVisit entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProjectId());
        statement.bindString(3, entity.getTitle());
        statement.bindLong(4, entity.getDate());
        statement.bindString(5, entity.getNotes());
        final String _tmp = __converters.fromStringList(entity.getPhotoUris());
        statement.bindString(6, _tmp);
        if (entity.getPdfUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPdfUri());
        }
        if (entity.getMarkupsJson() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMarkupsJson());
        }
      }
    };
    this.__deletionAdapterOfSiteVisit = new EntityDeletionOrUpdateAdapter<SiteVisit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `site_visits` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SiteVisit entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSiteVisit = new EntityDeletionOrUpdateAdapter<SiteVisit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `site_visits` SET `id` = ?,`projectId` = ?,`title` = ?,`date` = ?,`notes` = ?,`photoUris` = ?,`pdfUri` = ?,`markupsJson` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SiteVisit entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProjectId());
        statement.bindString(3, entity.getTitle());
        statement.bindLong(4, entity.getDate());
        statement.bindString(5, entity.getNotes());
        final String _tmp = __converters.fromStringList(entity.getPhotoUris());
        statement.bindString(6, _tmp);
        if (entity.getPdfUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPdfUri());
        }
        if (entity.getMarkupsJson() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMarkupsJson());
        }
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insertVisit(final SiteVisit visit, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSiteVisit.insertAndReturnId(visit);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteVisit(final SiteVisit visit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSiteVisit.handle(visit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateVisit(final SiteVisit visit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSiteVisit.handle(visit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SiteVisit>> getVisitsForProject(final long projectId) {
    final String _sql = "SELECT * FROM site_visits WHERE projectId = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, projectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"site_visits"}, new Callable<List<SiteVisit>>() {
      @Override
      @NonNull
      public List<SiteVisit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "projectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPhotoUris = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUris");
          final int _cursorIndexOfPdfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "pdfUri");
          final int _cursorIndexOfMarkupsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "markupsJson");
          final List<SiteVisit> _result = new ArrayList<SiteVisit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SiteVisit _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProjectId;
            _tmpProjectId = _cursor.getLong(_cursorIndexOfProjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final List<String> _tmpPhotoUris;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPhotoUris);
            _tmpPhotoUris = __converters.toStringList(_tmp);
            final String _tmpPdfUri;
            if (_cursor.isNull(_cursorIndexOfPdfUri)) {
              _tmpPdfUri = null;
            } else {
              _tmpPdfUri = _cursor.getString(_cursorIndexOfPdfUri);
            }
            final String _tmpMarkupsJson;
            if (_cursor.isNull(_cursorIndexOfMarkupsJson)) {
              _tmpMarkupsJson = null;
            } else {
              _tmpMarkupsJson = _cursor.getString(_cursorIndexOfMarkupsJson);
            }
            _item = new SiteVisit(_tmpId,_tmpProjectId,_tmpTitle,_tmpDate,_tmpNotes,_tmpPhotoUris,_tmpPdfUri,_tmpMarkupsJson);
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
