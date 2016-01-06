/*
 * @(#)Client.java $version 2016. 1. 4.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.asuraiv.coordination;

import com.asuraiv.coordination.menu.MainMenu;

/**
 * @author Jupyo Hong
 */
public class Client {

	public static void main(String[] args) {
		
		// application start
		MainMenu mainMenu = new MainMenu();
		mainMenu.displayMenu();
	}
}
