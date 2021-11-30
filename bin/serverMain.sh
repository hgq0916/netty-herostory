#!/bin/bash
#运行主类
java_main_clazz=org.tinygame.herostory.ServerMain

#//////////////////////////////////////////////////

java_cmd="java -server -cp .:./lib/* -Xmx256m ${java_main_clazz}"

nohup $java_cmd > /data/log/herostory.log &