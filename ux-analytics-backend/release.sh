#!/bin/bash

docker build -t duda973/ux-analytics .

docker login

docker tag duda973/ux-analytics duda973/ux-analytics:latest

docker push duda973/ux-analytics:latest