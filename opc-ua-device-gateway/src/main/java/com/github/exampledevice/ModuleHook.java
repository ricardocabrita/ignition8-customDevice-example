package com.lz.lip.coimbra.devices;

import java.util.List;
import javax.annotation.Nonnull;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.lz.lip.coimbra.devices.configuration.PulserDeviceType;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.opcua.server.api.AbstractDeviceModuleHook;
import com.inductiveautomation.ignition.gateway.opcua.server.api.DeviceType;
import org.jetbrains.annotations.NotNull;

import static org.python.google.common.collect.Lists.newArrayList;

public class ModuleHook extends AbstractDeviceModuleHook {

    @Override
    public void setup(@NotNull GatewayContext context) {
        super.setup(context);
        
        BundleUtil.get().addBundle(PulserDevice.class);
    }

    @Override
    public void startup(@NotNull LicenseState activationState) {
        super.startup(activationState);
    }

    @Override
    public void shutdown() {
        super.shutdown();

        BundleUtil.get().removeBundle(PulserDevice.class);
    }

    @Nonnull
    @Override
    protected List<DeviceType> getDeviceTypes() {
        return newArrayList(PulserDeviceType.INSTANCE);
    }

}
