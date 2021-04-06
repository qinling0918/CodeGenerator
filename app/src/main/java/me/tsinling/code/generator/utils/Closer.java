package me.tsinling.code.generator.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * created by tsinling on: 2020/9/11 16:54
 * description:
 */
class Closer {
    public static void close(Closeable... closeables) {

        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeable = null;
                }
            }
        }
    }
}