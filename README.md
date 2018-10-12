# ezyfox-server-android-client <img src="https://github.com/youngmonkeys/ezyfox-server/blob/master/logo.png" width="48" height="48" />
android client for ezyfox server

# Synopsis

android client for ezyfox server

# Code Example

**1. Create a TCP Client**

```java
final EzyClients clients = EzyClients.getInstance();
final EzyClient client = clients.newDefaultClient(config);
```

**2. Setup the client**

```java
final EzySetup setup = client.get(EzySetup.class);
setup.addEventHandler(EzyEventType.CONNECTION_SUCCESS, new EzyConnectionSuccessHandler() {
    @Override
    protected void postHandle() {
        connectionController.handleConnectSuccessfully();
    }
});
setup.addEventHandler(EzyEventType.CONNECTION_FAILURE, new EzyConnectionFailureHandler());
setup.addEventHandler(EzyEventType.DISCONNECTION, new EzyDisconnectionHandler() {
    @Override
    protected void preHandle(EzyDisconnectionEvent event) {
        connectionController.handleServerNotResponding();
    }
});
setup.addDataHandler(EzyCommand.HANDSHAKE, new HandshakeHandler(loginRequest));
setup.addDataHandler(EzyCommand.LOGIN, new EzyLoginSuccessHandler() {
    @Override
    protected void handleLoginSuccess(EzyData responseData) {
        EzyRequest request = new EzyAccessAppRequest("freechat");
        client.send(request);
    }
});
setup.addDataHandler(EzyCommand.APP_ACCESS, new EzyAccessAppHandler() {
    @Override
    protected void postHandle(EzyApp app, EzyArray data) {
        connectionController.handleAppAccessSuccessFully();
    }
});
setup.addEventHandler(EzyEventType.LOST_PING, new EzyEventHandler<EzyLostPingEvent>() {
    @Override
    public void handle(EzyLostPingEvent event) {
        connectionController.handleLostPing(event.getCount());
    }
});

setup.addEventHandler(EzyEventType.TRY_CONNECT, new EzyEventHandler<EzyTryConnectEvent>() {
    @Override
    public void handle(EzyTryConnectEvent event) {
        connectionController.handleTryConnect(event.getCount());
    }
});
```

**3. Setup an application**

```java
EzyAppSetup appSetup = setup.setupApp("freechat");

appSetup.addDataHandler("5", new EzyAppDataHandler<EzyObject>() {
    @Override
    public void handle(EzyApp app, EzyObject data) {
        contactController.handleGetContactsResponse(data);
    }
});
appSetup.addDataHandler(Commands.CHAT_SYSTEM_MESSAGE, new EzyAppDataHandler<EzyObject>() {
    @Override
    public void handle(EzyApp app, EzyObject data) {
        data.put("from", "System");
        messageController.handReceivedMessage(data);
    }
});

appSetup.addDataHandler(Commands.CHAT_USER_MESSAGE, new EzyAppDataHandler<EzyObject>() {
    @Override
    public void handle(EzyApp app, EzyObject data) {
        messageController.handReceivedMessage(data);
    }
});
```