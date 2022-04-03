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

#### 2. Vector Clock Time 
Scalar time suffers from concurrent messages, and can't advise the order in such case, 
that's why Vector clock come into the play, the algorithm depends on Vector data structure to track the order for each service within order defined per vector.

Basically, each process (service) will have same vector size, and each element in that vector will identify the order of message for that process.
for more details please refer to the pdf mentioned above.

#### How to use it?
````
var provider = TimeProviderFactory.ofType(TimeType.VECTOR_CLOCK);
provider.registerService(serviceName);

restTemplate.postForObject(
        "http://localhost:8088/api/message",
        provider.buildEvent("test message from sender", serviceName),
        String.class
);
````


#### General Usage 
This repo includes two simple spring boot services with generic code like the following to test 
you can use this CURL to test it , type 1 for scalar & otherwise for Vector
````
curl -X POST http://localhost:8089/api/message/sender1?type=1
curl -X POST http://localhost:8089/api/message/sender1?type=1
````

````
@PostMapping("/message/{appName}")
public void sendMessage(@RequestParam Integer type, @PathVariable String appName) {
    var provider = TimeProviderFactory.ofType(type == 1 ? 
                         TimeType.SCALER_CLOCK : TimeType.VECTOR_CLOCK);
    
    provider.registerService(appName);

    final String URL = type == 1 ?
            "http://localhost:8088/api/scalar-message" : "http://localhost:8088/api/vector-message";

    restTemplate.postForObject(
            URL,
            provider.buildEvent("test message from sender", appName),
            String.class
    );
}
````