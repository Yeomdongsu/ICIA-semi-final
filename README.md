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
- 신청자 목록 출력, 페이징처리, 과정명 검색<br>
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
- #### 로그인, 회원가입 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352529-a16697bc-c2d1-41b6-843e-3c908a06c86b.png)

- Front_메인화면<br>
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
- Back_메인화면<br>
```java
    @GetMapping("main") // 메인 화면
    public ModelAndView main() {
        log.info("main()");
        mv = iServ.getCourseList();
        return mv;
    }
```
```java
     public ModelAndView getCourseList() {
        log.info("getCourseList()");
        ModelAndView mv = new ModelAndView();

        List<Course> cList = new ArrayList<>();
        Iterable<Course> cIter = cRepo.findAll();

        for (Course c : cIter) {
            cList.add(c);
        }

        mv.addObject("cList", cList);
        mv.setViewName("main");

        return mv;
    }
```
- #### 메인 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215346300-ff1c5508-2b67-47e8-bbb8-82d1c10be049.png)

- Front_과정 등록<br>
```javascript
   <form name="form" id="form" role="form" th:action="@{courseWrt}" enctype="multipart/form-data">
            <div class="mb-3">
                <label>과정 제목</label>
                <input type="text" class="form-control" name="cname" id="title" placeholder="과정 제목을 입력해주세요" required>
            </div>
            <div class="row mb-3">
                <div class="teacher">
                    <label>강사 이름</label>
                    <input type="text" class="form-control" name="ctname" placeholder="강사 이름을 입력해주세요" required>
                </div>
                <div class="price">
                    <label>과정 가격</label>
                    <input type="text" class="form-control" name="cprice" placeholder="과정 가격을 입력해주세요" required>
                </div>
            </div>
            <div class="mb-3">
                <label>과정 기간</label>
                <input type="text" class="form-control" name="cdate" placeholder="과정 기간을 입력해주세요 ex) 22.01.01 ~ 22.12.31"
                       required>
            </div>
            <div class="mb-3">
                <label for="content">내용</label>
                <textarea class="form-control" rows="5" name="cintro" id="content" placeholder="내용을 입력해주세요"
                          required></textarea>
            </div>
            <div class="btn-area">
                <button class="btn" type="submit">등록</button>
            </div>
        </form>
```
- Back_과정 등록<br>
```java
    @GetMapping("courseWrt") // 과정 등록 메소드
    public String courseWrt(Course course, HttpSession session,
                            RedirectAttributes rttr) {
        log.info("courseWrt()");
        String view = iServ.insertCourse(course, session, rttr);

        return view;
    }
```
```java
   public String insertCourse(Course course, HttpSession session,
                               RedirectAttributes rttr) {
        log.info("insertCourse()");
        String msg = null;
        String view = null;

        try {
            cRepo.save(course);

            session.setAttribute("course", course);
            msg = "과정 등록을 성공하였습니다.";
            view = "redirect:main";

        } catch (Exception e) {
            e.printStackTrace();
            msg = "과정 등록을 실패하였습니다.";
            view = "redirect:courseFrm";
        }

        rttr.addFlashAttribute("msg", msg);
        return view;
    }
```

- #### 과정 등록 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352553-9f177e34-b069-4f58-8fee-eced4024e9a0.png)

- Front_신청자 리스트 출력, 페이징 처리 <br>
```javascript
   <div class="com-table">
            <!--      table 윗줄-->
            <div class="p-10 table-top">번호</div>
            <div class="p-30 table-top">강의명</div>
            <div class="p-15 table-top">아이디</div>
            <div class="t-date p-45 table-top">신청일</div>
        </div>
        <!--        중간-->
        <div class="middle-table">
            <th:block th:each="bitem:${cList}">
                <div style="margin: 0 auto">
                    <div class="middle-row">
                        <!--            DB에서 넘어올 자료 데이터 리스트-->
                        <th:block th:if="${#lists.isEmpty(cList)}">
                            <div class="data-non">신청자가 없습니다.</div>
                        </th:block>
                        <th:block th:unless="${#lists.isEmpty(cList)}">
                            <div class="cell-num" th:text="${bitem.cpnum}"></div>
                            <div class="cell-name" th:text="${bitem.cpcname.cname}"></div>
                            <div class="cell-id" th:text="${bitem.cpcmid.mid}"></div>
                            <div class="cell-date" th:text="${#dates.format(bitem.cpdate,'yyyy-MM-dd')}">DB 날짜</div>
                        </th:block>
                    </div>
                </div>
            </th:block>
        </div>
        <!--        페이징 처리-->
        <div class="btn-area">
            <div class="paging" th:utext="${paging}"></div>
        </div>
```

