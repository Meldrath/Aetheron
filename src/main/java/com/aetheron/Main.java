package com.aetheron;


import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentHashMap;
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

    public String getInput() {
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
                    inputQueue.offer(receivedString);
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

public class Main {

    public static void main(String[] args) throws Exception {
        final int port = 8060;
        final ConcurrentHashMap<Conn, Boolean> clientChannels = new ConcurrentHashMap<>();

        final Consumer<AsynchronousSocketChannel> acceptHandler = ch -> {

            System.out.println("Accepted a connection: " + ch);

            ByteBuffer buf = ByteBuffer.allocate(50);
            buf.put("Hello and goodbye.".getBytes());
            buf.flip();
            ch.write(buf);

            clientChannels.put(new Conn(ch), false);
        };

        AsyncServer server = new AsyncServer(port, acceptHandler);

        while (true) {
            clientChannels.forEach((Conn conn, Boolean isBusy) -> {
                final String input = conn.getInput();
                if (input != null) {
                    clientChannels.forEach((Conn c, Boolean b) -> c.sendOutput(input));
                }
            });
            // server.stop();
        }
    }
}
