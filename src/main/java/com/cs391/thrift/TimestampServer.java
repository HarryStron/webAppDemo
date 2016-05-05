package com.cs391.thrift;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.thrift.server.THsHaServer.Args;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

@Singleton
@Startup
public class TimestampServer {
    public static TimestampHandler handler;
    public static TimestampService.Processor processor;
    public static TNonblockingServerTransport serverTransport;
    public static TServer server;

    @PostConstruct
    public void startServer() {
        try {
            handler = new TimestampHandler();
            processor = new TimestampService.Processor(handler);

            Runnable simple = new Runnable() {
                @Override
                public void run() {
                    simple(processor);
                }
            };

            new Thread(simple).start();
            System.in.read();
            server.stop();
            
        } catch (Exception x) {
            System.err.println(x);
        }
    }

    public static void simple(TimestampService.Processor processor) {
        try {
            serverTransport = new TNonblockingServerSocket(10000);
            server = new TSimpleServer(new Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server in Thread " + Thread.currentThread().getId());
            server.serve();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    @PreDestroy
    public void stopServer() {
        server.stop();
    }
}
