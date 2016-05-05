package com.cs391.thrift;

import org.apache.thrift.TException;

public class TimestampHandler implements TimestampService.Iface {

    @Override
    public String stamp() throws TException {
        System.out.println("STAMP called in Thread " + Thread.currentThread().getId());
        return "GOTTEM";
    }

}
