package com.iwomi.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sause")
public class User implements Serializable {
	 @Id
	 @GeneratedValue(strategy= GenerationType.AUTO)
	 private Long id;

	 private Date crtd;

	 private Date mdfi;
	 private String sttus;
	 private Long acabs;
	 private String ttle;
	 private String lname;
	 private String fname;
	 private String matcl;
	 private String passw;
	 private String prfpc;
	 
	 private String gnder;
	 private Date birdy;
	 private String email;
	 private String uname;
	 private String phone;
	 private String regnm;
	 private String addrs;
	 private String ctry;
	 private Long subst;
	 private Date lstlg;
	 private Long cuser;
	 private Long muser;
	 
	 private String srvid;
	 private String inofis;
	 private String prfle;
	 private Long dycsm;
	 private String stycd;
	 private String hixdt;
	 private Long tvday;
	 private Long vardy;
	 private String csaut;
	 
	 private String canv;
	 private Long baln;
	 private String lcpdt;
	 private String brch;
	 private String cetab;
	 private String catg;
	 private Date aldx;
	 private Long adxtus;
	 private String fuctn;
         private String nfac;//numero de facturier

    public String getNfac() {
        return nfac;
    }

    public void setNfac(String nfac) {
        this.nfac = nfac;
    }
	 
	public User() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Date getCrtd() {
        return crtd;
    }

    public void setCrtd(Date crtd) {
        this.crtd = crtd;
    }

    public Date getMdfi() {
        return mdfi;
    }

    public void setMdfi(Date mdfi) {
        this.mdfi = mdfi;
    }

	
	public String getSttus() {
		return sttus;
	}

	public void setSttus(String sttus) {
		this.sttus = sttus;
	}

	public Long getAcabs() {
		return acabs;
	}

	public void setAcabs(Long acabs) {
		this.acabs = acabs;
	}

	public String getTtle() {
		return ttle;
	}

