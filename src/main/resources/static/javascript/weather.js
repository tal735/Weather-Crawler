function getWeather(countryName, cityName) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
  if (this.readyState == 4 && this.status == 200) {
    var myObj = JSON.parse(this.responseText);

    var table = document.getElementById("myTable");
    var row = table.insertRow(1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    var cell5 = row.insertCell(4);
    cell1.innerHTML = myObj.location;
    cell2.innerHTML = myObj.temperature;
    cell3.innerHTML = myObj.humidity;
    cell4.innerHTML = myObj.wind;
    cell5.innerHTML = "<img src=\"" + myObj.icon +"\" />";
  }
};

  var endpoint = "/weather"
  var params = {
    country: countryName,
    city: cityName
    }

  var url = endpoint + formatParams(params)
  xhttp.open("GET", url, true);
  xhttp.send();
}

function formatParams( params ) {
  return "?" + Object
        .keys(params)
        .map(function(key){
          return key+"="+encodeURIComponent(params[key])
        })
        .join("&")
}

function searchForLocations(theForm) {
    var query = theForm.query.value;

    var endpoint = "/locations"
    var params = {
    query: query
    }
    var url = endpoint + formatParams(params);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var ul = document.getElementById("location_list");
            ul.innerHTML = ""; // clear previous search results
            var json = JSON.parse(xhr.responseText);
            json.forEach(function(obj) {
                var node = document.createElement("LI");
                var button = document.createElement('button');
                button.innerHTML = obj.city + ', ' + obj.country;
                button.onclick =  function(){
                 getWeather(obj.countryUrl, obj.cityUrl);
                };
                node.appendChild(button);
                ul.appendChild(node);
             });
        }
    };
    xhr.send();

    return false;
}