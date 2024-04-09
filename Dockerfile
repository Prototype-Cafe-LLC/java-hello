# https://circleci.com/developer/images/image/cimg/openjdk
#   java 21.0.2, jq 1.6, maven 3.9.6, 
#   node 20.11.0, ubuntu 22.04.3 LTS, wget 1.21.2, yarn 1.22.19
FROM cimg/openjdk:21.0.2-node

RUN sudo apt-get update && sudo apt-get install -y \
    curl \
    unzip
RUN sudo npm install -g npm@10.5.1

# aws cli
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && unzip awscliv2.zip && sudo ./aws/install

# aws cdk
RUN sudo npm install -g aws-cdk

WORKDIR /home/circleci

CMD "tail -f /dev/null"