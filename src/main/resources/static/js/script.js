function createSnowflake() {
  const snowflake = document.createElement("div");
  snowflake.className = "snowflake";

  // Set a random position for the snowflake
  snowflake.style.left = Math.random() * window.innerWidth + "px";

  // Set a random animation duration for the falling effect
  const fallDuration = Math.random() * 3 + 2; // Between 2s and 5s
  snowflake.style.animationDuration = fallDuration + "s";

  // Add the snowflake to the snow container
  document.querySelector(".snow-container").appendChild(snowflake);

  // Remove the snowflake after it falls
  setTimeout(() => {
    snowflake.remove();
  }, fallDuration * 1000); // Remove after the duration of the fall
}

// Create snowflakes at intervals
setInterval(createSnowflake, 300); // Adjust the frequency of snowflakes
