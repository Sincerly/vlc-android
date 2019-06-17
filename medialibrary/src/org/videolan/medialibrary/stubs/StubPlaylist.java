package org.videolan.medialibrary.stubs;

import android.os.Parcel;

import org.videolan.medialibrary.interfaces.media.AMediaWrapper;
import org.videolan.medialibrary.interfaces.media.APlaylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StubPlaylist extends APlaylist {

    private ArrayList<Long> mTracksId = new ArrayList<>();
    private StubDataSource dt = StubDataSource.getInstance();

    public StubPlaylist(long id, String name, int trackCount) {
        super(id, name, trackCount);
    }

    public StubPlaylist(Parcel in) {
        super(in);
    }

    public AMediaWrapper[] getTracks() {
        ArrayList<AMediaWrapper> results = new ArrayList<>();
        for (AMediaWrapper media : dt.mAudioMediaWrappers) {
            if (mTracksId.contains(media.getId())) results.add(media);
        }
        return results.toArray(new AMediaWrapper[0]);
    }

    public AMediaWrapper[] getPagedTracks(int nbItems, int offset) {
        ArrayList<AMediaWrapper> results = new ArrayList<>(Arrays.asList(getTracks()));
        return dt.secureSublist(results, offset, offset + nbItems).toArray(new AMediaWrapper[0]);
    }

    public int getRealTracksCount() {
        int count = 0;
        for (AMediaWrapper media : dt.mAudioMediaWrappers) {
            if (mTracksId.contains(media.getId())) count++;
        }
        return count;
    }

    public boolean append(long mediaId) {
        mTracksId.add(mediaId);
        mTracksCount++;
        return true;
    }

    public boolean append(long[] mediaIds) {
        for (long id : mediaIds) {
            append(id);
        }
        return true;
    }

    public boolean append(List<Long> mediaIds) {
        for (long id : mediaIds) {
            append(id);
        }
        return true;
    }

    public boolean add(long mediaId, int position) {
        mTracksId.add(position, mediaId);
        return true;
    }

    public boolean move(int oldPosition, int newPosition) {
        long id = mTracksId.get(oldPosition);
        mTracksId.remove(oldPosition);
        mTracksId.add(newPosition, id);
        return true;
    }

    public boolean remove(int position) {
        mTracksId.remove(position);
        mTracksCount--;
        return true;
    }

    public boolean delete() {
        for (int i = 0 ; i <  dt.mPlaylists.size() ; i++) {
            if (dt.mPlaylists.get(i).getId() == this.getId()) {
                dt.mPlaylists.remove(i);
                return true;
            }
        }
        return false;
    }

    public AMediaWrapper[] searchTracks(String query, int sort, boolean desc, int nbItems, int offset) {
        ArrayList<AMediaWrapper> results = new ArrayList<>();
        for (AMediaWrapper media : dt.mAudioMediaWrappers) {
            if (mTracksId.contains(media.getId()) &&
                    media.getTitle().contains(query)) results.add(media);
        }
        results = new ArrayList<>(Arrays.asList(dt.sortMedia(results, sort, desc)));
        return dt.secureSublist(results, offset, offset + nbItems).toArray(new AMediaWrapper[0]);

    }

    public int searchTracksCount(String query) {
        int count = 0;
        for (AMediaWrapper media : dt.mAudioMediaWrappers) {
            if (mTracksId.contains(media.getId()) &&
                    media.getTitle().contains(query)) count++;
        }
        return count;
    }
}
