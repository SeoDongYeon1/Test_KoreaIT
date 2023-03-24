package com.KoreaIT.AM.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.AM.exam.dto.Article;
import com.KoreaIT.AM.exam.dto.Member;
import com.KoreaIT.AM.exam.util.Util;

public class App {
	static List<Article> articles = new ArrayList<>();
	static List<Member> members = new ArrayList<>();
	static Member loginedMember;

	public void start() {
		Scanner sc = new Scanner(System.in);
		int lastArticleId = 3;
		int lastMemberId = 3;

		System.out.println("==프로그램 시작==");
		
		makeTestData();
		makeTestData1();
		while (true) {
			System.out.printf("명령어 > ");
			String cmd = sc.nextLine().trim();

			if (cmd.length() == 0) {
				System.out.println("명령어를 입력해주세요.");
				continue;
			}

			if (cmd.equals("exit")) {
				break;
			}

			if (cmd.equals("article write")) {
				if (islogined() == false) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}
				int id = lastArticleId + 1;
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				String regDate = Util.getNowDateStr();
				String updateDate = Util.getNowDateStr();
				int memberId = loginedMember.id;

				Article article = new Article(id, regDate, updateDate, title, body, memberId);
				articles.add(article);

				System.out.printf("%d번글이 생성되었습니다.\n", id);
				lastArticleId++;
			} else if (cmd.equals("article list")) {
				if (articles.size() == 0) {
					System.out.println("게시글이 존재하지 않습니다.");
					continue;
				}
				
				System.out.println("번호 //  제목     //  조회  // 작성자");
				for (int i = articles.size() - 1; i >= 0; i--) {
					Article article = articles.get(i);
					Member member = getMemberBymemberId(article.memberId);
					System.out.printf(" %d  //  %s   //  %d   // %s\n", article.id, article.title, article.hit,
							member.name);
				}
			} else if (cmd.startsWith("article detail")) {
				String[] cmdDiv = cmd.split(" ");

				if (cmdDiv.length < 3) {
					System.out.println("명령어를 확인해주세요.");
					continue;
				}

				int id = Integer.parseInt(cmdDiv[2]);

				Article foundArticle = getArticleById(id);

				if (foundArticle == null) {
					System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				} else {
					Member member = getMemberBymemberId(foundArticle.memberId);
					foundArticle.IncreaseHit();
					System.out.printf("번호 : %d \n", foundArticle.id);
					System.out.printf("작성날짜 : %s \n", foundArticle.regDate);
					System.out.printf("수정날짜 : %s \n", foundArticle.updateDate);
					System.out.printf("작성자 : %s \n", member.name);
					System.out.printf("제목 : %s \n", foundArticle.title);
					System.out.printf("내용 : %s \n", foundArticle.body);
					System.out.printf("조회 : %d \n", foundArticle.hit);
				}
			} else if (cmd.startsWith("article delete")) {
				if (islogined() == false) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}

				String[] cmdDiv = cmd.split(" ");

				if (cmdDiv.length < 3) {
					System.out.println("명령어를 확인해주세요.");
					continue;
				}

				int id = Integer.parseInt(cmdDiv[2]);

				Article foundArticle = getArticleById(id);

				if (foundArticle == null) {
					System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
					continue;
				}
				if (loginedMember.id != foundArticle.memberId) {
					System.out.println("권한이 없습니다.");
					continue;
				}

				System.out.printf("%d번 게시글이 삭제되었습니다. \n", foundArticle.id);
				articles.remove(foundArticle);

			} else if (cmd.startsWith("article modify")) {
				if (islogined() == false) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}

				String[] cmdDiv = cmd.split(" ");

				if (cmdDiv.length < 3) {
					System.out.println("명령어를 확인해주세요.");
					continue;
				}

				int id = Integer.parseInt(cmdDiv[2]);

				Article foundArticle = getArticleById(id);

				if (foundArticle == null) {
					System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
					continue;
				}

				if (loginedMember.id != foundArticle.memberId) {
					System.out.println("권한이 없습니다.");
					continue;
				}

				System.out.printf("새 제목 : ");
				String title = sc.nextLine();
				System.out.printf("새 내용 : ");
				String body = sc.nextLine();
				String updateDate = Util.getNowDateStr();

				foundArticle.title = title;
				foundArticle.body = body;
				foundArticle.updateDate = updateDate;

