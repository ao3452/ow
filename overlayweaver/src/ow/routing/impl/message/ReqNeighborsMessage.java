/*
 * Copyright 2011 Kazuyuki Shudo, and contributors.
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

package ow.routing.impl.message;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ow.messaging.Message;

public final class ReqNeighborsMessage extends Message {
	public final static String NAME = "REQ_NEIGHBORS";
	public final static boolean TO_BE_REPORTED = false;
	public final static Color COLOR = null;

	// message members
	public int num;

	public ReqNeighborsMessage() { super(); }	// for Class#newInstance()

	public ReqNeighborsMessage(
			int num) {
		this.num = num;
	}

	public void encodeContents(ObjectOutputStream oos) throws IOException {
		oos.writeInt(this.num);
	}

	public void decodeContents(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		this.num = ois.readInt();
	}
}
