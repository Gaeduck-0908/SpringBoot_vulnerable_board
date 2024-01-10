# SpringBoot_vulnerable_board
- 스프링부트, AJAX 를 이용한 Non secure coding 과제
- 웹서버만 Docker 이며 Oracle 서버는 localhost 입니다
- 소요기간 : 2024.01.03 ~ 2024.01.04
# Vulnerability
- Parameter Modulation
- insufficient authorization, authentication
- information leakage (Comment)
# Table_Info
```sql
CREATE TABLE BOARD_POST (
    ID NUMBER PRIMARY KEY,
    TITLE VARCHAR2(100) NOT NULL,
    CONTENT VARCHAR2(4000) NOT NULL,
    USER_ID VARCHAR2(50) NOT NULL,
    POST_DATE DATE DEFAULT SYSDATE NOT NULL,
    UPDATE_DATE DATE
);

CREATE OR REPLACE TRIGGER trg_update_date
BEFORE INSERT OR UPDATE ON BOARD_POST
FOR EACH ROW
BEGIN
    :NEW.UPDATE_DATE := SYSDATE;
END;
/


CREATE TABLE BOARD_USER (
    USER_ID VARCHAR2(50) PRIMARY KEY,
    PASSWORD VARCHAR2(100) NOT NULL,
    NAME VARCHAR2(50) NOT NULL,
    EMAIL VARCHAR2(100),
    PHONE VARCHAR2(20),
    REG_DATE DATE DEFAULT SYSDATE
);

CREATE SEQUENCE BOARD_POST_SEQ
  START WITH 1
  INCREMENT BY 1;

CREATE SEQUENCE COMMENT_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE BOARD_COMMENTS (
    COMMENT_ID NUMBER(10) DEFAULT COMMENT_SEQ.NEXTVAL PRIMARY KEY,
    POST_ID NUMBER(10) NOT NULL,
    USER_ID VARCHAR2(50) NOT NULL,
    CONTENT VARCHAR2(255) NOT NULL,
    COMMENT_DATE DATE DEFAULT SYSDATE
);

```
# Run
```
./gradlew build
docker build -t [이미지명]
docker run --name [컨테이너명] -p [포트:포트] -v [host dir]:/usr/src/app/files [이미지명]
```
# Screenshot
![image](https://github.com/Gaeduck-0908/SpringBoot_vulnerable_board/assets/82009667/0a6fc732-9de2-45c8-8e09-e07ebaaa7624)
