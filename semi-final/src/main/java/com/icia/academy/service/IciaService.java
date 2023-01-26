package com.icia.academy.service;

import com.icia.academy.entity.Complete;
import com.icia.academy.entity.Course;
import com.icia.academy.entity.Member;
import com.icia.academy.repository.CompleteRepository;
import com.icia.academy.repository.CourseRepository;
import com.icia.academy.repository.MemberRepository;
import com.icia.academy.util.PagingUtil;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Log
public class IciaService {

    @Autowired
    private MemberRepository mRepo;

    @Autowired
    private CourseRepository cRepo;

    @Autowired
    private CompleteRepository cpRepo;

    private ModelAndView mv;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
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

    public String search(String mid) {
        log.info("중복검사");
        String num = null;


        Member member = null;
        try {
            member = mRepo.findById(mid).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (member == null) {
            num = "1";
        } else {
            num = "2";
        }
        return num;
    }

    // 과정 등록 (관리자용)
    @Transactional
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

    // 과정 리스트 출력
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

    // 과정 상세 보기 화면
//    @SneakyThrows
    public ModelAndView detailCourse(String cname) {
        log.info("detailCourse()");
        ModelAndView mv = new ModelAndView();

        Course course = cRepo.findByCname(cname);
//        String idx = URLEncoder.encode(cname, "UTF-8");
        mv.addObject("course", course);
        mv.setViewName("detail");
        return mv;
    }

    // 과정 신청하기
    @Transactional
    public String applyCourse(String cname, HttpSession session,
                              RedirectAttributes rttr) {
        log.info("applyCourse()");
        String msg = null;
        String view = null;

        try {
            Course cos = cRepo.findByCname(cname);
            Member mid = (Member) session.getAttribute("member");

            Complete com = new Complete();
            com.setCpcname(cos);
            com.setCpcmid(mid);

            cpRepo.save(com);

            msg = "신청 성공";
            view = "redirect:main";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "신청 실패";
            view = "redirect:detail?cname=" + cname;
        }

        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    // 페이징 및 pageNum 메소드 (역순출력)
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

    //페이징 처리 메소드
    private String getPaging(Integer pageNum, int totalPage) {
        String pageHtml = null;
        int pageCnt = 2;
        String listName = "?";

        PagingUtil paging = new PagingUtil(totalPage, pageNum, pageCnt, listName);

        pageHtml = paging.makePaging();

        return pageHtml;
    }


    @Transactional
    public String delmem(HttpSession session, RedirectAttributes rttr) {
        String msg = null;
        String view = null;


        Member mem = (Member) session.getAttribute("member");

        try {
            mRepo.delete(mem);
            session.invalidate();
            msg = "탈퇴 완료";
            view = "redirect:/";

        } catch (Exception e) {
            e.printStackTrace();
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }


    //입력한 아이디 정보조회 메소드
    public ModelAndView searchProc(String cpcname) {

        mv = new ModelAndView();
        Course cname = new Course();
        cname.setCname(cpcname);
//      입력된 강의명

        List<Complete> sList = cpRepo.findByCpcname(cname);

        mv.addObject("sList", sList);
        mv.setViewName("searchId");

        return mv;
    }

    @Transactional
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
} // class end
