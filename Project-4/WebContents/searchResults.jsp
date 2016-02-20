<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cheapo eBay</title>
</head>
<h1 style="text-align:center">Cheapo</h1>

<p style="text-align:center"><a href="./keywordSearch.html"><img src="eBayLogo.jpg" alt="Website Logo"></a></p>
<h2 style="text-align:center">Serving you auction items from over 10 years ago...</h2>
<body>

</body>

<script type ="text/javascript">
	function checkForm(form)
	{
		var alphabet = /^[a-zA-Z ]*$/;
		if(!alphabet.test(form.first_name.value) || !alphabet.test(form.last_name.value)  )
		{
			alert("Error: Name(s) contain(s) invalid characters.");
			return false;
		}
		var date_valid = /^(?:(?:(?:(?:(?:[13579][26]|[2468][048])00)|(?:[0-9]{2}(?:(?:[13579][26])|(?:[2468][048]|0[48]))))(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:0[1-9]|1[0-9]|2[0-9]))))|(?:[0-9]{4}(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:[01][0-9]|2[0-8])))))$/;
		if(!date_valid.test(form.dob.value))
		{
			alert("Error: Date of birth is invalid.");
			return false;
		}
		if (form.dod.value != "" && !date_valid.test(form.dod.value))
		{
			alert("Error: Date of death is invalid.");
			return false;
		}
		if (form.dod.value != "" && form.dod.value < form.dob.value)
		{
			alert("Error: Date of death and birth are invalid.");
			return false;
		}
	}
</script>

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

<%
	SearchResult[] results = (SearchResult[])request.getAttribute("results");
%>
<div>
	<h1>Results</h1>
	<ul>
		<% for (SearchResult item: results) { %>
			<a href="/eBay/item?id=<%=item.getItemId()%>"><li>ID:<%= item.getItemId() %> Name:<%= item.getName()%></li></a>	
		<% } %>
	</ul>
</div>

<div>
<%
	String q=(String)request.getAttribute("q");
	int numResultsToSkip=Integer.parseInt((String)request.getAttribute("numResultsToSkip"));
	int numResultsToReturn=Integer.parseInt((String)request.getAttribute("numResultsToReturn"));
	int skip=numResultsToSkip - 20;
	if (numResultsToSkip - 20 < 0)
		skip=0;
 if (numResultsToSkip>0) { %>
	<a href="/eBay/search?q=<%= q %>&numResultsToSkip=<%= skip %>&numResultsToReturn=20">Previous</a> 
<% } %>

<% if (results.length==numResultsToReturn) { %>
	<a href="/eBay/search?q=<%= q %>&numResultsToSkip=<%= numResultsToSkip+20 %>&numResultsToReturn=20">Next</a>
<% } %>
</div>
</html>