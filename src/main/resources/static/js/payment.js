////first request to server to create order
//const createOrder = () => {
//    console.log("order created");
//    let amount = $("#paymentAmount").val();
//    console.log(amount);
//    if(amount == null || amount == "") {
//        alert("Please enter amount");
//        return;
//    }
//    if(amount <= 0){
//        alert("Please enter valid amount");
//        return;
//    }
//    let licenseNumber = $("#licenseNumber").val();
//    let userLicenseNumber = $("#userLicenseNumber").val();
//    console.log(licenseNumber);
//    if(licenseNumber == null || licenseNumber == "") {
//        alert("Please enter license number");
//        return;
//    }
//    if(userLicenseNumber != licenseNumber) {
//        alert("License number does not match");
//        return;
//    }
//
//    $.ajax(
//        {
//            type: "POST",
//            url: "/user/payment/create-order",
//            data: JSON.stringify({
//                amount: amount,
//                licenseNumber: licenseNumber,
//                info: "Order for " + amount
//            }),
//            dataType: "json",
//            contentType: "application/json",
//            success: function(response) {
//                console.log(response);
//                if(response.status == "created") {
//                    let options = {
//                        key: 'rzp_test_FGwK1TkYDMtNSp',
//                        amount: response.amount,
//                        currency: response.currency,
//                        name: "VehicleVoyage",
//                        description: "Payment for order "+response.order_id,
//                        image: "https://i.pinimg.com/564x/57/b2/ee/57b2ee7c988b631e8f5e488f26041304.jpg"
//                        order_id: response.id,
//                        handler: function(response) {
//                            console.log(response.razorpay_payment_id);
//                            console.log(response.razorpay_order_id);
//                            console.log(response.razorpay_signature);
//                            console.log("payment successful");
//                            alert("Congrats!! Payment successful");
//                        },
//                        prefill: {
//                            name: $("#userName").val(),
//                            email: $("#userEmail").val(),
//                            contact: $("#userPhone").val()
//                        },
//                        notes: {
//                            address: "VehicleVoyage"
//                        },
//                        theme: {
//                            color: "#3399cc"
//                        }
//                    };
//                    let rzp = new Razorpay(options);
//                    rzp.on('payment.failed', function(response) {
//                        console.log(response.error.code);
//                        console.log(response.error.description);
//                        console.log(response.error.source);
//                        console.log(response.error.step);
//                        console.log(response.error.reason);
//                        console.log(response.error.metadata.order_id);
//                        console.log(response.error.metadata.payment_id);
//                        alert("Oops!! Payment failed");
//                    })
//                    rzp.open();
//                }
//            },
//            error: function(error) {
//                console.log(error);
//                alert("Something went wrong");
//            }
//        }
//    )
//};

// First request to server to create order
const createOrder = () => {
  console.log("order created");
  let amount = $("#paymentAmount").val();
  console.log(amount);
  if (amount == null || amount == "") {
    alert("Please enter amount");
    return;
  }
  if (amount <= 0) {
    alert("Please enter valid amount");
    return;
  }
  let licenseNumber = $("#licenseNumber").val();
  let userLicenseNumber = $("#userLicenseNumber").val();
  console.log(licenseNumber);
  if (licenseNumber == null || licenseNumber == "") {
    alert("Please enter license number");
    return;
  }
  if (userLicenseNumber != licenseNumber) {
    alert("License number does not match");
    return;
  }

  $.ajax({
    type: "POST",
    url: "/user/payment/create-order",
    data: JSON.stringify({
      amount: amount,
      licenseNumber: licenseNumber,
      info: "Order for " + amount,
    }),
    dataType: "json",
    contentType: "application/json",
    success: function (response) {
      console.log(response);
      if (response.status == "created") {
        let options = {
          key: "rzp_test_FGwK1TkYDMtNSp", // Replace with your Razorpay test key
          amount: response.amount,
          currency: response.currency,
          name: "VehicleVoyage",
          description: "Payment for order " + response.order_id,
          image: "https://i.pinimg.com/564x/57/b2/ee/57b2ee7c988b631e8f5e488f26041304.jpg",
          // Added missing comma here
          order_id: response.id,
          handler: function (response) {
            console.log(response.razorpay_payment_id);
            console.log(response.razorpay_order_id);
            console.log(response.razorpay_signature);
            console.log("payment successful");

            // Update the payment status in the database
            updatePaymentStatus(response.razorpay_payment_id, response.razorpay_order_id, "paid");
//            alert("Congrats!! Payment successful");
            swal("Payment Successful", "Thank you for your payment", "success");
          },
          prefill: {
            name: $("#userName").val(),
            email: $("#userEmail").val(),
            contact: $("#userPhone").val(),
          },
          notes: {
            address: "VehicleVoyage",
          },
          theme: {
            color: "#3399cc",
          },
        };

        // Ensure Razorpay SDK is loaded before this block
        let rzp = new Razorpay(options);
        rzp.on("payment.failed", function (response) {
          console.log(response.error.code);
          console.log(response.error.description);
          console.log(response.error.source);
          console.log(response.error.step);
          console.log(response.error.reason);
          console.log(response.error.metadata.order_id);
          console.log(response.error.metadata.payment_id);
          //alert("Oops!! Payment failed");
          // Handle payment failed  error by deleting the order details from the database
          updatePaymentStatus(response.error.metadata.payment_id, response.error.metadata.order_id, "failed");
          swal("Payment Failed", "Please try again", "error");
        });
        rzp.open();
      }
    },
    error: function (error) {
        console.log(error);
        swal({
            title: "Error",
            text: "Something went wrong, please try again. " + error.responseText,
            icon: "error"
        });
    },
  });
};

// Function to update payment status in the database
const updatePaymentStatus = (paymentId, orderId, status) => {
  $.ajax({
    type: "POST",
    url: "/user/payment/update-payment-status",
    data: JSON.stringify({
      paymentId: paymentId,
      orderId: orderId,
      status: status,
    }),
    dataType: "json",
    contentType: "application/json",
    success: function (response) {
      console.log(response);
    },
    error: function (error) {
      console.log(error);
    },
  });
}
