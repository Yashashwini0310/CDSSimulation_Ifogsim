
import streamlit as st
import pandas as pd
import os
import json

st.set_page_config(page_title="Fog & IoT Simulation Dashboard", layout="wide")
st.title("📡 Fog Computing + IoT Stream Dashboard")

# ------------------------------------------
# Fog Simulation Section
# ------------------------------------------
st.header("💻 Fog Simulation Summary")

summary_file = "FogSim_Simulation_Summary2.csv"
if os.path.exists(summary_file):
    df_summary = pd.read_csv(summary_file)
    st.subheader("📊 Simulation Summary Table")
    st.dataframe(df_summary, use_container_width=True)
else:
    st.error(f"{summary_file} not found. Please upload it or check the path.")

st.sidebar.header("📁 Upload FogSim Summary")
uploaded_file = st.sidebar.file_uploader("Upload CSV", type=["csv"])
if uploaded_file:
    df_summary = pd.read_csv(uploaded_file)
    st.success("FogSim summary uploaded!")
    st.dataframe(df_summary)

if 'df_summary' in locals():
    st.subheader("✅ Key Metrics")
    for i, row in df_summary.iterrows():
        st.markdown(f"**{row['Metric']}**: {row['Value / Status']}")

# Optional: Allow download
if os.path.exists("FogSim_Simulation_Summary2.csv"):
    with open("FogSim_Simulation_Summary2.csv", "rb") as f:
        st.sidebar.download_button("⬇️ Download FogSim Summary", f, file_name="FogSim_Simulation_Summary2.csv")

# Simulated Tuple Flow
st.subheader("📈 Tuple Flow Breakdown (Mock)")
tuple_data = pd.DataFrame({
    "Module": ["SENSOR", "processing", "decision", "DISPLAY"],
    "Tuples Processed": [100, 100, 100, 100]
})
st.bar_chart(tuple_data.set_index("Module"))

# ------------------------------------------
# IoT Secure vs Insecure Stream
# ------------------------------------------
st.markdown("---")
st.header("🔐 IoT Secure vs Insecure Stream Monitor")

default_iot_log = "simulated_iot_stream.log"
data = []

# Try loading from file automatically
if os.path.exists(default_iot_log):
    with open(default_iot_log, "r") as f:
        lines = f.readlines()
        data = [json.loads(line) for line in lines if line.strip()]
    st.success("Auto-loaded IoT stream from 'simulated_iot_stream.log'")
else:
    # Allow manual upload
    iot_file = st.file_uploader("Upload IoT stream log (.log or .txt)", type=["log", "txt"])
    if iot_file:
        lines = iot_file.readlines()
        data = [json.loads(line.decode("utf-8")) for line in lines if line.strip()]

if data:
    df_iot = pd.DataFrame(data)
    df_iot["timestamp"] = pd.to_datetime(df_iot["timestamp"], unit="s")

    st.subheader("📈 Traffic Over Time")
    chart_data = df_iot.groupby(["timestamp", "sensorType"]).size().unstack().fillna(0)
    st.line_chart(chart_data)

    st.subheader("📊 Count by Sensor Type")
    st.bar_chart(df_iot["sensorType"].value_counts())

    if "simulatedDelay" in df_iot.columns:
        st.subheader("⏱️ Delay Distribution (Optional)")
        st.box_chart(df_iot[["sensorType", "simulatedDelay"]])
else:
    st.info("Waiting for IoT data upload or log file...")

# Footer
st.markdown("---")
st.markdown("🛠️ Built by Yashashwini Suresh | Simulated via iFogSim + Azure IoT")
