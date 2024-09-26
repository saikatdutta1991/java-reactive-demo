const express = require('express');
const app = express();
const port = 3000;

// Function to generate a random string
const getRandomString = (length) => {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
};

// Route to fetch user by id
app.get('/users/:id', (req, res) => {
  const { id } = req.params;

  const user = {
    id: id,
    firstName: "RandomFirstName",
    lastName: "RandomLastName",
    username: getRandomString(8) // Generating a random username
  };

  setTimeout(() => res.json(user), 1000)
});

// Start server
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});