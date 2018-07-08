package com.chan.rainymood.biz.media;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chan on 2018/7/8.
 */

public class MediaError implements Parcelable {
	public static final int ERROR_IO = 1;
	public static final int ERROR_CORE = 2;

	public int code;
	public String msg;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.code);
		dest.writeString(this.msg);
	}

	public MediaError() {
	}

	protected MediaError(Parcel in) {
		this.code = in.readInt();
		this.msg = in.readString();
	}

	public static final Parcelable.Creator<MediaError> CREATOR = new Parcelable.Creator<MediaError>() {
		@Override
		public MediaError createFromParcel(Parcel source) {
			return new MediaError(source);
		}

		@Override
		public MediaError[] newArray(int size) {
			return new MediaError[size];
		}
	};
}
