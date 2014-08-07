export PATH=$PATH:/data/data/com.bcmon.bcmon/files/tools
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/data/data/com.bcmon.bcmon/files/libs
export LD_PRELOAD=/data/data/com.bcmon.bcmon/files/libs/libfake_driver.so
airodump-ng -w /datadata/de.uulm.miss/files/capture --output-format csv -w capture wlan0 2>&1