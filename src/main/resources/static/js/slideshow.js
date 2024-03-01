// // Get image elements and buttons
//        const images = document.querySelectorAll(".images img");
//        const nextButton = document.querySelector(".slideshow-controls button.next");
//        const prevButton = document.querySelector(".slideshow-controls button.prev");
//
//        let currentImageIndex = 0; // Track current image
//
//        // Show only the first image initially
//        images[0].style.display = "block";
//
//        // Define functions to show/hide images
//        function showNextImage() {
//          images[currentImageIndex].style.display = "none";
//          currentImageIndex = (currentImageIndex + 1) % images.length;
//          images[currentImageIndex].style.display = "block";
//        }
//
//        function showPreviousImage() {
//          images[currentImageIndex].style.display = "none";
//          currentImageIndex = (currentImageIndex - 1 + images.length) % images.length; // Handle negative index
//          images[currentImageIndex].style.display = "block";
//        }
//
//        // Add event listeners to buttons (if applicable)
//        nextButton.addEventListener("click", showNextImage);
//        prevButton.addEventListener("click", showPreviousImage);


// Get image elements and buttons
const images = document.querySelectorAll(".images img");
const nextButton = document.querySelector(".slideshow-controls .next");
const prevButton = document.querySelector(".slideshow-controls .prev");

// Track current image index
let currentImageIndex = 0;

// Show only the first image initially
images[0].style.display = "block";

// Define functions to show/hide images with fading transition
function showNextImage() {
  images[currentImageIndex].style.opacity = 0; // Start fading out current image
  setTimeout(() => { // Wait for transition
    images[currentImageIndex].style.display = "none";
    currentImageIndex = (currentImageIndex + 1) % images.length;
    images[currentImageIndex].style.opacity = 1; // Fade in new image
  }, 500); // Adjust transition duration as needed
}

function showPreviousImage() {
  images[currentImageIndex].style.opacity = 0;
  setTimeout(() => {
    images[currentImageIndex].style.display = "none";
    currentImageIndex = (currentImageIndex - 1 + images.length) % images.length;
    images[currentImageIndex].style.opacity = 1;
  }, 500);
}

// Add event listeners to buttons
nextButton.addEventListener("click", showNextImage);
prevButton.addEventListener("click", showPreviousImage);

// Handle autoplay (optional)
const autoplayInterval = 3000; // Adjust interval as needed
let autoplayTimeout = null;

function startAutoplay() {
  autoplayTimeout = setTimeout(showNextImage, autoplayInterval);
}

function stopAutoplay() {
  if (autoplayTimeout) {
    clearTimeout(autoplayTimeout);
    autoplayTimeout = null;
  }
}

// Optionally enable autoplay on page load
window.addEventListener("load", startAutoplay);

// Optionally add hover and focus events for visual clues (accessibility)
images.forEach((img) => {
  img.addEventListener("mouseover", () => {
    stopAutoplay(); // Pause autoplay on hover
  });
  img.addEventListener("mouseout", () => {
    startAutoplay(); // Resume autoplay on mouseout
  });
  img.addEventListener("focus", () => {
    stopAutoplay(); // Pause autoplay on focus
  });
  img.addEventListener("blur", () => {
    startAutoplay(); // Resume autoplay on blur
  });
});
