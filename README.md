# ICIA-semi-final
ICIA 세미프로젝트 

프로젝트 명 : SHIA<br>

프로젝트 기간 : 4일<br>

프로젝트 인원 : 4명<br>

사용 프로그램 : intellj, Mysql<br>

사용 언어 : Html, CSS, JavaScript, jQuery, Java<br>

환경 구성
IDE(통합개발환경) : IntelliJ Ultimate(유료 버전)<br>

프레임워크 : Spring Boot<br>

데이터베이스 : Mysql<br>

DB 접근 기술 : JPA<br>

View 템플릿 : Thymeleaf<br>

프로젝트 설명
---
최근 배웠던 jpa와 Thymeleaf 템플릿 엔진을 이용한 프로젝트입니다. 학원을 열심히 다니자는 마음으로 4일간 만든 소규모 프로젝트입니다.<br>

구현한 기능들 :<br>
- 로그인 관련(회원가입, 로그인, 로그아웃, 회원 정보 수정, 회원탈퇴)<br>
- 과정 리스트 출력, 과정 등록, 과정 상세 페이지<br>
- 신청자 목록 출력, 과정명 검색<br>
---

- Front_로그인, 회원가입<br>
```javascript
<div class="wrapper">
    <div class="container">
        <div class="sign-in-container">
            <form th:action="@{loginmem}" th:method="post"><br>
                <h1>로그인</h1><br>
                <input type="text" placeholder="아이디" name="mid" required>
                <input type="password" placeholder="비밀번호" name="mpwd" required><br>
                <button class="form_btn">로그인</button>
            </form>
        </div>
        <div class="overlay-right">
            <h1>회원가입</h1><br>
            <p class="pp"></p>
            <button id="signUp" class="overlay_btn" onclick="location.href='reg'">회원가입</button>
        </div>
    </div>
</div>
```
- Back_회원가입<br>
```java
    @PostMapping("regmem") // 회원가입 메소드
    public String regMem(Member member, RedirectAttributes rttr, String mid) {
        String view = iServ.regMem(member, rttr, mid);
        return view;
    }
```
```java
public String regMem(Member member, RedirectAttributes rttr, String mid) {
        String msg = null;
        String view = null;
        boolean check = mRepo.existsById(mid);

        String cpwd = encoder.encode(member.getMpwd());
        member.setMpwd(cpwd);

        try {
            if (check) {
                msg = "이미 존재하는 아이디입니다.";
                view = "redirect:reg";
            } else {
                mRepo.save(member);
                msg = "가입 성공";
                view = "redirect:/";
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg = "가입 실패";
            view = "redirect:reg";
        }

        rttr.addFlashAttribute("msg", msg);
        return view;
    }
```
- Back_로그인<br>
```java
    @PostMapping("loginmem") // 로그인 메소드
    public String loginmem(Member member, HttpSession session, RedirectAttributes rttr) {
        String view = iServ.loginmem(member, session, rttr);
        return view;
    }
```
```java
    public String loginmem(Member member, HttpSession session, RedirectAttributes rttr) {
        log.info("loginmem()");
        String msg = null;
        String view = null;

        Member mem = mRepo.findByMid(member.getMid());

        try {
            if (mem != null) {
                if (encoder.matches(member.getMpwd(), mem.getMpwd())) {
                    session.setAttribute("member", mem);

                    view = "redirect:main";
                } else {
                    msg = "비밀번호를 확인해주세요";
                    view = "redirect:/";
                }
            } else {
                msg = "존재하지 않는 아이디 입니다";
                view = "redirect:/";
            }
        } catch (Exception e) {
            msg = "뭔가안됨";
            view = "redirect:/";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }
```

※ Front_메인화면<br>
```javascript
<main id="main">
    <!-- 과정 리스트 섹션 -->
    <section id="about" class="about">
        <div class="container">
            <h2 class="hlist">학원 과정 목록</h2>
            <div class="clist-div">
                <div class="cwrtBtn-div">
                    <button class="cwrtBtn" onclick="location.href='Complete'">신청자 목록</button>
                    <th:block th:if="${session.member.getMgrade()} == '0'">
                        <button class="cwrtBtn" onclick="location.href='courseFrm'">과정 등록</button>
                    </th:block>
                    <th:block th:unless="${session.member.getMgrade()} == '0'">
                        <button class="cwrtBtn" onclick="location.href='courseFrm'" style="display: none">과정 등록</button>
                    </th:block>
                </div>
                <
                <!-- 리스트 테이블 -->
                <div id="wrap">
                    <table>
                        <tr colspan="2">
                            <th>과정 제목</th>
                            <th>과정 기간</th>
                        </tr>
                    </table>
                </div>
                <div id="scroll-table">
                    <table>
                        <th:block th:if="${#lists.isEmpty(cList)}">
                            <div class="non-content" style="margin-top: 80px; text-align: center">등록된 과정이 없습니다.</div>
                        </th:block>
                        <th:block th:unless="${#lists.isEmpty(cList)}">
                            <th:block th:each="cr:${cList}">
                                <tr>
                                    <td><a th:href="@{detail(cname=${cr.cname})}">[[${cr.cname}]]</a></td>
                                    <td th:text="${cr.cdate}"></td>
                                </tr>
                            </th:block>
                        </th:block>
                    </table>
                </div>
            </div>
        </div>
    </section>
</main> 
```


※ Front_로그인, 회원가입 <br>
- #### 메인 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215346300-ff1c5508-2b67-47e8-bbb8-82d1c10be049.png)

- #### 로그인, 회원가입 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352529-a16697bc-c2d1-41b6-843e-3c908a06c86b.png)

- #### 과정 등록 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352553-9f177e34-b069-4f58-8fee-eced4024e9a0.png)

- #### 신청자 리스트 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352622-a94312ff-e47c-4899-be5d-8b330c37666c.png)

- #### 회원 정보 수정 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352653-436d62ec-dd30-47dd-bd83-ae2e032c5d86.png)


