#!/bin/sh

#sudo ipfw show
#sudo ipfw flush
sudo ipfw add 100 fwd 127.0.0.1,8080 tcp from any to any 80 in

rm frontend/RUNNING_PID
./mysbt.sh live