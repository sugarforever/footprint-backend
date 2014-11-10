<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<title>Footprint Mobile</title>
<style type="text/css">
.full-screen {
	width: 100%;
	height: 100%;
	padding: 0;
}

img.uploadedImage {
	margin-bottom: 12px;
	display: block;
}

#timeline-close {
	margin-top: 24px;
}

.block-style {
	display: block;
}

#timeline-slot-listview .block-style {
	margin-bottom: 4px;
}
</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css">
<link rel="stylesheet" href="css/jquery.datetimepicker.css">
<link rel="stylesheet" href="css/swipebox.min.css">
</head>
<body>
    <div data-role="page" style="position: relative;" id="map-page" class="full-screen">
        <div data-role="panel" data-display="overlay" id="timeline-panel"> 
            <h4>My timeline</h4>

            <div id="timeline-slot-listview" data-role="collapsibleset"></div>

            <a href="#pageone" data-rel="close" id="timeline-close" class="ui-btn ui-mini ui-btn-inline ui-shadow ui-corner-all ui-btn-a ui-icon-delete ui-btn-icon-left">Close</a>
        </div>
        <div data-role="header">
            <a href="#" class="ui-btn ui-icon-clock ui-btn-icon-left" id="popup-timeline-panel">&nbsp;</a>
            <h3>Welcome To My Footprint</h3>
        </div>
        <div data-role="main" class="ui-content" style="display: none">
            <a id="btnOpenDialog" href="#popupDialog" data-rel="popup" data-position-to="window" data-transition="fade" class="ui-btn ui-corner-all ui-shadow ui-btn-inline">Open Dialog Popup</a>
            <div data-role="popup" id="popupDialog" data-theme="a" class="ui-corner-all">
                <div data-role="main" class="ui-content">
                    <h4>Create new marker</h4>
                    <a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right">Close</a>
                    <label for="date" >Date:</label>
                    <input type="text" name="date" id="date" value="" data-theme="a">
                    <label for="brief" >Brief:</label>
                    <input type="text" name="brief" id="brief" value="" data-theme="a">
                    <label for="content" >Content:</label>
                    <textarea rows="5" name="content" id="content" value="" data-theme="a"></textarea>
                    <label for="files" >Photo</label>
                    <input id="fileupload" type="file" name="files" data-url="api/image/upload" multiple>
                    <a id="marker-submit" href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b ui-icon-back ui-btn-icon-left" data-rel="back">Done</a>
                </div>
            </div>
            <a id="btnImageGallery" href="#imageGalleryDialog" data-rel="popup" data-position-to="window" data-transition="fade" class="ui-btn ui-corner-all ui-shadow ui-btn-inline"></a>
            <div data-role="popup" id="imageGalleryDialog" data-theme="a" class="ui-corner-all">
                <div data-role="main" class="ui-content" id="imageGallery">
                    <span id="gallery-brief" style="margin-right:24px;"></span><a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right">Close</a>
                    <p id="gallery-content"></p>
                </div>
            </div>
        </div>
        <div data-role="main" class="full-screen"><div id="map-canvas" class="full-screen"></div></div>
    </div>

    <!--script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDIekX4pNPTLAON4UFXJfI7YAfUVpliAtY"></script-->
    <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
    <script src="http://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.js"></script>
    <script src="js/jquery.fileupload.js"></script>
    <script src="js/jquery.datetimepicker.js"></script>
    <script src="js/jquery.swipebox.min.js"></script>
    <script src="js/ios-orientationchange-fix.js"></script>
    <script src="js/footprint.js"></script>
    <script type="text/javascript">
        var currentLatitude = 0;
        var currentLongitude = 0;
        var map;
        var galleryDialog;

        function setCurrentLocation(lat, lng) {
            currentLatitude = lat;
            currentLongitude = lng;
        }

        function setDefaultForFields() {
            setCurrentDateTime("#date");
            $("#brief").val("");
            $("#content").val("");
            $(".uploadedImage").remove();
        }

        function drawMap(latlng) {
            var myOptions = {
                zoom: 8,
                center: latlng,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                disableDoubleClickZoom: true
            };
            map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);

            google.maps.event.addListener(map, 'dblclick', function(event) {
                setCurrentLocation(event.latLng.lat(), event.latLng.lng());
                setDefaultForFields();
                $("#btnOpenDialog").click();
            });

            $.get("api/footprint/getAll", function(response) {
                var bounds = new google.maps.LatLngBounds();
                $.each(response, function(key, value) {                        
                    if (value.latitude != null && value.longitude != null && value.brief != null) {
                        createMarker(map, value.latitude, value.longitude, value.date, value.brief, value.images, value.content);
                        bounds.extend(new google.maps.LatLng(value.latitude, value.longitude));
                    }
                });
                map.fitBounds(bounds);
            });
        }  

        function init() {
            var defaultLatLng = new google.maps.LatLng(34.0983425, -118.3267434);  // Default to Hollywood, CA when no geolocation support
            if ( navigator.geolocation ) {
                function success(pos) {
                    // Location found, show map with these coordinates
                    drawMap(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
                }
                function fail(error) {
                    drawMap(defaultLatLng);  // Failed to find location, show default map
                }
                // Find the users current position.  Cache the location for 5 minutes, timeout after 6 seconds
                navigator.geolocation.getCurrentPosition(success, fail, {maximumAge: 500000, enableHighAccuracy:true, timeout: 6000});
            } else {
                drawMap(defaultLatLng);  // No geolocation support, show default map
            }          
        };

        $( document ).ready(function() {
            $('#fileupload').fileupload({
                dataType: 'json',
                done: function (e, data) {

                    var width = Math.ceil($("#date").outerWidth());

                    $.each(data.result, function (index, url) {
                        var img = $("<img />");
                        img.attr("id", "uploadedImage" + index);
                        img.addClass("uploadedImage");
                        img.attr("name", "uploadedImage");
                        img.attr("image-id", url);
                        img.attr("src", "api/image/?id=" + url);
                        img.attr("width", width);
                        $(img).insertAfter("#marker-submit");
                    });
                    
                    $("#map-page").trigger("pagecreate");
                }
            });

            $('#date').datetimepicker({lazyInit: true});

            $('#marker-submit').bind('click', function() {
                var imageArray = [];
                $.each($(".uploadedImage"), function(k, v) {
                    imageArray.push($(v).attr("image-id"));
                });
                createMarker(map, currentLatitude, currentLongitude, $("#date").val(), $("#brief").val(), imageArray, $("#content").val());

                $.ajax({
                    url: "api/footprint/add",
                    type: "post",
                    dataType: "json",
                    contentType: "application/json",
                    processData: false,
                    data: JSON.stringify({
                        date: $("#date").val(),
                        brief: $("#brief").val(),
                        content: $("#content").val(),
                        latitude: currentLatitude,
                        longitude: currentLongitude,
                        images: imageArray
                    }), 
                    success: function(data) {
                    },
                    error: function(data) {
                    }
                });
                setCurrentLocation(0, 0);
            });
            
            loadGoogleMap("init");
            //init();

            $("#popup-timeline-panel").bind("click", function() {
                generateTimelineSlotListView("#timeline-slot-listview", function() {
                    $("#timeline-panel").panel("open");
                });
                return false;    
            })
        });
    </script>

</body>
</html>

