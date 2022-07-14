<template>
  <head>
    <title>Login Page</title>
  </head>

  <body>
  <form>
    <h1>SIGN IN</h1>
    <label>Username</label>
    <input type="text" placeholder="Enter User ID or Email ID">
    <label>Password</label>
    <input type="text" placeholder="Enter password">
    <button>Sign in</button>
    <a>Forgot Password?</a>
    <button>Login with CILogon</button>
  </form>
  </body>


</template>

<script>
export default {
  name: "LoginPage"
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
  cursor: pointer;
}

/*@import url(https://fonts.googleapis.com/css2?family=Source+Sans+Pro&display=swap);*/

body {
  height: 100vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /*background: linear-gradient(to right, #2C2C2C, #2c3e50);*/
  background-image: url("../assets/molecule_bg1.jpeg");
}

form {
  width: 25rem;
  height: 28rem;
  margin-right: 50rem;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.04);
  box-shadow: 0 8px 32px 0 rgba(52, 52, 52, 0.52);
  border-radius: 30px;
  border-left: 1px solid rgba(255, 255, 255, 0.3);
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  border-right: 1px solid rgba(255, 255, 255, 0.3);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
}

h1 {
  font-size: 50px;
  color: #edf0f1;
  text-align: center;
  text-shadow: 2px 2px 4px rgba(255, 255, 255, 0.3);
  letter-spacing: 3px;
  margin-top: 20px;
  margin-bottom: 5%;
  opacity: 0.7;
}

label {
  font-size: 20px;
  color: #edf0f1;
  display: flex;
  text-align: left;
  margin-left: 10%;
  margin-bottom:1px;
  opacity: 0.8;
  text-shadow: 2px 2px 4px rgba(255, 255, 255, 0.3);
}

input {
  width: 80%;
  height: 20px;
  font-size: 18px;
  margin: 5% auto 8%;
  border: none;
  outline: none;
  background: transparent;
  color: white;
  border-bottom: 1px solid rgba(255, 255, 255, 0.6);
}

button {
  width: 50%;
  margin: 3% auto;
  color: white;
  font-size: 15px;
  opacity: 0.7;
  background: rgba(255, 255, 255, 0.06);
  padding: 10px 30px;
  border: none;
  outline: none;
  border-radius: 20px;
  text-shadow: 2px 2px 4px rgba(255, 255, 255, 0.3);
  box-shadow: 3px 3px 5px rgba(255, 255, 255, 0.32);
  border-left: 2px solid rgba(255, 255, 255, 0.3);
  border-right: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid rgba(255, 255, 255, 0.3);
}
</style>