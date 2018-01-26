package com.aotuman.share.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aotuman.share.CommonUtils;
import com.aotuman.share.R;
import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareContentType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Anthony on 2016/6/16.
 */
public class SharePlatform extends Dialog {
    private GridView mGridView;
    private ArrayList<ShareType> lstImageItem = new ArrayList<>();
    private IShareClickCallback mCallback;
    private LinkedHashMap<ShareChannelType, ShareContentType> mShareChannelType;

    public SharePlatform(Activity activity, LinkedHashMap<ShareChannelType, ShareContentType> shareContentTypeArrayMap, IShareClickCallback callback) {
        super(activity, R.style.Daily_datail_windws);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.popup_share_platforms, null);
        mShareChannelType = shareContentTypeArrayMap;
        int height = 0;
        if (null != mShareChannelType && mShareChannelType.size() < 4) {
            height = (int) (200 * activity.getResources().getDisplayMetrics().density);
        } else {
            height = (int) (271 * activity.getResources().getDisplayMetrics().density);
        }
        int width = CommonUtils.getScreenWidth(activity) - (int) (15 * activity.getResources().getDisplayMetrics().density);

        setContentView(contentView,new ViewGroup.LayoutParams(width, height));
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mGridView = (GridView) contentView.findViewById(R.id.gv_share);
        mCallback = callback;
        initGrid();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    private void initGrid() {
        if (null != mShareChannelType) {
            ShareType type;
            Iterator iter = mShareChannelType.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                ShareChannelType shareChannelType = (ShareChannelType) entry.getKey();
                switch (shareChannelType) {
                    case WX_FRIEND:
                        type = new ShareType();
                        type.image = R.drawable.share_wxfriend;
                        type.name = getContext().getResources().getString(R.string.weixin_friends);
                        type.type = ShareChannelType.WX_FRIEND;
                        lstImageItem.add(type);
                        break;
                    case WX_TIMELINE:
                        type = new ShareType();
                        type.image = R.drawable.share_wxgroup;
                        type.name = getContext().getResources().getString(R.string.weixin_friends_circle);
                        type.type = ShareChannelType.WX_TIMELINE;
                        lstImageItem.add(type);
                        break;
                    case QQ:
                        type = new ShareType();
                        type.image = R.drawable.share_qq;
                        type.name = getContext().getResources().getString(R.string.share_platform3);
                        type.type = ShareChannelType.QQ;
                        lstImageItem.add(type);
                        break;
                    case WB:
                        type = new ShareType();
                        type.image = R.drawable.share_sina;
                        type.name = getContext().getResources().getString(R.string.manual_share_type0);
                        type.type = ShareChannelType.WB;
                        lstImageItem.add(type);
                        break;
                    case MESSAGE:
                        type = new ShareType();
                        type.image = R.drawable.share_sms;
                        type.name = getContext().getResources().getString(R.string.sms);
                        type.type = ShareChannelType.MESSAGE;
                        lstImageItem.add(type);
                        break;
                }
            }
            GridAdapter adapter = new GridAdapter();

            mGridView.setAdapter(adapter);
        }
    }

    public void releaseResource() {
        mCallback = null;
    }


    private void clickMethod(ShareChannelType type) {
        if (mCallback == null) {
            return;
        }
        mCallback.onShareCallback(type);
    }

    public static class ShareType {
        public int image;
        public String name;
        public ShareChannelType type;
    }

    public class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lstImageItem.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null || view.getTag() == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getContext()).inflate(R.layout.share_dialog_grid_item, viewGroup, false);
                holder.imageView = (ImageView) view.findViewById(R.id.iv_type);
                holder.textView = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            ShareType type = lstImageItem.get(i);
            holder.textView.setText(type.name);
            holder.imageView.setImageResource(type.image);
            view.setOnClickListener(new SkipMethodListener(type.type));

            return view;
        }

        public class ViewHolder {
            public ImageView imageView;
            public TextView textView;
        }

    }

    public class SkipMethodListener implements View.OnClickListener {
        private final ShareChannelType type;

        public SkipMethodListener(ShareChannelType type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if (canClick(500)) {
                clickMethod(type);
            }
        }
    }

    private static long clickTime;

    private static boolean canClick(long time) {
        if ((Math.abs(System.currentTimeMillis() - clickTime) > time)) {
            clickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public interface IShareClickCallback {
        void onShareCallback(ShareChannelType type);
    }
}
