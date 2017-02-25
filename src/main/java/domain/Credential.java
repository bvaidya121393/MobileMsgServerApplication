package domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Entity;
import org.springframework.data.annotation.Id;


//@Table(name = "credentials")
//@DynamicUpdate
public class Credential {

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Column(name = "destinationId")
	private Integer destinationId;
	
//	@Column(name = "mobNo")
	private String mobileNumber;
	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "createdOnDt")
	private Date createdOnDt;
	
//	@Column(name = "isAvailable")
	private String isAvailable;

	public Integer getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Date getCreatedOnDt() {
		return createdOnDt;
	}

	public void setCreatedOnDt(Date createdOnDt) {
		this.createdOnDt = createdOnDt;
	}

	public String getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}

	
	
	
	
}
