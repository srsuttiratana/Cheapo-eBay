<%@ page import="edu.ucla.cs.cs144.*" %>

<!DOCTYPE html>
<html style = "font-family:Calibri">
<head> 
<meta charset="UTF-8">
</head>
<title>Cheapo eBay</title>
</head>
<h1 style="text-align:center">Cheapo</h1>

<p style="text-align:center"><img src="eBayLogo.jpg" alt="Website Logo"></a></p>
<h2 style="text-align:center">Serving you auction items from over 10 years ago...</h2>
<br>
<h2 style="text-align:center">Pay for your Auction Item Now!</h2>
<h3 style="text-align:center">Item ID:</h3>
<p style="text-align:center"><%= request.getAttribute("ItemID")%></p>
<h3 style="text-align:center">Item Name:</h3>
<p style="text-align:center"><%= request.getAttribute("Name")%></p>
<h3 style="text-align:center">Buy Price:</h3>
<p style="text-align:center">$<%= request.getAttribute("Buy_Price")%></p>
<br>
<h3 style="text-align:center">Please enter your credit card information below.</h3>
<form action = "https://<%= request.getServerName()%>:8443<%=request.getContextPath()%>/purchase" method="POST" action="" style="text-align:center"> 
<label>Credit Card Number</label>
   <br><br>
   <div>
		<input id = "creditcardnum" type="text" name="cardinput" autocomplete="off" required>
   </div>
   <br><br>
   <button type="submit" name="submit">Submit</button>
</form>

</html>
