JC = javac
.SUFFIXES:.java .class
.java.class:
	$(JC) $*.java

CLASSES = \
	Own/Client/Client.java \
	Own/Client/ClientTest.java \
	Own/Server/keyword.java \
	Own/Server/OwnServer.java \
	Own/Server/Packet.java \
	Own/Server/Pending.java \
	Own/Server/ServerTest.java \
	Own/Server/ServerThread.java \
	Own/Server/User.java \
	Own/Server/UserList.java \
	Own/Server/util.java 

classes:$(CLASSES:.java=.class)

clean:\
	$(RM) *.class

server:\
	$(java) Own.Server.ServerTest