package com.kaysanshi.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @user:kaysanshi
 * @date:2021/3/8
 * @Description:
 * 将文件进行读取
 */
public class FIleChannelTestRead {
    public static void main(String[] args) {
        try {
            File file = new File("d:\\fileChannel.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            // 创建fileInputStream对应的fileChannel
            FileChannel channel = fileInputStream.getChannel();
            // 创建缓存区
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
            // 将通道的数据读入Buffer
            channel.read(byteBuffer);
            // 将 byteBuffer 的字节转为String
            System.out.println(new String(byteBuffer.array()));
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // ~output~   hello fileChannel
}
