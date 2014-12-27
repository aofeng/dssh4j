`dssh4j`是Java版的`并行分布式``SSH客户端`，基于jsch实现。可并行或串行在多台主机上执行指令，上传或下载文件。

特性
===
1. 支持在多台远程主机上执行指令。
1. 支持从本地上传文件到多台远程主机。
1. 支持从多台远程主机下载文件到本地。
1. 支持将多台远程主机编成一个组，通过组来操作多台主机。

指南
===
一、执行Shell指令。
---
1、在多台主机上执行指令。
```shell
java -jar dssh4j-1.0.0.jar -host "st-101,st-102" -op cmd -cmd "date"
```

2、在一组主机上执行指令。
```shell
java -jar dssh4j-1.0.0.jar -group "account-st" -op cmd -cmd "date; ls"
```

3、-group参数和-host参数混用。
```shell
java -jar dssh4j-1.0.0.jar -group "account-st" -host "sh-201" -op cmd -cmd "date; ls"
```

二、用SFTP下载文件。
---
1、从多台主机下载文件。
```shell
java -jar dssh4j-1.0.0.jar -host "sh-201,sh-202" -op down -local /home/aofeng/down/phpunit.txt -remote /home/nieyong/phpunit.txt
```
注：成功下载后，在/home/aofeng/down目录下会有两个文件：phpunit.txt.sh-201, phpunit.txt.sh-202。

2、从一组主机下载文件。
```shell
java -jar dssh4j-1.0.0.jar -group "account-sh" -op down -local /home/aofeng/down/phpunit.txt -remote /home/nieyong/phpunit.txt
```
注：组account-sh包含两个主机：sh-201和sh-201。成功下载后，在/home/aofeng/down目录下会有两个文件：phpunit.txt.sh-201, phpunit.txt.sh-202。

3、-group参数和-host参数混用。
```shell
java -jar dssh4j-1.0.0.jar -group "account-sh" -host "st-101" -op down -local /home/aofeng/down/phpunit.txt -remote /home/nieyong/phpunit.txt
```
注：成功下载后，在/home/aofeng/down目录下会有三个文件：phpunit.txt.sh-201, phpunit.txt.sh-202， phpunit.txt.st-101。

三、用SFTP上传文件。
---
1、向多台主机上传文件。
```shell
java -jar dssh4j-1.0.0.jar -host "sh-201,sh-202" -op up -local /home/aofeng/stat.awk -remote /home/nieyong/stat.awk
```

2、向一组主机上传文件。
```shell
java -jar dssh4j-1.0.0.jar -group "account-sh" -op up -local /home/aofeng/stat.awk -remote /home/nieyong/stat.awk
```

3、-group参数和-host参数混用。
```shell
java -jar dssh4j-1.0.0.jar -group "account-sh" -host "st-101" -op up -local /home/aofeng/stat.awk -remote /home/nieyong/stat.awk
```
