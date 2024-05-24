function signUp() {
    const submitButton = document.querySelector(".signUpSubmit");
    submitButton.addEventListener("click", () => {
        const idValue = document.querySelector(".signUpID").value;
        const pwValue = document.querySelector(".signUpPW").value;
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:8080/userAdd", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4 && xhr.status === 200) {
                console.log(this.responseText);
            }
        }
        const body = JSON.stringify({"id" : idValue, "password" : pwValue, "role" : "tester"});
        xhr.send(body);
    })
}

function signIn() {
    const submitButton = document.querySelector(".signInSubmit");
    submitButton.addEventListener("click", () => {
        const idValue = document.querySelector(".signInID").value;
        const pwValue = document.querySelector(".signInPW").value;
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:8080/userFind", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4 && xhr.status === 200) {
                console.log(this.responseText);
            }
        }
        const body = JSON.stringify({"id" : idValue, "password" : pwValue, "role" : "tester"});
        xhr.send(body);
    })
}


signUp();
signIn();