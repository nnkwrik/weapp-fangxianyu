#!/usr/bin/env bash

#default username/password = guest/guest
# 15672 浏览器能访问的管理界面
# 5672 在项目中配置这个端口
docker stop fangxianyu-rabbitmq
docker rm fangxianyu-rabbitmq
docker run -d --name fangxianyu-rabbitmq --hostname my-rabbit-mq -p 5672:5672 -p 15672:15672 rabbitmq:3.7.8-management
