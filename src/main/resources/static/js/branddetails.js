function updateBrandDropdown(vehicleType) {
  $.ajax({
    url: "/user/retrieve-brand/" + vehicleType,
    method: "GET",
    dataType: "json", // Specify expected data type
    success: function(data) {
      $("#brandName").empty(); // Clear existing options
      $.each(data, function(index, brand) {
        $("#brandName").append("<option value='" + brand + "'>" + brand + "</option>");
      });
    },
    error: function(error) {
      // Handle error scenarios
      console.error("Error fetching brands:", error);
    }
  });
}

// Call the function on radio button change
$("input[type=radio][name='vehicleType']").change(function() {
  updateBrandDropdown($(this).val());
});
