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
<h3 style="text-align:center">Item ID:</h3>
<p style="text-align:center"><%= request.getAttribute("ItemID")%></p>
<h3 style="text-align:center">Item Name:</h3>
<p style="text-align:center"><%= request.getAttribute("Name")%></p>
<h3 style="text-align:center">Buy Price:</h3>
<p style="text-align:center">$<%= request.getAttribute("Buy_Price")%></p>
<h3 style="text-align:center">Credit Card Number:</h3>
<p style="text-align:center">$<%= request.getAttribute("Credit_Card_Number")%></p>
<h3 style="text-align:center">Time:</h3>
<p style="text-align:center"><%= request.getAttribute("Time")%></p>
<br>
</html>
