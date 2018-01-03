package com.aetheron;


import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class Conn {

    final AsynchronousSocketChannel channel;
    final ByteBuffer buf;
    Future<Integer> readFuture;
    String input;
    String output;

    public Conn(AsynchronousSocketChannel channel, ByteBuffer buf) {
        this.channel = channel;
        this.buf = buf;
        readFuture = CompletableFuture.completedFuture(0);
    }
}

public class Main {

    public static void main(String[] args) throws Exception {
        final int port = 8060;
        final ConcurrentHashMap<Conn, Boolean> clientChannels = new ConcurrentHashMap<>();

        final Consumer<AsynchronousSocketChannel> acceptHandler = new Consumer<AsynchronousSocketChannel>() {
            @Override
            public void accept(AsynchronousSocketChannel ch) {

                System.out.println("Accepted a connection: " + ch);

                ByteBuffer buf = ByteBuffer.allocate(50);
                buf.put("Hello and goodbye.".getBytes());
                buf.flip();
                ch.write(buf);

                clientChannels.put(new Conn(ch, ByteBuffer.allocate(1024)), false);
            }

            public void failed(Throwable exc, Object att) {
                System.out.println("Failed to accept connection: " + exc);
            }
        };

        AsyncServer server = new AsyncServer(port, acceptHandler);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (true) {
            clientChannels.forEach((Conn conn, Boolean isBusy) -> {
                if (conn.readFuture.isDone()) {
                    try {
                        int numRead = conn.readFuture.get();
                        if (numRead > 0) {
                            final byte[] receivedBytes = new byte[numRead];
                            conn.buf.flip();
                            conn.buf.get(receivedBytes);
                            conn.input = new String(receivedBytes).trim();
                            System.out.println("Input: " + conn.input);
                            conn.buf.clear();
                            
                            clientChannels.forEach((Conn c, Boolean b) -> {
                                c.channel.write(ByteBuffer.wrap(receivedBytes));
                            });
                            

                        }

                        // read again
                        conn.readFuture = conn.channel.read(conn.buf);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // server.stop();
        }
    }
}
