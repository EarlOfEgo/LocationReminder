/**
 * 
 */
package com.htwgkonstanz.locationreminder.database;

import java.io.Serializable;
import java.util.Date;

/**
 * @author stephan
 *
 */
public class LRTask implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int taskId;
	private String taskName;
	private String taskDescription;
	private double taskLongitude;
	private double taskLatitude;
	private double taskRange;
	private String taskCategory;
	private int taskUrgency;
	private int taskRemindType;
	private Date taskCreationDate;
	private Date taskExpireDate;
	private boolean taskExecuted;
	private int[][] remindTimeRanges;
	
	public LRTask() {
		setRemindTimeRanges(new int[7][2]);
	}
	
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public double getTaskLongitude() {
		return taskLongitude;
	}
	public void setTaskLongitude(double taskLongitude) {
		this.taskLongitude = taskLongitude;
	}
	public double getTaskLatitude() {
		return taskLatitude;
	}
	public void setTaskLatitude(double taskLatitude) {
		this.taskLatitude = taskLatitude;
	}
	public String getTaskCategory() {
		return taskCategory;
	}
	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}
	public int getTaskUrgency() {
		return taskUrgency;
	}
	public void setTaskUrgency(int taskUrgency) {
		this.taskUrgency = taskUrgency;
	}
	public int getTaskRemindType() {
		return taskRemindType;
	}
	public void setTaskRemindType(int taskRemindType) {
		this.taskRemindType = taskRemindType;
	}
	public Date getTaskCreationDate() {
		return taskCreationDate;
	}
	public void setTaskCreationDate(Date taskCreationDate) {
		this.taskCreationDate = taskCreationDate;
	}
	public Date getTaskExpireDate() {
		return taskExpireDate;
	}
	public void setTaskExpireDate(Date taskExpireDate) {
		this.taskExpireDate = taskExpireDate;
	}
	public boolean isTaskExecuted() {
		return taskExecuted;
	}
	public void setTaskExecuted(boolean taskExecuted) {
		this.taskExecuted = taskExecuted;
	}


	public int[][] getRemindTimeRanges() {
		return remindTimeRanges;
	}
	
	public int getRemindFromSpecific(int from) {
		return remindTimeRanges[from][0];
	}

	public int getRemindToSpecific(int to) {
		return remindTimeRanges[to][1];
	}

	public void setRemindTimeRanges(int[][] remindRanges) {
		this.remindTimeRanges = remindRanges;
	}


	public double getTaskRange() {
		return taskRange;
	}


	public void setTaskRange(double taskRange) {
		this.taskRange = taskRange;
	}


	public String getTaskDescription() {
		return taskDescription;
	}


	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
}
