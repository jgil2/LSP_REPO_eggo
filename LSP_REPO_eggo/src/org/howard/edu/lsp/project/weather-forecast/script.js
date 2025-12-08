/**
 * @typedef {Object} WeatherStyle
 * @property {string} icon - The emoji icon representing the weather condition.
 * @property {string} color - The hexadecimal color code for styling the UI.
 */

/**
 * Determines the appropriate emoji icon and color code for a given weather type
 * returned by the Google Weather API.
 *
 * @param {string} type - The standardized weather type string (e.g., 'CLEAR', 'RAIN', 'SNOW').
 * @returns {WeatherStyle} An object containing the icon emoji and color.
 */
function getWeatherStyle(type) {
    // This is used for both current conditions and forecast items
    switch (type) {
        case 'CLEAR':
        case 'MOSTLY_CLEAR':
            return { icon: '☀️', color: '#ffc107' }; // Yellow for sun
        case 'RAIN':
        case 'SHOWERS':
        case 'SCATTERED_SHOWERS':
        case 'LIGHT_RAIN':
            return { icon: '🌧️', color: '#007bff' }; // Blue for rain
        case 'CLOUDY':
        case 'PARTLY_CLOUDY':
        case 'MOSTLY_CLOUDY':
            return { icon: '☁️', color: '#6c757d' }; // Grey for clouds
        case 'SNOW':
            return { icon: '❄️', color: '#17a2b8' }; // Light blue for snow
        case 'HAZY':
        case 'SMOKE':
        case 'FOG':
            return { icon: '🌫️', color: '#6c757d' };
        default:
            return { icon: '🌡️', color: '#444' };
    }
}

/**
 * Displays the current weather conditions data in the designated result container.
 *
 * @param {Object} data - The JSON response data from the Cloud Function containing current weather details.
 * @param {HTMLElement} resultDiv - The DOM element (e.g., div#current-content) to render the results into.
 * @param {string} inputLocation - The original location string entered by the user.
 */
function displayCurrentConditions(data, resultDiv, inputLocation) {
    const weatherType = data.weatherCondition.type;
    const style = getWeatherStyle(weatherType);
    
    // Use the city name from Geocoding if available, otherwise use the raw input.
    const displayCity = data.locationInfo.city || inputLocation;
    // Append ZIP code if provided.
    const displayZip = data.locationInfo.zip ? ` (${data.locationInfo.zip})` : '';

    // Apply dynamic styling to the result box based on weather condition
    resultDiv.style.backgroundColor = style.color + '1a'; // 1a adds 10% opacity
    resultDiv.style.borderLeft = `5px solid ${style.color}`;

    // Construct the HTML using the data properties
    resultDiv.innerHTML = `
        <span style="font-size: 2.5em; display: block; margin-bottom: 5px;">${style.icon}</span>
        <strong style="color: ${style.color};">${displayCity}${displayZip}</strong>
        <p>
            Temperature: <span style="font-weight: 600;">${data.temperature.degrees.toFixed(1)}°C</span><br>
            Feels Like: ${data.feelsLikeTemperature.degrees.toFixed(1)}°C<br>
            Condition: ${data.weatherCondition.description.text}<br>
            Humidity: ${data.relativeHumidity}%<br>
            Wind: ${data.wind.speed.value.toFixed(1)} km/h
        </p>
    `;
}

/**
 * Displays the 5-Day Forecast data by iterating through each day and rendering a forecast card.
 *
 * @param {Object} data - The JSON response data from the Cloud Function containing 5-day forecast details.
 * @param {HTMLElement} resultDiv - The DOM element (e.g., div#forecast-content) to render the results into.
 * @param {string} inputLocation - The original location string entered by the user.
 */
