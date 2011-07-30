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

import ow.id.ID;
import ow.id.IDAddressPair;
import ow.routing.RoutingContext;
import ow.routing.RoutingHop;

public final class RecRouteJoinMessage extends AbstractRecRouteMessage {
	public final static String NAME = "REC_ROUTE_JOIN";
	public final static boolean TO_BE_REPORTED = true;
	public final static Color COLOR = Color.BLUE;

	public RecRouteJoinMessage() { super(); }	// for Class#newInstance()

	public RecRouteJoinMessage(
			int routingID, ID[] target, RoutingContext[] cxt, int numRespNodeCands, IDAddressPair initiator, int ttl, RoutingHop[] route, IDAddressPair[] blackList) {
		super(routingID, target, cxt, numRespNodeCands, initiator, ttl, route, blackList);
	}
}
