# server-scanner
Package of util classes to scan a LAN searching for different kind of servers (HTTP, HTPS, FTP...).

## Monitorized protocols
The following protocols can be scanned on the current version:
- ICMP
- HTTP
- HTTPS
- FTP

## Maven import

Just add the following dependency on your pom.xml:

```xml
<dependency>
  <groupId>brv.tools</groupId>
  <artifactId>server-scanner</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Generating a ScanDaemon

There are multiple ScanDaemons (at least, one per protocol) and they are immutable, so you need to use a builder to obtain one.

The most basic way to create the one is as it follows:

```java
ScanDaemon daemonHttp = new ScanDaemonBuilder(Protocol.HTTP).build();
```

Using build directly will use the default port for the protocol (in this case HTTP=80), default timeout and default sleep values in milliseconds.

In the case you want to customize that daemon behavior, you just need to add more steps to the builder:

```java
ScanDaemon daemonHttp = new ScanDaemonBuilder(Protocol.HTTP).withPort(8080)
                                                            .withTimeout(30)
                                                            .withSleep(1000)
                                                            .build();
```

In this case, the daemon will scan for HTTP servers on the 8080 port, the connection timeout will be 30ms and after each cycle of scans, it will sleep for 1 second.

Please note that ScanDaemon is just an abstract class. It is actually creating a HttpScanDaemon underneath, so you can also cast it to its real type, however it is highly recommended to just use the abstract class as it acts as an interface:

```java
HttpScanDaemon daemonHttp = (HttpScanDaemon) new ScanDaemonBuilder(Protocol.HTTP).build();
```


## Executing a ScanDaemon

Any ScanDaemon created, no matter the selected protocol, has a Thread worker inside of it. The abstract class provides three methods to control the daemon:

```java
ScanDaemon daemonHttp = new ScanDaemonBuilder(Protocol.HTTP).build();

// Starts the daemon execution:
daemonHttp.start();

// Soft-stops the daemon execution:
daemonHttp.stop();

// Hard-stops the daemon execution:
daemonHttp.interrupt();
```

The difference between the two kind of stops:
- ScanDaemon.stop(): the worker will stop on the next cycle of execution, finishing any currently ongoing tasks first.
- ScanDaemon.interrupt(): an interruption signal will be sent to the worker, therefore immediately stopping whatever it is doing at the moment(including sleeping, or waiting for a server connection).
