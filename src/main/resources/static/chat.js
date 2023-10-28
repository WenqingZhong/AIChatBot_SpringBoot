let currentProduct = null;
let chatOutput = document.getElementById("chat-output");
let chatInput = document.getElementById("chat-input");
let productsList = [];

chatInput.addEventListener("keyup", function(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
});

async function getAvailableProducts() {
    try {
        let response = await fetch("http://localhost:8080/api/products");
        let data = await response.json();
        let productsDescription = data.products;
        // Store the product list for validation
        productsList = productsDescription.split(', '); // Assuming products are comma-separated
        appendMessage("Bot", productsDescription);
        appendMessage("Bot", "Please pick a valid product from the list above.");
    } catch (error) {
        appendMessage("Bot", "Error fetching products. Please try again.");
    }
}

async function sendMessage() {
    let message = chatInput.value.trim();
    chatInput.value = "";
    appendMessage("You", message);

    if (!currentProduct) {
        // Check if the product is valid
        console.log(productsList);
        if (productsList.includes(message)) {
            currentProduct = message;
            appendMessage("Bot", `You selected product "${currentProduct}". Please ask your question about this product.`);
        } else {
            appendMessage("Bot", `The product "${message}" is not valid. Please pick a valid product from the list.`);
        }
        return;
    }

    try {
        let response = await fetch("http://localhost:8080/api/ask", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                product: currentProduct,
                question: message
            })
        });

        let data = await response.json();
        appendMessage("Bot", data.response);
    } catch (error) {
        appendMessage("Bot", "Error processing your request. Please try again.");
    }
}

function appendMessage(sender, message) {
    chatOutput.innerHTML += `<p><strong>${sender}:</strong> ${message}</p>`;
    chatOutput.scrollTop = chatOutput.scrollHeight; // Scroll to bottom
}

// Fetch available products once the page is loaded
getAvailableProducts();
