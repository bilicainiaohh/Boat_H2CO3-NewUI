/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.util.freetype;

import static org.lwjgl.system.APIUtil.apiClosureRet;
import static org.lwjgl.system.APIUtil.apiCreateCIF;
import static org.lwjgl.system.MemoryUtil.memGetAddress;
import static org.lwjgl.system.libffi.LibFFI.FFI_DEFAULT_ABI;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_pointer;
import static org.lwjgl.system.libffi.LibFFI.ffi_type_sint32;

import org.lwjgl.system.CallbackI;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.libffi.FFICIF;

/**
 * <h3>Type</h3>
 *
 * <pre><code>
 * FT_Error (*{@link #invoke}) (
 *     FT_Pointer *data_pointer
 * )</code></pre>
 */
@FunctionalInterface
@NativeType("SVG_Lib_Init_Func")
public interface SVG_Lib_Init_FuncI extends CallbackI {

    FFICIF CIF = apiCreateCIF(
            FFI_DEFAULT_ABI,
            ffi_type_sint32,
            ffi_type_pointer
    );

    @Override
    default FFICIF getCallInterface() {
        return CIF;
    }

    @Override
    default void callback(long ret, long args) {
        int __result = invoke(
                memGetAddress(memGetAddress(args))
        );
        apiClosureRet(ret, __result);
    }

    /**
     * A callback that is called when the first OT-SVG glyph is rendered in the lifetime of an {@code FT_Library} object.
     */
    @NativeType("FT_Error")
    int invoke(@NativeType("FT_Pointer *") long data_pointer);

}