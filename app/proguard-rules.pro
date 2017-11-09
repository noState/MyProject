# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\AndroidSDK\Studio_Sdk/tools/proguard/proguard-android.txt
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

# 基本不用动区域
 -optimizationpasses 5
 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -dontskipnonpubliclibraryclassmembers
 -dontpreverify
 -verbose
 -printmapping proguardMapping.txt
 -optimizations !code/simplification/cast,!field/*,!class/merging/*
 -keepattributes *Annotation*,InnerClasses
 -keepattributes Signature
 -keepattributes SourceFile,LineNumberTable

 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class * extends android.view.View
 -keep public class com.android.vending.licensing.ILicensingService
 -keep class android.support.** {*;}

 -keepclasseswithmembernames class * {
     native <methods>;
 }
 -keepclassmembers class * extends android.app.Activity{
     public void *(android.view.View);
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }
 -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }
 -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }
 -keep class **.R$* {
  *;
 }
 -keepclassmembers class * {
     void *(**On*Event);
 }

# WebView
 -dontwarn com.tencent.smtt.export.external.**
 -keepclassmembers class fqcn.of.javascript.interface.for.Webview {
    public *;
 }
 -keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
     public boolean *(android.webkit.WebView, java.lang.String);
 }
 -keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, jav.lang.String);
 }
 -keep class android.webkit.JavascriptInterface {*;}

#=======================================================================SELF=============================================================================

 -keep class com.liuwen.myproject.base.entities.request.** { *; }
 -keep class com.liuwen.myproject.base.entities.response.** { *; }
 -keep class com.liuwen.myproject.utils.httpUtil.HttpResult{ *; }

#=======================================================================三方库============================================================================

# Glide 的混淆代码
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

# Banner23 的混淆代码
 -keep class com.youth.banner.** {
     *;
 }

# OkHttp3 的混淆代码
 -dontwarn okhttp3.**

# Retrofit2 的混淆代码
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions

# Okio 的混淆代码
 -dontwarn okio.**

# Rx 的混淆代码
 -dontwarn rx.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
     long producerIndex;
     long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

# Gson 的混淆代码
 -keep class sun.misc.Unsafe { *; }
 -keep class * implements com.google.gson.TypeAdapterFactory
 -keep class * implements com.google.gson.JsonSerializer
 -keep class * implements com.google.gson.JsonDeserializer

# Butterknife 的混淆代码
 -keep class butterknife.*
 -keepclasseswithmembernames class * { @butterknife.* <methods>; }
 -keepclasseswithmembernames class * { @butterknife.* <fields>; }
 -keep public class * implements butterknife.Unbinder { public <init>(...); }

# Umeng统计 的混淆代码
 -keepclassmembers class * {
   public <init> (org.json.JSONObject);
 }

# Umeng推送 的混淆代码
 -dontwarn com.taobao.**
 -dontwarn anet.channel.**
 -dontwarn anetwork.channel.**
 -dontwarn org.android.**
 -dontwarn org.apache.thrift.**
 -dontwarn com.xiaomi.**
 -dontwarn com.huawei.**
 -keepattributes *Annotation*
 -keep class com.taobao.** {*;}
 -keep class org.android.** {*;}
 -keep class anet.channel.** {*;}
 -keep class com.umeng.** {*;}
 -keep class com.xiaomi.** {*;}
 -keep class com.huawei.** {*;}
 -keep class org.apache.thrift.** {*;}
 -keep class com.alibaba.sdk.android.**{*;}
 -keep class com.ut.**{*;}
 -keep class com.ta.**{*;}
 -keep public class **.R$*{
    public static final int *;
 }
 -assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
 }

# Bugtags 的混淆代码
 -keepattributes LineNumberTable,SourceFile
 -keep class com.bugtags.library.** {*;}
 -dontwarn com.bugtags.library.**
 -keep class io.bugtags.** {*;}
 -dontwarn io.bugtags.**
 -dontwarn org.apache.http.**
 -dontwarn android.net.http.AndroidHttpClient

# RecyclerView 的混淆代码
 -keep class com.lsjwzh.widget.recyclerviewpager.**
 -dontwarn com.lsjwzh.widget.recyclerviewpager.**