	public void setTtle(String ttle) {
		this.ttle = ttle;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMatcl() {
		return matcl;
	}

	public void setMatcl(String matcl) {
		this.matcl = matcl;
	}

	public String getPassw() {
		return passw;
	}

	public void setPassw(String passw) {
		this.passw = passw;
	}

	public String getPrfpc() {
		return prfpc;
	}

	public void setPrfpc(String prfpc) {
		this.prfpc = prfpc;
	}

	public String getGnder() {
		return gnder;
	}

	public void setGnder(String gnder) {
		this.gnder = gnder;
	}
	
	
	public Date getBirdy() {
		return birdy;
	}

	public void setBirdy(Date birdy) {
		this.birdy = birdy;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegnm() {
		return regnm;
	}

	public void setRegnm(String regnm) {
		this.regnm = regnm;
	}

	public String getAddrs() {
		return addrs;
	}

	public void setAddrs(String addrs) {
		this.addrs = addrs;
	}

	public String getCtry() {
		return ctry;
	}

	public void setCtry(String ctry) {
		this.ctry = ctry;
	}

	public Long getSubst() {
		return subst;
	}

	public void setSubst(Long subst) {
		this.subst = subst;
	}

	public Date getLstlg() {
		return lstlg;
	}

	public void setLstlg(Date lstlg) {
		this.lstlg = lstlg;
	}

	public Long getCuser() {
		return cuser;
	}

	public void setCuser(Long cuser) {
		this.cuser = cuser;
	}

	public Long getMuser() {
		return muser;
	}

	public void setMuser(Long muser) {
		this.muser = muser;
	}

	public String getSrvid() {
		return srvid;
	}

	public void setSrvid(String srvid) {
		this.srvid = srvid;
	}

	public String getInofis() {
		return inofis;
	}

	public void setInofis(String inofis) {
		this.inofis = inofis;
	}

	public String getPrfle() {
		return prfle;
	}

	public void setPrfle(String prfle) {
		this.prfle = prfle;
	}

	public Long getDycsm() {
		return dycsm;
	}

	public void setDycsm(Long dycsm) {
		this.dycsm = dycsm;
	}

	public String getStycd() {
		return stycd;
	}

	public void setStycd(String stycd) {
		this.stycd = stycd;
	}

	public String getHixdt() {
		return hixdt;
	}

	public void setHixdt(String hixdt) {
		this.hixdt = hixdt;
	}

	public Long getTvday() {
		return tvday;
	}

	public void setTvday(Long tvday) {
		this.tvday = tvday;
	}

	public Long getVardy() {
		return vardy;
	}

	public void setVardy(Long vardy) {
		this.vardy = vardy;
	}

	public String getCsaut() {
		return csaut;
	}

	public void setCsaut(String csaut) {
		this.csaut = csaut;
	}

	public String getCanv() {
		return canv;
	}

	public void setCanv(String canv) {
		this.canv = canv;
	}

	public Long getBaln() {
		return baln;
	}

	public void setBaln(Long baln) {
		this.baln = baln;
	}

	public String getLcpdt() {
		return lcpdt;
	}

	public void setLcpdt(String lcpdt) {
		this.lcpdt = lcpdt;
	}

	public String getBrch() {
		return brch;
	}

	public void setBrch(String brch) {
		this.brch = brch;
	}

	public String getCetab() {
		return cetab;
	}

	public void setCetab(String cetab) {
		this.cetab = cetab;
	}

	public String getCatg() {
		return catg;
	}

	public void setCatg(String catg) {
		this.catg = catg;
	}

	public Date getAldx() {
		return aldx;
	}

	public void setAldx(Date aldx) {
		this.aldx = aldx;
	}

	public Long getAdxtus() {
		return adxtus;
	}

	public void setAdxtus(Long adxtus) {
		this.adxtus = adxtus;
	}

	public String getFuctn() {
		return fuctn;
	}

	public void setFuctn(String fuctn) {
		this.fuctn = fuctn;
	}

	public User(Long id, Date crtd, Date mdfi, String sttus, Long acabs, String ttle, String lname, String fname, String matcl, String passw, String prfpc, 
			String gnder, Date birdy, String email, String uname, String phone, String regnm, String addrs, String ctry, Long subst, Date lstlg, Long cuser,
			 Long muser, String srvid, String inofis, String prfle, Long dycsm, String stycd, String hixdt, Long tvday, Long vardy, String csaut, String canv,
			 Long baln, String lcpdt, String brch, String cetab, String catg, Date aldx, Long adxtus, String fuctn,String nfac) {
					super();
					this.id = id;
					this.crtd = crtd;
					this.mdfi = mdfi;
					this.sttus = sttus;
					this.acabs = acabs;
					this.ttle = ttle;
					this.lname = lname;
					this.fname = fname;
					this.matcl = matcl;
					this.passw = passw;
					this.prfpc = prfpc;
					
					this.gnder = gnder;
					this.birdy = birdy ;
					this.email = email ;
					this.uname = uname ;
					this.phone = phone ;
					this.regnm = regnm ;
					this.addrs = addrs;
					this.ctry = ctry;
					this.subst = subst;
					this.lstlg = lstlg;
					this.muser = muser;
					this.cuser = cuser;
					this.srvid = srvid;
					this.inofis = inofis;
					this.prfle = prfle;
					this.dycsm = dycsm;
					this.stycd = stycd;
					this.hixdt = hixdt;
					this.tvday = tvday;
					this.vardy = vardy;
					this.csaut = csaut;
					this.canv = canv;
					this.baln = baln;
					this.lcpdt = lcpdt;
					this.brch = brch;
					this.cetab = cetab;
					this.catg = catg;
					this.aldx = aldx;
					this.adxtus = adxtus;
					this.fuctn = fuctn;
                                        this.nfac = nfac;

				}

	public boolean setByUser1(User d) {
		if (d.lname != null) {
			this.lname = d.lname;
		}
		if (d.fname != null) {
			this.fname = d.fname;
		}
		if (d.addrs != null) {
			this.addrs = d.addrs;
		}
		if (d.birdy != null) {
			this.birdy = d.birdy;
		}
		if (d.brch != null) {
			this.brch = d.brch;
		}
		if (d.catg != null) {
			this.catg = d.catg;
		}
		if (d.csaut != null) {
			this.csaut = d.csaut;
		}
		if (d.ctry != null) {
			this.ctry = d.ctry;
		}
		if (d.cuser != null) {
			this.cuser = d.cuser;
		}
		if (d.email != null) {
			this.email = d.email;
		}
		if (d.fuctn != null) {
			this.fuctn = d.fuctn;
		}
		if (d.gnder != null) {
			this.gnder = d.gnder;
		}
		if (d.hixdt != null) {
			this.hixdt = d.hixdt;
		}
		if (d.matcl != null) {
			this.matcl = d.matcl;
		}
		if (d.muser != null) {
			this.muser = d.muser;
		}
		if (d.passw != null) {
			this.passw = d.passw;
		}
		if (d.phone != null) {
			this.phone = d.phone;
		}
		if (d.prfpc != null) {
			this.prfpc = d.prfpc;
		}
		if (d.regnm != null) {
			this.regnm = d.regnm;
		}
		if (d.srvid != null) {
			this.srvid = d.srvid;
		}
		if (d.subst != null) {
			this.subst = d.subst;
		}
		if (d.ttle != null) {
			this.ttle = d.ttle;
		}
		if (d.uname != null) {
			this.uname = d.uname;
		}
		return true;
	}

    public void setAcabs(int v1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	 
}
