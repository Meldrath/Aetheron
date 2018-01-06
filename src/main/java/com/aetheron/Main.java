package com.aetheron;

public class Main {

    public static void main(String[] args) throws Exception {
        final int port = 8060;
        final World world = new World();

        final AsyncServer server = new AsyncServer(port, ch -> {
            System.out.println("Accepted a connection: " + ch);
            world.add(new Conn(ch));
        });

        while (true) {
            world.doTick();
        }
        // server.stop();
    }
}
