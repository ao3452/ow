package ow.tool.dhtshell;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ow.messaging.Message;
import ow.messaging.MessagingAddress;

public class DL2Message extends Message{
	public final static String NAME = "DL2";
	public final static boolean TO_BE_REPORTED = true;
	public final static Color COLOR = null;
	
	public DL2Message(MessagingAddress src,byte[] contents){
		super(src,contents);
	}
	
	public DL2Message() {
		// TODO 自動生成されたコンストラクター・スタブ
		super();
	}
	public void encodeContents(ObjectOutputStream oos) throws IOException {
		oos.writeObject(super.contents);
	}

	public void decodeContents(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		super.contents=(byte[])ois.readObject();
	}
	
}