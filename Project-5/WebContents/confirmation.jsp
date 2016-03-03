<%@ page import="edu.ucla.cs.cs144.*" %>

<!DOCTYPE html>
<html style = "font-family:Calibri">
<head> 
<meta charset="UTF-8">
</head>
<title>Cheapo eBay</title>
</head>
<h1 style="text-align:center">Cheapo</h1>

<p style="text-align:center"><a href="./keywordSearch.html"><img src="eBayLogo.jpg" alt="Website Logo"></a></p>
<h2 style="text-align:center">Serving you auction items from over 10 years ago...</h2>
<br>
<h2 style="text-align:center">Congratulations! You just bought the following item:</h2>
<h3 style="text-align:center">Item ID: <%= request.getAttribute("ItemID")%></h3>
<h3 style="text-align:center">Item Name: <%= request.getAttribute("Name")%></h3>
<h3 style="text-align:center">Buy Price: $<%= request.getAttribute("Buy_Price")%></h3>
<h3 style="text-align:center">Credit Card Number: $<%= request.getAttribute("Credit_Card_Number")%></h3>
<h3 style="text-align:center">Time: <%= request.getAttribute("Time")%></h3>
<br>
</html>
