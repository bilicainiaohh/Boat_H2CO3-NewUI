/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.stb;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memASCII;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBufferNT1;
import static org.lwjgl.system.MemoryUtil.memGetAddress;
import static org.lwjgl.system.MemoryUtil.memPointerBuffer;
import static org.lwjgl.system.MemoryUtil.nmemAllocChecked;
import static org.lwjgl.system.MemoryUtil.nmemCallocChecked;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeResource;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.Struct;
import org.lwjgl.system.StructBuffer;

import java.nio.ByteBuffer;

import javax.annotation.Nullable;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct stb_vorbis_comment {
 *     char * vendor;
 *     int comment_list_length;
 *     char ** comment_list;
 * }</code></pre>
 */
@NativeType("struct stb_vorbis_comment")
public class STBVorbisComment extends Struct<STBVorbisComment> implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        VENDOR,
        COMMENT_LIST_LENGTH,
        COMMENT_LIST;

    static {
        Layout layout = __struct(
            __member(POINTER_SIZE),
            __member(4),
            __member(POINTER_SIZE)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        VENDOR = layout.offsetof(0);
        COMMENT_LIST_LENGTH = layout.offsetof(1);
        COMMENT_LIST = layout.offsetof(2);
    }

    protected STBVorbisComment(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    @Override
    protected STBVorbisComment create(long address, @Nullable ByteBuffer container) {
        return new STBVorbisComment(address, container);
    }

    /**
     * Creates a {@code STBVorbisComment} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public STBVorbisComment(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** @return a {@link ByteBuffer} view of the null-terminated string pointed to by the {@code vendor} field. */
    @NativeType("char *")
    public ByteBuffer vendor() { return nvendor(address()); }
    /** @return the null-terminated string pointed to by the {@code vendor} field. */
    @NativeType("char *")
    public String vendorString() { return nvendorString(address()); }
    /** @return the value of the {@code comment_list_length} field. */
    public int comment_list_length() { return ncomment_list_length(address()); }
    /** @return a {@link PointerBuffer} view of the data pointed to by the {@code comment_list} field. */
    @NativeType("char **")
    public PointerBuffer comment_list() { return ncomment_list(address()); }

    // -----------------------------------

    /** Returns a new {@code STBVorbisComment} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static STBVorbisComment malloc() {
        return new STBVorbisComment(nmemAllocChecked(SIZEOF), null);
    }

    /** Returns a new {@code STBVorbisComment} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static STBVorbisComment calloc() {
        return new STBVorbisComment(nmemCallocChecked(1, SIZEOF), null);
    }

    /** Returns a new {@code STBVorbisComment} instance allocated with {@link BufferUtils}. */
    public static STBVorbisComment create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return new STBVorbisComment(memAddress(container), container);
    }

    /** Returns a new {@code STBVorbisComment} instance for the specified memory address. */
    public static STBVorbisComment create(long address) {
        return new STBVorbisComment(address, null);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static STBVorbisComment createSafe(long address) {
        return address == NULL ? null : new STBVorbisComment(address, null);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer malloc(int capacity) {
        return new Buffer(nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer calloc(int capacity) {
        return new Buffer(nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return new Buffer(memAddress(container), container, -1, 0, capacity, capacity);
    }

    /**
     * Create a {@link Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static Buffer create(long address, int capacity) {
        return new Buffer(address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : new Buffer(address, capacity);
    }

    /**
     * Returns a new {@code STBVorbisComment} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static STBVorbisComment malloc(MemoryStack stack) {
        return new STBVorbisComment(stack.nmalloc(ALIGNOF, SIZEOF), null);
    }

    /**
     * Returns a new {@code STBVorbisComment} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static STBVorbisComment calloc(MemoryStack stack) {
        return new STBVorbisComment(stack.ncalloc(ALIGNOF, 1, SIZEOF), null);
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer malloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer calloc(int capacity, MemoryStack stack) {
        return new Buffer(stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #vendor}. */
    public static ByteBuffer nvendor(long struct) { return memByteBufferNT1(memGetAddress(struct + STBVorbisComment.VENDOR)); }
    /** Unsafe version of {@link #vendorString}. */
    public static String nvendorString(long struct) { return memASCII(memGetAddress(struct + STBVorbisComment.VENDOR)); }
    /** Unsafe version of {@link #comment_list_length}. */
    public static int ncomment_list_length(long struct) { return UNSAFE.getInt(null, struct + STBVorbisComment.COMMENT_LIST_LENGTH); }
    /** Unsafe version of {@link #comment_list() comment_list}. */
    public static PointerBuffer ncomment_list(long struct) { return memPointerBuffer(memGetAddress(struct + STBVorbisComment.COMMENT_LIST), ncomment_list_length(struct)); }

    // -----------------------------------

    /** An array of {@link STBVorbisComment} structs. */
    public static class Buffer extends StructBuffer<STBVorbisComment, Buffer> implements NativeResource {

        private static final STBVorbisComment ELEMENT_FACTORY = STBVorbisComment.create(-1L);

        /**
         * Creates a new {@code STBVorbisComment.Buffer} instance backed by the specified container.
         *
         * <p>Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link STBVorbisComment#SIZEOF}, and its mark will be undefined.</p>
         *
         * <p>The created buffer instance holds a strong reference to the container object.</p>
         */
        public Buffer(ByteBuffer container) {
            super(container, container.remaining() / SIZEOF);
        }

        public Buffer(long address, int cap) {
            super(address, null, -1, 0, cap, cap);
        }

        Buffer(long address, @Nullable ByteBuffer container, int mark, int pos, int lim, int cap) {
            super(address, container, mark, pos, lim, cap);
        }

        @Override
        protected Buffer self() {
            return this;
        }

        @Override
        protected STBVorbisComment getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return a {@link ByteBuffer} view of the null-terminated string pointed to by the {@code vendor} field. */
        @NativeType("char *")
        public ByteBuffer vendor() { return STBVorbisComment.nvendor(address()); }
        /** @return the null-terminated string pointed to by the {@code vendor} field. */
        @NativeType("char *")
        public String vendorString() { return STBVorbisComment.nvendorString(address()); }
        /** @return the value of the {@code comment_list_length} field. */
        public int comment_list_length() { return STBVorbisComment.ncomment_list_length(address()); }
        /** @return a {@link PointerBuffer} view of the data pointed to by the {@code comment_list} field. */
        @NativeType("char **")
        public PointerBuffer comment_list() { return STBVorbisComment.ncomment_list(address()); }

    }

}