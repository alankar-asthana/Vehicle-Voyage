// Function to update input field types based on selected booking type
function updateInputTypes() {
    const dailyRadio = document.getElementById('daily');
    const weeklyRadio = document.getElementById('weekly');
    const monthlyRadio = document.getElementById('monthly');

    const daysInput = document.getElementById('days');
    const weeksInput = document.getElementById('weeks');
    const monthsInput = document.getElementById('months');

    dailyRadio.addEventListener('change', function() {
        if (this.checked) {
            daysInput.type = 'text'; // Change the type to text for daily booking
            weeksInput.type = 'hidden'; // Hide other input fields
            monthsInput.type = 'hidden';
        }
    });

    weeklyRadio.addEventListener('change', function() {
        if (this.checked) {
            daysInput.type = 'hidden'; // Hide other input fields
            weeksInput.type = 'text'; // Change the type to text for weekly booking
            monthsInput.type = 'hidden';
        }
    });

    monthlyRadio.addEventListener('change', function() {
        if (this.checked) {
            daysInput.type = 'hidden'; // Hide other input fields
            weeksInput.type = 'hidden';
            monthsInput.type = 'text'; // Change the type to text for monthly booking
        }
    });
}

// Call the function to initialize the event listeners
updateInputTypes();
