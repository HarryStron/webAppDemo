package com.cs391.thrift;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.thrift.TException;

public class TimestampHandler implements TimestampService.Iface {

    @Override
    public String stamp() throws TException {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

}
