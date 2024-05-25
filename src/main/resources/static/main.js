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

            if (!xhr.ok) {
                throw new Error("No response from server." + xhr.statusText);
            }
            const data = xhr.json();
            if (data) {
                console.log("id : " + data.id);
                console.log("password : " + data.password);
                console.log("role : " + data.role);
            } else {
                console.log("no received data");
            }
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
            if (!xhr.ok) {
                throw new Error("No response from server." + xhr.statusText);
            }
            const data = xhr.json();
            if (data) {
                console.log("id : " + data.id);
                console.log("password : " + data.password);
                console.log("role : " + data.role);
            } else {
                console.log("no received data");
            }
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

function issueAdd() {
    const submitButton = document.querySelector(".issueAddSubmit");
    submitButton.addEventListener("click", () => {
        const titleValue = document.querySelector(".issueAddTitle").value;
        const contentValue = document.querySelector(".issueAddContent").value;
        const idValue = "user_name";
        const tagValue = ["PL"];
        if (titleValue.trim() !== "" && contentValue.trim() !== "") {
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:8080/issueAdd", true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.onreadystatechange = () => {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(this.responseText);
                }
            }
            const body = JSON.stringify({"title" : titleValue, "content" : contentValue, "accountId" : idValue, "tags" : tagValue});
            console.log(body);
            xhr.send(body);
        }
    })
}

function issueFind() {
    const submitButton = document.querySelector(".issueFindSubmit");
    submitButton.addEventListener("click", () => {
        const titleValue = document.querySelector(".issueFindTitle").value;
        if (titleValue.trim() !== "" && contentValue.trim() !== "") {
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "http://localhost:8080/issueFind", true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.onreadystatechange = () => {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(this.responseText);
                }
            }
            const body = JSON.stringify({"title" : titleValue});
            console.log(body);
            xhr.send(body);
        }
    })
}



signUp();
signIn();
signUpButtons();
issueAdd();
issueFind();