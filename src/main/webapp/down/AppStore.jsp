<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<% String browser=request.getHeader("User-Agent"); boolean result=false; String type="" ; if
    (browser.indexOf("Android")> 0) {
    result = true;
    type = "a";
    } else if (browser.indexOf("iPhone") > 0 || browser.indexOf("Mac OS X") > 0) {
    result = true;
    type = "i";
    } else if (browser.indexOf("iPod") > 0) {
    result = true;
    type = "i";
    } else if (browser.indexOf("iPad") > 0) {
    result = true;
    type = "i";
    }else{
    result = true;
    type = "a";
    }

    if ("i".equals(type)) {

    response.sendRedirect(request.getContextPath()+"/down/ios/html/ios-download.html");

    } else {

    response.sendRedirect(request.getContextPath()+"/down/android/html/android-download.html");

    }
%>
<html>
<!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=Edge" /><![endif]-->

<body>

<script>

</script>
</body>

</html>