- Back_신청자 리스트 출력, 페이징 처리<br>
```java
   @GetMapping("Complete")
    public ModelAndView getList(Integer pageNum, HttpSession session) {
        log.info("getList()");
        mv = iServ.getCompleteList(pageNum, session);
        mv.setViewName("Complete");
        return mv;
    }
```
```java
   public ModelAndView getCompleteList(Integer pageNum, HttpSession session) {
        log.info("getCompleteList()");
        ModelAndView mv = new ModelAndView();
        mv = new ModelAndView();

        if (pageNum == null) {
            pageNum = 1;
        }
        int listCnt = 5;//페이지 당 신청목록 개수
        //페이지 조건
        Pageable pb = PageRequest.of((pageNum - 1), listCnt,
                Sort.Direction.DESC, "cpnum");

        Page<Complete> result = cpRepo.findByCpnumGreaterThan(0L, pb);

        List<Complete> cList = result.getContent();

        int totalPage = result.getTotalPages();//전체 페이지 개수

        String paging = getPaging(pageNum, totalPage);

        mv.addObject("cList", cList);
        mv.addObject("paging", paging);

        //현재 보이는 페이지의 번호를 저장.
        session.setAttribute("pageNum", pageNum);

        return mv;
    }
```
- #### 신청자 리스트 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352622-a94312ff-e47c-4899-be5d-8b330c37666c.png)

- Front_회원 정보 수정<br>
```javascript
   <h1>정보 수정</h1><br>
        <form th:action="@{updateMem2}" method="post">
            <input type="text" placeholder="아이디" name="mid" id="idin"
                   th:value="${session.member.getMid()}" readonly>
            <input type="password" placeholder="비밀번호" name="mpwd" maxlength="20" required>
            <input type="text" name="mname" placeholder="이름" maxlength="30"
                   th:value="${session.member.getMname()}" required>
            <input type="text" name="mage" placeholder="나이" pattern="\d*"
                   th:value="${session.member.getMage()}" maxlength="3" title="숫자를 입력해주세요">
            <input type="text" name="maddress" placeholder="주소"
                    th:value="${session.member.getMaddress()}">
            <input type="tel" name="mphone" placeholder="000-0000-0000"
                   th:value="${session.member.getMphone()}" pattern="[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}"
                   title="전화번호를 입력해주세요">
            <input type="hidden" name="mgrade" th:value="${session.member.getMgrade()}">
            <button id="signUp" class="overlay_btn">정보수정</button>
        </form>
```
- Back_회원 정보 수정<br>
```java
    @PostMapping("updateMem2")
    public String updateMem2(Member member, HttpSession session, RedirectAttributes rttr){
        log.info("updateMem2()");
        String update = iServ.updateMember(member, session, rttr);
        return update;
    }
```
```java
   public String updateMember(Member member, HttpSession session, RedirectAttributes rttr) {
        log.info("updateMember()");
        String msg = null;
        String view = null;

        try {
            String cpwd = encoder.encode(member.getMpwd());
            log.info(cpwd);
            member.setMpwd(cpwd);

            mRepo.save(member);

            session.invalidate();

            msg = "수정 성공";
            view = "redirect:/";
        } catch (Exception e){
            msg = "수정 실패";
            view = "redirect:updateMem";
        }
        rttr.addFlashAttribute("me", msg);
        return view;
    }
```

- #### 회원 정보 수정 화면<br><br>
![image](https://user-images.githubusercontent.com/117874997/215352653-436d62ec-dd30-47dd-bd83-ae2e032c5d86.png)

마치며
---
#### 소감<br>
짧은 기간의 프로젝트긴 하지만, 아쉬움이 많이 남던 프로젝트입니다. jstl 문법도 정리가 되지 않은 상태에서 thymeleaf 문법을 배워 부족한 상태에서 했었습니다. 더 많은 경험과 노력으로 더 나아지는 모습 보여드리겠습니다!
