package com.kaysanshi.nio;

import java.nio.ByteBuffer;

/**
 * @user:kaysanshi
 * @date:2021/3/8
 * @Description:
 * ByBuffer支持类型化的put和get,put和get的数据类型应一致。否者会出现BufferUnderflowException
 */
public class BufferTypeData {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 类型化方式放入
        byteBuffer.putInt(1);
        byteBuffer.putLong(1);
        byteBuffer.putDouble(1.0);
        byteBuffer.putShort((short) 1);
        byteBuffer.putChar('a');

        // flip() 翻转此缓冲区。将buffer从写模式切换到读模式。 限制设置limit为当前位置position的值，然后位置position设置为零。如果定义了标记，则将丢弃
        byteBuffer.flip();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getDouble());
        System.out.println(byteBuffer.getShort());
        System.out.println(byteBuffer.getChar());
        /**
         * ~out
         * 1
         * 1
         * 1.0
         * 1
         * a
         */
    }
}
