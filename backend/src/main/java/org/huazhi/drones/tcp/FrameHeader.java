package org.huazhi.drones.tcp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * TCP 数据帧头
 * <p>
 * 协议说明：
 * - Header 固定 20 字节
 * - 大端序 (Big Endian)
 * - 用于客户端与服务端之间 Frame 数据的解析
 */
public class FrameHeader {

    /** Header 总长度 */
    public static final int HEADER_LENGTH = 20;

    /** 协议魔数，用于校验帧合法性 */
    public static final int MAGIC = 0xCAFEBABE;

    /** 魔数，固定 MAGIC */
    private int magic;

    /** 协议版本号 */
    private int version;

    /** 帧类型 (0=INIT, 1=MESSAGE, 2=FILE_DATA, 3=FINISH, 4=ERROR) */
    private byte frameType;

    /** 标志位，可扩展 */
    private byte flags;

    /** 保留字段 */
    private byte reserved;

    /** Body 长度 */
    private int length;

    /** 帧序列号，用于追踪 */
    private long sequence;

    public FrameHeader() {
        this.magic = MAGIC;
        this.version = 1;
    }

    /**
     * 将字节数组解析为 FrameHeader
     *
     * @param headerBytes 20 字节数组
     * @return FrameHeader 对象
     * @throws IllegalArgumentException header 长度不对
     * @throws IllegalStateException    魔数不匹配
     */
    public static FrameHeader fromBytes(byte[] headerBytes) {
        if (headerBytes == null || headerBytes.length != HEADER_LENGTH) {
            throw new IllegalArgumentException("Header length invalid, expected 20 bytes");
        }

        ByteBuffer buffer = ByteBuffer.wrap(headerBytes);
        buffer.order(ByteOrder.BIG_ENDIAN);

        FrameHeader header = new FrameHeader();
        header.magic = buffer.getInt();
        header.version = buffer.get();
        header.frameType = buffer.get();
        header.flags = buffer.get();
        header.reserved = buffer.get();
        header.length = buffer.getInt();
        header.sequence = buffer.getLong();

        if (header.magic != MAGIC) {
            throw new IllegalStateException(
                    String.format("Invalid magic number: 0x%08X, expected: 0x%08X", header.magic, MAGIC));
        }

        return header;
    }

    /**
     * 将 FrameHeader 转为字节数组，用于发送
     *
     * @return 20 字节的 header
     */
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.putInt(MAGIC);
        buffer.put((byte) version);
        buffer.put(frameType);
        buffer.put(flags);
        buffer.put(reserved);
        buffer.putInt(length);
        buffer.putLong(sequence);

        return buffer.array();
    }

    public static int getHeaderLength() {
        return HEADER_LENGTH;
    }


    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte getFrameType() {
        return frameType;
    }

    public void setFrameType(byte frameType) {
        this.frameType = frameType;
    }

    public byte getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public byte getReserved() {
        return reserved;
    }

    public void setReserved(byte reserved) {
        this.reserved = reserved;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    
}
