`dssh4j`是Java版的`并行分布式SSH客户端`，基于jsch实现。可并行或串行在多台主机上执行指令，上传或下载文件。

# 特性
1. 支持在多台远程主机上执行指令。
2. 支持从本地上传文件到多台远程主机。
3. 支持从多台远程主机下载文件到本地。
4. 支持将多台远程主机编成一个组，通过组来操作多台主机。

# 指南

## 一、配置XML
在CLASSPATH的路径中增加一个dssh4j.xml：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<pssh4j>
    <!-- 
    host有四个子元素：
        address：主机的名称或IP地址。
        port：主机端口。
        user：登陆主机的用户名。
        password：登陆主机的密码。
     -->
    <host name="st-101">
        <address>192.168.56.101</address>
        <port>6622</port>
        <user>nieyong</user>
        <password>nieyong123456</password>
    </host>
    <host name="st-102">
        <address>192.168.56.102</address>
        <port>6622</port>
        <user>nieyong</user>
        <password>nieyong654321</password>
    </host>
    <host name="sh-201">
        <address>192.168.56.201</address>
        <port>6622</port>
        <user>aofeng</user>
        <password>aofeng123456</password>
    </host>
    <host name="sh-202">
        <address>192.168.56.202</address>
        <port>6622</port>
        <user>aofeng</user>
        <password>aofeng654321</password>
    </host>
    
    <!-- group下面可以有一个或多个host子元素（其值为单个主机的name属性的值）。 -->
    <group name="account-st">
        <host>st-101</host>
        <host>st-102</host>
    </group>
    <group name="account-sh">
        <host>sh-201</host>
        <host>sh-202</host>
    </group>

</pssh4j>
```

## 二、执行Shell指令。
1、在多台主机上执行指令。
```bash
java -jar dssh4j-1.0.0.jar -host "st-101,st-102" -op cmd -cmd "date"
```

2、在一组主机上执行指令。
```bash
java -jar dssh4j-1.0.0.jar -group "account-st" -op cmd -cmd "date; ls"
```

3、-group参数和-host参数混用。
```bash
java -jar dssh4j-1.0.0.jar -group "account-st" -host "sh-201" -op cmd -cmd "date; ls"
```

## 三、用SFTP下载文件。
1、从多台主机下载文件。
```bash
java -jar dssh4j-1.0.0.jar -host "sh-201,sh-202" -op down -local /home/aofeng/down/phpunit.txt -remote /home/nieyong/phpunit.txt
```
注：成功下载后，在/home/aofeng/down目录下会有两个文件：phpunit.txt.sh-201, phpunit.txt.sh-202。

2、从一组主机下载文件。
```bash
java -jar dssh4j-1.0.0.jar -group "account-sh" -op down -local /home/aofeng/down/phpunit.txt -remote /home/nieyong/phpunit.txt
```
注：组account-sh包含两个主机：sh-201和sh-201。成功下载后，在/home/aofeng/down目录下会有两个文件：phpunit.txt.sh-201, phpunit.txt.sh-202。

3、-group参数和-host参数混用。
```bash
java -jar dssh4j-1.0.0.jar -group "account-sh" -host "st-101" -op down -local /home/aofeng/down/phpunit.txt -remote /home/nieyong/phpunit.txt
```
注：成功下载后，在/home/aofeng/down目录下会有三个文件：phpunit.txt.sh-201, phpunit.txt.sh-202， phpunit.txt.st-101。

## 四、用SFTP上传文件。
1、向多台主机上传文件。
```bash
java -jar dssh4j-1.0.0.jar -host "sh-201,sh-202" -op up -local /home/aofeng/stat.awk -remote /home/nieyong/stat.awk
```

2、向一组主机上传文件。
```bash
java -jar dssh4j-1.0.0.jar -group "account-sh" -op up -local /home/aofeng/stat.awk -remote /home/nieyong/stat.awk
```

3、-group参数和-host参数混用。
```bash
java -jar dssh4j-1.0.0.jar -group "account-sh" -host "st-101" -op up -local /home/aofeng/stat.awk -remote /home/nieyong/stat.awk
```
