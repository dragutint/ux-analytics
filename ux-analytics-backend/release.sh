#!/bin/bash

# amd64 because of the linux server
docker build --platform linux/amd64 -t duda973/ux-analytics .

docker login

docker tag duda973/ux-analytics duda973/ux-analytics:1.5

docker push duda973/ux-analytics:1.5