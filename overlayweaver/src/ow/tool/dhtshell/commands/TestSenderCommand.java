package ow.tool.dhtshell.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ow.dht.ByteArray;
import ow.dht.DHT;
import ow.dht.ValueInfo;
import ow.id.ID;
import ow.routing.RoutingException;
import ow.tool.util.shellframework.Command;
import ow.tool.util.shellframework.CommandUtil;
import ow.tool.util.shellframework.Shell;
import ow.tool.util.shellframework.ShellContext;

import ow.tool.dhtshell.DLMessage;
import ow.tool.dhtshell.commands.GetCommand;
import ow.tool.dhtshell.commands.PutCommand;
import ow.messaging.MessageHandler;
import ow.messaging.MessageReceiver;
import ow.messaging.MessageSender;
import ow.messaging.MessagingAddress;
import ow.messaging.Message;
import ow.messaging.MessagingFactory;
import ow.messaging.MessagingProvider;
import ow.messaging.Signature;


public final class TestSenderCommand implements Command<DHT<String>> {
	private final static String[] NAMES = {"testsender"};
	private final static String NodeID[]= {"1987","2469","3065","4563","5946","6084","7268","8885","9547","10075"};
	private final PutCommand put = new PutCommand();
	
	// private void putNodeID(ShellContext<DHT<String>> context,Integer i){
	//     String[] args = context.getArguments();
	//     String[] tmp={i.toString(),NodeID[i]};
	//     context.setArguments(tmp);
	//     put.execute(context);
	//     context.setArguments(args);
	// }
	
	public String[] getNames() { return NAMES; }
	
	public String getHelp() {
		return "testsender";
	}
	
	public boolean execute(ShellContext<DHT<String>> context) {
		DHT<String> dht = context.getOpaqueData();
		PrintStream out = context.getOutputStream();
		String[] args = context.getArguments();
		boolean showStatus = false;
		String transport = "TCP";
	    int port = 10000;
	    int portRange = 10;

	    try {
			msgsender(transport, port, portRange,dht);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
    	return false;
	}
	
	private static void msgsender(String transport, int port, int portRange,DHT<String> dht)throws Exception {
     /*   MessagingProvider provider = MessagingFactory.getProvider(transport, Signature.getAllAcceptingSignature());
        MessageReceiver receiver = provider.getReceiver(provider.getDefaultConfiguration(), port, portRange);
        MessageSender sender = receiver.getSender();

        String host="133.68.15.197";
		MessagingAddress dest = provider.getMessagingAddress(host, port);
		byte[] sig={'h','w','!'};
        DLMessage msg=new DLMessage(123,null,sig);
        msg = (DLMessage) sender.sendAndReceive(dest, msg);*/
/*	    try {
			msgwaitstart(transport, port, portRange);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}*/
	    DLMessage messageClass = new DLMessage();

		dht.getRoutingService().addMessageHandler(messageClass.getClass(), new MessageHandler() {
		            public Message process(Message msg) {
		            	byte[] contents = msg.getSignature();
		            	System.out.println("msg received.");
		            	System.out.println("  tag: " + msg.getTag());
		            	System.out.print("  content: " + contents[0]);
		            	for (int i = 1; i < contents.length; i++) {
		            		System.out.print(", " + contents[i]);
		            	}
		            	System.out.println();
		            	return msg;
		            };});
	    try {Thread.sleep(5000);} catch (Exception e) {}
		MessagingAddress addr = 
			    dht.getRoutingService().getMessagingProvider()
			    .getMessagingAddress("133.68.15.197:3998");
		byte[] sig={'h','w','!'};
        DLMessage msg=new DLMessage(addr,sig);
        dht.getRoutingService().getMessageSender().send(addr, msg);
        byte[] contents = msg.getSignature();

        System.out.println("tag: " + msg.getTag());

        System.out.print("contents: " + contents[0]);
        for (int j = 1; j < contents.length; j++) {
            System.out.print(", " + contents[j]);
        }
        System.out.println();
	}
	
	private static void msgwaitstart(String transport, int port, int portRange) 
		    throws Exception {
		    MessagingProvider provider = 
		        MessagingFactory.getProvider(transport, Signature.getAllAcceptingSignature());
		    MessageReceiver receiver = 
		        provider.getReceiver(provider.getDefaultConfiguration(), port, portRange);
		    receiver.addHandler(
		        new MessageHandler() {
		            public Message process(Message msg) {
		            	byte[] contents = msg.getSignature();
		            	System.out.println("msg received.");
		            	System.out.println("  tag: " + msg.getTag());
		            	System.out.print("  content: " + contents[0]);
		            	for (int i = 1; i < contents.length; i++) {
		            		System.out.print(", " + contents[i]);
		            	}
		            	System.out.println();
		            	return msg;
		            };
		        }
		    );
		}

	private byte[] copyItoB (int fs,byte[] b,int[] re){
		int start=fs-16;
//		int[] re=new int[4];
//		byte[] byteData=new byte[4];
		for(int i=0;i<4;i++){
			b[start+i*4] = (byte)((re[i] >> 24) & 0xFF);
			b[start+i*4+1] = (byte)((re[i] >> 16) & 0xFF);
			b[start+i*4+2] = (byte)((re[i] >> 8) & 0xFF);
			b[start+i*4+3] = (byte)(re[i] & 0xFF);
		}
		
		return b;
	}
	private int[] copyBtoI (int fs,byte[] b){
		int start=fs-16;
		int[] re=new int[4];
//		byte[] byteData=new byte[4];
		for(int i=0;i<re.length;i++){
			int x0=(b[start+i*4]&0xFF)<<24;
			int x1=(b[start+i*4+1]&0xFF)<<16;
			int x2=(b[start+i*4+2]&0xFF)<<8;
			int x3=(b[start+i*4+3]&0xFF);
			re[i]=(x0|x1|x2|x3);
		}
/*		b[start+i*4] = (byte)((re[i] >> 24) & 0xFF);
		b[start+i*4+1] = (byte)((re[i] >> 16) & 0xFF);
		b[start+i*4+2] = (byte)((re[i] >> 8) & 0xFF);
		b[start+i*4+3] = (byte)(re[i] & 0xFF);*/
		
		
		return re;
	}
	private Integer copyBtoI (int fs,byte[] b,int num){
		int start=num*4;
		Integer re=0;
//		byte[] byteData=new byte[4];
		int x0=(b[start]&0xFF)<<24;
		int x1=(b[start+1]&0xFF)<<16;
		int x2=(b[start+2]&0xFF)<<8;
		int x3=(b[start+3]&0xFF);
		re=(x0|x1|x2|x3);
/*		b[start+i*4] = (byte)((re[i] >> 24) & 0xFF);
		b[start+i*4+1] = (byte)((re[i] >> 16) & 0xFF);
		b[start+i*4+2] = (byte)((re[i] >> 8) & 0xFF);
		b[start+i*4+3] = (byte)(re[i] & 0xFF);*/
		
		return re;
	}


}