function displayForecast(data, resultDiv, inputLocation) {
    const cityName = data.locationInfo.city || inputLocation;
    const displayZip = data.locationInfo.zip ? ` (${data.locationInfo.zip})` : '';

    // Reset styling for the forecast view (not single-card style)
    resultDiv.style.backgroundColor = '#f8f9fa';
    resultDiv.style.borderLeft = 'none';

    let htmlContent = `<strong style="font-size: 1.5em; display: block; text-align: center; margin-bottom: 15px;">5-Day Forecast for ${cityName}${displayZip}</strong><div class="forecast-list">`;

    // Check if forecast data is available before looping
    if (data.forecastDays && data.forecastDays.length > 0) {
        data.forecastDays.forEach(day => {
            // The forecast data is nested under 'daytimeForecast'
            const forecast = day.daytimeForecast;
            const weatherDesc = forecast.weatherCondition.description.text;
            const conditionType = forecast.weatherCondition.type; // <-- THIS LINE NOW WORKS
            const maxTemp = day.maxTemperature.degrees.toFixed(1);
            const minTemp = day.minTemperature.degrees.toFixed(1);
            const iconBaseUri = forecast.weatherCondition.iconBaseUri;
            // Extract core data points
            const style = getWeatherStyle(forecast.weatherCondition.type);
            // Construct Date object using year, month (0-indexed), and day from API
            const date = new Date(day.displayDate.year, day.displayDate.month - 1, day.displayDate.day);
            
            htmlContent += `
                <div class="forecast-item" style="border-left: 5px solid ${style.color};">
                    <p style="font-weight: 600; color: #333;">${date.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' })}</p>
                    <div style="display: flex; align-items: center; gap: 10px;">
                        <span style="font-size: 1.5em;">${style.icon}</span>
                        <p style="text-align: right;">
                            ${forecast.weatherCondition.description.text}<br>
                            High: ${day.maxTemperature.degrees.toFixed(1)}°C / Low: ${day.minTemperature.degrees.toFixed(1)}°C
                        </p>
                    </div>
                </div>
            `;
        });
    } else {
        // Display a fallback message if no forecast data is returned
        htmlContent += `<p style="text-align: center; padding: 20px;">Forecast data is currently unavailable.</p>`;
    }

    htmlContent += `</div>`;
    resultDiv.innerHTML = htmlContent;
}

/**
 * Initiates the weather data fetch by calling the Google Cloud Function endpoint.
 *
 * @param {('current'|'forecast')} [type='current'] - The type of weather data to fetch.
 * @returns {Promise<void>}
 */
async function fetchWeather(type = 'current') {
    const location = document.getElementById("location").value.trim();
    const currentContent = document.getElementById("current-content");
    const forecastContent = document.getElementById("forecast-content");

    // Client-side validation: ensure a location is entered
    if (!location) {
        currentContent.innerHTML = "❗ Please enter a location.";
        forecastContent.innerHTML = "";
        return;
    }

    // Determine which content container to show/update
    const resultDiv = (type === 'forecast') ? forecastContent : currentContent;
    
    // Manage tab visibility using CSS classes
    currentContent.classList.toggle('active', type === 'current');
    forecastContent.classList.toggle('active', type === 'forecast');

    // Set loading state while waiting for the API call
    resultDiv.innerHTML = "Loading...";

    try {
        // Fetch data from your Google Cloud Function, passing location and type as query params
        const response = await fetch(
            `https://us-central1-weather-forecast-479920.cloudfunctions.net/getWeather?location=${encodeURIComponent(location)}&type=${type}`
        );

        if (!response.ok) {
            // Check for HTTP error codes (4xx, 5xx) returned by the Cloud Function
            throw new Error(`API returned status ${response.status}. Check function logs.`);
        }

        const data = await response.json();

        // Check for specific error messages sent back in the JSON body by the function
        if (data.error) {
            resultDiv.innerHTML = `❗ ${data.error}`;
            return;
        }

        // Route the data to the correct display function based on the request type
        if (data.requestType === 'forecast') {
            displayForecast(data, resultDiv, location);
        } else { // type === 'current'
            displayCurrentConditions(data, resultDiv, location);
        }

    } catch (error) {
        // This catches network errors, DNS failures, or errors thrown above
        console.error("Fetch Error:", error);
        resultDiv.innerHTML = "❗ Something went wrong. Check console for details (e.g., CORS or network error).";
        
        // Ensure the active tab remains visible on error
        resultDiv.classList.add('active');
    }
}