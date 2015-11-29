package com.abcxo.android.ifootball.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.abcxo.android.ifootball.BR;
import com.abcxo.android.ifootball.restfuls.RestfulError;
import com.abcxo.android.ifootball.restfuls.TweetRestful;
import com.abcxo.android.ifootball.restfuls.UserRestful;
import com.abcxo.android.ifootball.utils.NavUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SHARON on 15/10/29.
 */
public class User extends BaseObservable implements Parcelable, Serializable {
    public long id = 0;
    public String index;
    public String username;
    public String email;
    public String name;
    public String sign;
    public String password;
    public String avatar;
    public String cover;
    public String distance;
    public String time;
    public double lon;
    public double lat;
    public int focusCount;
    public int fansCount;
    public GenderType gender = GenderType.MALE;
    public UserType userType = UserType.NORMAL;
    public UserMainType mainType = UserMainType.NORMAL;

    @Bindable
    public boolean focus;

    public transient BindingHandler handler = new BindingHandler();


    public User() {
        super();

    }

    public boolean isMe() {
        return id == UserRestful.INSTANCE.meId();
    }

    protected User(Parcel in) {
        id = in.readLong();
        index = in.readString();
        username = in.readString();
        email = in.readString();
        name = in.readString();
        sign = in.readString();
        password = in.readString();
        avatar = in.readString();
        cover = in.readString();
        distance = in.readString();
        time = in.readString();
        lon = in.readDouble();
        lat = in.readDouble();
        focusCount = in.readInt();
        fansCount = in.readInt();
        focus = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void init() {
        handler = new BindingHandler();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(index);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(sign);
        dest.writeString(password);
        dest.writeString(avatar);
        dest.writeString(cover);
        dest.writeString(distance);
        dest.writeString(time);
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeInt(focusCount);
        dest.writeInt(fansCount);
        dest.writeByte((byte) (focus ? 1 : 0));
    }


    public enum GenderType {

        MALE(0),
        FEMALE(1);
        private int index;

        GenderType(int index) {
            this.index = index;
        }

        public static int size() {
            return GenderType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }


    public enum UserType {

        NORMAL(0),
        TEAM(1),
        VIP(2),
        SUPER(3);
        private int index;

        UserType(int index) {
            this.index = index;
        }

        public static int size() {
            return UserType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum UserMainType {

        NORMAL(0),
        CONTACT(1),
        DISCOVER(2),
        SPECIAL(3);
        private int index;

        UserMainType(int index) {
            this.index = index;
        }

        public static int size() {
            return UserMainType.values().length;
        }

        public int getIndex() {
            return index;
        }
    }

    public class BindingHandler {
        public void onClickAvatar(View view) {
            Image image = new Image();
            image.url = avatar;
            image.handler.onClickImage(view);
        }

        public void onClickUser(View view) {
            NavUtils.toUserDetail(view.getContext(), User.this);
        }

        public void onClickChat(View view) {
            if (UserRestful.INSTANCE.isLogin()) {
                NavUtils.toChatDetail(view.getContext(), UserRestful.INSTANCE.meId(), id);
            } else {
                NavUtils.toSign(view.getContext());
            }

        }

        public void onClickFocus(View view) {
            if (UserRestful.INSTANCE.isLogin()) {
                focus = !focus;
                notifyPropertyChanged(BR.focus);
                UserRestful.INSTANCE.focus(id, focus, new UserRestful.OnUserRestfulDo() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(RestfulError error) {
                    }

                    @Override
                    public void onFinish() {

                    }
                });
            } else {
                NavUtils.toSign(view.getContext());
            }
        }


    }

}
