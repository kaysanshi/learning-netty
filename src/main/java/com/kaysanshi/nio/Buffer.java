package com.kaysanshi.nio;

import java.nio.IntBuffer;

/**
 * @user:
 * @date:2021/3/8
 * @Description: nio的buffer
 */
public class Buffer {
    public static void main(String[] args) {
        // 举简单的示例说明Buffer
        // 创建一个buffer可存放数据
        IntBuffer intBuffer = IntBuffer.allocate(10);
        for(int i=0;i<intBuffer.capacity();i++){
            intBuffer.put(i);
        }
        // 从buffer中读取数据
        // 将buffer进行转换.
        //
        // flip() 翻转此缓冲区。将buffer从写模式切换到读模式。 限制设置limit为当前位置position的值，然后位置position设置为零。如果定义了标记，则将丢弃
        intBuffer.flip();
        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
