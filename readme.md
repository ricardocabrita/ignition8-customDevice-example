# Simple OPC-UA Device Example - Ignition 8

Example OPC-UA Device for Ignition 8. Implements a utility class that's similar to the old Ignition's AbstractTagDriver using the new Eclipse Milo OPC-UA implementation.

## Basics of programming device drivers with Ignition SDK

### Scopes

Ignition SDK modules can be programmed for the Gateway (Ignition server) or for the Designer/Client (running on a user or developer's computer). Device drivers run of course on the gateway scope.

### ModuleHook

Modules that are gateway scoped need to define a GatewayModuleHook that sets up the functions Ignition expects an entry point to have. OPC Drivers extend from the AbstractDeviceModuleHook (in Ignition 7.9 they used to extend from AbstractDriverModuleHook).

### Settings- ExampleDeviceSettings
Extends the PersistentRecord class to define configurable settings and properties the driver should present to the user on the create/edit device menu. Metadata information associated with the settings is configured in *ExampleDevicesSettings.properties* in the resources folder.

### Driver Type - ExampleDeviceType

A DriverType class is needed to crate new driver instances and bridge the gateway and the above mentioned settings.

### Properties

Files with .properties extension store metadata to be shown to the user. ExampleDevice.properties holds the display name and description for example, while ExampleDeviceSettings.properties holds metadata relevant for the settings.

### Device

In Ignition 7.9 drivers should extend from AbstractTagDriver which took care of managing OPC nodes and subscriptions etc - this API has now been deprecated and the Device API should be used. Most of high level OPC-UA node managing now needs to be programmed by the developer using the Eclip Milo OPC-UA Implementation. The opc-ua-device example provided by Ignition should be enough to start.

## This Example

### AbstractTagDevice

This class takes care of adding folder node directories to the OPC-UA server, as well as static, read-only and writable nodes. It implements interfaces for read-only tags and writable tags, with handles for setter and getter functions. The idea is to provide an interface similar to the old AbstractTagDriver using the new Device API and the Milo implementation.

### ExampleDevice

Extends from AbstractTagDevice and presents some examples of setting up folders and nodes with the getter and setter functions associated with a separater Driver class that handles whatever logic and communication with an actual physical device necessary.

### ExampleDriver

Just a simple Runnable (Threaded) class to feed values to the created tags in the ExampleDevice. Actual communication with a device and whatever logic and computations needed should go here.