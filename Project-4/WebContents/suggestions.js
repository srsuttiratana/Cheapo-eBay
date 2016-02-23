
/**
 * Provides suggestions for state names (USA).
 * @class
 * @scope public
 */
function StateSuggestions() {
}

var xml_http_request = new XMLHttpRequest();

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    var sTextboxValue = oAutoSuggestControl.textbox.value;
    
    if (sTextboxValue){	//user is inputting text
		var new_url = "suggest?q=" + sTextboxValue;
		xml_http_request.open("GET", new_url);
		xml_http_request.onreadystatechange = this.getXMLSuggestions(xml_http_request, oAutoSuggestControl,bTypeAhead);
		xml_http_request.send(null);
    }
};

StateSuggestions.prototype.getXMLSuggestions = function (xml_http_req, oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
	return function()
	{
		if(xml_http_req.readyState == 4)	//xml is ready
		{
			var aSuggestions = [];
			var response = xml_http_req.responseXML.getElementsByTagName('CompleteSuggestion');
			
			var node = "";
			for (i = 0; i < response.length; i++)
			{
				node = response[i].childNodes[0].getAttribute('data');
				aSuggestions.push(node);
			}
			
			//provide suggestions to the control
			oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
		}
	}
}