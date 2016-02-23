<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.ucla.cs.cs144.*" %>

<!DOCTYPE html>
<html style = "font-family:Calibri">
<head>
<head> 
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<style type="text/css"> 
  html { height: 100% } 
  body { height: 100%; margin: 0px; padding: 0px } 
  #map_canvas { height: 100% } 
</style> 
<script type="text/javascript" 
    src="http://maps.google.com/maps/api/js?sensor=false"> 
</script> 
<script type="text/javascript"> 
  function initialize() { 
    var latlng = new google.maps.LatLng(<%= request.getAttribute("Latitude")%>,<%= request.getAttribute("Longitude")%>); 
    var myOptions = { 
      zoom: 14, // default is 8  
      center: latlng, 
      mapTypeId: google.maps.MapTypeId.ROADMAP 
    }; 
    var map = new google.maps.Map(document.getElementById("map_canvas"), 
        myOptions); 
  } 

</script> 
<meta charset="UTF-8">
<title>Cheapo eBay</title>
</head>
<h1 style="text-align:center">Cheapo</h1>

<p style="text-align:center"><a href="./keywordSearch.html"><img src="eBayLogo.jpg" alt="Website Logo"></a></p>
<h2 style="text-align:center">Serving you auction items from over 10 years ago...</h2>

<body onload="initialize()"> 
<form name = "query" action = "./item" method="GET" action="" style="text-align:center"> 
<!-- onsubmit="return checkForm(this);"-->
<label>Item ID Search</label>
   <br><br>
   <div>
		<input type="text" name="id" required>
   </div>
   <br><br>

   <button type="submit" name="submit">Submit</button>
</form>
<br>
<form name = "query" action = "./search" method="GET" action="" style="text-align:center"> 
<!-- onsubmit="return checkForm(this);"-->
<label>Keyword Search</label>
   <br><br>
   <div>
		<input type="text" name="q" required>
   </div>
   <input name="numResultsToSkip" value="0" type="hidden">
	<input name="numResultsToReturn" value="20" type="hidden">
   <br><br>

   <button type="submit" name="submit">Submit</button>
</form>

<h1> Search Result for Item ID: <%= request.getAttribute("ItemID") %></h1>
<h2>Name: <%= request.getAttribute("Name") %></h2>
<h2>Item Categories</h2>
<%
	ArrayList<String> Categories = (ArrayList<String>) request.getAttribute("Categories"); 
%>
<div>
<ul>
		<% for (String item: Categories) { %>
			<li><%= item%></li></a>	
		<% } %>
</ul>
</div>

<br>
<h2>Other Item Information</h2>
<div>
<p><b>Currently: </b> <%= request.getAttribute("Currently")%></p>
<p><b>Buy Price: </b> <%= request.getAttribute("Buy_Price")%></p>
<p><b>Started on: </b> <%= request.getAttribute("Started")%></p>
<p><b>Ends on: </b> <%= request.getAttribute("Ends")%></p>
<p><b>Description: </b> <%= request.getAttribute("Description")%></p>
<p><b>First Bid: </b> <%= request.getAttribute("First_Bid")%></p>
</div>
<br>

<h2>Bids</h2>
<p><b>Number of Bids: </b> <%= request.getAttribute("Number_of_Bids")%></p>
<% ArrayList<ItemServlet.Bid> Bids = (ArrayList<ItemServlet.Bid>) request.getAttribute("Bids");
%>
<ol>
	<% for(ItemServlet.Bid bid: Bids) { %>
		<li>
			<p><b>Bidder ID: </b> <%= bid.bidder_id%></p>
			<p><b>Bidder Rating: </b> <%= bid.bidder_rating%></p>
			<p><b>Location: </b> <%= bid.bidder_location%></p>
			<p><b>Country: </b> <%= bid.bidder_country%></p>
			<p><b>Time: </b> <%= bid.bid_time%></p>
			<p><b>Amount: </b> <%= bid.bid_amount%></p>
		</li>
		<% } %>
</ol>
<br>
<h2> Location </h2>
<div>
<p><b>Location: </b> <%= request.getAttribute("Location")%></p>
<p><b>Country: </b> <%= request.getAttribute("Country")%></p>
<p><b>Latitude: </b> <%= request.getAttribute("Latitude")%></p>
<p><b>Longitude: </b> <%= request.getAttribute("Longitude")%></p>
</div>
<div id="map_canvas" style="width:100%; height:100%"></div> 
<h2> Seller Information </h2>
<p><b>Seller ID: </b> <%= request.getAttribute("seller_id")%></p>
<p><b>Seller Rating: </b> <%= request.getAttribute("seller_rating")%></p>
</body> 
</html>