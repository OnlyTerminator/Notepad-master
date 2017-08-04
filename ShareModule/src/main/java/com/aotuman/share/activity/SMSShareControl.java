package com.aotuman.share.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;

import com.aotuman.share.entity.ShareContentType;
import com.aotuman.share.entity.ShareRealContent;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by aotuman on 2017/7/24.
 */

public class SMSShareControl {
    private static final String TAG = "SMSShareControl";

    public void doShare(final Context context, final ShareRealContent shareData) {
        new Thread(){
            @Override
            public void run() {
                if (shareData.mShareContentType == ShareContentType.PICANDTEXT || shareData.mShareContentType == ShareContentType.PIC && isSDCardReady()) {
                    String url = context.getFilesDir().getPath() + "/mmstemp.jpg";
                    Bitmap tempBtm = BitmapFactory.decodeFile(shareData.mShareLocalImage);
                    savePictureShot(tempBtm, "mmstemp.jpg", 60, context);
                    try {
                        if (!TextUtils.isEmpty(shareData.mShareSummary)) {
                            sendMMSByMsg(shareData.mShareSummary, "file://" + url, context);
                        }
                    } catch (Exception e) {
                        sendMMSByAppList("file://" + url, context);
                    }
                } else {
                    try {
                        String smsContent = shareData.mShareSummary;
                        if(!TextUtils.isEmpty(shareData.mShareURL)){
                            String shortUrl = getShortUrlSync(shareData.mShareURL);
                            if(!TextUtils.isEmpty(shortUrl)) {
                                smsContent = smsContent + " " + shortUrl;
                            }
                        }
                        if (Build.VERSION.SDK_INT >= 19) {//At least KitKat
                            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); //Need to change the build to API 19
                            Intent sendIntent = new Intent(Intent.ACTION_SEND);
                            sendIntent.setType("text/plain");
                            sendIntent.putExtra(Intent.EXTRA_TEXT, smsContent);

                            if (defaultSmsPackageName != null) {//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
                                sendIntent.setPackage(defaultSmsPackageName);
                                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            context.startActivity(sendIntent);

                        } else { //For early versions, do what worked for you before.
                            Uri smsToUri = Uri.parse("sms://");
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_VIEW);
                            sendIntent.setData(smsToUri);
                            sendIntent.putExtra("sms_body", smsContent);
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            context.startActivity(sendIntent);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }

    private void sendMMSByAppList(String url, Context context) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mms://"));
        sendIntent.setType("image/jpeg");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        context.startActivity(Intent.createChooser(sendIntent, "MMS:"));
    }

    private void sendMMSByMsg(String content, String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
        intent.putExtra("sms_body", content);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        intent.setType("image/jpeg");
        context.startActivity(intent);
    }

    private boolean isSDCardReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Save bitmap to picture
     *
     * @param bitmap
     * @param saveName e.g: "weather1.jpg"
     * @param quality  0-100
     * @return
     */
    private boolean savePictureShot(Bitmap bitmap, String saveName, int quality, Context context) {
        FileOutputStream fos = null;
        try {
            context.deleteFile(saveName);
            fos = context.openFileOutput(saveName, Context.MODE_WORLD_READABLE);
            if (null != fos && bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                fos.flush();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return true;
    }

    private String getShortUrlSync(String longurl) {
        if (TextUtils.isEmpty(longurl)) {
            return "";
        }
        return longurl;
    }
}
