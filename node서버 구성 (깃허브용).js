var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express(); 

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.listen(3000, '0.0.0.0', function () {
    console.log('서버 실행 중...');
});

var connection = mysql.createConnection({
    host: "db 엔드포인트",
    user: "db 사용자 계정",
    database: "db 스키마 명",
    password: "db 사용자 비밀번호",
    port: 3306
});

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