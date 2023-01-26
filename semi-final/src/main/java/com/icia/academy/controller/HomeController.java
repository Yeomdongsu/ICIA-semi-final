package com.icia.academy.controller;

import com.icia.academy.entity.Course;
import com.icia.academy.entity.Member;
import com.icia.academy.service.IciaService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@Log
public class HomeController {
    @Autowired
    private IciaService iServ;


    ModelAndView mv = new ModelAndView();

    @GetMapping("/") // 첫 화면 (로그인)
    public String login() {
        return "login";
    }

    @GetMapping("reg") // 회원가입 화면
    public String regMember() {
        return "reg";
    }

    @GetMapping("main") // 메인 화면
    public ModelAndView main() {
        log.info("main()");
        mv = iServ.getCourseList();
        return mv;
    }

    @PostMapping("regmem") // 회원가입 메소드
    public String regMem(Member member, RedirectAttributes rttr, String mid) {
        String view = iServ.regMem(member, rttr, mid);
        return view;
    }

    @PostMapping("loginmem") // 로그인 메소드
    public String loginmem(Member member, HttpSession session, RedirectAttributes rttr) {
        String view = iServ.loginmem(member, session, rttr);
        return view;
    }

    @GetMapping("courseFrm")
    public String courseFrm() {
        log.info("courseFrm()");
        return "courseFrm";
    }

    @GetMapping("courseWrt") // 과정 등록 메소드
    public String courseWrt(Course course, HttpSession session,
                            RedirectAttributes rttr) {
        log.info("courseWrt()");
        String view = iServ.insertCourse(course, session, rttr);

        return view;
    }

    @GetMapping("detail") // 과정 상세보기 화면
    public ModelAndView detail(String cname, HttpSession session) {
        log.info("detail()");
        mv = iServ.detailCourse(cname);
        return mv;
    }

    @GetMapping("logoutProc") // 로그 아웃
    public String logoutProc(HttpSession session) {
        log.info("logoutProc()");
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("apply") // 과정 신청하기
    public String apply(String cname, HttpSession session,
                        RedirectAttributes rttr) {
        log.info("apply()");
        String ap = iServ.applyCourse(cname, session, rttr);
        return ap;
    }

    @GetMapping("header")
    public String header() {
        return "header";
    }

    @GetMapping("delmem")
    public String delmem(HttpSession session, RedirectAttributes rttr) {
        String del = iServ.delmem(session, rttr);
        return del;
    }

    // 과정이름 검색

    @GetMapping("Complete")
    public ModelAndView getList(Integer pageNum, HttpSession session) {
        log.info("getList()");
        mv = iServ.getCompleteList(pageNum, session);
        mv.setViewName("Complete");
        return mv;
    }

    //Complete 아이디 조회 맵소드
    @PostMapping("searchBar")
    public ModelAndView searchProc(String cpcname) {
        mv = iServ.searchProc(cpcname);
        return mv;
    }

    @GetMapping("updateMem")
    public String updateMem(){
        log.info("updateMem()");
        return "updateMem";
    }

    @PostMapping("updateMem2")
    public String updateMem2(Member member, HttpSession session, RedirectAttributes rttr){
        log.info("updateMem2()");
        String update = iServ.updateMember(member, session, rttr);
        return update;
    }


} // class end
