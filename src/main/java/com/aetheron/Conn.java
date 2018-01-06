package com.aetheron;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ConcurrentLinkedQueue;

class Conn {

    private final ByteBuffer buf = ByteBuffer.allocate(1024);
    private final AsynchronousSocketChannel channel;
    private final ConcurrentLinkedQueue<String> inputQueue = new ConcurrentLinkedQueue<>();

    public Conn(AsynchronousSocketChannel channel) {
        this.channel = channel;
        readChannel();
    }

    public void sendOutput(String msg) {
        try {
            ByteBuffer buf = ByteBuffer.wrap(msg.getBytes());
            while (buf.hasRemaining()) {
                channel.write(buf).get();
            }
        } catch (Exception e) {
            System.out.println("Error writing output: " + e);
            e.printStackTrace();
        }
    }

    public String pollInput() {
        return inputQueue.poll();
    }

    private void readChannel() {
        channel.read(buf, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer numRead, Object att) {
                if (numRead > 0) {
                    final byte[] receivedBytes = new byte[numRead];
                    buf.flip();
                    buf.get(receivedBytes);
                    final String receivedString = new String(receivedBytes).trim();
                    for (String s : receivedString.split("[\r\n]+")) {
                        inputQueue.offer(s);
                    }
                    buf.clear();
                    readChannel();
                }
            }

            @Override
            public void failed(Throwable ex, Object att) {
                System.out.println("Failed to read channel: " + ex);
            }
        });
    }
}
