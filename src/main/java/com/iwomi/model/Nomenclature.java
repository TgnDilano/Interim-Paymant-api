package com.iwomi.model;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name ="sanm")
public class Nomenclature {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String tabcd;
	public String getTabcd() {
		return tabcd;
	}
	public void setTabcd(String tabcd) {
		this.tabcd = tabcd;
	}
	private String acscd;
	private int dele;
	private String lib1;
	private String lib2;
	private String lib3;
	private String lib4;
	private String lib5;
	private Long  taux1;
	private Long  taux2;
	private Long  taux3;
	private Long  taux4;
	private Long  taux5;
	private Long  mnt1;
	private Long  mnt2;
	private Long  mnt3;
	private Long  mnt4;
	private Long  mnt5;
	private Date dt1;
	private Date dt2;
	private Date dt3;
	private Date dt4;
	private Date dt5;
	private String crtd;
	private String mdfi;
	private String muser;
	private String cuser;
	private String cetab;
	
	public Boolean setByColumn(String column, String value) {
		switch (column) {
		case "lib1":
			this.lib1 = value;
			break;
			
		case "acscd":
			this.acscd = value;
			break;
		default:
			return false;
		}
		return true;
	
	}

	public Nomenclature() {
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setAcscd(String acscd) {
		this.acscd = acscd;
	}
	public void setDele(int dele) {
		this.dele = dele;
	}
	public void setLib1(String lib1) {
		this.lib1 = lib1;
	}
	public void setLib2(String lib2) {
		this.lib2 = lib2;
	}
	public void setLib3(String lib3) {
		this.lib3 = lib3;
	}
	public void setLib4(String lib4) {
		this.lib4 = lib4;
	}
	public void setLib5(String lib5) {
		this.lib5 = lib5;
	}
	public void setTaux1(Long taux1) {
		this.taux1 = taux1;
	}
	public void setTaux2(Long taux2) {
		this.taux2 = taux2;
	}
	public void setTaux3(Long taux3) {
		this.taux3 = taux3;
	}
	public void setTaux4(Long taux4) {
		this.taux4 = taux4;
	}
	public void setTaux5(Long taux5) {
		this.taux5 = taux5;
	}
	public void setMnt1(Long mnt1) {
		this.mnt1 = mnt1;
	}
	public void setMnt2(Long mnt2) {
		this.mnt2 = mnt2;
	}
	public void setMnt3(Long mnt3) {
		this.mnt3 = mnt3;
	}
	public void setMnt4(Long mnt4) {
		this.mnt4 = mnt4;
	}
	public void setMnt5(Long mnt5) {
		this.mnt5 = mnt5;
	}
	public void setDt1(Date dt1) {
		this.dt1 = dt1;
	}
	public void setDt2(Date dt2) {
		this.dt2 = dt2;
	}
	public void setDt3(Date dt3) {
		this.dt3 = dt3;
	}
	public void setDt4(Date dt4) {
		this.dt4 = dt4;
	}
	public void setDt5(Date dt5) {
		this.dt5 = dt5;
	}
	public void setCrtd(String crtd) {
		this.crtd = crtd;
	}
	public void setMdfi(String mdfi) {
		this.mdfi = mdfi;
	}
	public void setMuser(String muser) {
		this.muser = muser;
	}
	public void setCuser(String cuser) {
		this.cuser = cuser;
	}
	public void setCetab(String cetab) {
		this.cetab = cetab;
	}
	public Long getId() {
		return id;
	}
	
	public String getAcscd() {
		return acscd;
	}
	public int getDele() {
		return dele;
	}
	public String getLib1() {
		return lib1;
	}
	public String getLib2() {
		return lib2;
	}
	public String getLib3() {
		return lib3;
	}
	public String getLib4() {
		return lib4;
	}
	public String getLib5() {
		return lib5;
	}
	public Long getTaux1() {
		return taux1;
	}
	public Long getTaux2() {
		return taux2;
	}
	public Long getTaux3() {
		return taux3;
	}
	public Long getTaux4() {
		return taux4;
	}
	public Long getTaux5() {
		return taux5;
	}
	public Long getMnt1() {
		return mnt1;
	}
	public Long getMnt2() {
		return mnt2;
	}
	public Long getMnt3() {
		return mnt3;
	}
	public Long getMnt4() {
		return mnt4;
	}
	public Long getMnt5() {
		return mnt5;
	}
	public Date getDt1() {
		return dt1;
	}
	public Date getDt2() {
		return dt2;
	}
	public Date getDt3() {
		return dt3;
	}
	public Date getDt4() {
		return dt4;
	}
	public Date getDt5() {
		return dt5;
	}
	public String getCrtd() {
		return crtd;
	}
	public String getMdfi() {
		return mdfi;
	}
	public String getMuser() {
		return muser;
	}
	public String getCuser() {
		return cuser;
	}
	public String getCetab() {
		return cetab;
	}
	
}
