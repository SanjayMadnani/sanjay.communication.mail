/*
 * Copyright (C) 2014, 2015 Sanjay Madnani
 *
 * This file is free to use: you can redistribute it and/or modify it under the terms of the 
 * GPL General Public License V2 as published by the Free Software Foundation, subject to the following conditions:
 *                                                                                          
 * The above copyright notice should never be changed and should always included wherever this file is used.
 *                                                                                          
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.  
 * See the GNU General Public License V2 for more details.                                       
 *
 */
package com.sanjay.communication.helper;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test case for testing send mail functionality.
 * 
 * @author SANJAY
 * @see CommunicationHelper
 */
public class CommunicationHelperTest {

	private CommunicationHelper communicationHelper;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception { 
		communicationHelper = new CommunicationHelper();
	}

	/**
	 * Test method for
	 * {@link com.sanjay.communication.helper.CommunicationHelper#sendMail(java.util.List, java.util.List, java.lang.String, java.lang.String, java.io.File)}
	 * .
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testSendMail() throws Exception {
		final List<String> toList = new ArrayList<String>();
		toList.add("your Mail address");
		final String msgSubject = "Let's Plan to meet.";
		final String msgBody = "Hi,<br/><b>I think we should plan to meet.</b>";
		assertTrue(communicationHelper.sendMail(toList, null, msgSubject, msgBody, null));
	}

}
