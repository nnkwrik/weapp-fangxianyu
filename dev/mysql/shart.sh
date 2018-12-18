#!/bin/bash
cur_dir=`pwd`
docker stop fangxianyu-mysql
docker rm fangxianyu-mysql

docker run -d --name fangxianyu-mysql \
-v ${cur_dir}/conf:/etc/mysql/conf.d \
-v ${cur_dir}/data:/var/lib/mysql \
-v ${cur_dir}/dump:/docker-entrypoint-initdb.d \
-p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234  mysql:5.7

