FROM mewlody/debian-graalvm

USER root

WORKDIR /root

COPY target/demo-aws-0.0.1-SNAPSHOT.jar /root/app/demo-aws.jar

RUN mkdir /root/log

CMD ["/bin/bash", "-c", "java -Daws.accessKeyId=$(printenv aws.accessKeyId) -Daws.secretKey=$(printenv aws.secretKey) -jar /root/app/demo-aws.jar"]
