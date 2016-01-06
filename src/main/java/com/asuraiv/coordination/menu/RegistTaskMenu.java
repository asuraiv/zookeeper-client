/*
 * @(#)RegistJobView.java $version 2016. 1. 4.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.asuraiv.coordination.menu;

import java.io.IOException;
import java.util.Scanner;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

import com.asuraiv.coordination.enums.TaskStatus;

/**
 * @author Jupyo Hong
 */
public class RegistTaskMenu implements Menu, Watcher {
	
	private Menu nextMenu;
	
	@Override
	public void displayMenu() {
		
		boolean isContinue = true;
		
		while(isContinue) {
						
			System.out.println();
			System.out.println();
			System.out.println("### 작업 등록 ###");			
			System.out.println();
			System.out.print("작업을 등록 하시겠습니까?(Y/N)>  ");
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			
			switch(scanner.next()) {
				case "N":
				case "n":
					nextMenu = new MainMenu();
					isContinue = false;
					break;
				case "Y":
				case "y":
					registTaskProcess();
					isContinue = true;
					break;
				default:
					System.out.println();
					System.out.println("다시입력하세요");
					isContinue = true;
					break;
			}
		}
		
		nextMenu.displayMenu();
	}

	/**
	 * ZooKeeper에 Task를 등록한다.
	 */
	private void registTaskProcess() {
		
		try {
			ZooKeeper zk = new ZooKeeper("10.113.182.195:2181", 15000, this);
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			
			System.out.println();
			System.out.println("Task이름을 입력하세요 >  ");
			
			String taskName = scanner.next();
			
			zk.create("/tasks/" + taskName, TaskStatus.WAITING.name().getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			
			System.out.println();
			System.out.println("## 등록되었습니다!");
			
			zk.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			if(e.getMessage().contains("NodeExists")) {
				System.out.println("이미 해당 작업이 존재합니다!");
			} else {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
	}
}
