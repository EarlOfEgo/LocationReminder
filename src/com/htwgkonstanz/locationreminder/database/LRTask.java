/**
 * 
 */
package com.htwgkonstanz.locationreminder.database;

import java.util.Date;

import android.R.integer;

/**
 * @author stephan
 *
 */
public class LRTask {
	
	private int taskId;
	private String taskName;
	private double taskLongitude;
	private double taskLatitude;
	private double taskRange;
	private String taskCategory;
	private int taskUrgency;
	private int taskRemindType;
	private Date taskCreationDate;
	private Date taskExpireDate;
	private boolean taskExecuted;
	private String[][] remindRanges;
	
	public LRTask() {
		setRemindRanges(new String[7][2]);
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


	public String[][] getRemindRanges() {
		return remindRanges;
	}
	
	public String getRemindFromSpecific(int from) {
		return remindRanges[from][0];
	}

	public String getRemindToSpecific(int to) {
		return remindRanges[to][1];
	}

	public void setRemindRanges(String[][] remindRanges) {
		this.remindRanges = remindRanges;
	}


	public double getTaskRange() {
		return taskRange;
	}


	public void setTaskRange(double taskRange) {
		this.taskRange = taskRange;
	}
}
