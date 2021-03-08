package com.kaysanshi.nio;

import java.nio.ByteBuffer;

/**
 * @user:
 * @date:2021/3/8
 * @Description:
 * 将buffer转换为只读的Buffer
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        // 创建一个buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for(int i=0;i<64;i++){
            byteBuffer.put((byte) i);
        }
        // 读取
        byteBuffer.flip();
        // 得到一个只读的buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        // buffer中是否有元素：
        // 告诉当前位置和极限之间是否有任何元素。
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.getChar());
        }
        // readOnlyBuffer.putInt(123); // ReadOnlyBufferException
    }
}
