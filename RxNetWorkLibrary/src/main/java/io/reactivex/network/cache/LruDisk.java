package io.reactivex.network.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import io.reactivex.annotations.NonNull;
import io.reactivex.network.util.OkioUtils;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * by y on 20/09/2017.
 * file缓存
 */

public class LruDisk {

    private DiskLruCache diskLruCache;
    private Gson gson;

    LruDisk(File path, int version, int valueCount, int maxSize) {
        if (!path.exists()) {
            path.mkdirs();
        }
        gson = new Gson();
        diskLruCache = DiskLruCache.create(FileSystem.SYSTEM, path, version, valueCount, maxSize);
    }

    @NonNull
    public <T> boolean insert(@NonNull Object key, T value) {
        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit(String.valueOf(key));
            if (editor == null) {
                return false;
            }
            BufferedSink bufferedSink = Okio.buffer(editor.newSink(0));
            bufferedSink.writeString(gson.toJson(value), Charset.defaultCharset());
            bufferedSink.flush();
            editor.commit();
            return true;
        } catch (IOException e) {
            OkioUtils.abort(editor);
            e.printStackTrace();
        }
        OkioUtils.abort(editor);
        return false;
    }

    public <T> T update(@NonNull Object key, T value) {
        return null;
    }

    public <T> T query(@NonNull Object key, TypeToken<T> typeToken) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = diskLruCache.get(String.valueOf(key));
            if (snapshot == null) {
                return null;
            }
            BufferedSource bufferedSource = Okio.buffer(snapshot.getSource(0));
            return gson.fromJson(bufferedSource.readString(Charset.defaultCharset()), typeToken.getType());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            OkioUtils.snapshot(snapshot);
        }
        return null;
    }

    public boolean delete(@NonNull Object key) {
        try {
            return diskLruCache.remove(String.valueOf(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteAll() {
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean containsKey(@NonNull Object key) {
        try {
            return diskLruCache.get(String.valueOf(key)) == null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean onDestroy() {
        if (diskLruCache != null) {
            try {
                diskLruCache.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
