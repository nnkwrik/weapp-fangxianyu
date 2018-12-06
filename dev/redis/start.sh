#!/bin/bash
cur_dir=`pwd`
docker stop fangxianyu-redis
docker rm fangxianyu-redis
docker run -idt --name fangxianyu-redis -v ${cur_dir}/data:/data -p 6379:6379 redis:3.2
