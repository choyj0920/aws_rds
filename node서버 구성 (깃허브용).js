var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express(); 

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// 0.0.0.0 호스트를 지정하지 않음 설정. 모든 인터페이스에서 실행가능.
//app.listen(port,[hostname],[backlog],[callback])
//서버 생성뒤 port(3000) 을 보고 hostname(ip) 를 선별하고, 비동기적으로 작용.
app.listen(3000, '0.0.0.0', function () {
    console.log('서버 실행 중...');
});

//db 연결
var connection = mysql.createConnection({
    host: "db 엔드포인트",
    user: "db 사용자 계정",
    database: "db 스키마 명",
    password: "db 사용자 비밀번호",
    port: 3306
});
//~/user/data(routing 대상) 에 접속해서 함수 실행 
//주소를 받아서 특정 주소에 해당하는 요청이 왔을 때 미들웨어 동작
//req : Request Object 요청객체, 클라이언트에서 보낸 여러 정보 포함
//res : response Object 응답객체 , 클라이언트에게 응답할 수 있게하는 객체
//- Annotation : 인터페이스 기반 문법, 특별한 의미 부여 or 기능 주입 가능. 
//- retrofit : http(클과 서버 요청 프로토콜) rest api 구현을 위한 라이브러리 (위에 36강) 
//- rest api : rest 기반 api 
//- rest : 웹에서 사용하는 Architecture의 한 형식. 네트워크 상 클라이언트와 서버간의 통신 방식, 분산환경에서 클라우드 서비스에 연결 및 상호작용 도와줌. URI를 통해 자원(Resouce)을 명시하고 HTTP Method(GET(데이터 얻기), POST(제출), PUT(업데이트), PATCH, DELETE 등)를 통해  해당 자원에 대한 CRUD를 적용하는 것을 의미 
app.post('/user/join', function (req, res) {
    console.log(req.body);
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var userName = req.body.userName;

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO Users (UserEmail, UserPwd, UserName) VALUES (?, ?, ?)';
    var params = [userEmail, userPwd, userName];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = '회원가입에 성공했습니다.';
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

app.post('/user/login', function (req, res) {
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var sql = 'select * from Users where UserEmail = ?';
    

    connection.query(sql,userEmail, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';
        var uid =null

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 계정입니다!';
            } else if (userPwd !== result[0].UserPwd) {
                resultCode = 204;
                message = '비밀번호가 틀렸습니다!';
            } else {
                resultCode = 200;
                message = '로그인 성공! ' + result[0].UserName + '님 환영합니다!';
                userId=result[0].UserID
            }
        }

        res.json({
            'code': resultCode,
            'message': message,
            'userId' : userId
        });
    })
});

app.post('/user/friends',function(req,res){
    var useruid = req.body.useruid;
    console.log(useruid+'친구리스트...');

    var sql = 'select User1Uid,User1Name,User2Uid,User2Name from FriendsList where User1Uid=? or User2Uid=?';

    connection.query(sql,[useruid,useruid], function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';
        
        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '칭구가 없습니다...';
            } else {
                resultCode = 200;
                message = '칭구 ' + result.length + '명 발견';
            }
        }

        res.json({
            'code': resultCode,
            'message': message,
            'userlist' : result
        });
    })

});