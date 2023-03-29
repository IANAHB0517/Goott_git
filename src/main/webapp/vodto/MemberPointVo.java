package com.miniproj.vodto;

import java.sql.Timestamp;

public class MemberPointVo {
	private int no;
	private String who;
	private Timestamp when;
	private String why;
	private int howmuch;
	
	public MemberPointVo(int no, String who, Timestamp when, String why, int howmuch) {
		super();
		this.no = no;
		this.who = who;
		this.when = when;
		this.why = why;
		this.howmuch = howmuch;
	}

	public int getNo() {
		return no;
	}

	public String getWho() {
		return who;
	}

	public Timestamp getWhen() {
		return when;
	}

	public String getWhy() {
		return why;
	}

	public int getHowmuch() {
		return howmuch;
	}

	@Override
	public String toString() {
		return "MemberPointVo [no=" + no + ", who=" + who + ", when=" + when + ", why=" + why + ", howmuch=" + howmuch
				+ "]";
	}
	
	
	
}
