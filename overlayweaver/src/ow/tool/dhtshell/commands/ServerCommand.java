/*
 * Copyright 2006-2008 National Institute of Advanced Industrial Science
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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import ow.dht.ByteArray;
import ow.dht.DHT;
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


public final class ServerCommand implements Command<DHT<String>> {
	private final static String[] NAMES = {"server"};
	private final static String NodeID[]= {"1987","2469","3065","4563","5946","6084","7268","8885","9547","10075"};
	private final PutCommand put = new PutCommand();
	private int count=0;
	private int filenumber=1000;
	private int flag=0;
	int check=0;
	// private void putNodeID(ShellContext<DHT<String>> context,Integer i){
	//     String[] args = context.getArguments();
	//     String[] tmp={i.toString(),NodeID[i]};
	//     context.setArguments(tmp);
	//     put.execute(context);
	//     context.setArguments(args);
	// }
	
	public String[] getNames() { return NAMES; }
	
	public String getHelp() {
		return "server";
	}
	
	public boolean execute(ShellContext<DHT<String>> context) {
		DHT<String> dht = context.getOpaqueData();
		final PrintStream out = context.getOutputStream();
		String[] args = context.getArguments();
		boolean showStatus = false;
		byte[] aaa={'1'};

	
		ByteArray bA=new ByteArray(aaa);
		dht.setHashedSecretForPut(bA);

    	int idSize = dht.getRoutingAlgorithmConfiguration().getIDSizeInByte();

    	StringBuilder sb = new StringBuilder();
    	for(int i=1;i<11;i++){
    		Integer obj =new Integer(i*1000+i*100+i*10+i);
    		//ID key = ID.getHashcodeBasedID(obj.toString(), idSize);
    		ID key = ID.parseID(obj.toString(), idSize);
    		try {
    			dht.put(key, NodeID[i-1]);
    		} catch (Exception e) {
    			// TODO 自動生成された catch ブロック
    			e.printStackTrace();
    		}
    		sb.append("put: ").append(obj.toString()+" ").append(NodeID[i-1]).append(" "+key).append(Shell.CRLF);
    	}
    	

		ID flagkey = ID.parseID("flag", idSize);
		try {
			dht.put(flagkey, "-1");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

    	out.print(sb);
    	out.flush();
    	try{

    		Thread.sleep(10000); //3000ミリ秒Sleepする
    		
    	}catch(InterruptedException e){}
		DL2Message messageClass = new DL2Message();
		dht.getRoutingService().addMessageHandler(messageClass.getClass(), new MessageHandler() {
		            public Message process(Message msg) {
		                byte[] contents = msg.getContents();

/*  			                System.out.println("msg received.");
		                System.out.println("  tag: " + msg.getTag());*/

		
		                Integer result=copyBtoI(0,contents,0);
		                if(check==count){
		                	out.println("num : "+count+"; result : "+result);
		                	count++;
		                	flag=1;
		                }
					
		          //try {Thread.sleep(100000);} catch (Exception e) {}
		                return msg;
		            };
		});
    	try {
    	    long start = System.currentTimeMillis();
    		for(int i=0;i<filenumber;i++){
    			check=i;
    			// From test.jpg
    			DataInputStream dataInStream = 
    					new DataInputStream(
    							new BufferedInputStream(
    									new FileInputStream("/home/hirose/tmp/messageset/msg"+i+".dat")));
    			int fileSize =dataInStream.available();
    			byte[] b = new byte[fileSize];
    			int readByte = 0, totalByte = 0;
    			while(-1 != (readByte = dataInStream.read(b))){
    				totalByte += readByte;
    				//System.out.println("Read number: "+i+" Read: " + readByte + " Total: " + totalByte+" fileSize: "+fileSize);
    			}
    			dataInStream.close();
    			if(fileSize>65564){
    				out.println(i+" is pass ; filesize : "+fileSize);
    				count++;
    				continue;
    			}
 
    				

    			int checkflag=0;
    			int checkflag2=0;
    			for(int j=0;;j++){
    				Integer checkNode=this.copyBtoI(fileSize, b, j);

        			if(checkNode==0)break;
        			Integer checkNode1=this.copyBtoI(fileSize, b, j+1);
 
        			if(checkNode1==0)break;
        			Integer checkNode2=this.copyBtoI(fileSize, b, j+2);

        			if(checkNode2==0)break;
        			if(checkNode.equals(checkNode2)){
        				checkflag=1;
        				break;
        			}
        			Integer checkNode3=this.copyBtoI(fileSize, b, j+3);
        			if(checkNode3==0)break;
        			if(checkNode.equals(checkNode3)){
        				checkflag2=1;
        				break;
        			}
    			}
    			if(checkflag==1){
    				out.println(i+" is pass ; checkflag=1");
    				count++;
    				continue;
    			}
    			if(checkflag2==1){
    				out.println(i+" is pass ; checkflag2=1");
    				count++;
    				continue;
    			}
    			int[] tmp = this.copyBtoI(fileSize, b);
    			//tmp[0]=totalByte;
    			tmp[0]=i;
    			tmp[1]=i;
    			tmp[2]=0;
    			Integer nextNode=this.copyBtoI(fileSize, b, tmp[2]);
    			tmp[2]=tmp[2]+1;

    			b=this.copyItoB(fileSize, b, tmp);
    			
    			

    			ID mykey = ID.parseID("55128", idSize);	
    			RoutingResult routeResult1;

    			routeResult1=dht.getRoutingService().route(mykey, 1);
    			IDAddressPair[] myIDAddr=routeResult1.getResponsibleNodeCandidates();

    			MessagingAddress myaddr=myIDAddr[0].getMessagingAddress();
    			
    			ID nextkey = ID.parseID(nextNode.toString(), idSize);

    			RoutingResult routeResult;
    			try {
					routeResult=dht.getRoutingService().route(nextkey, 1);
					IDAddressPair[] IDAddr=routeResult.getResponsibleNodeCandidates();

					MessagingAddress addr=IDAddr[0].getMessagingAddress();
					DLMessage sendmsg=new DLMessage(myaddr,b);
					dht.getRoutingService().getMessageSender().send(addr, sendmsg);
				} catch (RoutingException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
    			
 

    			while(flag==0){
    			    try{

    			        Thread.sleep(10); //3000ミリ秒Sleepする

    			        }catch(InterruptedException e){}

    			}
    			flag=0;
 
    		}

    			try {
    				String[] value={"-1"};
    				dht.put(flagkey, "1");
    				dht.remove(flagkey, value, bA);
    			} catch (Exception e) {
    				// TODO 自動生成された catch ブロック
    				e.printStackTrace();
    			}
        	
    			long stop = System.currentTimeMillis();
    	    	out.println("end server");
    			out.println("time : " + (stop - start) + " msec");
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	

    	
    	return false;
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
//		byte[] byteData=new byte[4];
		int x0=(b[start]&0xFF)<<24;
		int x1=(b[start+1]&0xFF)<<16;
		int x2=(b[start+2]&0xFF)<<8;
		int x3=(b[start+3]&0xFF);
		re=(x0|x1|x2|x3);

		return re;
	}
}
