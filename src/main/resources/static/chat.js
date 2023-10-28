let currentProduct = null;
let chatOutput = document.getElementById("chat-output");
let chatInput = document.getElementById("chat-input");
let productsList = [];
let allProducts = "Test";

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
        //appendMessage("Bot", productsDescription);
        appendMessage("Bot", "Hello! How may I help you?");
    } catch (error) {
        appendMessage("Bot", "Error fetching products. Please try again.");
    }
}

async function sendMessage() {
    let message = chatInput.value.trim();
    chatInput.value = "";
    appendMessage("You", message);

    let detectedProduct = containsValidProduct(message);

    if (detectedProduct && detectedProduct !== currentProduct) {
        currentProduct = detectedProduct;
        appendMessage("Bot", `It seems like you might be interested in "${currentProduct}". Please feel free to ask me anything about this product.`);
        return;
    } else if (!currentProduct) {
        appendMessage("Bot", `The product "${message}" is not valid. Please pick a valid product.`);
        //appendMessage("Bot", allProducts);
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
    let convertedMessage = convertUrlsToAnchors(message);
    let messageClass = sender === "Bot" ? "bot-message" : "user-message";
    chatOutput.innerHTML += `
        <div class="message-row">
            <div class="${messageClass}">${convertedMessage}</div>
        </div>`;
    chatOutput.scrollTop = chatOutput.scrollHeight;
}

function containsValidProduct(message) {
    for (let product of productsList) {
        if (message.toLowerCase().includes(product.toLowerCase())) {
            return product;
        }
    }
    return null;
}

function convertUrlsToAnchors(text) {
    text = text.replace(/^\W+|\W+$/g, '');
    const urlRegex = /(https?:\/\/[^\s]+)\.?/g;
    return text.replace(urlRegex, (match, urlWithoutPeriod) => {

        return `<a href="${urlWithoutPeriod}" target="_blank">follow me</a>`;
    });
}



// Fetch available products once the page is loaded
getAvailableProducts();
