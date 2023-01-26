package com.icia.academy.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PagingUtil {
    private int totalPage; // 전체 페이지의 개수(지금은 게시글 개수)
    private int pageNum; // 현재 보이고 있는 페이지의 번호
    private int pageCnt; // 보여질 페이지 번호의 개수
    private String listName; // 게시판이 여러개일 경우
                            //게시판을 구분하는 url을 지정.

    //페이징용 html 코드를 만드는 메소드
    public String makePaging(){
        String pageHtml = null;
        StringBuffer sb = new StringBuffer();


        //1. 현재 페이지가 속한 그룹 구하기
        // 한 페이지에 보일 페이지 번호가 5개일 때
        // [이전] 6 7 8 9 10 [다음] - 6,7,8,9,10번이 한 그룹.
        int curGroup = (pageNum % pageCnt) > 0 ?
                pageNum / pageCnt + 1 :
                pageNum / pageCnt;

        //2. 현재 보이는 페이지 그룹의 시작 번호 구하기
        // 위의 예일 경우 6을 구함.(이전 페이지는 5)
        int start = (curGroup * pageCnt) - (pageCnt - 1);

        //3. 현재 보이는 페이지 그룹의 마지막 번호 구하기
        // 위의 예일 경우 10을 구함.(다음 페이지는 11)
        int end = (curGroup * pageCnt) >= totalPage ?
                totalPage : curGroup * pageCnt;

        //paging용 HTML 태그 작성
        //1. 이전 버튼에 대한 처리
        if (start != 1){//첫 페이지에서는 [이전] 버튼을 출력하지 않음.
            sb.append("<a class='pno' href='Complete" + listName +
                    "pageNum=" + (start - 1) + "'>");
            sb.append("&nbsp;이전&nbsp;</a>");
//            sb.append('[' + "이전" + ']' + "</a>");
        }//<a class='pno' href='/?pageNum=5'> 이전 </a>

        //2. 그룹 내 페이지 번호 처리
        for (int i = start; i <= end; i++){
            //보여질 페이지 번호 처리(링크가 없는 페이지 번호)
            if (pageNum == i){
                sb.append("<font class='pno' style='color: white;'>");
                sb.append("&nbsp;" + i + "&nbsp;</font>");
            }//<font class='pno' style='color=red;'> 3 </font>
            else {//링크가 붙는 페이지 번호
                sb.append("<a class='pno' href='Complete" + listName +
                        "pageNum=" + i + "'>");
                sb.append("&nbsp;" + i + "&nbsp;</a>");
            }//<a class='pno' href='/?pageNum=1'> 1 </a>
        }

        //[다음] 버튼 처리
        if (end != totalPage){
            sb.append("<a class='pno' href='Complete" + listName +
                    "pageNum=" + (end + 1) + "'>");
            sb.append("&nbsp;다음&nbsp;</a>");
        }//<a class='pno' href='/?pageNum=11'> 다음 </a>

        //StringBuffer에서 작성한 문장을 문자열로 변환
        pageHtml = sb.toString();

        return pageHtml;
    }


}
