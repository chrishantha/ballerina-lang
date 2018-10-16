# Ballerina Performance Test Results

During each release, we execute various automated performance test scenarios and publish the results.

| Test Scenarios | Description |
| --- | --- |
| Passthrough HTTP service | An HTTP Service, which forwards all requests to a back-end service. |
| Passthrough HTTPS service | An HTTPS Service, which forwards all requests to a back-end service. |
| JSON to XML transformation HTTP service | An HTTP Service, which transforms JSON requests to XML and then forwards all requests to a back-end service. |
| JSON to XML transformation HTTPS service | An HTTPS Service, which transforms JSON requests to XML and then forwards all requests to a back-end service. |
| Passthrough HTTP2 (HTTPS) service | An HTTPS Service exposed over HTTP2 protocol, which forwards all requests to a back-end service. |

Our test client is [Apache JMeter](https://jmeter.apache.org/index.html). We test each scenario for a fixed duration of
time. We split the test results into warmup and measurement parts and use the measurement part to compute the
performance metrics.

A majority of test scenarios use a [Netty](https://netty.io/) based back-end service which echoes back any request
posted to it after a specified period of time.

We run the performance tests under different numbers of concurrent users, message sizes (payloads) and back-end service
delays.

The main performance metrics: 

1. **Throughput**: The number of requests that the Ballerina service processes during a specific time interval (e.g. per second).
2. **Response Time**: The end-to-end latency for an operation of invoking a Ballerina service. The complete distribution of response times was recorded.

In addition to the above metrics, we measure the load average and several memory-related metrics.

The following are the test parameters.

| Test Parameter | Description | Values |
| --- | --- | --- |
| Scenario Name | The name of the test scenario. | Refer to the above table. |
| Heap Size | The amount of memory allocated to the application | 1G |
| Concurrent Users | The number of users accessing the application at the same time. | 100, 200 |
| Message Size (Bytes) | The request payload size in Bytes. | 50B, 1024B |
| Back-end Delay (ms) | The delay added by the back-end service. | 0ms, 30ms |

The duration of each test is **3 minutes**. The warm-up period is **1 minute**.
The measurement results are collected after the warm-up period.

A [**c5.xlarge** Amazon EC2 instance](https://aws.amazon.com/ec2/instance-types/) was used to install Ballerina.

The following are the measurements collected from each performance test conducted for a given combination of
test parameters.

| Measurement | Description |
| --- | --- |
| Error % | Percentage of requests with errors |
| Average Response Time (ms) | The average response time of a set of results |
| Standard Deviation of Response Time (ms) | The “Standard Deviation” of the response time. |
| 99th Percentile of Response Time (ms) | 99% of the requests took no more than this time. The remaining samples took at least as long as this |
| Throughput (Requests/sec) | The throughput measured in requests per second. |
| Average Memory Footprint After Full GC (M) | The average memory consumed by the application after a full garbage collection event. |

The following is the summary of performance test results collected for the measurement period.

|Scenario Name|Heap Size|Concurrent Users|Message Size (Bytes)|Back-end Delay (ms)|Error %|Average Response Time (ms)|Standard Deviation of Response Time (ms)|99th Percentile of Response Time (ms)|Throughput (Requests/sec)|Average Memory Footprint After Full GC (M)|
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
|Passthrough HTTP service|1G|100|50|0|0|4.62|6.08|37|21454.15||
|Passthrough HTTP service|1G|100|50|30|0|30.89|1.89|32|3230.99||
|Passthrough HTTP service|1G|100|1024|0|0|4.94|6.39|39|20078.07||
|Passthrough HTTP service|1G|100|1024|30|0|31.01|2.4|33|3218.08||
|Passthrough HTTP service|1G|200|50|0|0|8.4|8.25|47|23674.53||
|Passthrough HTTP service|1G|200|50|30|0|31|1.62|34|6440.53||
|Passthrough HTTP service|1G|200|1024|0|0|8.98|8.46|49|22135.11||
|Passthrough HTTP service|1G|200|1024|30|0|31.14|2.37|36|6411.43||
|Passthrough HTTP2 (HTTPS) service|1G|100|50|0|0|5.32|7.41|43|18204.27|26.156|
|Passthrough HTTP2 (HTTPS) service|1G|100|50|30|0|30.86|1.03|32|3228.43|26.195|
|Passthrough HTTP2 (HTTPS) service|1G|100|1024|0|0|5.46|7.39|42|17428.84|26.157|
|Passthrough HTTP2 (HTTPS) service|1G|100|1024|30|0|30.9|1|32|3221.54|26.156|
|Passthrough HTTP2 (HTTPS) service|1G|200|50|0|0|10.19|10.86|60|19042.07|26.149|
|Passthrough HTTP2 (HTTPS) service|1G|200|50|30|0|37.74|12.46|61|5281.12|26.198|
|Passthrough HTTP2 (HTTPS) service|1G|200|1024|0|0|10.12|10.58|58|18621.75|26.157|
|Passthrough HTTP2 (HTTPS) service|1G|200|1024|30|0|35.86|10.98|61|5553.21|26.156|
|Passthrough HTTPS service|1G|100|50|0|0|4.71|6.87|39|21046.75|16.586|
|Passthrough HTTPS service|1G|100|50|30|0|31.07|2.63|34|3211.26|16.62|
|Passthrough HTTPS service|1G|100|1024|0|0|5.72|5.01|24|17327.54|16.591|
|Passthrough HTTPS service|1G|100|1024|30|0|30.95|0.98|32|3223.96|16.596|
|Passthrough HTTPS service|1G|200|50|0|0|9.08|9.16|51|21877.01|17.176|
|Passthrough HTTPS service|1G|200|50|30|0|31.18|2.36|35|6399.06|17.154|
|Passthrough HTTPS service|1G|200|1024|0|0|11.19|7.62|39|17781.36|17.189|
|Passthrough HTTPS service|1G|200|1024|30|0|31.25|2.24|35|6386.43|17.141|
|JSON to XML transformation HTTP service|1G|100|50|0|0|7.23|9.25|53|13749.34|24.924|
|JSON to XML transformation HTTP service|1G|100|50|30|0|30.93|1.08|32|3226.9|24.875|
|JSON to XML transformation HTTP service|1G|100|1024|0|0|10.11|9.43|50|9843.64|24.891|
|JSON to XML transformation HTTP service|1G|100|1024|30|0|31.14|1.14|34|3204.94|24.889|
|JSON to XML transformation HTTP service|1G|200|50|0|0|13.67|11.49|64|14571.85|24.897|
|JSON to XML transformation HTTP service|1G|200|50|30|0|31.37|2.23|37|6363.49|24.913|
|JSON to XML transformation HTTP service|1G|200|1024|0|0|19.08|14.29|77|10448.22|15.539|
|JSON to XML transformation HTTP service|1G|200|1024|30|0|32.78|2.55|43|6088.53|24.92|
|JSON to XML transformation HTTPS service|1G|100|50|0|0|8|7.74|43|12431.85|24.517|
|JSON to XML transformation HTTPS service|1G|100|50|30|0|31.03|0.92|33|3216.31|24.516|
|JSON to XML transformation HTTPS service|1G|100|1024|0|0|11.3|9.85|54|8806.23|24.502|
|JSON to XML transformation HTTPS service|1G|100|1024|30|0|31.49|2.96|49|3169.58|25.035|
|JSON to XML transformation HTTPS service|1G|200|50|0|0|14.63|11.77|65|13618.01|24.507|
|JSON to XML transformation HTTPS service|1G|200|50|30|0|31.54|2.08|38|6327.33|24.498|
|JSON to XML transformation HTTPS service|1G|200|1024|0|0|21.75|14.24|77|9167.35|24.851|
|JSON to XML transformation HTTPS service|1G|200|1024|30|0|33.5|2.97|46|5958.27|24.526|
