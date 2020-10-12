package com.github.exampledevice;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import com.lz.lip.coimbra.devices.configuration.settings.ExampleDeviceSettings;

import org.apache.log4j.Logger;
import com.inductiveautomation.ignition.common.util.LoggerEx;

public class ExampleDriver implements Runnable {
  public String name;
  ExampleDeviceSettings settings;

  private AtomicBoolean running = new AtomicBoolean(true);
  private AtomicBoolean stopped = new AtomicBoolean(false);
  private String hostname;
  private int numberChans = 10;

  //store node/tag values internally
  DataValue id;
  DataValue[] channels = new DataValue[numberChans];

  public final LoggerEx log = new LoggerEx(Logger.getLogger(getClass()));

  public ExampleDriver(ExampleDeviceSettings settings, String name){
    this.settings = settings;
    this.name = name;

    hostname = settings.getHostname();
    log.infof("%s [ExampleDriver] - Starting out mock device with hostname: %s", name, hostname);

  }

  public DataValue getID(){
    int idnr = (int) (Math.random() * 1000);
    id = new DataValue(new Variant("id-"+String.valueOf(idnr)), StatusCode.GOOD);
    return id;
  }

  public DataValue getChannelValue(int idx){
    //request value from device maybe
    float val =  (Math.random() * 5);
    channels[idx] = new DataValue(new Variant(val), StatusCode.GOOD);
    return channels[idx];
  }

  public void setChannelValue(DataValue val, int idx){
    //val DataValue comes from Ignitions OPC-UA server
    float fval = Float.valueOf(val.getValue().getValue().toString().trim());
    //do something with it, write to device etc.. maybe check if write was successfull
    boolean wsuccess = true;
    if(wsuccess){
      channels[idx] = new DataValue(new Variant(fval), StatusCode.GOOD);
    }else{
      //never reached, just as an example of setting node/tag quality based on readback
      //or something of the sort
      channels[idx] = new DataValue(new Variant(-1.0), StatusCode.BAD);
    }
  }

  @Override
  public void run(){

    while(running.get()){
      //do stuff here

      try{
        Thread.sleep(1000);
      }catch(InterruptedException ex){
        log.infof("%s - interrupted, time to stop sleeping and say goodbye!", name);
        Thread.currentThread().interrupt();
      }
    }
    //clean up
    stopped.set(true);
  }

}
