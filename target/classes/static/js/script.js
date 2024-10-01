document.addEventListener("DOMContentLoaded", () => {
  const snowContainer = document.querySelector(".snow-container");

  function createSnowflake() {
    const snowflake = document.createElement("div");
    snowflake.classList.add("snowflake");

    // Set random position, size, and animation duration
    snowflake.style.left = `${Math.random() * 100}vw`; // Random horizontal position
    snowflake.style.animationDuration = `${Math.random() * 3 + 2}s`; // Random fall speed (2-5 seconds)
    snowflake.style.width = snowflake.style.height = `${
      Math.random() * 10 + 5
    }px`; // Random snowflake size

    snowContainer.appendChild(snowflake);

    // Remove snowflake after it falls out of view
    setTimeout(() => {
      snowflake.remove();
    }, 5000); // Match with animation duration
  }

  // Continuously create snowflakes every 300ms
  setInterval(createSnowflake, 300);
});
