package com.KoreaIT.AM.exam.dto;

public class Article extends Dto{
	public String title;
	public String body;
	public int hit;
	public int memberId;
	
	public Article(int id, String regDate, String updateDate, String title, String body, int memberId) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.title = title;
		this.body = body;
		this.memberId = memberId;
		this.hit = 0;
	}
	
	public Article(int id, String regDate, String updateDate, String title, String body, int memberId, int hit) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.title = title;
		this.body = body;
		this.memberId = memberId;
		this.hit =hit;
	}

	public void IncreaseHit() {
		this.hit++;
	}
}
