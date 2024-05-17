function submitUserData() {
  const userDataForm = document.querySelector(".userInput");
  const submitButton = userDataForm.querySelector("#submitButton");
  submitButton.addEventListener("click", () => {
    const userID = userDataForm.querySelector("#userID").value;
    const userPW = userDataForm.querySelector("#userPassword").value;

    if (!userID.trim() || !userPW.trim()) {
      return;
    }

    const data = {
      userID: userID,
      userPW: userPW,
    };
    console.log(userID);
    console.log(userPW);

    fetch("http://localhost:8080/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
  });
}

submitUserData();
