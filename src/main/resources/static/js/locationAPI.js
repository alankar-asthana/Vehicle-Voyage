var config = {
    cUrl: 'https://api.countrystatecity.in/v1/countries',
    ckey: 'OW0xNkRyY0FnV2JISTA1dGtXWWFlS1BXZDBoUG1CMTlNaUFjVUJsQQ=='
}
var countrySelect = document.querySelector('.country'),
    stateSelect = document.querySelector('.state'),
    citySelect = document.querySelector('.city')

function loadCountries(){
    let apiEndPoint = config.cUrl

    fetch(apiEndPoint, {headers: {"X-CSCAPI-KEY": config.ckey}})
    .then(Response => Response.json())
    .then(data =>{
//        console.log(data);
        data.forEach(country => {
            const option = document.createElement('option');
            option.value = country.iso2;
            option.textContent = country.name;
            countrySelect.appendChild(option);
        })
    })
    .catch(error => console.error('Error loading countries:', error));

    stateSelect.disabled = true
    citySelect.disabled = true
    stateSelect.style.pointerEvents = 'none'
    citySelect.style.pointerEvents = 'none'
}

function loadStates(){
    stateSelect.disabled = false
    citySelect.disabled = true
    stateSelect.style.pointerEvents = 'auto'
    citySelect.style.pointerEvents = 'none'

    const selectedCountryCode = countrySelect.value
//    console.log(selectedCountryCode);
    stateSelect.innerHTML = '<option value="">Select State</option>'

    fetch(`${config.cUrl}/${selectedCountryCode}/states`, {headers: {"X-CSCAPI-KEY": config.ckey}})
    .then(response => response.json())
    .then(data => {
//        console.log(data);
        data.forEach(state => {
            const option = document.createElement('option')
            option.value = state.iso2
            option.textContent = state.name
            stateSelect.appendChild(option)
        })
    })
    .catch(error => console.error('Error loading states:', error))
}

function loadCities(){

    citySelect.disabled = false
    citySelect.style.pointerEvents = 'auto'

    const selectedCountryCode = countrySelect.value
    const selectedStateCode = stateSelect.value

    citySelect.innerHTML = '<option value="">Select City</option>'

    fetch(`${config.cUrl}/${selectedCountryCode}/states/${selectedStateCode}/cities`, {headers: {"X-CSCAPI-KEY": config.ckey}})
    .then(response => response.json())
    .then(data => {

        data.forEach(city => {
            const option = document.createElement('option')
            option.value = city.name
            option.textContent = city.name
            citySelect.appendChild(option)
        })
    })
}

window.onload = loadCountries