 // Get the user's current city and fetch available vehicles
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(showCityAndFetchVehicles);
            } else {
                console.log("Geolocation is not supported by this browser.");
            }


        function showCityAndFetchVehicles(position) {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            // Make a request to a Geocoding API to get the city name
            const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=AIzaSyBTKDxn_ZQ8YI9cAgZGiIO0UMCEN8dxA0A`;

            fetch(url)
                .then((response) => response.json())
                .then((data) => {
                    // Parse the city name from the API response
                    const city = data.results[0].address_components.find((component) =>
                        component.types.includes("locality")
                    ).long_name;

                    console.log(`Your city is ${city}.`);
                    if (city) {
                        const vehicleFetchUrl = `/user/vehicle-booking/${city}`;
                        window.location.href = vehicleFetchUrl; // Redirect to fetch vehicles by city
                    }
                })
                .catch((error) => console.log(error));
        }