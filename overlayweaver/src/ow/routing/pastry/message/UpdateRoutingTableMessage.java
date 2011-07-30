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

package ow.routing.pastry.message;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ow.id.IDAddressPair;
import ow.messaging.Message;

public final class UpdateRoutingTableMessage extends Message {
	public final static String NAME = "UPDATE_ROUTING_TABLE";
	public final static boolean TO_BE_REPORTED = true;
	public final static Color COLOR = null;

	// message members
	public IDAddressPair[] nodes;
	public IDAddressPair[] leafSet;
	public boolean fromFinalHop;

	public UpdateRoutingTableMessage() { super(); }	// for Class#newInstance()

	public UpdateRoutingTableMessage(
			IDAddressPair[] nodes, IDAddressPair[] leafSet, boolean fromFinalHop) {
		this.nodes = nodes;
		this.leafSet = leafSet;
		this.fromFinalHop = fromFinalHop;
	}

	public void encodeContents(ObjectOutputStream oos) throws IOException {
		oos.writeObject(this.nodes);
		oos.writeObject(this.leafSet);
		oos.writeBoolean(this.fromFinalHop);
	}

	public void decodeContents(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		this.nodes = (IDAddressPair[])ois.readObject();
		this.leafSet = (IDAddressPair[])ois.readObject();
		this.fromFinalHop = ois.readBoolean();
	}
}
