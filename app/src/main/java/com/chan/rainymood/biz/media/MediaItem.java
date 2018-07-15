package com.chan.rainymood.biz.media;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chan on 2018/7/8.
 */

public class MediaItem implements Parcelable {
	public String url;
	public boolean looping;

	public MediaItem() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.url);
		dest.writeByte(this.looping ? (byte) 1 : (byte) 0);
	}

	protected MediaItem(Parcel in) {
		this.url = in.readString();
		this.looping = in.readByte() != 0;
	}

	public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
		@Override
		public MediaItem createFromParcel(Parcel source) {
			return new MediaItem(source);
		}

		@Override
		public MediaItem[] newArray(int size) {
			return new MediaItem[size];
		}
	};
}
