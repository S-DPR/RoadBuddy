function initMap() {
    const mapOptions = {
        center: new google.maps.LatLng(37.5665, 126.9780),
        zoom: 8
    }
    const map = new google.maps.Map(document.getElementById("map"), mapOptions)
}
