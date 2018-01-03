package com.aetheron;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.function.Consumer;

public class AsyncServer {

    private AsynchronousServerSocketChannel server;
    private Consumer<AsynchronousSocketChannel> acceptConsumer;

    public AsyncServer(final int port, final Consumer<AsynchronousSocketChannel> acceptConsumer) {

        this.acceptConsumer = acceptConsumer;

        try {
            server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));

            System.out.println("Server listening on " + port);

            server.accept("Client connection", new CompletionHandler<AsynchronousSocketChannel, Object>() {
                @Override
                public void completed(AsynchronousSocketChannel ch, Object att) {

                    acceptConsumer.accept(ch);

                    // accept the next connection
                    server.accept(null, this);
                }

                @Override
                public void failed(Throwable exc, Object att) {
                    System.out.println("Failed to accept connection: " + exc);
                }
            });
        } catch (IOException e) {
        }
    }

    public void stop() {
        try {
            server.close();
        } catch (IOException e) {
        }
    }
}