# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\akhil\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# For Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# For Compose
-keep class androidx.compose.material.icons.** { *; }
