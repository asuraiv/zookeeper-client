/*
 * @(#)MainView.java $version 2016. 1. 4.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.asuraiv.coordination.menu;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.util.CollectionUtils;

/**
 * @author Jupyo Hong
 */
public class MainMenu implements Menu, Watcher {
	
	private Menu nextMenu;
	
	@Override
	public void displayMenu() {
		
		boolean isContinue = true;
		
		while(isContinue) {
			
			System.out.println();
			System.out.println();
			System.out.println("### Main MENU ###");
			System.out.println("1. 작업 등록");
			System.out.println("2. 작업 현황");
			System.out.println("3. 작업 초기화");
			System.out.println("99. 프로그램 종료");
			System.out.println();
			System.out.print("메뉴를 선택하세요 >  ");
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			
			switch(scanner.next()) {
				case "1":
					nextMenu = new RegistTaskMenu();
					isContinue = false;
					break;
				case "2":
					doTaskListUp();
					isContinue = true;
					break;
				case "3":
					doDeleteTasks();
					isContinue = true;
					break;
				case "99":
					// 프로그램 종료
					isContinue = false;
					break;
				default:
					System.out.println("[WRONG!] 다시입력하세요.");
					isContinue = true;
					break;
			}
		}
		
		nextMenu.displayMenu();
	}

	/**
	 * 작업들을 모두 초기화(삭제)한다.
	 */
	private void doDeleteTasks() {
		try {
			
			ZooKeeper zk = new ZooKeeper("10.113.182.195:2181", 15000, this);
			
			System.out.println();
			System.out.println("### 작업 초기화 수행 ###");
			
			List<String> tasks = zk.getChildren("/tasks", false, null);
			
			if(CollectionUtils.isEmpty(tasks)) {
				System.out.println("[WARNNING] 현재 등록된 작업이 없습니다.");
				return ;
			}
			
			for(String task : tasks) {
				zk.delete("/tasks/" + task, -1);
				System.out.println(task + "가 삭제되었습니다 ");
			}
			
			zk.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 현재 작업현황을 보여준다.
	 */
	private void doTaskListUp() {
		try {
			
			ZooKeeper zk = new ZooKeeper("10.113.182.195:2181", 15000, this);
			
			System.out.println();
			System.out.println("### 작업 현황 ###");
			
			List<String> tasks = zk.getChildren("/tasks", false, null);
			
			if(CollectionUtils.isEmpty(tasks)) {
				System.out.println("[WARNNING] 현재 등록된 작업이 없습니다.");
			}
			
			for(String task : tasks) {
				String status = new String(zk.getData("/tasks/" + task, false, null));
				System.out.println(task + ": " + status);
			}
			
			zk.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
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
