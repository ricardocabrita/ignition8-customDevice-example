package com.github.exampledevice;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

import com.github.exampledevice.configuration.ExampleDeviceType;
import com.github.exampledevice.configuration.settings.ExampleDeviceSettings;
import com.github.exampledevice.ExampleDriver;

import com.inductiveautomation.ignition.gateway.opcua.server.api.DeviceContext;

public class ExampleDevice extends AbstractTagDevice {
    private final DeviceContext deviceContext;
    private final PulserDeviceSettings settings;
    private final String typeID;

    private UaFolderNode configFolder;
    private UaFolderNode channelsFolder;

    ExampleDriver drv;
    Thread driverThread;

    public ExampleDevice(DeviceContext deviceContext, ExampleDeviceSettings settings){
      this.deviceContext = deviceContext;
      typeID = ExampleDeviceType.TYPE_ID;
      super(deviceContext, typeID);

      //has the driver logic and actual communication with the physical device
      drv = new ExampleDriver(settings, deviceContext.getName());
      //it's generally useful to the driver logic and actual communication running on a thread
      driverThread = new Thread(drv);
      driverThread.start();
    }

    @Nonnull
    @Override
    public String getStatus() {
        return drv.status;
    }

    @Override
    public void onStartup() {
        super.onStartup();

        //Create whatever device folders you want, these are just an example
        configFolder = super.addFolder(rootNode, "Config");
        channelsFolder = super.addFolder(rootNode, "Channels");

        //Config tags - Static, like hostname, ports, and other config variables needed
        addStaticNode(configFolder, "Hostname", BuiltinDataType.String.getNodeId(), new DataValue(new Variant(settings.getHostname())));

        addReadOnlyNode(configFolder, "ID", BuiltinDataType.String.getNodeId(), new ReadOnlyTag() {
            @Override
            public DataValue getter(){
                return drv.getID();
            }
        });

        //example creating multiple similar tags/nodes dinamically
        for(int i = 0; i < drv.numberChans; i++ ){
          String nodeName = "Channel " + String.valueOf(i);
          addReadWriteNode(channelsFolder, nodeName, BuiltinDataType.Float.getNodeId(), new WritableTag(){
              @Override
              public void setter(DataValue val) {
                  drv.setChannelValue(val, i);
              }

              @Override
              public DataValue getter() {
                  return drv.getChannelValue(i);
              }
          });
        }

        // fire initial subscription creation
        List<DataItem> dataItems = deviceContext.getSubscriptionModel().getDataItems(getName());
        onDataItemsCreated(dataItems);
    }

    @Override
    public void onShutdown() {
        super.onShutdown();

        //clean up thread
        drv.running.set(false);
        drv.interrupt();

    }
}
