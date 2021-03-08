package com.kaysanshi.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/8
 * @Description:
 * 将文件进行copy到当前的目录下。
 * 使用两种方式
 */
public class FileChannelCopy {
    /**
     * 传统的copy
     */
    public void test (){
        try {
            FileInputStream fileInputStream = new FileInputStream("d:\\fileChannel.txt");
            FileChannel readChannel = fileInputStream.getChannel();

            // 这个是会创建一个文件在项目下
            FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
            FileChannel writeChannel = fileOutputStream.getChannel();

            // 分配缓冲区大小
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            while (true){
                // 循环读取
                // 清空buffer
                byteBuffer.clear();

                int read = readChannel.read((byteBuffer));
                if(read == -1){ // 读完
                    break;
                }
                // 将buffer中的数据写入fileChannel01
                // flip() 翻转此缓冲区。将buffer从写模式切换到读模式。 限制设置limit为当前位置position的值，然后位置position设置为零。如果定义了标记，则将丢弃
                byteBuffer.flip();
                writeChannel.write(byteBuffer);
            }
            fileInputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用channel中的transferForm进行copy
     * 将两个通道直接相连，这样的化会进行效率更好。比使用循环读更快。
     */
    public void test1(){
        try {
            FileInputStream fileInputStream = new FileInputStream("d:\\fileChannel.txt");
            FileChannel readChannel = fileInputStream.getChannel();

            // 这个是会创建一个文件在项目下
            FileOutputStream fileOutputStream = new FileOutputStream("transferFrom.txt");
            FileChannel writeChannel = fileOutputStream.getChannel();
            /**
             * 从给定的可读字节通道将字节传输到此通道的文件中。
             * 尝试从源通道读取计数字节并将其从给定位置开始写入该通道的文件。 调用此方法可能会或可能不会传输所有请求的字节； 是否这样做取决于渠道的性质和状态。
             * 如果源通道剩余的字节数少于计数字节，或者源通道是非阻塞的并且输入缓冲区中立即可用的字节数少于计数字节，则传输的字节数将少于请求的字节数。
             * 此方法不会修改此通道的位置。 如果给定位置大于文件的当前大小，则不会传输任何字节。 如果源通道有一个位置，则从该位置开始读取字节，然后将该位置增加读取的字节数。
             * 与从源通道读取并写入此通道的简单循环相比，此方法的效率可能要高得多。 许多操作系统可以将字节直接从源通道直接传输到文件系统缓存中，而无需实际复制它们。
             */
            writeChannel.transferFrom(readChannel,0,readChannel.size());
            /**
             *  transferTo是将当前通道数据写到另一个通道, 对象是当前通道, 所以我们不用考虑另一个通道的什么, 我就把文件数据写给你就行了
             */
            // writeChannel.transferTo(0,readChannel.size(),writeChannel);
            writeChannel.close();
            readChannel.close();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        FileChannelCopy fileChannelCopy = new FileChannelCopy();
        fileChannelCopy.test();
        // fileChannelCopy.test1();
    }
}
