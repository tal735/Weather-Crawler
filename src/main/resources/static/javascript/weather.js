function getWeather(countryName, cityName) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
  if (this.readyState == 4 && this.status == 200) {
    var myObj = JSON.parse(this.responseText);

    var table = document.getElementById("myTable");
    var row = table.insertRow(1);
    var loc = row.insertCell(0);
    var temp = row.insertCell(1);
    var hum = row.insertCell(2);
    var wind = row.insertCell(3);
    var icon = row.insertCell(4);
    var remove = row.insertCell(5);
    loc.innerHTML = myObj.location;
    temp.innerHTML = myObj.temperature;
    hum.innerHTML = myObj.humidity;
    wind.innerHTML = myObj.wind;
    icon.innerHTML = "<img src=\"" + myObj.icon +"\" />";
    remove.innerHTML = "<input type=\"button\" value=\"Remove\" onclick=\"deleteRow(this)\">";
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

function deleteRow(r) {
  var i = r.parentNode.parentNode.rowIndex;
  document.getElementById("myTable").deleteRow(i);
}