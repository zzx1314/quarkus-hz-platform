package org.huazhi.drones.tcp;

import java.nio.ByteBuffer;

import lombok.Data;

@Data
public class FrameHeader {

    public static final int HEADER_LENGTH = 20;
    public static final int MAGIC = 0xCAFEBABE;

    private int magic;
    private int version;
    private byte frameType;
    private byte flags;
    private byte reserved;
    private int length;
    private long sequence;

    public FrameHeader() {}

    // 解析 Header
    public static FrameHeader fromBytes(byte[] headerBytes) {
        if (headerBytes.length != HEADER_LENGTH) {
            throw new IllegalArgumentException("Header length invalid");
        }
        ByteBuffer buffer = ByteBuffer.wrap(headerBytes);
        buffer.order(java.nio.ByteOrder.BIG_ENDIAN);
        FrameHeader header = new FrameHeader();
        header.magic = buffer.getInt();
        header.version = buffer.get();
        header.frameType = buffer.get();
        header.flags = buffer.get();
        header.reserved = buffer.get();
        header.length = buffer.getInt();
        header.sequence = buffer.getLong();
        if (header.magic != MAGIC) {
            throw new IllegalStateException("Invalid magic number: " + header.magic);
        }
        return header;
    }

    public int getLength() {
        return length;
    }

    public byte getFrameType() {
        return frameType;
    }

    public long getSequence() {
        return sequence;
    }
}

