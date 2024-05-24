let buttonInProgress = "tester";


function signUp() {
    const submitButton = document.querySelector(".signUpSubmit");
    submitButton.addEventListener("click", () => {
        const idValue = document.querySelector(".signUpID").value;
        const pwValue = document.querySelector(".signUpPW").value;
        if (idValue.trim() !== "" && pwValue.trim() !== "") {
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:8080/userAdd", true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.onreadystatechange = () => {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(this.responseText);
                }
            }
            const body = JSON.stringify({"id" : idValue, "password" : pwValue, "role" : buttonInProgress});
            xhr.send(body);
        }

    })
}

function signIn() {
    const submitButton = document.querySelector(".signInSubmit");
    submitButton.addEventListener("click", () => {
        const idValue = document.querySelector(".signInID").value;
        const pwValue = document.querySelector(".signInPW").value;
        if (idValue.trim() !== "" && pwValue.trim() !== "") {
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:8080/userFind", true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.onreadystatechange = () => {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(this.responseText);
                }
            }
            const body = JSON.stringify({"id" : idValue, "password" : pwValue, "role" : buttonInProgress});
            xhr.send(body);
        }
    })
}

function signUpButtons() {
    const buttonList = document.querySelector(".buttonList");
    const plButton = document.getElementById("plButton");
    const devButton = document.getElementById("devButton");
    const testerButton = document.getElementById("testerButton");
    plButton.addEventListener("click", () => {
        buttonInProgress = "pl";
    });
    devButton.addEventListener("click", () => {
        buttonInProgress = "dev";
    });
    testerButton.addEventListener("click", () => {
        buttonInProgress = "tester";
    });

}

signUp();
signIn();
signUpButtons();