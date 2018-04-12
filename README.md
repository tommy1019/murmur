# murmur

### What is it?
Murmur is an encrypted chat client that emphasizes security and privacy without sacrificing ease of use. 

### How does it work?
The first time you use murmur, the program will generate a "key" file for you. This file is the only way to verify your identity, so guard it carefully. Once you have your key file, you can open it on any computer and all of your chat logs and contacts will be available from the murmur server.

### My data is stored on a server? Isn't that unsafe?
Murmur uses 2048 bit end-to-end encryption. All of your data on our server is encrypted using your personal key, which only you have access to. If somebody were to breach our server, the data would appear to be gibberish.

This is the client implementation. See https://github.com/tommy1019/murmur-server for server implementation.

![Screenshot](https://i.imgur.com/oQraL1U.jpg)
