#!/bin/bash
sshpass -p 'lejos' ssh -o KexAlgorithms=+diffie-hellman-group1-sha1 -c aes128-cbc root@10.0.1.1 'cd lejos && rm settings.properties && echo "lejos.keyclick_volume=0" >> settings.properties && echo "lejos.volume=50" >> settings.properties && cd config && rm pan.config && echo "USB * * 192.168.42.253 0.0.0.0 0.0.0.0 0.0.0.0 0.0.0.0" > pan.config' 

