// IMediaService.aidl
package com.chan.rainymood.biz.media;

import com.chan.rainymood.biz.media.MediaItem;
import com.chan.rainymood.biz.media.Listener;
// Declare any non-default types here with import statements

interface IMediaService {
    void play(in MediaItem item, in Listener listener);

    void pause();

    void stop();

    void release();

    boolean isPlaying();
}
