const axios = require("axios");

// Function to find a specific address component (e.g., 'locality' for City, 'postal_code' for ZIP)
const getComponent = (addressComponents, type) => 
  addressComponents.find(c => c.types.includes(type))?.long_name || '';

exports.getWeather = async (req, res) => {
    
    // --- CORS FIX START ---
    // Allows requests from any origin (*). This is essential for your local web page to access the function.
    res.set('Access-Control-Allow-Origin', '*'); 
    res.set('Access-Control-Allow-Headers', 'Content-Type');

    // Handle preflight OPTIONS request required by CORS
    if (req.method === 'OPTIONS') {
        res.status(204).send('');
        return;
    }
    // --- CORS FIX END ---
    
    try {
        const apiKey = process.env.GOOGLE_API_KEY;
        const location = req.query.location || "Washington, DC";
        const requestType = req.query.type || 'current'; // 'current' or 'forecast'

        // 1. Convert location to coordinates using the Geocoding API
        const geoURL = `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(location)}&key=${apiKey}`;
        const geoResponse = await axios.get(geoURL);

        if (!geoResponse.data.results.length) {
            // This is the source of the 404 error if Geocoding fails to find the location
            return res.status(404).json({ error: `Location '${location}' not found via Geocoding API.` });
        }

        const { lat, lng } = geoResponse.data.results[0].geometry.location;
        const addressComponents = geoResponse.data.results[0].address_components;

        // Extract City and ZIP Code
        const cityName = getComponent(addressComponents, 'locality') || getComponent(addressComponents, 'postal_code_city') || getComponent(addressComponents, 'administrative_area_level_1');
        const postalCode = getComponent(addressComponents, 'postal_code');

        let weatherResponse;

        // 2. Decide which Weather API endpoint to call
        if (requestType === 'forecast') {
            // Endpoint for 5-day forecast
            const forecastURL = `https://weather.googleapis.com/v1/forecast/days:lookup?key=${apiKey}&location.latitude=${lat}&location.longitude=${lng}`;
            weatherResponse = await axios.get(forecastURL);
            console.log("DEBUG: Forecast API Raw Response Data:", JSON.stringify(weatherResponse.data));
        } else { // type === 'current'
            // Endpoint for current conditions
            const currentURL = `https://weather.googleapis.com/v1/currentConditions:lookup?key=${apiKey}&location.latitude=${lat}&location.longitude=${lng}`;
            weatherResponse = await axios.get(currentURL);
        }

        // 3. Combine and return data
        return res.status(200).json({
            ...weatherResponse.data, // Spread all weather data (current or forecast)
            requestType: requestType, 
            locationInfo: {
                city: cityName,
                zip: postalCode
            }
        });

    } catch (err) {
        // Log the full error for debugging in Google Cloud Logs (Crucial for 500 errors)
        console.error("API Call Error:", err.message);
        
        // Handle Axios errors from upstream API calls (e.g., 400 Bad Request, 403 Forbidden)
        if (err.response) {
            console.error("Upstream API Response Status:", err.response.status);
            console.error("Upstream API Response Data:", err.response.data);
            
            // Pass the upstream status code back, but ensure it's not a generic 404/500 if possible
            const statusCode = err.response.status >= 400 ? err.response.status : 500;
            
            return res.status(statusCode).json({ 
                error: `External API Request Failed (Status ${err.response.status})`,
                details: err.response.data
            });
        }

        // Default 500 server error for unhandled exceptions
        return res.status(500).json({ error: "Internal Server Error", message: err.message });
    }
};