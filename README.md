## Logical Time in Distributed Systems

###    Overview
It is known in distributed systems Message ordering is a challenge, and there are a couple of algorithms to avoid this problem like Scalar time and Vector clock.

for more information please refer to this pdf https://www.cs.uic.edu/~ajayk/Chapter3.pdf

This repo will maintain those algorithms in simple way to be a starter for anyone who is interested in this topic.


###    Technology Stack 
The following repo is built using Java 17 and delivered as self fat JAR, however to demonstrate the usage with example there are also two simple service built using springboot 2.6


###     Algorithms 
#### 1. Scalar Time 
The algorithm is maintaining the total order property, and data is stored within `EventClock` that contains the clock scalar value **long value**, 
the processes' data are stored in memory in HashMap, however you can save it in permanent datastore if required 

The idea behind the algorithm is a follow
````
1. local_clock = max(local_clock, received_clock)
2. local_clock = local_clock + 1
3. message becomes available.
````

#### How to use it?
You need to start by register the process name (application name "Unique") , once process is registered you can use buildEvent to get EventClock. 
````
var provider = TimeProviderFactory.ofType(TimeType.SCALER_CLOCK);
provider.registerService(serviceName);

restTemplate.postForObject(
        "http://localhost:8088/api/message",
        provider.buildEvent("test message from sender", serviceName),
        String.class
);
````

