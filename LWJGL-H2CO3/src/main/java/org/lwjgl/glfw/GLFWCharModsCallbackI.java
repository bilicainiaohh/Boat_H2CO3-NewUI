/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.glfw;

import static org.lwjgl.system.APIUtil.apiCreateCIF;
import static org.lwjgl.system.MemoryUtil.memGetAddress;
import static org.lwjgl.system.MemoryUtil.memGetInt;
import static org.lwjgl.system.libffi.LibFFI.FFI_DEFAULT_ABI;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_pointer;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_sint32;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_uint32;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_void;

import org.lwjgl.system.CallbackI;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.libffi.FFICIF;

/**
 * Instances of this interface may be passed to the {@link GLFW#glfwSetCharModsCallback SetCharModsCallback} method.
 * 
 * <p>Deprecared: scheduled for removal in version 4.0.</p>
 * 
 * <h3>Type</h3>
 * 
 * <pre><code>
 * void (*{@link #invoke}) (
 *     GLFWwindow *window,
 *     unsigned int codepoint,
 *     int mods
 * )</code></pre>
 *
 * @since version 3.1
 */
@FunctionalInterface
@NativeType("GLFWcharmodsfun")
public interface GLFWCharModsCallbackI extends CallbackI {

    FFICIF CIF = apiCreateCIF(
        FFI_DEFAULT_ABI,
        ffi_type_void,
        ffi_type_pointer, ffi_type_uint32, ffi_type_sint32
    );

    @Override
    default FFICIF getCallInterface() { return CIF; }

    @Override
    default void callback(long ret, long args) {
        invoke(
            memGetAddress(memGetAddress(args)),
            memGetInt(memGetAddress(args + POINTER_SIZE)),
                memGetInt(memGetAddress(args + 2L * POINTER_SIZE))
        );
    }

    /**
     * Will be called when a Unicode character is input regardless of what modifier keys are used.
     *
     * @param window    the window that received the event
     * @param codepoint the Unicode code point of the character
     * @param mods      bitfield describing which modifier keys were held down
     */
    void invoke(@NativeType("GLFWwindow *") long window, @NativeType("unsigned int") int codepoint, int mods);

}