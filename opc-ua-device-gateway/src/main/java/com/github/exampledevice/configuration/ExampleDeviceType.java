package com.github.exampledevice;

import com.github.exampledevice.ExampleDevice;
import com.github.exampledevice.configuration.settings.ExampleDeviceSettings;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistentRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.localdb.persistence.ReferenceField;
import com.inductiveautomation.ignition.gateway.opcua.server.api.Device;
import com.inductiveautomation.ignition.gateway.opcua.server.api.DeviceContext;
import com.inductiveautomation.ignition.gateway.opcua.server.api.DeviceSettingsRecord;
import com.inductiveautomation.ignition.gateway.opcua.server.api.DeviceType;

public class ExampleDeviceType extends DeviceType {
    public static final ExampleDeviceType INSTANCE = new ExampleDeviceType();

    public static final String TYPE_ID = "ExampleDevice";

    public ExampleDeviceType(){
        //DisplayName and Description are retrieved from ExampleDevice.properties on resources
        super(TYPE_ID, "ExampleDevice.Meta.DisplayName", "ExampleDevice.Meta.Description")
    }

    @Override
    public RecordMeta<? extends PersistentRecord> getSettingsRecordType() {
        return ExampleDeviceSettings.META;
    }

    @Override
    public ReferenceField<?> getSettingsRecordForeignKey() {
        return ExampleDeviceSettings.DEVICE_SETTINGS;
    }

    @Nonnull
    @Override
    public Device createDevice(
            @Nonnull DeviceContext deviceContext,
            @Nonnull DeviceSettingsRecord deviceSettingsRecord
    ) {

        ExampleDeviceSettings settings = findProfileSettingsRecord(
                deviceContext.getGatewayContext(),
                deviceSettingsRecord
        );

        return new ExampleDevice(deviceContext, settings);
    }
}