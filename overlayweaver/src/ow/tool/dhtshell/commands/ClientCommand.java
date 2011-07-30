/*
 * Copyright 2006-2009 National Institute of Advanced Industrial Science
 * and Technology (AIST), and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ow.tool.dhtshell.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import ow.dht.ByteArray;
import ow.dht.DHT;
import ow.dht.ValueInfo;
import ow.id.ID;
import ow.id.IDAddressPair;
import ow.messaging.Message;
import ow.messaging.MessageHandler;
import ow.messaging.MessagingAddress;
import ow.routing.RoutingException;
import ow.routing.RoutingResult;
import ow.tool.dhtshell.DL2Message;
import ow.tool.dhtshell.DLMessage;
import ow.tool.util.shellframework.Command;
import ow.tool.util.shellframework.Shell;
import ow.tool.util.shellframework.ShellContext;



public final class ClientCommand implements Command<DHT<String>> {
	private final static String[] NAMES = {"client"};
	private String NodeID[]=new String[10];
	
	private String myNumS;
	private int myNum;
	private String myNodeID;
	
	public String[] getNames() { return NAMES; }
	
	public String getHelp() {
		return "client <node-number> <port-number>";
	}
	
	public boolean execute(ShellContext<DHT<String>> context) {
		final DHT<String> dht = context.getOpaqueData();
		final PrintStream out = context.getOutputStream();
		String[] args = context.getArguments();
		boolean showStatus = false;
		

		int portRange=100;
		byte[] aaa ={'1'};
		ByteArray bA=new ByteArray(aaa);
		dht.setHashedSecretForPut(bA);
		
		myNumS=args[0];
		myNum=Integer.parseInt(myNumS);
		final int idSize = dht.getRoutingAlgorithmConfiguration().getIDSizeInByte();
    	Set<ValueInfo<String>> setValue=null;
    	StringBuilder sb = new StringBuilder();
    	for(int i=1;i<11;i++){
    		Integer obj =new Integer(i*1000+i*100+i*10+i);
    		
    		ID key = ID.parseID(obj.toString(), idSize);
    		NodeID[i-1]=this.get(key, dht);
			sb.append("get: ").append(NodeID[i-1]).append(Shell.CRLF);

    		
    	}
    	
    	myNodeID=NodeID[myNum];

    	sb.append("myID: ").append(myNumS).append(" ").append(myNodeID).append(Shell.CRLF);
    	out.print(sb);
    	out.flush();
    	
    	ID mykey=ID.parseID(myNodeID, idSize);
    	RoutingResult routeResult;
		final MessagingAddress myaddr;
		IDAddressPair[] IDAddr = null;
		try {
			routeResult = dht.getRoutingService().route(mykey, 1);
			IDAddr=routeResult.getResponsibleNodeCandidates();

		} catch (RoutingException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		myaddr=IDAddr[0].getMessagingAddress();
		
		DLMessage messageClass = new DLMessage();
		dht.getRoutingService().addMessageHandler(messageClass.getClass(), new MessageHandler() {
		            public Message process(Message msg) {
		                byte[] contents = msg.getContents();

		                //System.out.println("msg received."+myNum+" "+myNodeID);
		               // System.out.println("  tag: " + msg.getTag());

		                System.out.println();
		                
						int[] tmp = copyBtoI(contents.length, contents);

						switch(myNum){
						case 0: tmp[1]+=1;	break;
						case 1: tmp[1]/=2;	break;
						case 2: tmp[1]*=3;	break;
						case 3: tmp[1]-=4;	break;
						case 4: tmp[1]/=5;	break;
						case 5: tmp[1]+=6;   break;
						case 6: tmp[1]-=7;	break;
						case 7: tmp[1]*=8;	break;
						case 8: tmp[1]-=9;	break;
						case 9: tmp[1]*=10;	break;
						}
						Integer nextNode=copyBtoI(contents.length, contents, tmp[2]);
						tmp[2]=tmp[2]+1;
						while(nextNode.toString().equals(myNodeID)){
							switch(myNum){
							case 0: tmp[1]+=1;	break;
							case 1: tmp[1]/=2;	break;
							case 2: tmp[1]*=3;	break;
							case 3: tmp[1]-=4;	break;
							case 4: tmp[1]/=5;	break;
							case 5: tmp[1]+=6;   break;
							case 6: tmp[1]-=7;	break;
							case 7: tmp[1]*=8;	break;
							case 8: tmp[1]-=9;	break;
							case 9: tmp[1]*=10;	break;
							}
							nextNode=copyBtoI(contents.length, contents, tmp[2]);
							tmp[2]=tmp[2]+1;
						}
						System.out.println("num : "+tmp[0]+" ; result : "+tmp[1]+" ; count : "+tmp[2]);
						if(nextNode!=0){
			    			contents=copyItoB(contents.length, contents, tmp);
			    			ID nextkey=ID.parseID(nextNode.toString(), idSize);
			    			out.println(nextNode+" "+nextkey);
			    			//out.println(idSize);
			    			RoutingResult routeResult;
			    			try {
								routeResult=dht.getRoutingService().route(nextkey, 1);
								IDAddressPair[] IDAddr=routeResult.getResponsibleNodeCandidates();
								MessagingAddress addr=IDAddr[0].getMessagingAddress();
								DLMessage sendmsg=new DLMessage(myaddr,contents);
								dht.getRoutingService().getMessageSender().send(addr, sendmsg);
							} catch (RoutingException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							} catch (IOException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
						}else{
							byte[] result=new byte[4];
							result = copyItoB(result,tmp[1]);
							ID nextkey=ID.parseID("55128", idSize);
			    			RoutingResult routeResult;
			    			try {
								routeResult=dht.getRoutingService().route(nextkey, 1);
								IDAddressPair[] IDAddr=routeResult.getResponsibleNodeCandidates();
								MessagingAddress addr=IDAddr[0].getMessagingAddress();
								DL2Message sendmsg=new DL2Message(myaddr,result);
								dht.getRoutingService().getMessageSender().send(addr, sendmsg);
							} catch (RoutingException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							} catch (IOException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
						}
//try {Thread.sleep(100000);} catch (Exception e) {}
		                return msg;
		            };
		        });

		int flag=-1;
		ID flagkey=ID.parseID("flag", idSize);
		while(flag==-1){
		    try{

		        Thread.sleep(5000); //3000ミリ秒Sleepする
		        out.println("sleep end");
		        }catch(InterruptedException e){}

			flag=Integer.parseInt(this.get(flagkey, dht));

		}
    				
		out.println("end client "+myNum);
    	return false;
	}
	
	private String get(ID key,DHT<String> dht){
		String re=null;
		Set<ValueInfo<String>> setValue=null;
		try {
			setValue=dht.get(key);
		} catch (RoutingException e) {
			e.printStackTrace();
		}
		if (setValue != null) {
			if (!setValue.isEmpty()) {

				for(ValueInfo<String> v: setValue){
						re=v.getValue();						
					}
				}
			}
		return re;
	}
	private byte[] copyItoB (int fs,byte[] b,int[] re){
		int start=fs-12;

		for(int i=0;i<3;i++){
			b[start+i*4] = (byte)((re[i] >> 24) & 0xFF);
			b[start+i*4+1] = (byte)((re[i] >> 16) & 0xFF);
			b[start+i*4+2] = (byte)((re[i] >> 8) & 0xFF);
			b[start+i*4+3] = (byte)(re[i] & 0xFF);
		}
		
		return b;
	}
	private byte[] copyItoB (byte[] b,int re){

			b[0] = (byte)((re >> 24) & 0xFF);
			b[1] = (byte)((re >> 16) & 0xFF);
			b[2] = (byte)((re >> 8) & 0xFF);
			b[3] = (byte)(re & 0xFF);
		
		return b;
	}
	private int[] copyBtoI (int fs,byte[] b){
		int start=fs-12;
		int[] re=new int[3];
//		byte[] byteData=new byte[4];
		for(int i=0;i<re.length;i++){
			int x0=(b[start+i*4]&0xFF)<<24;
			int x1=(b[start+i*4+1]&0xFF)<<16;
			int x2=(b[start+i*4+2]&0xFF)<<8;
			int x3=(b[start+i*4+3]&0xFF);
			re[i]=(x0|x1|x2|x3);
		}

		return re;
	}
	private Integer copyBtoI (int fs,byte[] b,int num){
		int start=num*4;
		Integer re=0;

		int x0=(b[start]&0xFF)<<24;
		int x1=(b[start+1]&0xFF)<<16;
		int x2=(b[start+2]&0xFF)<<8;
		int x3=(b[start+3]&0xFF);
		re=(x0|x1|x2|x3);

		return re;
	}
	
}
