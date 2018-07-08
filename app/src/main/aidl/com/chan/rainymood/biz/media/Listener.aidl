// Listener.aidl
package com.chan.rainymood.biz.media;

import com.chan.rainymood.biz.media.MediaError;

// Declare any non-default types here with import statements

interface Listener {
    void onPlay();

    void onPause();

    void onStop();

    void onEnd();

    void onError(in MediaError error);
}
