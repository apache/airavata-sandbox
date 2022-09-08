let spawn = require("child_process").spawn;

let bat = spawn("cmd.exe", [
    "/c",          // Argument for cmd.exe to carry out the specified script
    "test.bat", // Path to your file
    //"argument1",   // First argument
    //"argumentN"    // n-th argument
]);

bat.stdout.on("data", (data) => {
    // Handle data...
});

bat.stderr.on("data", (err) => {
    // Handle error...
});

bat.on("exit", (code) => {
    // Handle exit
});