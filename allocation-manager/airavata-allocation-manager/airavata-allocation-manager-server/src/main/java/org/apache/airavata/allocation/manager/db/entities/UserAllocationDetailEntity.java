package org.apache.airavata.allocation.manager.db.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the USER_ALLOCATION_DETAILS database table.
 * 
 */
@Entity
@Table(name="USER_ALLOCATION_DETAILS")
@NamedQuery(name="UserAllocationDetailEntity.findAll", query="SELECT u FROM UserAllocationDetailEntity u")
public class UserAllocationDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserAllocationDetailEntityPK id;

	@Lob
	@Column(name="APPLICATIONS_TO_BE_USED")
	private String applicationsToBeUsed;

	@Column(name="DISK_USAGE_RANGE_PER_JOB")
	private BigInteger diskUsageRangePerJob;

	@Lob
	@Column(name="DOCUMENTS")
	private byte[] documents;

	@Lob
	@Column(name="FIELD_OF_SCIENCE")
	private String fieldOfScience;

	@Lob
	@Column(name="KEYWORDS")
	private String keywords;

	@Column(name="MAX_MEMORY_PER_CPU")
	private BigInteger maxMemoryPerCpu;

	@Column(name="NUMBER_OF_CPU_PER_JOB")
	private BigInteger numberOfCpuPerJob;

	@Lob
	@Column(name="PROJECT_DESCRIPTION")
	private String projectDescription;

	@Lob
	@Column(name="PROJECT_REVIEWED_AND_FUNDED_BY")
	private String projectReviewedAndFundedBy;

	@Column(name="REQ_DATE")
	private BigInteger reqDate;

	@Column(name="SERVICE_UNITS")
	private BigInteger serviceUnits;

	@Lob
	@Column(name="SPECIFIC_RESOURCE_SELECTION")
	private String specificResourceSelection;

	@Lob
	@Column(name="TITLE")
	private String title;

	@Column(name="TYPE_OF_ALLOCATION")
	private String typeOfAllocation;

	@Column(name="TYPICAL_SU_PER_JOB")
	private BigInteger typicalSuPerJob;

	public UserAllocationDetailEntity() {
	}

	public UserAllocationDetailEntityPK getId() {
		return this.id;
	}

	public void setId(UserAllocationDetailEntityPK id) {
		this.id = id;
	}

	public String getApplicationsToBeUsed() {
		return this.applicationsToBeUsed;
	}

	public void setApplicationsToBeUsed(String applicationsToBeUsed) {
		this.applicationsToBeUsed = applicationsToBeUsed;
	}

	public BigInteger getDiskUsageRangePerJob() {
		return this.diskUsageRangePerJob;
	}

	public void setDiskUsageRangePerJob(BigInteger diskUsageRangePerJob) {
		this.diskUsageRangePerJob = diskUsageRangePerJob;
	}

	public byte[] getDocuments() {
		return this.documents;
	}

	public void setDocuments(byte[] documents) {
		this.documents = documents;
	}

	public String getFieldOfScience() {
		return this.fieldOfScience;
	}

	public void setFieldOfScience(String fieldOfScience) {
		this.fieldOfScience = fieldOfScience;
	}

	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
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

	public String getProjectDescription() {
		return this.projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getProjectReviewedAndFundedBy() {
		return this.projectReviewedAndFundedBy;
	}

	public void setProjectReviewedAndFundedBy(String projectReviewedAndFundedBy) {
		this.projectReviewedAndFundedBy = projectReviewedAndFundedBy;
	}

	public BigInteger getReqDate() {
		return this.reqDate;
	}

	public void setReqDate(BigInteger reqDate) {
		this.reqDate = reqDate;
	}

	public BigInteger getServiceUnits() {
		return this.serviceUnits;
	}

	public void setServiceUnits(BigInteger serviceUnits) {
		this.serviceUnits = serviceUnits;
	}

	public String getSpecificResourceSelection() {
		return this.specificResourceSelection;
	}

	public void setSpecificResourceSelection(String specificResourceSelection) {
		this.specificResourceSelection = specificResourceSelection;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
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

}