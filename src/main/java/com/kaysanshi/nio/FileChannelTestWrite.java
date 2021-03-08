package com.kaysanshi.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/8
 * @Description:
 * 利用byteBuffer和FileChannel写入文件数据
 */
public class FileChannelTestWrite {
    public static void main(String[] args) {
        String string= "hello fileChannel";

        try {
            // 创建一个输出流
            FileOutputStream fileOutputStream = new FileOutputStream("d:\\fileChannel.txt");
            //通过FileOutputstream创建对应的channel
            FileChannel channel = fileOutputStream.getChannel();
            // 创建缓存区,并分配缓存区的大小
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 将写入的对象放入缓存区
            byteBuffer.put(string.getBytes());
            // flip() 翻转此缓冲区。将buffer从写模式切换到读模式。 限制设置limit为当前位置position的值，然后位置position设置为零。如果定义了标记，则将丢弃
            byteBuffer.flip();
            // 将byteBuffer写入到fileChannel
            channel.write(byteBuffer);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
