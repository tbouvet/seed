/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.web;

import org.seedstack.seed.web.internal.SeedClientEndpointConfigurator;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 19/12/13
 */
@ClientEndpoint(configurator = SeedClientEndpointConfigurator.class)
public class ChatClientEndpoint1 {
    public static final String TEXT = "Client1 joins";
    public static CountDownLatch latch;
    public static String response;

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText(TEXT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {
        response = message;
        latch.countDown();
    }
}