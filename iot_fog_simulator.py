#iot-file-yashashwini

from azure.iot.device import IoTHubDeviceClient, Message
import time
import random
import json
import os

conn_str = "HostName=my-iot-hub-yashashwini.azure-devices.net;DeviceId=HTTP_Device;SharedAccessKey=8drVAIJZ67MM3Lv0n+PBB1urdgtQhr9PHk0pAOaurIg="

log_dir = "FogMilitaryIoT"
log_path = os.path.join(log_dir, "simulated_iot_stream.log")
os.makedirs(log_dir, exist_ok=True)

client = IoTHubDeviceClient.create_from_connection_string(conn_str)

print("Starting to send both HTTP and HTTPS data to Azure IoT Hub...")

for i in range(10):
    # Simulated HTTP Data
    http_data = {
        "sensorType": "HTTP",
        "source": "EdgeProcessor",
        "data": random.uniform(20.0, 30.0),
        "timestamp": time.time()
    }
    http_msg = Message(str(http_data))
    client.send_message(http_msg)
    print("Sending HTTP message:", http_data)

    # Save to local file
    with open(log_path, "a") as f:
        f.write(json.dumps(http_data) + "\n")

    # Simulated HTTPS Data
    https_data = {
        "sensorType": "HTTPS",
        "source": "CDSModule",
        "secureData": random.uniform(50.0, 70.0),
        "timestamp": time.time()
    }
    https_msg = Message(str(https_data))
    client.send_message(https_msg)
    print("Sending HTTPS message:", https_data)

    # Save HTTPS to file
    with open(log_path, "a") as f:
        f.write(json.dumps(https_data) + "\n")

    time.sleep(3)

client.shutdown()
