# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xufeng.zhang/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#ShareSDK
-keep class * extends android.app.Dialog
-keep class com.tencent.mm.sdk.** {*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-dontwarn cn.sharesdk.**

# 腾讯混淆
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.qq.e.** {
    public protected *;
}

# 新浪微博混淆
-dontwarn com.weibo.sdk.android.WeiboDialog
-keep class com.weibo.sdk.android.** {*;}
-keep class com.sina.sso.** {*;}
-keep class com.sina.weibo.sdk.** { *; }

# 微信混淆
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
