<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script>
function fire() {
	var url = "<%=request.getContextPath()%>/svc/realtest/fire"
	url += "?sleep=" + $("#sleepTime").val()
	url += "&count=" + $("#countOfFire").val()
	url += "&postfix=" + $("#postFix").val()
	lcAjax(url,
			null,
			function(data) {
				alert(JSON.stringify(data))
			},
			function(error) {
				alert(JSON.stringify(error))
			},
			"GET")
}
</script>
</head>
<body>
<h1>Real Tester</h1>
건당Sleep시간 : <input type="text" id="sleepTime" value="1"><br>
전송건수 : <input type="text" id="countOfFire" value="1000"><br>
파일뒷부분 : <input type="text" id="postFix" value="006800"><br>
<input type="button" value="Fire!!!" onclick="fire()">
</body>
</html>
