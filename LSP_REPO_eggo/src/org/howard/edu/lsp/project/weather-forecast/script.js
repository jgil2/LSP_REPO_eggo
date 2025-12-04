// Function to determine icon and color based on the Weather API type
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

// Function to display the Current Conditions data
function displayCurrentConditions(data, resultDiv, inputLocation) {
    const weatherType = data.weatherCondition.type;
    const style = getWeatherStyle(weatherType);
    
    const displayCity = data.locationInfo.city || inputLocation;
    const displayZip = data.locationInfo.zip ? ` (${data.locationInfo.zip})` : '';

    // Apply dynamic styling to the result box
    resultDiv.style.backgroundColor = style.color + '1a'; // 1a adds 10% opacity
    resultDiv.style.borderLeft = `5px solid ${style.color}`;

    // 3. Construct the HTML using the correct nested paths
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

// Function to display the 5-Day Forecast data
function displayForecast(data, resultDiv, inputLocation) {
    const cityName = data.locationInfo.city || inputLocation;
    const displayZip = data.locationInfo.zip ? ` (${data.locationInfo.zip})` : '';

    resultDiv.style.backgroundColor = '#f8f9fa';
    resultDiv.style.borderLeft = 'none';

    let htmlContent = `<strong style="font-size: 1.5em; display: block; text-align: center; margin-bottom: 15px;">5-Day Forecast for ${cityName}${displayZip}</strong><div class="forecast-list">`;

    if (data.forecastDays && data.forecastDays.length > 0) {
        data.forecastDays.forEach(day => {
            const forecast = day.daytimeForecast;
            const weatherDesc = forecast.weatherCondition.description.text;
            const conditionType = forecast.weatherCondition.type; // <-- THIS LINE NOW WORKS
            const maxTemp = day.maxTemperature.degrees.toFixed(1);
            const minTemp = day.minTemperature.degrees.toFixed(1);
            const iconBaseUri = forecast.weatherCondition.iconBaseUri;
            const style = getWeatherStyle(forecast.weatherCondition.type);
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
        htmlContent += `<p style="text-align: center; padding: 20px;">Forecast data is currently unavailable.</p>`;
    }

    htmlContent += `</div>`;
    resultDiv.innerHTML = htmlContent;
}


// --- Main Fetch Function (called by the HTML buttons) ---

async function fetchWeather(type = 'current') {
    const location = document.getElementById("location").value.trim();
    const currentContent = document.getElementById("current-content");
    const forecastContent = document.getElementById("forecast-content");

    if (!location) {
        currentContent.innerHTML = "❗ Please enter a location.";
        forecastContent.innerHTML = "";
        return;
    }

    // Determine which content container to show/update
    const resultDiv = (type === 'forecast') ? forecastContent : currentContent;
    
    // Manage tab visibility
    currentContent.classList.toggle('active', type === 'current');
    forecastContent.classList.toggle('active', type === 'forecast');

    // Set loading state
    resultDiv.innerHTML = "Loading...";

    try {
        // 1. Fetch data from your Google Cloud Function, including the 'type'
        const response = await fetch(
            `https://us-central1-weather-forecast-479920.cloudfunctions.net/getWeather?location=${encodeURIComponent(location)}&type=${type}`
        );

        if (!response.ok) {
            // Check for HTTP error codes
            throw new Error(`API returned status ${response.status}. Check function logs.`);
        }

        const data = await response.json();

        // Check for specific error messages sent back by the function
        if (data.error) {
            resultDiv.innerHTML = `❗ ${data.error}`;
            return;
        }

        // 2. Route the data to the correct display function
        if (data.requestType === 'forecast') {
            displayForecast(data, resultDiv, location);
        } else { // type === 'current'
            displayCurrentConditions(data, resultDiv, location);
        }

    } catch (error) {
        // This catches network errors and general fetch failures
        console.error("Fetch Error:", error);
        resultDiv.innerHTML = "❗ Something went wrong. Check console for details (e.g., CORS or network error).";
        
        // Ensure the active tab remains visible on error
        resultDiv.classList.add('active');
    }
}