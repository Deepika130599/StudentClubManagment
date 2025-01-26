const COLORS = {
    primary: "#59857B",     // Primary color
    secondary: "#DOB182",   // Secondary color
    background: "#F8EACA",  // Background color
    text: "#F8EACA",        // Text color
    brown: "#9C6C4A",       // Brown color
    lightBrown: "#A76C3C",  // Light Brown color
  };

  // Dynamically inject CSS variables
const setCSSVariables = () => {
    const root = document.documentElement;
    Object.keys(COLORS).forEach((key) => {
      root.style.setProperty(`--${key}`, COLORS[key]);
    });
  };
  
  setCSSVariables();
  
  export default COLORS;