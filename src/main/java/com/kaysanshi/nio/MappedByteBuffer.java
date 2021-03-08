package com.kaysanshi.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/8
 * @Description:
 * MappedByteBuffer可让文件直接在内存（堆内存之外）中进行修改。操作系统不需要copy一次
 */
public class MappedByteBuffer {
    public static void main(String[] args) {
        try {
            // 随机访问文件类
            RandomAccessFile randomAccessFile = new RandomAccessFile("2.txt","rw");
            // 获取对应的通道
            FileChannel channel = randomAccessFile.getChannel();
            /**
             * FileChannel.MapMode.READ_WRITE ：使用读写模式
             * 0：可以直接修改的起始位置
             * 5：是映射到内存的大小(不是索引位置）即将2.txt的多少个自己二映射到内存。
             */
            java.nio.MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
            map.put(0,(byte) 'A');
            map.put(3,(byte) 'C');
            // map.put(5,(byte) 'M'); // IndexOutOfBoundsException
            randomAccessFile.close();
            System.out.println("修改成功");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