				System.out.printf("%d번 게시글이 수정되었습니다. \n", foundArticle.id);

			} else if (cmd.startsWith("member join")) {
				if (islogined()) {
					System.out.println("로그아웃 후 이용해주세요.");
					continue;
				}

				int id = lastMemberId + 1;
				String loginId = null;
				String loginPw = null;
				String loginPwConfirm = null;
				String name = null;
				while (true) {
					System.out.printf("로그인 아이디 : ");
					loginId = sc.nextLine();

					if (loginId.length() == 0) {
						System.out.println("필수 정보입니다.");
						continue;
					}

					if (isJoinableLoginId(loginId) == false) {
						System.out.println("이미 사용중인 아이디입니다.");
						continue;
					}
					System.out.println("사용가능한 아이디입니다.");
					break;
				}

				while (true) {
					System.out.printf("로그인 비밀번호 : ");
					loginPw = sc.nextLine();

					if (loginPw.length() == 0) {
						System.out.println("필수 정보입니다.");
						continue;
					}

					while (true) {
						System.out.printf("로그인 비밀번호 확인 : ");
						loginPwConfirm = sc.nextLine();

						if (loginPwConfirm.length() == 0) {
							System.out.println("필수 정보입니다.");
							continue;
						}
						break;
					}
					if (loginPw.equals(loginPwConfirm) == false) {
						System.out.println("비밀번호를 확인해주세요.");
						continue;
					}
					break;
				}
				while (true) {
					System.out.printf("이름 : ");
					name = sc.nextLine();

					if (name.length() == 0) {
						System.out.println("필수 정보입니다.");
						continue;
					}
					break;
				}
				String regDate = Util.getNowDateStr();
				String updateDate = Util.getNowDateStr();

				Member member = new Member(id, regDate, updateDate, loginId, loginPw, name);
				members.add(member);

				System.out.printf("%d번 회원이 가입되었습니다. \n", id);
				lastMemberId++;
			} else if (cmd.startsWith("member login")) {
				if (islogined()) {
					System.out.println("로그아웃 후 이용해주세요.");
					continue;
				}

				System.out.printf("로그인 아이디 : ");
				String loginId = sc.nextLine();

				Member member = getMemberByLoginId(loginId);

				if (member == null) {
					System.out.println("일치하는 회원이 없습니다.");
					continue;
				}

				System.out.printf("로그인 비밀번호 : ");
				String loginPw = sc.nextLine();
				if (member.loginPw.equals(loginPw) == false) {
					System.out.println("비밀번호가 일치하지 않습니다.");
					continue;
				}

				System.out.printf("로그인 성공! %s님 반갑습니다.\n", member.name);
				loginedMember = member;
			} else if (cmd.startsWith("member logout")) {
				if (islogined() == false) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}

				System.out.println("로그아웃 되었습니다.");
				loginedMember = null;
			} else {
				System.out.println("존재하지 않는 명령어입니다.");
			}
		}
		System.out.println("==프로그램 종료==");
	}

	private Member getMemberBymemberId(int memberId) {
		int i = 0;
		for(Member member : members) {
			if(member.id==memberId) {
				return members.get(i);
			}
			i++;
		}
		return null;
	}

	private void makeTestData1() {
		System.out.println("테스트를 위한 회원 데이터를 생성합니다.");
		members.add(new Member(1, Util.getNowDateStr(), Util.getNowDateStr(), "test1", "test1", "회원1"));
		members.add(new Member(2, Util.getNowDateStr(), Util.getNowDateStr(), "test2", "test2", "회원2"));
		members.add(new Member(3, Util.getNowDateStr(), Util.getNowDateStr(), "test3", "test3", "회원3"));
	}

	private void makeTestData() {
		System.out.println("테스트를 위한 게시글 데이터를 생성합니다.");
		articles.add(new Article(1, Util.getNowDateStr(), Util.getNowDateStr(), "제목1", "내용1", 1, 11));
		articles.add(new Article(2, Util.getNowDateStr(), Util.getNowDateStr(), "제목2", "내용2", 2, 22));
		articles.add(new Article(3, Util.getNowDateStr(), Util.getNowDateStr(), "제목3", "내용3", 3, 33));
	}

	private boolean islogined() {
		if (loginedMember != null) {
			return true;
		}
		return false;
	}

	private Member getMemberByLoginId(String loginId) {
		int i = 0;
		for (Member member : members) {
			if (member.loginId.equals(loginId)) {
				return members.get(i);
			}
			i++;
		}
		return null;
	}

	private boolean isJoinableLoginId(String loginId) {
		for (Member member : members) {
			if (member.loginId.equals(loginId)) {
				return false;
			}
		}
		return true;
	}

	private Article getArticleById(int id) {
		int i = 0;
		for (Article article : articles) {
			if (article.id == id) {
				return articles.get(i);
			}
			i++;
		}
		return null;
	}

}
