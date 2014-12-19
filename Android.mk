#LOCAL_PATH := $(call my-dir)
#include $(CLEAR_VARS)
#
#LOCAL_MODULE_TAGS := optional
#
#LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4
#
#LOCAL_SRC_FILES := $(call all-java-files-under, src)
#
#LOCAL_PRIVILEGED_MODULE := true
#LOCAL_PACKAGE_NAME := DU-Tweaks
#
#include $(BUILD_PACKAGE)
##################################################
#include $(CLEAR_VARS)
#
#
#include $(BUILD_MULTI_PREBUILT)
#
# Use the folloing include to make our test apk.
#include $(call all-makefiles-under,$(LOCAL_PATH))
