LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE            := h2co3Launcher
LOCAL_SHARED_LIBRARIES  := bytehook
LOCAL_SRC_FILES         := h2co3Launcher/h2co3Launcher_bridge.c \
                           h2co3Launcher/h2co3Launcher_event.c \
                           h2co3Launcher/h2co3Launcher_loader.c \
                           h2co3Launcher/jre_launcher.c \
                           h2co3Launcher/utils.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/h2co3Launcher/include
LOCAL_LDLIBS            := -llog -ldl -landroid
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := glfw
LOCAL_SHARED_LIBRARIES  := h2co3Launcher
LOCAL_SRC_FILES         := glfw/context.c \
                           glfw/init.c \
                           glfw/input.c \
                           glfw/monitor.c \
                           glfw/vulkan.c \
                           glfw/window.c \
                           glfw/h2co3Launcher_init.c \
                           glfw/h2co3Launcher_monitor.c \
                           glfw/h2co3Launcher_window.c \
                           glfw/egl_context.c \
                           glfw/osmesa_context.c \
                           glfw/platform.c \
                           glfw/posix_thread.c \
                           glfw/posix_time.c \
                           glfw/driver_helper.c \
                           driver_helper/nsbypass.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/h2co3Launcher/include \
                           $(LOCAL_PATH)/glfw/include
LOCAL_CFLAGS            := -Wall
LOCAL_LDLIBS            := -llog -ldl -landroid
ifeq ($(TARGET_ARCH_ABI), arm64-v8a)
LOCAL_CFLAGS            += -DADRENO_POSSIBLE
LOCAL_LDLIBS            += -lEGL -lGLESv2
endif
include $(BUILD_SHARED_LIBRARY)

#ifeq ($(TARGET_ARCH_ABI), arm64-v8a)
include $(CLEAR_VARS)
LOCAL_MODULE            := linkerhook
LOCAL_SRC_FILES         := driver_helper/hook.c
LOCAL_LDFLAGS           := -z global
include $(BUILD_SHARED_LIBRARY)
#endif

include $(CLEAR_VARS)
LOCAL_MODULE            := awt_headless
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := awt_xawt
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/awt_xawt
LOCAL_SHARED_LIBRARIES  := awt_headless
LOCAL_SRC_FILES         := awt_xawt/xawt_fake.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := pojavexec_awt
LOCAL_SHARED_LIBRARIES  := h2co3Launcher
LOCAL_SRC_FILES         := awt/awt_bridge.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
# Link GLESv2 for test
LOCAL_LDLIBS := -ldl -llog -landroid
# -lGLESv2
LOCAL_MODULE := pojavexec
LOCAL_SHARED_LIBRARIES  := h2co3Launcher
# LOCAL_CFLAGS += -DDEBUG
# -DGLES_TEST
LOCAL_SRC_FILES := \
    pojav/bigcoreaffinity.c \
    pojav/egl_bridge.c \
    pojav/ctxbridges/gl_bridge.c \
    pojav/ctxbridges/osm_bridge.c \
    pojav/ctxbridges/egl_loader.c \
    pojav/ctxbridges/osmesa_loader.c \
    pojav/ctxbridges/swap_interval_no_egl.c \
    pojav/environ/environ.c \
    pojav/input_bridge_v3.c \
    pojav/virgl/virgl.c \
    driver_helper/nsbypass.c
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/pojav
ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
LOCAL_CFLAGS += -DADRENO_POSSIBLE
LOCAL_LDLIBS += -lEGL -lGLESv2
endif
include $(BUILD_SHARED_LIBRARY)

$(call import-module,prefab/bytehook)