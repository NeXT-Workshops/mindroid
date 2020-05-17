#!/bin/bash

sshpass -p 'lejos' ssh -o KexAlgorithms=+diffie-hellman-group1-sha1 -c aes128-cbc root@10.0.1.1 'cd lejos && echo -e "lejos.keyclick_volume=0\nlejos.volume=50\n" > settings.properties'

sshpass -p 'lejos' ssh -o KexAlgorithms=+diffie-hellman-group1-sha1 -c aes128-cbc root@10.0.1.1 'shutdown now'
