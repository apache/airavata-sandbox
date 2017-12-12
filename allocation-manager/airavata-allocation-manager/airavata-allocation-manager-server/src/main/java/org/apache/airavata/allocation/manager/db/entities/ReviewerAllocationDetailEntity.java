package org.apache.airavata.allocation.manager.db.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the REVIEWER_ALLOCATION_DETAILS database table.
 * 
 */
@Entity
@Table(name="REVIEWER_ALLOCATION_DETAILS")
@NamedQuery(name="ReviewerAllocationDetailEntity.findAll", query="SELECT r FROM ReviewerAllocationDetailEntity r")
public class ReviewerAllocationDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="APPLICATIONS_TO_BE_USED")
	private Object applicationsToBeUsed;

	@Column(name="AWARD_ALLOCATION")
	private BigInteger awardAllocation;

	@Column(name="DISK_USAGE_RANGE_PER_JOB")
	private BigInteger diskUsageRangePerJob;

	@Column(name="DOCUMENTS")
	private Object documents;

	@Column(name="END_DATE")
	private BigInteger endDate;

	@Column(name="FIELD_OF_SCIENCE")
	private Object fieldOfScience;

	@Column(name="KEYWORDS")
	private Object keywords;

	@Column(name="MAX_MEMORY_PER_CPU")
	private BigInteger maxMemoryPerCpu;

	@Column(name="NUMBER_OF_CPU_PER_JOB")
	private BigInteger numberOfCpuPerJob;

	@Column(name="PROJECT_DESCRIPTION")
	private Object projectDescription;

	@Column(name="PROJECT_ID")
	private String projectId;

	@Column(name="PROJECT_REVIEWED_AND_FUNDED_BY")
	private Object projectReviewedAndFundedBy;

	@Column(name="REQUESTED_DATE")
	private BigInteger requestedDate;

	@Column(name="SERVICE_UNITS")
	private BigInteger serviceUnits;

	@Column(name="SPECIFIC_RESOURCE_SELECTION")
	private Object specificResourceSelection;

	@Column(name="START_DATE")
	private BigInteger startDate;

	@Column(name="STATUS")
	private String status;

	@Column(name="TITLE")
	private Object title;

	@Column(name="TYPE_OF_ALLOCATION")
	private String typeOfAllocation;

	@Column(name="TYPICAL_SU_PER_JOB")
	private BigInteger typicalSuPerJob;

	@Column(name="USERNAME")
	private String username;

	public ReviewerAllocationDetailEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object getApplicationsToBeUsed() {
		return this.applicationsToBeUsed;
	}

	public void setApplicationsToBeUsed(Object applicationsToBeUsed) {
		this.applicationsToBeUsed = applicationsToBeUsed;
	}

	public BigInteger getAwardAllocation() {
		return this.awardAllocation;
	}

	public void setAwardAllocation(BigInteger awardAllocation) {
		this.awardAllocation = awardAllocation;
	}

	public BigInteger getDiskUsageRangePerJob() {
		return this.diskUsageRangePerJob;
	}

	public void setDiskUsageRangePerJob(BigInteger diskUsageRangePerJob) {
		this.diskUsageRangePerJob = diskUsageRangePerJob;
	}

	public Object getDocuments() {
		return this.documents;
	}

	public void setDocuments(Object documents) {
		this.documents = documents;
	}

	public BigInteger getEndDate() {
		return this.endDate;
	}

	public void setEndDate(BigInteger endDate) {
		this.endDate = endDate;
	}

	public Object getFieldOfScience() {
		return this.fieldOfScience;
	}

	public void setFieldOfScience(Object fieldOfScience) {
		this.fieldOfScience = fieldOfScience;
	}

	public Object getKeywords() {
		return this.keywords;
	}

	public void setKeywords(Object keywords) {
		this.keywords = keywords;
	}

	public BigInteger getMaxMemoryPerCpu() {
		return this.maxMemoryPerCpu;
	}

	public void setMaxMemoryPerCpu(BigInteger maxMemoryPerCpu) {
		this.maxMemoryPerCpu = maxMemoryPerCpu;
	}

	public BigInteger getNumberOfCpuPerJob() {
		return this.numberOfCpuPerJob;
	}

	public void setNumberOfCpuPerJob(BigInteger numberOfCpuPerJob) {
		this.numberOfCpuPerJob = numberOfCpuPerJob;
	}

	public Object getProjectDescription() {
		return this.projectDescription;
	}

	public void setProjectDescription(Object projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Object getProjectReviewedAndFundedBy() {
		return this.projectReviewedAndFundedBy;
	}

	public void setProjectReviewedAndFundedBy(Object projectReviewedAndFundedBy) {
		this.projectReviewedAndFundedBy = projectReviewedAndFundedBy;
	}

	public BigInteger getRequestedDate() {
		return this.requestedDate;
	}

	public void setRequestedDate(BigInteger requestedDate) {
		this.requestedDate = requestedDate;
	}

	public BigInteger getServiceUnits() {
		return this.serviceUnits;
	}

	public void setServiceUnits(BigInteger serviceUnits) {
		this.serviceUnits = serviceUnits;
	}

	public Object getSpecificResourceSelection() {
		return this.specificResourceSelection;
	}

	public void setSpecificResourceSelection(Object specificResourceSelection) {
		this.specificResourceSelection = specificResourceSelection;
	}

	public BigInteger getStartDate() {
		return this.startDate;
	}

	public void setStartDate(BigInteger startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getTitle() {
		return this.title;
	}

	public void setTitle(Object title) {
		this.title = title;
	}

	public String getTypeOfAllocation() {
		return this.typeOfAllocation;
	}

	public void setTypeOfAllocation(String typeOfAllocation) {
		this.typeOfAllocation = typeOfAllocation;
	}

	public BigInteger getTypicalSuPerJob() {
		return this.typicalSuPerJob;
	}

	public void setTypicalSuPerJob(BigInteger typicalSuPerJob) {
		this.typicalSuPerJob = typicalSuPerJob;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}