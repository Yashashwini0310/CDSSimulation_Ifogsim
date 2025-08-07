# Introduction

IoT devices are growing exponentially in numbers, which necessitates the need to scale up their data processing architectures in a secure and low-latency manner. Fog computing brings the Cloud services even closer to the edge providing local decisions and this is very much needed in military or surveillance setting. With the traditional cloud systems already experiencing problems related to latency and bandwidth, fog computing presents great potential in helping fill this gap between low-level edge devices and the more centralized data centres.

The targeted IEEE paper proposes a Cache Decision System (CDS) to utilize the capabilities of the fog nodes on the basis of differentiating between secure (HTTPS) and insecure (HTTP) IoT traffic. The system delivers higher energy efficiency and minimal latency through emphasis on secure data and maximum optimization of the location of application modules.

Its project aims to replicate the original architecture using iFogSim and using Azure IoT Hub services to simulate the traffic and the development of a real-time Streamlit-based dashboard through visual analytics. Through the simulation of the flow of tuples, the number of transactions made using energy and the usage of the network, the project may provide the data on the feasibility of such a system execution in real smart environments.
“IoT-Based Big Data Secure Management in the Fog Over a 6G Wireless Network”
DOI: 10.1109/JIOT.2020.3033131

# How to Execute:
1.  Clone the repository in VS code and install the package required from requirements.txt file
2.	Compile and run the source file path based on java on Eclipse or VS Code: FogMilitaryIoTfix\FogMilitaryIoT\src\org\fog\test\MilitaryFogSimulation_FIXED.java
3.	Make sure to create .csv file
4.	Begin the Python-based traffic simulation to Azure IoT
5.	Run the Streamlit app (app.py) to obtain the plot of results

# Architecture Diagram

<img width="989" height="504" alt="image" src="https://github.com/user-attachments/assets/77588416-a233-4f05-bba6-f1ae0a341147" />

# Key Parameters:
•	Tuple Size is about 500-5000 bytes
•	Sensor Frequency is about 1 Second
•	1000 to 3000 of MIPS Module
•	Bandwidth of 100 to 10000 kbps
•	Processing: Fog1 does classification, sensing, Cloud does decision logic.

# Future improvements and Limitations
•	The simulation of real-time data was done through static distributions and not a varying load.
•	Their tuple classification was deterministic; a learner-based classification paradigm (e.g., anomaly detection, probabilistic security classification) would be more realistic to the real networks.
•	Limited to 3 Fog devices; an increase in the number of nodes can be used more efficiently to put resource allocation and congestion resolution to the test.
•	Additional improvements that may be done in the future are MQTT stream ingestion, Docker-based deployment to be portable and testing when faced with dynamic network.
