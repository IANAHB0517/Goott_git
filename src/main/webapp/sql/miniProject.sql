use jsh;
-- member 테이블 생성
CREATE TABLE `member` (
  `userId` varchar(8) NOT NULL,
  `userPwd` varchar(200) NOT NULL,
  `userEmail` varchar(50) NOT NULL,
  `userMobile` varchar(13) DEFAULT NULL,
  `userGender` varchar(1) NOT NULL,
  `hobbies` varchar(100) DEFAULT NULL,
  `job` varchar(20) DEFAULT NULL,
  `userImg` varchar(100) DEFAULT 'uploadMember/noimage.png',
  `memo` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 패스워드 암호화
select md5('1234');  -- md5 알고리즘으로 암호화
select sha1('1234'); -- sha1 알고리즘으로 암호화
select sha1(md5('1234')); 

-- 모든 회원 조회
select * from pointpolicymember;

-- 회원 가입
-- 회원가입시 이미지가 없을때는 default 값이 들어가야 하므로 sql문
insert into member(userId, userPwd, userEmail, userMobile, userGender, hobbies, job, memo)
 values('abc', sha1(md5('1234')) , 'abc@abc.com', '010-1111-2222', 'M', '', '학생', '');

insert into member(userId, userPwd, userEmail, userMobile, userGender, hobbies, job, memo)
 values(?, sha1(md5(?)), ?, ?, ?, ?, ?, ?);

-- 회원가입시 이미지가 있을때 sql문
insert into member
 values(?, sha1(md5(?)) , ?, ?, ?, ?, ?, ?, ?);

-- 중복된 유저 아이디 검사 sql문
select count(*) as userCnt from member where userId = 'abc';


-- 로그인 처리
select * from member where userId = ? and userPwd = sha1(md5(?));
select * from member where userId = 'abcd' and userPwd = sha1(md5('1234'));

-- 회원 포인트 적립을 위한 테이블 생성
CREATE TABLE `memberpoint` (
  `who` varchar(8) NOT NULL,
  `when` datetime DEFAULT CURRENT_TIMESTAMP,
  `why` varchar(50) DEFAULT NULL,
  `howmuch` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pointpolicy` (
  `why` varchar(50) NOT NULL,
  `howmuch` int(11) DEFAULT NULL,
  PRIMARY KEY (`why`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 테이블 참조 관계 만들기
ALTER TABLE `jsh`.`memberpoint` 
ADD CONSTRAINT `who_fk`
  FOREIGN KEY (`who`)
  REFERENCES `jsh`.`member` (`userId`)
  ON DELETE CASCADE;
  

alter table memberpoint
add constraint memberpoint_why_fk foreign key(why) references pointpolicy(why);

-- 멤버 포인트 조회
select * from memberpoint;

-- 유저에게 포인트를 부여하는 sql
insert into memberpoint(who, why, howmuch)
values(?, '회원가입', (select howmuch from pointpolicy where why='회원가입'));

-- 회원의 로그인 기록을 저장하는 테이블 생성
CREATE TABLE `jsh`.`latestloginlog` (
  `who` VARCHAR(8) NOT NULL,
  `latestLoginDate` DATETIME NULL DEFAULT now(),
  PRIMARY KEY (`who`));
  
  -- member, latestloginlog 관계지정
  alter table latestloginlog
  add constraint member_userId_fk foreign key(who) references member(userId);
  
  -- 최근 로그인 한 것이 날짜가 바뀌었나?
  -- 회원 가입후 처음 로그인 하면 null
select datediff(now(), (select latestlogindate from latestloginlog where who='dooly')) as diff;
-- 처음 로그인 x, 로그인 하고 날짜가 바뀌었으면 > 0
select datediff(now(), (select latestlogindate from latestloginlog where who='dooly')) as diff;  
-- 처음 로그인 x, 로그인 한 날짜가 바뀌지 않았으면 0
  
-- 처음 로그인 한 사람 로그인 기록 남기는 sql문
insert into latestloginlog(who) values(?);

-- 기존 로그인 기록이 있는 사람 update 하는 sql문
update latestloginlog set latestLoginDate = now() where who = ?;

-- 과거 로그인 했던 기록이 언제였는지(기록이 없다면 -1)
select ifnull(a.diff, -1) as datediff from 
(select datediff(now(), (select latestlogindate from latestloginlog where who='dooly')) as diff) a;

-- 유저 아이디로 회원정보 가져오기 
select * from member where userId = ?;

-- 유저 아이디로 포인트 내역 가져오기
select * from memberpoint where who = 'dooly' order by no desc;


------------------------------------------------------------------------------------------------
-- 답글형 (계층형) 게시판 구현

-- 게시판 테이블 생성
CREATE TABLE `jsh`.`board` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `writer` VARCHAR(8) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `postDate` DATETIME NULL DEFAULT now(),
  `content` VARCHAR(500) NOT NULL,
  `imgFile` VARCHAR(50) NULL,
  `readcount` INT NOT NULL DEFAULT 0,
  `likecount` INT NOT NULL DEFAULT 0,
  `ref` INT NOT NULL DEFAULT 0,   -- 부모글을 참조
  `step` INT NOT NULL DEFAULT 0,  -- 답글의 깊이
  `reforder` INT NOT NULL DEFAULT 0,  -- 부모글과 답글을 보여주는 순서
  PRIMARY KEY (`no`));

use jsh;

-- member , board 테이블 관계 설정
alter table board
add constraint board_writer_fk 
foreign key(writer)
references member(userId)
on delete cascade;

-- 게시판에 글 등록(부모글) 하는 쿼리문
insert into board(writer, title, content, imgFile, ref)
values('dooly', '아싸 1빠다~', '냉무', '', (select max(no) + 1 from board));


insert into board(writer, title, content, imgFile, ref)
values('kildong', '잘 되나?~', '되야 할텐데', '', 
(select auto_increment from information_schema.tables
where table_schema = 'jsh' and table_name = 'board')
);

commit;
-- ref 컬럼은 부모 글을 참조하는 컬럼이다. 
-- 답글이 아닌 부모글을 쓸 때는 자기 자신의 글번호(no)와 같은 값을 가지도록 해야 한다.

select max(no) + 1 as nextRef from board;
insert into board(writer, title, content, imgFile, ref)
values('kildong', '에잇~', '에고 1등 놓쳤네', '', 2);

-- 게시판 전체 글 목록을 보여주는 쿼리문
select * from board order by no desc;

-- 현재board 테이블에 있는 auto_increment 값을 참조
(select auto_increment from information_schema.tables
where table_schema = 'jsh' and table_name = 'board');

use jsh;

-----------------------------------------------------------------------------
-- 게시물 상세 조회
----------------------------------------------------------------------------
-- 조회수 처리를 위한 테이블 생성
CREATE TABLE `jsh`.`readcountprocess` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `ipAddr` VARCHAR(15) NOT NULL,
  `boardNo` INT NOT NULL,
  `readTime` DATETIME NOT NULL DEFAULT now(),
  PRIMARY KEY (`no`),
  INDEX `readcount_boardNo_fk_idx` (`boardNo` ASC) VISIBLE,
  CONSTRAINT `readcount_boardNo_fk`
    FOREIGN KEY (`boardNo`)
    REFERENCES `jsh`.`board` (`no`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
    
-- ?번 ip주소가 ?번 글을 읽은적이 있냐? 
select * from readcountprocess where ipAddr = ? and boardNo = ?;

select readtime from readcountprocess where ipAddr = ? and boardNo = ?

-- ?번 ip주소가 ?번 글을 읽은 시간이 몇 시간전?
select ifNull(timestampdiff(hour, 
(select readtime from readcountprocess where ipAddr = '211.197.18.247' 
and boardNo = 2), now()) > 24, -1) as diff;


-- ?번 글을 조회하는 쿼리문
select * from board where no=?;



-- ?번 글의 조회수를 증가하는 쿼리문
update board set readcount = readcount + 1 where no = ?;

-- ip주소, 글번호, 읽은 시간을 insert
insert into readcountprocess(ipAddr, boardNo) 
values (?, ?);

-- ip주소, 글번호의 데이터의 readTime을 현재시간으로  update
update readcountprocess set readTime = now() 
where ipAddr=? and boardNo=?;


------------------------------------------------------------------------------------------------
-- 게시판 답글 달기
------------------------------------------------------------------------------------------------
-- 모든 게시물을 출력하는 쿼리문
select * from board order by ref desc, reforder asc;

-- 부모글이 원래글인지 답글인지 판단하여 update한다(답글이 끼어들기할 공간을 만들어준다)
update board
set reforder = reforder + 1
where ref=? and reforder > ?;

-- 답글을 board테이블에 등록
insert into board(writer, title, content, ref, step, reforder)
values (?, ?, ?, ?, pStep+1, pRefOrder+1);

-- 내가쓴글
select * from board where writer = 'dooly';


-- 답글 수정의 경우(답글 작성자가 로그인하여 수정 가능, ref, step, reforder가 기존 유지)
-- 답글 삭제의 경우(답글 작성자가 로그인하여 삭제 가능, 
-- 답글의 writer : admin
-- 답글의 title : 삭제된 글입니다
-- 답글의 내용 : x
-- viewBoard.bo 페이지로 링크를 안걸어도 된다.
-- 조회수, 좋아요 수 초기화 되면서 대체  (ref, step, reforder 대체된 글이 유지)
--------------------------------------------------------------------------------------------------
-- 인기글 또는 최신글 몇개 가져오기
-----------------------------------------------------------------------------------
select * from board order by readcount desc limit 3;



--------------------------------------------------------------------------------------------------
-- 페이징 처리
-- select * from board order by ref desc, eforder asc 
-- limit 보여주기시작할row index번호, 보여줄 글의 갯수;
select * from board order by ref desc, reforder asc limit 0, 3;

-------------------------------------------------------------------------------------------------
-- 1) 하나의 페이지에 몇개의 글(ROW)을 보여줄 것인가 결정 : 3
-- 2) 게시판 글의 총 갯수를 얻어온다 : 23
select count(*) as cnt from board;
-- 3) 총 페이지의 수 : 게시판글 수 / 하나의 페이지에 보여줄 글의 수
-- 나누어 떨어지지 않으면 +1
-- 23 / 3 = 7 (나누어 떨어지지 않았으므로 +1 ) + 1 = 8페이지

-- 연습) X번 페이지에서 출력하게 될 글의 시작 번호
-- 전체글의 갯수(23) - (현재페이지번호-1) * 한페이장 보여줄 글 갯수
-- 1페이지 : 23 - 0 * 3 = 23
-- 2페이지 : 23 - 1 * 3 = 19
-- ....


select * from board order by ref desc, reforder asc limit 0, 5;
-- 2페이지 :
select * from board order by ref desc, reforder asc limit 5, 5;
-- 3페이지 :
select * from board order by ref desc, reforder asc limit 10, 5;
-- 4페이지 :
select * from board order by ref desc, reforder asc limit 15, 5;
-- 5페이지 :
select * from board order by ref desc, reforder asc limit 20, 5;

-- 5개보여준다


-- 4) 보여주기시작할row index번호 = 한페이지에 보여줄 글의 갯수 * (현재페이지 -1) 
-- 1페이지  = 3 * (1 - 1) = 0
--            5 * (1 - 1) = 0
--             10 *  (1 - 1) = 0
  
-- 2페이지 = 3 * (2 -1) = 3
--             5 * (2 -1) = 5
--			10 * (2 - 1) = 10
  
  select * from board order by ref desc, reforder asc limit 20, 10;

-- 페이징 블럭 만들기 (pagination에 페이지 번호를 몇개씩  보여줄 것인가)
-- 1) 1개의블럭에 몇개 페이지를 둘것인가(pageCntPerBlock) : 3
-- 전체 페이징 블럭 갯수 : 전체 페이지 수 / pageCntPerBlock;
-- 나누어 떨어지지 않으면 + 1 = 8 / 3 = 2 (나누어 떨어지지 않으므로) 3 

-- 2) 현재 페이지가 속한 페이징 블럭 : 현재페이지번호 / pageCntPerBlock
-- 2페이지 ->   2 / 3 (나누어 떨어지지 않으면 올림) . 1번블럭
-- 6페이지 -> 6 / 3 = 2번블럭

-- 3) 현재 페이징 블럭 시작번호 : ((현재 페이징블럭-1) * pageCntPerBlock ) + 1
-- 2번블럭 : ((2-1) * 3) + 1 => 4

-- 4) 현재 페이징 블럭 끝번호 : (현재 페이징블럭 시작번호 + pageCntPerBlock) - 1
-- 2번블럭 : (4 + 3) - 1 = 6


-----------------------------------------------------------------------------------------------
-- 게시판 검색
-----------------------------------------------------------------------------------------------
-- 제목으로 검색
select * from board where title like '%을%' order by ref desc, reforder asc;

-- 작성자 검색
select * from board where writer like '%dooly%' order by ref desc, reforder asc limit ?, ?;

-- 본문 검색
select * from board where content like '%사랑%' order by ref desc, reforder asc limit ?, ?;

-- 제목 + 내용 검색
select * from board where title like '%을%' or content like '%을%' order by ref desc, reforder asc;