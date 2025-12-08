const axios = require("axios");

/**
 * @typedef {Object} AddressComponent
 * @property {string} long_name The full text name of the component.
 * @property {Array<string>} types An array of types for the component (e.g., 'locality', 'postal_code').
 */

/**
 * Finds a specific address component (e.g., 'locality' for City, 'postal_code' for ZIP) 
 * within the array of components returned by the Google Geocoding API.
 * * @param {Array<AddressComponent>} addressComponents - The array of address components from the Geocoding response.
 * @param {string} type - The specific type of component to search for (e.g., 'locality' for city, 'postal_code' for ZIP code).
 * @returns {string} The long name of the component, or an empty string if not found.
 */
const getComponent = (addressComponents, type) => 
  addressComponents.find(c => c.types.includes(type))?.long_name || '';

/**
 * Google Cloud Function entry point to fetch current or forecast weather data for a given location.
 * * It performs the following steps:
 * 1. Handles CORS preflight request.
 * 2. Uses the Geocoding API to convert a text location into latitude and longitude.
 * 3. Extracts city and ZIP code information.
 * 4. Calls the Google Weather API for either current conditions or a 5-day forecast.
 * 5. Returns the combined weather and location data to the client.
 *
 * @param {Object} req - The request object (contains query parameters like 'location' and 'type').
 * @param {Object} res - The response object used to send back status and data.
 * @returns {void}
 */
exports.getWeather = async (req, res) => {
    // Sets headers to allow cross-origin requests from any domain.
    res.set('Access-Control-Allow-Origin', '*'); 
    res.set('Access-Control-Allow-Headers', 'Content-Type');

    // Handles preflight OPTIONS request required by CORS for non-simple requests
    if (req.method === 'OPTIONS') {
        res.status(204).send('');
        return;
    }
    
    try {
        const apiKey = process.env.GOOGLE_API_KEY;
        // Extract location from query
        const location = req.query.location;
        if (!location) {
            return res.status(400).json({ error: "Location is required. Please enter a location." });
        }
        // Extract request type, default to 'current'
        const requestType = req.query.type || 'current';

        // Convert location to coordinates using the Geocoding API
        const geoURL = `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(location)}&key=${apiKey}`;
        const geoResponse = await axios.get(geoURL);

        // Check if the Geocoding API found any results
        if (!geoResponse.data.results.length) {
            return res.status(404).json({ error: `Location '${location}' not found via Geocoding API.` });
        }

        // Extract coordinates and address components
        const { lat, lng } = geoResponse.data.results[0].geometry.location;
        const addressComponents = geoResponse.data.results[0].address_components;

        // Extract City (using various potential component types) and ZIP Code
        const cityName = getComponent(addressComponents, 'locality') || getComponent(addressComponents, 'postal_code_city') || getComponent(addressComponents, 'administrative_area_level_1');
        const postalCode = getComponent(addressComponents, 'postal_code');

        let weatherResponse;

        // Decide which Weather API endpoint to call based on requestType
        if (requestType === 'forecast') {
            // Endpoint for 5-day forecast (using GET request with URL parameters)
            const forecastURL = `https://weather.googleapis.com/v1/forecast/days:lookup?key=${apiKey}&location.latitude=${lat}&location.longitude=${lng}`;
            weatherResponse = await axios.get(forecastURL);
            // Log the raw response for debugging forecast data availability
            console.log("DEBUG: Forecast API Raw Response Data:", JSON.stringify(weatherResponse.data));
        } else { // type === 'current'
            // Endpoint for current conditions
            const currentURL = `https://weather.googleapis.com/v1/currentConditions:lookup?key=${apiKey}&location.latitude=${lat}&location.longitude=${lng}`;
            weatherResponse = await axios.get(currentURL);
        }

        // Combine weather API data with location metadata and return to client
        return res.status(200).json({
            ...weatherResponse.data, // Spread all weather data (e.g., 'temperature', 'weatherCondition', 'forecastDays')
            requestType: requestType, 
            locationInfo: {
                city: cityName,
                zip: postalCode
            }
        });

    } catch (err) {
        // Log the full error for debugging in Google Cloud Logs (Crucial for 500 errors)
        console.error("API Call Error:", err.message);
        
        // Handle specific Axios errors from upstream API calls (e.g., 400 Bad Request, 403 Forbidden)
        if (err.response) {
            console.error("Upstream API Response Status:", err.response.status);
            console.error("Upstream API Response Data:", err.response.data);
            
            // Return the upstream status code if it's a client error (4xx), otherwise default to 500
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