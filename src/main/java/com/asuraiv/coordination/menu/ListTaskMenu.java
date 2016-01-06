/*
 * @(#)ListJobView.java $version 2016. 1. 4.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.asuraiv.coordination.menu;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.util.CollectionUtils;

/**
 * @author Jupyo Hong
 */
public class ListTaskMenu implements Menu, Watcher {

	@Override
	public void displayMenu() {
		
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
