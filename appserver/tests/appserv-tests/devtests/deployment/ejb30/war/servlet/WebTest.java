/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

import java.io.*;
import java.net.*;

/**
 * Unit test for annotation associated to jsp.
 */
public class WebTest {

    private String host;
    private int port;
    private String contextRoot;

    public WebTest(String[] args) {
        host = args[0];
        port = Integer.parseInt(args[1]);
        contextRoot = args[2];
    }
    
    public static void main(String[] args) {

        WebTest webTest = new WebTest(args);
        try {
            webTest.doTest();
        } catch(Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void doTest() throws Exception {
     
        String contextPath = "/" + contextRoot + "/servlet";
        Socket s = new Socket(host, port);

        OutputStream os = s.getOutputStream();
        
        System.out.println(("GET " + contextPath + " HTTP/1.0\n"));
        os.write(("GET " + contextPath + " HTTP/1.0\n").getBytes());
        os.write("Authorization: Basic ajJlZTpqMmVl\n".getBytes());
        os.write("\n".getBytes());

        InputStream is = s.getInputStream();
        BufferedReader bis = new BufferedReader(new InputStreamReader(is));
        String line = null;

        int count = 0;
        int lineNum = 0;
        while ((line = bis.readLine()) != null) {
            System.out.println(lineNum + ": " + line);
            int index = line.indexOf("Hello World");
            if (index != -1) {
                count++;
            }
            lineNum++;
        }

        int correctCount = 2;
        if (count != correctCount) {
            System.err.println("Incorrect Message count: " + count +
                ", should be " + correctCount);
            System.exit(-1);
        }
    }
}
